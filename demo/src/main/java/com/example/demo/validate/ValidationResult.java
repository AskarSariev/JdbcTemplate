package com.example.demo.validate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ValidationResult {

    public List<Map<String, String>> bodyFormatError;

    public List<Map<String, String>> getBodyFormatError() {
        return bodyFormatError;
    }

    public void setBodyFormatError(List<Map<String, String>> bodyFormatError) {
        this.bodyFormatError = bodyFormatError;
    }
}
