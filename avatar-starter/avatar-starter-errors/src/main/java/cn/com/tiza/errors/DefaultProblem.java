package cn.com.tiza.errors;

import java.util.Map;

public final class DefaultProblem extends AbstractThrowableProblem {

    DefaultProblem(
            final String title,
            final StatusType status,
            final ThrowableProblem cause) {
        super(title, status, cause);
    }

    DefaultProblem(final String title,
                   final StatusType status,
                   final ThrowableProblem cause,
                   final Map<String, Object> parameters) {
        super(title, status, cause, parameters);
    }
}
