package com.example.budget_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.budget_app.model.Expense;
import com.example.budget_app.model.User;
import com.example.budget_app.repository.ExpenseRepository;
import com.example.budget_app.repository.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository; // Assuming you have a UserRepository

    @PostMapping
    public ResponseEntity<Expense> addExpense(@RequestBody Expense expense) {
        // Fetch the User from the database based on some identifier (e.g., username)
        // and set it in the Expense object before saving.
        User user = userRepository.findByUsername(expense.getUser().getUsername());
        expense.setUser(user);

        Expense savedExpense = expenseRepository.save(expense);

        // Optional: Set the saved expense in the user's list of expenses
        if (user != null) {
            user.getExpenses().add(savedExpense);
        }

        return ResponseEntity.ok(savedExpense);
    }

    @GetMapping
    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    // Add methods for Update, Delete, Filter and other operations
}