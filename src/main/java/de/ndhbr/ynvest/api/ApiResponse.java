package de.ndhbr.ynvest.api;

public record ApiResponse(ApiResult result, String message) {
    public String toJson() {
        return "{\"result\":\"" + result.name() + "\"," +
                "\"message\":\"" + message + "\"}";
    }
}
