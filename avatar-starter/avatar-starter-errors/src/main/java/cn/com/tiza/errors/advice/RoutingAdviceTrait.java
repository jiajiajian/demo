package cn.com.tiza.errors.advice;

import cn.com.tiza.errors.AdviceTrait;
import cn.com.tiza.errors.Problem;
import cn.com.tiza.errors.Status;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * @see AdviceTrait
 */
public interface RoutingAdviceTrait extends AdviceTrait {

    @ExceptionHandler
    default ResponseEntity<Problem> handleMissingServletRequestParameter(
            final MissingServletRequestParameterException exception,
            final NativeWebRequest request) {
        return create(Status.BAD_REQUEST, exception, request);
    }

    @ExceptionHandler
    default ResponseEntity<Problem> handleMissingServletRequestPart(
            final MissingServletRequestPartException exception,
            final NativeWebRequest request) {
        return create(Status.BAD_REQUEST, exception, request);
    }

    @ExceptionHandler
    default ResponseEntity<Problem> handleNoHandlerFound(
            final NoHandlerFoundException exception,
            final NativeWebRequest request) {
        return create(Status.NOT_FOUND, exception, request);
    }

    @ExceptionHandler
    default ResponseEntity<Problem> handleServletRequestBinding(
            final ServletRequestBindingException exception,
            final NativeWebRequest request) {
        return create(Status.BAD_REQUEST, exception, request);
    }
}
