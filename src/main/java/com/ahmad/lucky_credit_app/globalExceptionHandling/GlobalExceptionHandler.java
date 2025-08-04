package com.ahmad.lucky_credit_app.globalExceptionHandling;

import com.ahmad.lucky_credit_app.dto.response.ApiErrorResponse;
import com.ahmad.lucky_credit_app.globalExceptionHandling.exceptions.InsufficientFundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.View;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final View error;

    public GlobalExceptionHandler(View error) {
        this.error = error;
    }

    @ExceptionHandler({
            ResourceNotFoundException.class,
            AlreadyExistsException.class
    })
    public ResponseEntity<ApiErrorResponse> handleAllExceptions(Exception exception, HttpServletRequest request){
        HttpStatus status = determineHttpStatus(exception);

        ApiErrorResponse response = ApiErrorResponse.builder()
                .timeStamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .error(status.getReasonPhrase())
                .statusCode(status.value())
                .message(exception.getMessage())
                .build();

        return ResponseEntity.status(status).body(response);
    }

    private HttpStatus determineHttpStatus(Exception exception){
        if (exception instanceof ResourceNotFoundException){
            return HttpStatus.NOT_FOUND;
        }
        if (exception instanceof InsufficientFundException){
            return HttpStatus.NOT_FOUND;
        }
        if (exception instanceof  AlreadyExistsException){
            return HttpStatus.CONFLICT;
        }

        //fallback
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

//    private String determineErrorMessage(Exception exception){
//
//    }
}

//@Slf4j
//@Hidden
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler({
//            ResourceNotFoundException.class,
//            AlreadyExistsException.class,
//            IllegalArgumentException.class,
//            CloudinaryException.class,
//            AccessDeniedException.class,
//            MethodArgumentNotValidException.class,
//            Exception.class
//    })
//    public ResponseEntity<ApiErrorResponse> handleAllExceptions(Exception ex, HttpServletRequest request) {
//        HttpStatus status = determineHttpStatus(ex);
//        String message = determineMessage(ex);
//
//        log.error("Exception [{}]: {}", status, ex.getMessage(), ex);
//
//        ApiErrorResponse error = ApiErrorResponse.builder()
//                .timeStamp(LocalDateTime.now())
//                .path(request.getRequestURI())
//                .error(status.getReasonPhrase())
//                .statusCode(status.value())
//                .message(message)
//                .build();
//
//        return ResponseEntity.status(status).body(error);
//    }
//
//    private HttpStatus determineHttpStatus(Exception ex) {
//        if (ex instanceof ResourceNotFoundException) {
//            return HttpStatus.NOT_FOUND;
//        }
//        if (ex instanceof AlreadyExistsException) {
//            return HttpStatus.CONFLICT;
//        }
//        if (ex instanceof IllegalArgumentException) {
//            return HttpStatus.BAD_REQUEST;
//        }
//        if (ex instanceof CloudinaryException) {
//            int code = ((CloudinaryException) ex).getStatusCode();
//            return HttpStatus.resolve(code) != null ? HttpStatus.resolve(code) : HttpStatus.INTERNAL_SERVER_ERROR;
//        }
//        if (ex instanceof AccessDeniedException) {
//            return HttpStatus.FORBIDDEN;
//        }
//        if (ex instanceof MethodArgumentNotValidException) {
//            return HttpStatus.BAD_REQUEST;
//        }
//        // Fallback for all other exceptions
//        return HttpStatus.INTERNAL_SERVER_ERROR;
//    }
//
//    private String determineMessage(Exception ex) {
//        if (ex instanceof CloudinaryException) {
//            return "Cloudinary error: " + ((CloudinaryException) ex).getCloudinaryError();
//        }
//        if (ex instanceof MethodArgumentNotValidException) {
//            String errors = ((MethodArgumentNotValidException) ex).getBindingResult().getFieldErrors().stream()
//                    .map(err -> err.getField() + ": " + err.getDefaultMessage())
//                    .collect(Collectors.joining(", "));
//            return "Validation failed: " + errors;
//        }
//        if (ex instanceof AccessDeniedException) {
//            return "Access denied: You don't have permission to access this resource.";
//        }
//        // Default to exception's message
//        return ex.getMessage();
//    }
//}
