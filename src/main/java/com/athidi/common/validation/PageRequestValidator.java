package com.athidi.common.validation;

import com.athidi.exception.BookingException;
import org.springframework.stereotype.Component;

@Component
public class PageRequestValidator {
    public void validate(int page, int size) {

        if (page < 0) {
            throw new BookingException(
                    "Page number cannot be negative");
        }

        if (size <= 0) {
            throw new BookingException(
                    "Page size must be greater than zero");
        }

        if (size > 100) {
            throw new BookingException(
                    "Page size cannot exceed 100");
        }
    }
}
