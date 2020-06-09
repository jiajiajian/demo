package cn.com.tiza.web.rest.errors;

import cn.com.tiza.errors.AbstractThrowableProblem;
import cn.com.tiza.errors.Status;

/**
 * Simple exception with a message, that returns an Internal Server Error code.
 */
public class InternalServerErrorException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public InternalServerErrorException(String message) {
        super(message, Status.INTERNAL_SERVER_ERROR);
    }
}
