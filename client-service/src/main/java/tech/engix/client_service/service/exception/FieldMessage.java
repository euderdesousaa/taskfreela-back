package tech.engix.client_service.service.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FieldMessage {
    private String fieldName;
    private String message;
}