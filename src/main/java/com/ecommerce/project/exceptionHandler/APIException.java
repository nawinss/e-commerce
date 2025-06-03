package com.ecommerce.project.exceptionHandler;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class APIException extends RuntimeException {

    public APIException(String message) {
        super(message);
    }
}
