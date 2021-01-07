package org.aru.number.generator.enums;

public enum Status {
    SUCCESS("Status"),
    IN_PROGRESS("In Progress"),
    ERROR("Error");

    Status(String status) {
        this.status = status;
    }

    /**
     * String representation of the enum
     */
    private String status;


    /**
     *
     * Method to return textual representation of the enum
     */
    @Override
    public String toString() {
    return status;
    }
}
