package io.github.iamwells.w2zserver.util;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonEntity<T> {

    private Integer status;

    private String message;

    private String code;

    private String info;

    private T data;

    public  static <T> CommonEntity<T> ok(T data) {
        return null;
    }

    public static CommonEntity<Object> error(HttpStatus status,String code,String info) {
        CommonEntity<Object> entity = new CommonEntity<>();
        entity.status = status.value();
        entity.message = status.getReasonPhrase();
        entity.code = code;
        entity.info = info;
        return entity;
    }
}
