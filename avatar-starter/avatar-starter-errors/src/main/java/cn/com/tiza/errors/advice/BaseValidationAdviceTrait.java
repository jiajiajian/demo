package cn.com.tiza.errors.advice;

import cn.com.tiza.errors.AdviceTrait;
import cn.com.tiza.errors.Problem;
import cn.com.tiza.errors.Status;
import cn.com.tiza.errors.StatusType;
import cn.com.tiza.errors.violation.ConstraintViolationProblem;
import cn.com.tiza.errors.violation.Violation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Collection;
import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

interface BaseValidationAdviceTrait extends AdviceTrait {

    default StatusType defaultConstraintViolationStatus() {
        return Status.BAD_REQUEST;
    }

    /**
     * Format the name of a violating field (e.g. lower camel to snake case)
     *
     * @param fieldName the field name to format
     * @return the formatted field name, defaults to the parameter, i.e. doesn't apply any transformation
     */
    default String formatFieldName(final String fieldName) {
        return fieldName;
    }

    default ResponseEntity<Problem> newConstraintViolationProblem(final Throwable throwable,
                                                                  final Collection<Violation> stream, final NativeWebRequest request) {

        final StatusType status = defaultConstraintViolationStatus();

        final List<Violation> violations = stream.stream()
                // sorting to make tests deterministic
                .sorted(comparing(Violation::getField).thenComparing(Violation::getMessage))
                .collect(toList());

        final Problem problem = new ConstraintViolationProblem(status, violations);

        return create(throwable, problem, request);
    }

}
