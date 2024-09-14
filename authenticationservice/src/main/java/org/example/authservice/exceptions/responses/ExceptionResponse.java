package org.example.authservice.exceptions.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Max;
import lombok.*;

import java.util.Map;
import java.util.Set;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExceptionResponse {

    private Integer buisnessErrorCode ;
    private String buisnessExceptionDescription;
    private String error;
    private Set<String> validationErrors;
    private Map<String ,String> errors;
}
