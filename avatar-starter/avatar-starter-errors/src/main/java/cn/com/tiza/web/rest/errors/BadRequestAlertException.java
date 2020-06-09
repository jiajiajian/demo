package cn.com.tiza.web.rest.errors;

import cn.com.tiza.errors.AbstractThrowableProblem;
import cn.com.tiza.errors.Status;

import java.util.HashMap;
import java.util.Map;

public class BadRequestAlertException extends AbstractThrowableProblem implements ErrorConstants {

    private static final long serialVersionUID = 1L;

    private final String entityName;

    private final String errorKey;

    public BadRequestAlertException(String defaultMessage, String entityName, String errorKey) {
        super(defaultMessage, Status.BAD_REQUEST, null, getAlertParameters(entityName, errorKey));
        this.entityName = entityName;
        this.errorKey = errorKey;
    }

    public BadRequestAlertException(Map<String, Object> entity, String errorKey) {
        super("", Status.BAD_REQUEST, null, getAlertParameters(entity, errorKey));
        this.entityName = null;
        this.errorKey = errorKey;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getErrorKey() {
        return errorKey;
    }

    private static Map<String, Object> getAlertParameters(String entityName, String errorKey) {
        Map<String, Object> parameters = new HashMap<>(3);
        parameters.put("message", "error." + errorKey);
        parameters.put("params", entityName);
        return parameters;
    }


    private static Map<String, Object> getAlertParameters(Map<String, Object> params, String errorKey) {
        Map<String, Object> entity = new HashMap<>(3);
        entity.put("message", "error." + errorKey);
        entity.put("params", params);
        return entity;
    }
}
