package cn.com.tiza.errors.advice;

import cn.com.tiza.errors.AdviceTrait;
import cn.com.tiza.errors.Problem;
import cn.com.tiza.errors.Status;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartException;

/**
 * @see AdviceTrait
 */
public interface IOAdviceTrait extends AdviceTrait {

    @ExceptionHandler
    default ResponseEntity<Problem> handleMessageNotReadableException(
            final HttpMessageNotReadableException exception,
            final NativeWebRequest request) {
        return create(Status.BAD_REQUEST, exception, request);
    }

    @ExceptionHandler
    default ResponseEntity<Problem> handleMultipart(
            final MultipartException exception,
            final NativeWebRequest request) {
        return create(Status.BAD_REQUEST, exception, request);
    }

    @ExceptionHandler
    default ResponseEntity<Problem> handleTypeMismatch(
            final TypeMismatchException exception,
            final NativeWebRequest request) {
        return create(Status.BAD_REQUEST, exception, request);
    }
}
