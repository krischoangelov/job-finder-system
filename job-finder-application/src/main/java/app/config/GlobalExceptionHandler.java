package app.config;

import app.exception.ApplicationException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ModelAndView handleApplicationException(ApplicationException exception) {

        log.error("Application Exception occurred: {}", exception.getMessage(), exception);

        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorMessage", exception.getMessage());
        modelAndView.addObject("errorTitle", exception.getErrorTitle());
        modelAndView.addObject("errorCode", exception.getErrorCode());

        return modelAndView;
    }

    // all feign client exceptions
    @ExceptionHandler(FeignException.class)
    public ModelAndView handleFeignException(FeignException exception) throws IOException {

        log.error("Application Exception occurred: {}", exception.getMessage(), exception);


        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorMessage", exception.getMessage());
        modelAndView.addObject("errorTitle", "Feign Exception");
        modelAndView.addObject("errorCode", exception.status());

        return modelAndView;
    }

    // All non handled exception
    @ExceptionHandler(Exception.class)
    public ModelAndView handleGenericException(Exception exception) {

        log.error("Application Exception occurred: {}", exception.getMessage(), exception);

        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorMessage", "Unexpected error occurred");
        modelAndView.addObject("errorTitle", "Internal Server Error");
        modelAndView.addObject("errorCode", "500");

        return modelAndView;
    }
}
