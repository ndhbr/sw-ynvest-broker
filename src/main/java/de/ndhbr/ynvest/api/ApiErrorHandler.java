package de.ndhbr.ynvest.api;

import de.ndhbr.ynvest.dto.ApiResponseDTO;
import de.ndhbr.ynvest.enumeration.ApiResult;
import de.ndhbr.ynvest.exception.ServiceUnavailableException;
import org.hibernate.service.spi.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.MethodNotAllowedException;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice(value = "de.ndhbr.ynvest.api")
public class ApiErrorHandler {

    @ExceptionHandler({ServiceException.class, ServiceUnavailableException.class})
    public ResponseEntity<Object> handleServiceException(ServiceException e, HttpServletResponse res) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        res.setStatus(httpStatus.value());

        return new ResponseEntity<>(
                new ApiResponseDTO(ApiResult.Error, e.getMessage()),
                httpStatus
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleServiceException(EntityNotFoundException e, HttpServletResponse res) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        res.setStatus(httpStatus.value());

        return new ResponseEntity<>(
                new ApiResponseDTO(ApiResult.Error, e.getMessage()),
                httpStatus
        );
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<Object> handleMediaTypeNotSupportedException(
            HttpMediaTypeNotSupportedException e, HttpServletResponse res) {
        HttpStatus httpStatus = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
        res.setStatus(httpStatus.value());

        return new ResponseEntity<>(
                new ApiResponseDTO(ApiResult.Error, e.getMessage()),
                httpStatus
        );
    }

    @ExceptionHandler(MethodNotAllowedException.class)
    public ResponseEntity<Object> handleMethodNotAllowedException(
            MethodNotAllowedException e, HttpServletResponse res) {
        HttpStatus httpStatus = HttpStatus.METHOD_NOT_ALLOWED;
        res.setStatus(httpStatus.value());

        return new ResponseEntity<>(
                new ApiResponseDTO(ApiResult.Error, e.getMessage()),
                httpStatus
        );
    }
}
