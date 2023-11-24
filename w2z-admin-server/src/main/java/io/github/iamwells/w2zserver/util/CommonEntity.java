package io.github.iamwells.w2zserver.util;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonEntity<T> {

    private Integer status;

    private String message;

    private String code;

    private String detail;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public CommonEntity(HttpStatus status, String code, String detail, T data) {
        this.status = status.value();
        this.message = status.getReasonPhrase();
        this.code = code;
        this.detail = detail;
        this.data = data;
    }

    public static <T> CommonEntity<T> ok(String info, T data) {
        return new CommonEntity<>(HttpStatus.OK, null, info, data);
    }

    public static CommonEntity<Object> error(HttpStatus status,String code,String info) {
        CommonEntity<Object> entity = new CommonEntity<>();
        entity.status = status.value();
        entity.message = status.getReasonPhrase();
        entity.code = code;
        entity.detail = info;
        return entity;
    }

    public static CommonEntity<Object> error(int status,String code,String info) {
        CommonEntity<Object> entity = new CommonEntity<>();
        HttpStatus httpStatus = HttpStatus.valueOf(status);
        entity.status = status;
        entity.message = httpStatus.getReasonPhrase();
        entity.code = code;
        entity.detail = info;
        return entity;
    }
}
