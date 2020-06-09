package cn.com.tiza.errors.violation;


import cn.com.tiza.errors.StatusType;
import cn.com.tiza.errors.ThrowableProblem;

import java.util.Collections;
import java.util.List;


public class ConstraintViolationProblem extends ThrowableProblem {

    private final StatusType status;
    private final List<Violation> violations;

    public ConstraintViolationProblem(final StatusType status, final List<Violation> violations) {
        this.status = status;
        this.violations = violations != null ? Collections.unmodifiableList(violations) : Collections.emptyList();
    }

    @Override
    public String getTitle() {
        return "Constraint Violation";
    }

    @Override
    public StatusType getStatus() {
        return status;
    }

    public List<Violation> getViolations() {
        return violations;
    }

}
