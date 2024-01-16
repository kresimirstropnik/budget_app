package com.example.budget_app.repository;

import com.example.budget_app.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ExpenseRepository extends JpaRepository<Expense, Long>, JpaSpecificationExecutor<Expense> {

    // Last Month
    @Query(value = "SELECT SUM(e.amount) FROM expenses e WHERE e.expense_date >= LAST_DAY(CURRENT_DATE - INTERVAL 1 MONTH) + INTERVAL 1 DAY AND e.expense_date < CURRENT_DATE", nativeQuery = true)
    Double sumAmountLastMonth();

    // Last Quarter
    @Query(value = "SELECT SUM(e.amount) FROM expenses e WHERE e.expense_date >= LAST_DAY(CURRENT_DATE - INTERVAL 3 MONTH) + INTERVAL 1 DAY AND e.expense_date < CURRENT_DATE", nativeQuery = true)
    Double sumAmountLastQuarter();

    // Last Year
    @Query(value = "SELECT SUM(e.amount) FROM expenses e WHERE e.expense_date >= LAST_DAY(CURRENT_DATE - INTERVAL 12 MONTH) + INTERVAL 1 DAY AND e.expense_date < CURRENT_DATE", nativeQuery = true)
    Double sumAmountLastYear();
}
