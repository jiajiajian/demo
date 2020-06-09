package cn.com.tiza.errors.advice;

import cn.com.tiza.errors.AdviceTrait;
import cn.com.tiza.errors.Problem;
import cn.com.tiza.errors.violation.Violation;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Advice trait to handle any validation exceptions.
 * <p>
 * Be careful if you use {@link org.springframework.validation.beanvalidation.MethodValidationPostProcessor}
 * in order to validate method parameter field directly but {@code violations[].field} value looks like {@code arg0}
 * instead of parameter name, you have to configure a
 * {@link org.springframework.validation.beanvalidation.LocalValidatorFactoryBean} with your
 * {@link org.springframework.validation.beanvalidation.MethodValidationPostProcessor} like following:
 *
 * <pre><code>
 * {@literal @}Bean
 *  public Validator validator() {
 *      return new LocalValidatorFactoryBean();
 *  }
 *
 * {@literal @}Bean
 *  public MethodValidationPostProcessor methodValidationPostProcessor() {
 *      MethodValidationPostProcessor methodValidationPostProcessor = new MethodValidationPostProcessor();
 *      methodValidationPostProcessor.setValidator(validator());
 *      return methodValidationPostProcessor;
 *  }
 * </code></pre>
 *
 * @see AdviceTrait
 */
public interface ValidationAdviceTrait extends BaseBindingResultAdviceTrait {

    default Violation createViolation(final ConstraintViolation violation) {
        return new Violation(formatFieldName(violation.getPropertyPath().toString()), violation.getMessage());
    }

    @ExceptionHandler
    default ResponseEntity<Problem> handleConstraintViolation(
            final ConstraintViolationException exception,
            final NativeWebRequest request) {

        final List<Violation> violations = exception.getConstraintViolations().stream()
                .map(this::createViolation)
                .collect(toList());

        return newConstraintViolationProblem(exception, violations, request);
    }

    @ExceptionHandler
    default ResponseEntity<Problem> handleBindingResult(
            final BindException exception,
            final NativeWebRequest request) {
        return newConstraintViolationProblem(exception, createViolations(exception), request);
    }

    @ExceptionHandler
    default ResponseEntity<Problem> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException exception,
            final NativeWebRequest request) {
        return newConstraintViolationProblem(exception, createViolations(exception.getBindingResult()), request);
    }

}
