package com.example.budget_app.dto;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data Transfer Object for Expense")
public class ExpenseDTO {

    @Schema(description = "Unique identifier of the Expense", example = "1")
    private Long id;

    @Schema(description = "User ID associated with the Expense", example = "101")
    private Long userId;

    @Schema(description = "Category ID for the Expense", example = "5")
    private Long categoryId;

    @Schema(description = "Amount of the Expense", example = "100.50")
    private Double amount;

    @Schema(description = "Date of the Expense", example = "2023-01-15", format = "date")
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