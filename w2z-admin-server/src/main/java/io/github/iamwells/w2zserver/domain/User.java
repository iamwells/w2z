package io.github.iamwells.w2zserver.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.iamwells.w2zserver.domain.validate.group.Insert;
import io.github.iamwells.w2zserver.domain.validate.group.SignIn;
import io.github.iamwells.w2zserver.domain.validate.group.SignUp;
import io.github.iamwells.w2zserver.domain.validate.group.WithoutPwd;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * 用户
 * @TableName user
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "user")
public class User implements Serializable, UserDetails {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    @Min(value = 1, message = "没有id小于1的这号人哦(*^_^*)")
    @Null(message = "登录而已，不需要Id", groups = {SignIn.class})
    @Null(message = "都还没有该用户呢，你咋知道ID的ε=( o｀ω′)ノ", groups = {SignUp.class})
    private Integer id;

    /**
     * 用户名
     */
    @NotBlank(message = "您用户名呢？怕不是注册了个寂寞(ㄒoㄒ)", groups = {Insert.class})
    @NotBlank(message = "您用户名呢？怕不是登录了个寂寞(ㄒoㄒ)", groups = {SignIn.class})
    @Size(min = 5, max = 32, message = "用户名长度(5-32)不合法")
    private String username;

    /**
     * 密码
     */
    @NotNull(message = "虽然也没有什么重要的东西，但是多少咱还是设置个密码吧(*^_^*)", groups = {Insert.class})
    @NotNull(message = "怎么没有密码，企图蒙混过关是吧，不可能，绝对不可能(╯▔皿▔)╯", groups = {SignIn.class})
    @Null(message = "不好意思该接口不能改密码(*^_^*)", groups = {WithoutPwd.class})
    @Size(min = 5, max = 32, message = "密码长度(5-32)不合法")
    private String password;

    /**
     * 昵称
     */
    @Size(min = 1, max = 32, message = "昵称长度(1-32)不合法")
    @Null(message = "登录而已，不需要昵称(*^_^*)", groups = {SignIn.class})
    private String nickname;

    /**
     * 性别
     */
    @Pattern(regexp = "^男$|^女$", message = "性别不合法，请确认为'男'、'女'，若您不想填，或者您的国家有其他性别，请暂时留空哦(*^_^*)")
    @Null(message = "咱这边不搞性别歧视呢，登录没必要传该字段(*^_^*)", groups = {SignIn.class})
    private String gender;

    /**
     * 生日
     */
    @Past(message = "亲，为了防止时空紊乱，咱这里不支持未来过来的朋友注册呢(*^_^*)")
    @Null(message = "登陆而已，没必要传生日", groups = {SignIn.class})
    private LocalDate birthday;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不太对，看不懂可以留空哦，或者您可以留个手机号(*^_^*)")
    @Size(max = 255, message = "邮箱长度(<255)不合法")
    private String email;

    /**
     * 手机号码
     */
    @Pattern(regexp = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$", message = "咱这边不支持其他国家的手机号哦，或者您可以留个邮箱(*^_^*)")
    private String phoneNumber;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @Null(message = "createTime字段由系统控制哈！")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Null(message = "updateTime字段由系统控制哈！")
    private LocalDateTime updateTime;

    /**
     * 乐观锁
     */
    @TableField(fill = FieldFill.INSERT)
    @Null(message = "version字段由系统控制哈！")
    private Integer version;


    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    @JsonIgnore
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}