package com.ecommerce.project.exceptionHandler;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class ResourceNotFoundException extends RuntimeException{

    String resourceName;
    String field;
    Long fieldId;
    String fieldName;

    public ResourceNotFoundException(String resourceName, String field, Long fieldId) {

        super(String.format("%s not found with %s : %s",resourceName,field,fieldId));
        this.resourceName = resourceName;
        this.fieldId = fieldId;
        this.field= field;
    }

    public ResourceNotFoundException( String resourceName, String field, String fieldName) {
        super(String.format("%s not found for %s : %s",resourceName,field,fieldName));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldName = resourceName;
    }
}
