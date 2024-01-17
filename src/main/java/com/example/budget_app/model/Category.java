package com.example.budget_app.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Table(name = "categories")
@Schema(description = "Category Entity representing a category of expenses")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the Category", example = "1")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Name of the Category", example = "Utilities")
    private String name;

    // Constructors
    public Category() {
        // Default constructor for JPA/Hibernate
    }

    public Category(String name) {
        this.name = name;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
