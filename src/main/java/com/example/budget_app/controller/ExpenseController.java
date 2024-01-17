package com.example.budget_app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.budget_app.model.Expense;
import com.example.budget_app.model.User;
import com.example.budget_app.model.Category;
import com.example.budget_app.dto.ExpenseDTO;
import com.example.budget_app.repository.ExpenseRepository;
import com.example.budget_app.repository.UserRepository;
import com.example.budget_app.specifications.ExpenseSpecifications;
import com.example.budget_app.repository.CategoryRepository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/expenses")
@Tag(name = "Expenses", description = "The Expense API")
public class ExpenseController {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Operation(summary = "Get all expenses", description = "Returns a list of all expenses")
    @GetMapping
    public List<ExpenseDTO> getAllExpenses() {
        List<Expense> expenses = expenseRepository.findAll();
        return expenses.stream()
                       .map(this::convertToDTO)
                       .collect(Collectors.toList());
    }

    @Operation(summary = "Get expense by ID", description = "Returns a single expense by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseDTO> getExpenseById(@PathVariable Long id) {
        return expenseRepository.findById(id)
                                .map(this::convertToDTO)
                                .map(ResponseEntity::ok)
                                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Filter expenses", description = "Filters expenses by various criteria")
    @GetMapping("/filter")
    public List<ExpenseDTO> getExpensesByCriteria(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double minAmount,
            @RequestParam(required = false) Double maxAmount,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {

        List<Expense> expenses = expenseRepository.findAll(
            ExpenseSpecifications.findByCriteria(categoryId, minAmount, maxAmount, startDate, endDate),
            Sort.by("expenseDate").descending()
        );

        return expenses.stream()
                       .map(this::convertToDTO)
                       .collect(Collectors.toList());
    }

    @Operation(summary = "Get expense aggregates", description = "Gets aggregated expense data for the last month, quarter, and year")
    @GetMapping("/aggregates")
    public ResponseEntity<Map<String, Double>> getExpenseAggregates() {
        Map<String, Double> aggregates = new HashMap<>();
        aggregates.put("lastMonth", expenseRepository.sumAmountLastMonth());
        aggregates.put("lastQuarter", expenseRepository.sumAmountLastQuarter());
        aggregates.put("lastYear", expenseRepository.sumAmountLastYear());

        return ResponseEntity.ok(aggregates);
    }

    @Operation(summary = "Create a new expense", description = "Creates a new expense")
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

    @Operation(summary = "Update an existing expense", description = "Updates an existing expense")
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

    @Operation(summary = "Delete an expense", description = "Deletes an expense")
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
