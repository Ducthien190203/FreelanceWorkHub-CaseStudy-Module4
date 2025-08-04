package vn.codegym.freelanceworkhub.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ModelAndView handleAllExceptions(Exception ex) {
        ModelAndView modelAndView = new ModelAndView("error"); // Assuming you have an error.html or error.jsp
        modelAndView.addObject("errorMessage", ex.getMessage());
        return modelAndView;
    }

    // You can add more specific exception handlers here
    // For example, for custom exceptions or specific HTTP status codes
}
