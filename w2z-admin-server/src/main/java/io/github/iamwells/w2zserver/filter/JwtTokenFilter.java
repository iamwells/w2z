package io.github.iamwells.w2zserver.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.iamwells.w2zserver.domain.User;
import io.github.iamwells.w2zserver.properties.TokenProperties;
import io.github.iamwells.w2zserver.util.JwtUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RLock;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Slf4j
@Setter
@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    /**
     * Token的Header Name
     */

    private static Thread blackListClearThread = null;

    private final TokenProperties tokenProperties;

    private final ObjectMapper objectMapper;

    private final RedissonClient redissonClient;

    public JwtTokenFilter(TokenProperties tokenProperties, ObjectMapper objectMapper, RedissonClient redissonClient) {
        this.tokenProperties = tokenProperties;
        this.objectMapper = objectMapper;
        this.redissonClient = redissonClient;
    }


    @PostConstruct
    private void initCleanTask() {
        if (tokenProperties.getBlackListAutoCleanTaskIfCreate()) {
            RScoredSortedSet<Object> tokenBlackList = redissonClient.getScoredSortedSet("tokenBlackList");
            RLock rLock = redissonClient.getLock("autoCleanTokenBlackListLock");
            if (rLock.tryLock()) {
                try {
                    RAtomicLong autoCleanTBLFlag = redissonClient.getAtomicLong("autoCleanTBLFlag");
                    TimeUnit blackListAutoCleanTimeUnit = tokenProperties.getBlackListAutoCleanTimeUnit();
                    Integer blackListAutoCleanTimeInterval = tokenProperties.getBlackListAutoCleanTimeInterval();
                    if (autoCleanTBLFlag.get() == 0 && blackListClearThread == null) {
                        blackListClearThread = Thread.ofVirtual().unstarted(() -> {
                            for (; ; ) {
                                log.info("Token注销列表过期清理任务开始执行...");
                                if (!tokenBlackList.isEmpty()) {
                                    tokenBlackList.removeRangeByScore(0, true, System.currentTimeMillis(), true);
                                }
                                try {
                                    blackListAutoCleanTimeUnit.sleep(blackListAutoCleanTimeInterval);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                    }

                    log.info("Token注销列表过期清理任务已创建，执行周期为{}{}等待执行", blackListAutoCleanTimeInterval, blackListAutoCleanTimeUnit.name());
                    if (autoCleanTBLFlag.get() == 0 && !blackListClearThread.isAlive()) {
                        blackListClearThread.start();
                        autoCleanTBLFlag.incrementAndGet();
                    }

                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    rLock.unlock();
                }
            }
        }
    }

    @PreDestroy
    public void removeTask() {
        if (tokenProperties.getBlackListAutoCleanTaskIfCreate()) {
            RAtomicLong autoCleanTBLFlag = redissonClient.getAtomicLong("autoCleanTBLFlag");
            autoCleanTBLFlag.delete();
        }
    }

    private final List<String> witheList = Arrays.asList("/", "/sign/in", "/sign/up");


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        String servletPath = request.getServletPath();

        for (String uri : witheList) {
            if (uri.equals(servletPath)) {
                try {
                    filterChain.doFilter(request, response);
                } catch (IOException | ServletException e) {
                    throw new RuntimeException(e);
                }
                return;
            }
        }
        String token = request.getHeader(tokenProperties.getName());

        if (token == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null && cookies.length > 0) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(tokenProperties.getName())) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }
        }

        if (token != null) {
            try {
                RScoredSortedSet<Object> tokenBlackList = redissonClient.getScoredSortedSet("tokenBlackList");
                boolean contains = tokenBlackList.contains(token);
                JwtClaims claims;
                if (!contains && (claims = JwtUtil.verifyAndGetClaims(token)) != null) {
                    SecurityContext context = SecurityContextHolder.getContext();
                    Authentication authentication = context.getAuthentication();
                    if (authentication == null || !authentication.isAuthenticated()) {
                        try {
                            long end = claims.getExpirationTime().getValueInMillis();
                            request.setAttribute("tokenExpirationTime", end);
                        } catch (MalformedClaimException e) {
                            throw new RuntimeException(e);
                        }
                        Object userObj = claims.getClaimValue("user");
                        User user = null;
                        try {
                            String userString = objectMapper.writeValueAsString(userObj);
                            user = objectMapper.readValue(userString, User.class);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                        authentication = new UsernamePasswordAuthenticationToken(user, null, null);
                        if (user == null) {
                            authentication.setAuthenticated(false);
                        }
                        context.setAuthentication(authentication);
                    }
                }
            } catch (InvalidJwtException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            filterChain.doFilter(request, response);
        } catch (IOException | ServletException e) {
            throw new RuntimeException(e);
        }
    }
}
