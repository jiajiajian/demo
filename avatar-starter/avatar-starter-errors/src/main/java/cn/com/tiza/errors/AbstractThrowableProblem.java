package cn.com.tiza.errors;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author tiza
 */
public abstract class AbstractThrowableProblem extends ThrowableProblem {

    private static final long serialVersionUID = -3304408761866105208L;
    private final String title;
    private final StatusType status;
    private final Map<String, Object> parameters;

    protected AbstractThrowableProblem() {
        this(null);
    }

    protected AbstractThrowableProblem(final String title) {
        this(title, null);
    }

    protected AbstractThrowableProblem(final String title,
                                       final StatusType status) {
        this(title, status, null);
    }

    protected AbstractThrowableProblem(final String title,
                                       final StatusType status,
                                       final ThrowableProblem cause) {
        this(title, status, cause, null);
    }

    protected AbstractThrowableProblem(final String title,
                                       final StatusType status,
                                       final ThrowableProblem cause,
                                       final Map<String, Object> parameters) {
        super(cause);
        this.title = title;
        this.status = status;
        this.parameters = Optional.ofNullable(parameters).orElseGet(LinkedHashMap::new);
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public StatusType getStatus() {
        return status;
    }

    @Override
    public Map<String, Object> getParameters() {
        return Collections.unmodifiableMap(parameters);
    }

}
