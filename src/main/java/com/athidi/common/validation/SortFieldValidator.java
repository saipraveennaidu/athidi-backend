package com.athidi.common.validation;

import com.athidi.exception.InvalidRequestException;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SortFieldValidator {
    public void validate(
            String sortBy,
            Set<String> allowedFields) {

        if (sortBy == null || sortBy.isBlank()) {
            throw new InvalidRequestException(
                    "Sort field cannot be empty");
        }

        if (!allowedFields.contains(sortBy)) {
            throw new InvalidRequestException(
                    "Invalid sort field: " + sortBy);
        }
    }
}
