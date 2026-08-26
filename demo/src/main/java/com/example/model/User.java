package com.example.model;

import jakarta.persistence.Entity;
import java.time.LocalDate;

@Entity
public class User {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String subscriptionType;
    private LocalDate dateJoined;

    public User(
        int id,
        String fn,
        String ln,
        String email,
        String st,
        LocalDate dj
    ) {
        this.id = id;
        this.firstName = fn;
        this.lastName = ln;
        this.email = email;
        this.subscriptionType = st;
        this.dateJoined = dj;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public LocalDate getDateJoined() {
        return dateJoined;
    }
}
