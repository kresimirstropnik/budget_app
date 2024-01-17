package com.example.budget_app.model;

import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "users")
@Schema(description = "User Entity representing a user of the application")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the User", example = "1")
    private Long id;

    @Column(unique = true, nullable = false)
    @Schema(description = "Username of the User", example = "john_doe")
    private String username;

    @Column(nullable = false)
    @Schema(description = "Password of the User", example = "password123", accessMode = Schema.AccessMode.WRITE_ONLY)
    private String password;

    @Column(name = "account_balance", nullable = false)
    @Schema(description = "Account balance of the User", example = "500.00")
    private Double accountBalance;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    @Schema(description = "List of Expenses associated with the User")
    private List<Expense> expenses;

    // Constructors
    public User() {
        // Default constructor for JPA/Hibernate
    }

    public User(String username, String password, Double accountBalance) {
        this.username = username;
        this.password = password;
        this.accountBalance = accountBalance;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }
}
