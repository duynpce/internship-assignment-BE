package org.example.userservice.infrastructure.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// common dto used to return response
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // null --> not created in json response
public class ResponseDto<T> {
    boolean isSuccess;
    private String message;
    private T data;
    private MetaDto metaData;

    //no message and meta date
    public static <T> ResponseDto<T> success(T data) {
        return ResponseDto.<T>builder()
                .isSuccess(true)
                .data(data)
                .build();
    }

    //no meta data
    public static <T> ResponseDto<T> success(T data, String message) {
        return ResponseDto.<T>builder()
                .isSuccess(true)
                .data(data)
                .message(message)
                .build();
    }

    //with meta data
    public static <T> ResponseDto<T> success(T data,  String message, MetaDto meta ) {
        return ResponseDto.<T>builder()
                .isSuccess(true)
                .data(data)
                .metaData(meta)
                .message(message)
                .build();
    }

    public static <T> ResponseDto<T> failure(String message) {
        return ResponseDto.<T>builder()
                .isSuccess(false)
                .message(message)
                .build();
    }
}
