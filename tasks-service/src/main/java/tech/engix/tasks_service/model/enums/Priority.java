package tech.engix.tasks_service.model.enums;

import lombok.Getter;

@Getter
public enum Priority {
    LOW("low"),
    MEDIUM("Medium"),
    HIGH("High");

    final String value;

    Priority(String value) {
        this.value = value;
    }
}
