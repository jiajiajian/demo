package cn.com.tiza.web;


import cn.com.tiza.errors.Problem;
import cn.com.tiza.fwp.FWPException;
import cn.com.tiza.web.rest.errors.BadRequestException;
import cn.com.tiza.web.rest.errors.ExceptionTranslator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;

@ControllerAdvice
public class FWPExceptionTranslator extends ExceptionTranslator {

    @ExceptionHandler
    public ResponseEntity<Problem> handleFWPException(FWPException ex, NativeWebRequest request) {
        BadRequestException exception = new BadRequestException(ex.getErrorKey());
        return create(exception, request);
    }


}
