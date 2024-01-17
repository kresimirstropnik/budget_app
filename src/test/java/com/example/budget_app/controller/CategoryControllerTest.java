package com.example.budget_app.controller;

import com.example.budget_app.model.Category;
import com.example.budget_app.repository.CategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;

@ExtendWith(SpringExtension.class)
public class CategoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryController categoryController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    @Test
    public void testGetAllCategories() throws Exception {
        // Setup our mocked categories
        Category category1 = new Category("Category1");
        Category category2 = new Category("Category2");
    
        // Mocking the findAll method of the repository
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2));
    
        // Perform the GET request and verify the response
        mockMvc.perform(get("/api/categories"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(jsonPath("$[0].name").value("Category1"))
               .andExpect(jsonPath("$[1].name").value("Category2"));
    
        // Verify interactions with mock
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    public void testGetCategoryById() throws Exception {
        // Setup a category
        Category category = new Category("Category1");
    
        // Mocking the findById method of the repository
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
    
        // Perform the GET request and verify the response
        mockMvc.perform(get("/api/categories/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("Category1"));
    
        // Verify interactions with mock
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    public void testCreateCategory() throws Exception {
        // Setup a category to be saved
        Category categoryToBeSaved = new Category("NewCategory");
    
        // Setup the response after saving
        Category savedCategory = new Category("NewCategory");
        savedCategory.setId(1L); // Assuming the saved category is assigned an ID of 1
    
        // Mocking the save method of the repository
        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);
    
        // Perform the POST request and verify the response
        mockMvc.perform(post("/api/categories")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(categoryToBeSaved)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1))
               .andExpect(jsonPath("$.name").value("NewCategory"));
    
        // Verify interactions with mock
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    public void testUpdateCategory() throws Exception {
        // Create a category instance to represent the existing category
        Category originalCategory = new Category("OriginalName");
    
        // Create another category instance to represent the updated category
        Category updatedCategory = new Category("UpdatedName");
    
        // Mock the findById method to return the original category when searched by ID
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(originalCategory));
    
        // Mock the save method to return the updated category when any category instance is saved
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);
    
        // Perform a PUT request to update the category and assert the expected behavior
        mockMvc.perform(put("/api/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCategory)))
               .andExpect(status().isOk()) // Expecting an OK status
               .andExpect(jsonPath("$.name").value("UpdatedName")); // Expecting the name to be updated in the response
    
        // Verify that findById and save methods are called once
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    public void testDeleteCategory() throws Exception {
        // Create a category instance to represent the category to be deleted
        Category category = new Category("CategoryToDelete");
    
        // Mock the findById method to return the category when searched by ID
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
    
        // Mock the delete method to do nothing when a category is deleted
        doNothing().when(categoryRepository).delete(any(Category.class));
    
        // Perform a DELETE request to delete the category and assert the expected behavior
        mockMvc.perform(delete("/api/categories/1"))
               .andExpect(status().isOk()); // Expecting an OK status after deletion
    
        // Verify that findById and delete methods are called once
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).delete(any(Category.class));
    }
}
