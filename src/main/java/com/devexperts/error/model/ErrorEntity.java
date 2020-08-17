package com.devexperts.error.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ErrorEntity  implements Serializable {
    private static final long serialVersionUID = 7969386926697638296L;
    private String status;
    private String code;
    private String reason;
    private String message;
}
