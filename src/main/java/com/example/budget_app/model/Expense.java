package com.example.budget_app.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private Double amount;

    @Column(name = "expense_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date expenseDate;

    // Constructors
    public Expense() {
        // Default constructor
    }

    public Expense(User user, Category category, Double amount, Date expenseDate) {
        this.user = user;
        this.category = category;
        this.amount = amount;
        this.expenseDate = expenseDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(Date expenseDate) {
        this.expenseDate = expenseDate;
    }

    // Additional methods or overrides can be added as needed

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", user=" + (user != null ? user.getUsername() : null) +
                ", category=" + (category != null ? category.getName() : null) +
                ", amount=" + amount +
                ", expenseDate=" + expenseDate +
                '}';
    }
}
