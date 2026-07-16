package com.restaurantmanager.api.unit.infrastructure.web.exception;

import com.restaurantmanager.api.domain.exception.ConflictException;
import com.restaurantmanager.api.domain.exception.DomainException;
import com.restaurantmanager.api.infrastructure.web.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.http.ProblemDetail;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GlobalExceptionHandlerTest {

    @Test
    public void handlersProduceProblemDetail() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.setRequestURI("/test");
        WebRequest webRequest = new ServletWebRequest(servletRequest);

        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "target");
        bindingResult.addError(new FieldError("target", "name", "invalid"));

        MethodArgumentNotValidException manv = mock(MethodArgumentNotValidException.class);
        when(manv.getBindingResult()).thenReturn(bindingResult);
        when(manv.getMessage()).thenReturn("validation failed");

        ProblemDetail pd = handler.handleMethodArgumentNotValid(manv, webRequest);
        assertNotNull(pd);
        assertEquals("Invalid Request", pd.getTitle());
        assertTrue(pd.getProperties().containsKey("errors"));

        ConflictException conflict = new ConflictException("resource", "already exists");
        ProblemDetail cpd = handler.handleConflict(conflict, webRequest);
        assertNotNull(cpd);
        assertEquals("Conflict", cpd.getTitle());

        DomainException domain = new DomainException("domain failure");
        ProblemDetail dd = handler.handleDomainException(domain, webRequest);
        assertNotNull(dd);
        assertEquals("Domain Error", dd.getTitle());

        Exception ex = new RuntimeException("boom");
        ProblemDetail gd = handler.handleGenericException(ex, webRequest);
        assertNotNull(gd);
        assertEquals("Internal Server Error", gd.getTitle());
    }
}

