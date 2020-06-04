/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.aescryptography.exceptions.ResponseEntityhandlers;

 
 
import java.util.Date;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
 
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
 import com.mycompany.aescryptography.exceptions.BusinessException;
import unilever.gprs.exceptions.pojo.ErrorDetails;

/**
 *
 * @author rehab.abd-elhamid
 */
//http://www.springboottutorial.com/spring-boot-exception-handling-for-rest-services
@ControllerAdvice
@RestController
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler {
private static final  Logger LOGGER = Logger.getLogger(GeneralExceptionHandler.class);


    @ExceptionHandler(BusinessException.class)
    public final ResponseEntity<ErrorDetails> handleBusinessException(BusinessException ex, WebRequest request) {
         
     LOGGER.error(ex.getMessage());
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
                request.getDescription(false));
        if (ex.getParams() != null) {
            errorDetails.setParamObjects(ex.getParams());
        }
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
   
    }
}
