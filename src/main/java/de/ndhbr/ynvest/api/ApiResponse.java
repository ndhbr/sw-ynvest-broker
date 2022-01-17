package de.ndhbr.ynvest.api;

public class ApiResponse {
    private final ApiResult result;
    private final String message;

    public ApiResponse(ApiResult result, String message) {
        this.result = result;
        this.message = message;
    }

    public String toJson() {
        return "{\"result\":\"" + result.name() + "\"," +
                "\"message\":\"" + message + "\"}";
    }
}
