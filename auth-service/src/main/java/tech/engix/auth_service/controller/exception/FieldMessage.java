package tech.engix.auth_service.controller.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FieldMessage {
    private String fieldName;
    private String message;
}