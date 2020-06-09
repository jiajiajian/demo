package cn.com.tiza.errors;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author tiza
 */
public abstract class ThrowableProblem extends RuntimeException implements Problem {

    private static final long serialVersionUID = -6435695951506056362L;

    protected ThrowableProblem() {
        this(null);
    }

    protected ThrowableProblem(final ThrowableProblem cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        return getTitle();
    }

    @JsonIgnore
    @Override
    public ThrowableProblem getCause() {
        // cast is safe, since the only way to set this is our constructor
        return (ThrowableProblem) super.getCause();
    }

    @JsonIgnore
    @Override
    public StackTraceElement[] getStackTrace() {
        return super.getStackTrace();
    }

    @Override
    public String toString() {
        return Problem.toString(this);
    }

}
