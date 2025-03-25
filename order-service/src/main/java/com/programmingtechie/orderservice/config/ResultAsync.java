package com.programmingtechie.orderservice.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultAsync {
    private Integer code;

    private String message;
}
