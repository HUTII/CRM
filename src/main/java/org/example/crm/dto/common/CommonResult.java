package org.example.crm.dto.common;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author hutianlin
 * 2024/11/10 17:18
 */
@NoArgsConstructor
@Getter
public class CommonResult<T> {
    private long status;
    private String message;
    private T data;

    protected CommonResult(long status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<>(200, "success", data);
    }

    public static <T> CommonResult<T> failed(String message) {
        return new CommonResult<>(500, message, null);
    }

    public static <T> CommonResult<T> failed(long status, String message) {
        return new CommonResult<>(status, message, null);
    }
}
