package com.example.demo.model;

public class UserSubscriptionType {

    private String subscriptionType;
    private int usersInSubscription;

    public UserSubscriptionType(String st, int us) {
        this.subscriptionType = st;
        this.usersInSubscription = us;
    }

    @Override
    public String toString() {
        return (
            "Subscription type: " +
            this.subscriptionType +
            ", Users in subscription: " +
            this.usersInSubscription
        );
    }
}
