package com.example.budget_app.dto;

import java.util.Date;

public class ExpenseDTO {
    private Long id;
    private Long userId;
    private Long categoryId;
    private Double amount;
    private Date expenseDate;

    public ExpenseDTO() {
    }

    public ExpenseDTO(Long id, Long userId, Long categoryId, Double amount, Date expenseDate) {
        this.id = id;
        this.userId = userId;
        this.categoryId = categoryId;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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

    // toString Method for Debugging
    @Override
    public String toString() {
        return "ExpenseDTO{" +
                "id=" + id +
                ", userId=" + userId +
                ", categoryId=" + categoryId +
                ", amount=" + amount +
                ", expenseDate=" + expenseDate +
                '}';
    }
}