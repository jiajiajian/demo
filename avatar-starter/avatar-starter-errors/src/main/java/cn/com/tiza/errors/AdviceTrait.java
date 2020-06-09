package cn.com.tiza.errors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static javax.servlet.RequestDispatcher.ERROR_EXCEPTION;
import static org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

public interface AdviceTrait {

    default ThrowableProblem toProblem(final Throwable throwable) {
        final StatusType status = Optional.ofNullable(resolveResponseStatus(throwable))
                .<StatusType>map(ResponseStatusAdapter::new)
                .orElse(Status.INTERNAL_SERVER_ERROR);

        return toProblem(throwable, status);
    }

    default ResponseStatus resolveResponseStatus(final Throwable type) {
        final ResponseStatus candidate = findMergedAnnotation(type.getClass(), ResponseStatus.class);
        return candidate == null && type.getCause() != null ? resolveResponseStatus(type.getCause()) : candidate;
    }

    default ThrowableProblem toProblem(final Throwable throwable, final StatusType status) {
        final ThrowableProblem problem = prepare(throwable, status).build();
        final StackTraceElement[] stackTrace = createStackTrace(throwable);
        problem.setStackTrace(stackTrace);
        return problem;
    }

    default ProblemBuilder prepare(final Throwable throwable, final StatusType status) {
        return Problem.builder()
                .withTitle(status.getReasonPhrase())
                .withStatus(status)
                .withCause(Optional.ofNullable(throwable.getCause())
                        .filter(cause -> isCausalChainsEnabled())
                        .map(this::toProblem)
                        .orElse(null));
    }

    default StackTraceElement[] createStackTrace(final Throwable throwable) {
        final Throwable cause = throwable.getCause();

        if (cause == null || !isCausalChainsEnabled()) {
            return throwable.getStackTrace();
        } else {

            final StackTraceElement[] next = cause.getStackTrace();
            final StackTraceElement[] current = throwable.getStackTrace();

            final int length = current.length - lengthOfTrailingPartialSubList(asList(next), asList(current));
            final StackTraceElement[] stackTrace = new StackTraceElement[length];
            System.arraycopy(current, 0, stackTrace, 0, length);
            return stackTrace;
        }
    }

    default int lengthOfTrailingPartialSubList(final List<?> source, final List<?> target) {
        final int s = source.size() - 1;
        final int t = target.size() - 1;
        int l = 0;

        while (l <= s && l <= t && source.get(s - l).equals(target.get(t - l))) {
            l++;
        }

        return l;
    }


    default boolean isCausalChainsEnabled() {
        return false;
    }

    default ResponseEntity<Problem> process(final ResponseEntity<Problem> entity) {
        return entity;
    }

    /**
     * Creates a {@link Problem problem} {@link ResponseEntity response} for the given {@link Throwable throwable}
     * by taking any {@link ResponseStatus} annotation on the exception type or one of the causes into account.
     *
     * @param throwable exception being caught
     * @param request   incoming request
     * @return the problem response
     */
    default ResponseEntity<Problem> create(final Throwable throwable, final NativeWebRequest request) {
        final ThrowableProblem problem = toProblem(throwable);
        return create(throwable, problem, request);
    }

    default ResponseEntity<Problem> create(final StatusType status, final Throwable throwable,
                                           final NativeWebRequest request) {
        return create(status, throwable, request, new HttpHeaders());
    }

    default ResponseEntity<Problem> create(final StatusType status, final Throwable throwable,
                                           final NativeWebRequest request, final HttpHeaders headers) {
        return create(throwable, toProblem(throwable, status), request, headers);
    }

    default ResponseEntity<Problem> create(final ThrowableProblem problem, final NativeWebRequest request) {
        return create(problem, request, new HttpHeaders());
    }

    default ResponseEntity<Problem> create(final ThrowableProblem problem, final NativeWebRequest request,
                                           final HttpHeaders headers) {
        return create(problem, problem, request, headers);
    }

    default ResponseEntity<Problem> create(final Throwable throwable, final Problem problem,
                                           final NativeWebRequest request) {
        return create(throwable, problem, request, new HttpHeaders());
    }

    default ResponseEntity<Problem> create(final Throwable throwable, final Problem problem,
                                           final NativeWebRequest request, final HttpHeaders headers) {

        final HttpStatus status = HttpStatus.valueOf(Optional.ofNullable(problem.getStatus())
                .orElse(Status.INTERNAL_SERVER_ERROR)
                .getStatusCode());

        log(throwable, problem, request, status);

        if (status == HttpStatus.INTERNAL_SERVER_ERROR) {
            request.setAttribute(ERROR_EXCEPTION, throwable, SCOPE_REQUEST);
        }

        return process(ResponseEntity
                .status(status)
                .headers(headers)
                .body(problem), request) ;
    }

    default void log(
            final Throwable throwable,
            @SuppressWarnings("UnusedParameters") final Problem problem,
            @SuppressWarnings("UnusedParameters") final NativeWebRequest request,
            final HttpStatus status) {
        AdviceTraits.log(throwable, status);
    }

    default ResponseEntity<Problem> fallback(
            @SuppressWarnings("UnusedParameters") final Throwable throwable,
            final Problem problem,
            @SuppressWarnings("UnusedParameters") final NativeWebRequest request,
            final HttpHeaders headers) {
        return AdviceTraits.fallback(problem, headers);
    }

    default ResponseEntity<Problem> process(
            final ResponseEntity<Problem> entity,
            @SuppressWarnings("UnusedParameters") final NativeWebRequest request) {
        return process(entity);
    }

}
