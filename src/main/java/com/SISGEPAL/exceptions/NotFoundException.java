package com.SISGEPAL.exceptions;

import lombok.Data;

@Data
public class NotFoundException extends Exception {
    public NotFoundException(String message) {
        super(message);
    }
}
