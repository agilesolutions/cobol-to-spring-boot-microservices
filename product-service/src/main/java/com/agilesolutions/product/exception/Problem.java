package com.agilesolutions.product.exception;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Problem {

    private String code;
    private String message;

}