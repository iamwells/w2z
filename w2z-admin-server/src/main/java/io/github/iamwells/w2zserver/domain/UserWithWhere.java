package io.github.iamwells.w2zserver.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserWithWhere extends User implements Serializable {

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 2L;

    @Valid
    @NotNull(message = "给个where条件呗，总不能全改吧")
    private User where;
}
