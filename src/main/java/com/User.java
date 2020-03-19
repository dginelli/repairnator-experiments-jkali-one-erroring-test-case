package com;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User class, Holds a collection of memberships.
 *
 */
@Entity
public class User {
    /* User fields */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(unique=true)
    private String email;
    @Column(unique=true)
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String confirmPassword;

    @ManyToMany(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
    private List<Subscription> subscriptions;

    /* Constructors */
    public User(String username, String password)
    {
        this.username = username;
        this.password = password;
        this.subscriptions = new ArrayList<Subscription>();
    }

    public User()
    {

    }

    /* Add/Remove Subscriptions */
    public void addSubscription(Subscription s)
    {
        this.subscriptions.add(s);
    }


    public void removeSubscription(Subscription s)
    {
        this.subscriptions.remove(s);
    }

    /* Getters & Setters */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername(){return username;}

    public void setUsername(String username){this.username=username;}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword(){return confirmPassword;}

    public void setConfirmPassword(String confirmPassword) {this.confirmPassword=confirmPassword;}

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(ArrayList<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }
}
