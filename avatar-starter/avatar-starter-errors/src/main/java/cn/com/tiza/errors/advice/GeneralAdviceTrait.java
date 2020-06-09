package cn.com.tiza.errors.advice;

import cn.com.tiza.errors.AdviceTrait;
import cn.com.tiza.errors.Problem;
import cn.com.tiza.errors.Status;
import cn.com.tiza.errors.ThrowableProblem;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * @see AdviceTrait
 */
public interface GeneralAdviceTrait extends AdviceTrait {

    @ExceptionHandler
    default ResponseEntity<Problem> handleProblem(
            final ThrowableProblem problem,
            final NativeWebRequest request) {
        return create(problem, request);
    }

    @ExceptionHandler
    default ResponseEntity<Problem> handleThrowable(
            final Throwable throwable,
            final NativeWebRequest request) {
        return create(throwable, request);
    }

    @ExceptionHandler
    default ResponseEntity<Problem> handleUnsupportedOperation(
            final UnsupportedOperationException exception,
            final NativeWebRequest request) {
        return create(Status.NOT_IMPLEMENTED, exception, request);
    }
}
