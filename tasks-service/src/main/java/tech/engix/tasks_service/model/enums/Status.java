package tech.engix.tasks_service.model.enums;

import lombok.Getter;

@Getter
public enum Status {
    BACKLOG("BACKLOG"),
    TODO("TODO"),
    IN_PROGRESS("IN_PROGRESS"),
    COMPLETED("COMPLETED"),
    DONE("DONE"),
    CANCELED("CANCELED");

    final String value;

    Status(String value) {
        this.value = value;
    }
}
