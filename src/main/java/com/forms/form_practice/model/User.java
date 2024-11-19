package com.forms.form_practice.model;

import java.time.LocalDate;
import java.time.Period;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class User {

    @NotNull(message = "Name cannot be NULL")
    @Size(min = 3, max = 64, message = ("Name must be between 3 and 64 characters long"))
    private String name;

    @Email(message = "Must be a valid email address")
    private String email;

    @Past(message = "Birthday must be in the past")
    @NotNull(message = "Must set date of birth")
    private LocalDate dateOfBirth; // Use LocalDate for birthday

    @Pattern(regexp = "^\\+?[0-9]{7,}$", message = "Please provide a valid phone number with at least 7 digits")
    private String phoneNumber;

    public User() {
    }

    public User(String name, String email, LocalDate dateOfBirth, String phoneNumber) {
        this.setName(name);
        this.setEmail(email);
        this.setDateOfBirth(dateOfBirth);
        this.setPhoneNumber(phoneNumber);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            throw new IllegalArgumentException("Date of birth cannot be null");
        }
        LocalDate current = LocalDate.now();
        int age = Period.between(dateOfBirth, current).getYears();

        if (age < 10 || age > 100) {
            throw new IllegalArgumentException("Date of birth must be between 10 and 100 years ago.");
        }
        this.dateOfBirth = dateOfBirth;
    }

}
