package com.example.budget_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.budget_app.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
