package com.lims.shared.apierror;

import com.lims.shared.apierror.ApiSubError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class ApiValidationErrorResponse extends ApiSubError {
    private String object;
    private String field;
    private Object rejectedValue;
    private String message;

    ApiValidationErrorResponse(String object, String message) {
        this.object = object;
        this.message = message;
    }
}
