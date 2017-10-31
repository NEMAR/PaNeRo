package org.panero.debs.config.validator;

import java.io.File;

import org.panero.debs.config.DebsProperties;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class DebsPropertiesValidator implements Validator {
    @Override
    public boolean supports(final Class<?> type) {
        return type == DebsProperties.class;
    }

    @Override
    public void validate(final Object o, final Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "file", "file.empty");
        final File file = ((DebsProperties) o).getFile();
        if (!file.exists()) errors.rejectValue("file", "file.not.present");
        if (!file.isFile()) errors.rejectValue("file", "file.invalid");
        if (!file.canRead()) errors.rejectValue("file", "file.cannot.read");
    }
}
