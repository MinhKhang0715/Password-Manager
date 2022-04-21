package org.example.passwordmanager.Password;

import java.time.LocalDate;

public class PasswordDTO {
    private int id;
    private String value;
    private String name;
    private String description;
    private LocalDate dateCreated;

    public int getId() {
        return id;
    }

    public PasswordDTO setId(int id) {
        this.id = id;
        return this;
    }

    public String getValue() {
        return value;
    }

    public PasswordDTO setValue(String value) {
        this.value = value;
        return this;
    }

    public String getName() {
        return name;
    }

    public PasswordDTO setName(String name) {
        this.name = name;
        return this;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public PasswordDTO setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public PasswordDTO setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public String toString() {
        return "PasswordDTO{" +
                "value='" + value + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", dateCreated=" + dateCreated +
                '}';
    }
}
