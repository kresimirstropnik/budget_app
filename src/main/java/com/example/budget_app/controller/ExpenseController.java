package com.example.budget_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.budget_app.model.Expense;
import com.example.budget_app.model.User;
import com.example.budget_app.model.Category;
import com.example.budget_app.dto.ExpenseDTO;
import com.example.budget_app.repository.ExpenseRepository;
import com.example.budget_app.repository.UserRepository;
import com.example.budget_app.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // Get All Expenses
    @GetMapping
    public List<ExpenseDTO> getAllExpenses() {
        List<Expense> expenses = expenseRepository.findAll();
        return expenses.stream()
                       .map(this::convertToDTO)
                       .collect(Collectors.toList());
    }

    // Get Expense by ID
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseDTO> getExpenseById(@PathVariable Long id) {
        return expenseRepository.findById(id)
                                .map(this::convertToDTO)
                                .map(ResponseEntity::ok)
                                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create a New Expense
    @PostMapping
    public ResponseEntity<?> addExpense(@RequestBody Expense expense) {
        Optional<User> user = userRepository.findById(expense.getUser().getId());
        if (!user.isPresent()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        Optional<Category> category = categoryRepository.findById(expense.getCategory().getId());
        if (!category.isPresent()) {
            return ResponseEntity.badRequest().body("Category not found");
        }

        expense.setUser(user.get());
        expense.setCategory(category.get());
        Expense savedExpense = expenseRepository.save(expense);
        return ResponseEntity.ok(convertToDTO(savedExpense));
    }

    // Update an Existing Expense
    @PutMapping("/{id}")
    public ResponseEntity<?> updateExpense(@PathVariable Long id, @RequestBody Expense expenseDetails) {
        return expenseRepository.findById(id).map(existingExpense -> {
            if (expenseDetails.getUser() != null && expenseDetails.getUser().getId() != null) {
                Optional<User> user = userRepository.findById(expenseDetails.getUser().getId());
                if (!user.isPresent()) {
                    return ResponseEntity.badRequest().body("User not found");
                }
                existingExpense.setUser(user.get());
            }

            if (expenseDetails.getCategory() != null && expenseDetails.getCategory().getId() != null) {
                Optional<Category> category = categoryRepository.findById(expenseDetails.getCategory().getId());
                if (!category.isPresent()) {
                    return ResponseEntity.badRequest().body("Category not found");
                }
                existingExpense.setCategory(category.get());
            }

            if (expenseDetails.getAmount() != null) {
                existingExpense.setAmount(expenseDetails.getAmount());
            }
            if (expenseDetails.getExpenseDate() != null) {
                existingExpense.setExpenseDate(expenseDetails.getExpenseDate());
            }

            Expense updatedExpense = expenseRepository.save(existingExpense);
            return ResponseEntity.ok(convertToDTO(updatedExpense));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete an Expense
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable Long id) {
        return expenseRepository.findById(id).map(expense -> {
            expenseRepository.delete(expense);
            return ResponseEntity.ok().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Utility method to convert Expense to ExpenseDTO
    private ExpenseDTO convertToDTO(Expense expense) {
        return new ExpenseDTO(
            expense.getId(),
            expense.getUser().getId(),
            expense.getCategory().getId(),
            expense.getAmount(),
            expense.getExpenseDate()
        );
    }
}
