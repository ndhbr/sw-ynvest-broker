package de.ndhbr.ynvest.dto;

import de.ndhbr.ynvest.enumeration.ApiResult;

import java.io.Serializable;

/**
 * API Response class for REST interface
 */
public class ApiResponseDTO implements Serializable {
    private final ApiResult result;
    private final String message;

    public ApiResponseDTO(ApiResult result, String message) {
        this.result = result;
        this.message = message;
    }

    public ApiResult getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }
}
