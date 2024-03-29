package com.example.budget_app.model;

import jakarta.persistence.*;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "expenses")
@Schema(description = "Expense Entity representing a financial expense")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the Expense", example = "1")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    @Schema(description = "User associated with the Expense", implementation = User.class)
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @Schema(description = "Category of the Expense", implementation = Category.class)
    private Category category;

    @Column(nullable = false)
    @Schema(description = "Amount of the Expense", example = "150.50")
    private Double amount;

    @Column(name = "expense_date", nullable = false)
    @Temporal(TemporalType.DATE)
    @Schema(description = "Date when the Expense was incurred", example = "2023-01-15", format = "date")
    private Date expenseDate;

    // Constructors
    public Expense() {
        // Default constructor for JPA/Hibernate
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
