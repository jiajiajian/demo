package cn.com.tiza.errors.advice;

import cn.com.tiza.errors.AdviceTrait;
import cn.com.tiza.errors.Problem;
import cn.com.tiza.errors.Status;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * @see AdviceTrait
 */
public interface HttpAdviceTrait extends AdviceTrait {

    @ExceptionHandler
    default ResponseEntity<Problem> handleMediaTypeNotAcceptable(
            final HttpMediaTypeNotAcceptableException exception,
            final NativeWebRequest request) {
        return create(Status.NOT_ACCEPTABLE, exception, request);
    }

    @ExceptionHandler
    default ResponseEntity<Problem> handleMediaTypeNotSupportedException(
            final HttpMediaTypeNotSupportedException exception,
            final NativeWebRequest request) {

        final HttpHeaders headers = new HttpHeaders();
        headers.setAccept(exception.getSupportedMediaTypes());

        return create(Status.UNSUPPORTED_MEDIA_TYPE, exception, request, headers);
    }

    @ExceptionHandler
    default ResponseEntity<Problem> handleRequestMethodNotSupportedException(
            final HttpRequestMethodNotSupportedException exception,
            final NativeWebRequest request) {

        final String[] methods = exception.getSupportedMethods();

        if (methods == null || methods.length == 0) {
            return create(Status.METHOD_NOT_ALLOWED, exception, request);
        }

        final HttpHeaders headers = new HttpHeaders();
        headers.setAllow(exception.getSupportedHttpMethods());

        return create(Status.METHOD_NOT_ALLOWED, exception, request, headers);
    }
}
