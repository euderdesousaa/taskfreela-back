package tech.engix.tasks_service.model.enums;

import lombok.Getter;

@Getter
public enum Label {
    BUG("BUG"),
    FAILURE("FAILURE"),
    DOCUMENTATION("DOCUMENTATION");

    final String value;

    Label(String value) {
        this.value = value;
    }
}
