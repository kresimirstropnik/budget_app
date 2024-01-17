package com.example.budget_app.controller;

import com.example.budget_app.model.Category;
import com.example.budget_app.model.Expense;
import com.example.budget_app.model.User;
import com.example.budget_app.repository.CategoryRepository;
import com.example.budget_app.repository.ExpenseRepository;
import com.example.budget_app.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;

import org.springframework.data.domain.Sort;

@ExtendWith(SpringExtension.class)
public class ExpenseControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ExpenseController expenseController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(expenseController).build();
    }

    @Test
    public void testGetAllExpenses() throws Exception {
        // Create a mock user to associate with the expenses
        User mockUser = new User();
        mockUser.setId(1L);
    
        // Create a mock category to associate with the expenses
        Category mockCategory = new Category();
        mockCategory.setId(1L);
        
        // Create two mock expense objects and set their properties
        Expense expense1 = new Expense();
        expense1.setId(1L);
        expense1.setUser(mockUser);
        expense1.setCategory(mockCategory);
        expense1.setAmount(100.00);
        expense1.setExpenseDate(new Date());
    
        Expense expense2 = new Expense();
        expense2.setId(2L);
        expense2.setUser(mockUser);
        expense2.setCategory(mockCategory);
        expense2.setAmount(200.00);
        expense2.setExpenseDate(new Date());
    
        // Mock the findAll method of the expenseRepository to return the mock expenses
        when(expenseRepository.findAll()).thenReturn(Arrays.asList(expense1, expense2));
    
        // Perform a GET request to the '/api/expenses' endpoint and verify the response
        mockMvc.perform(get("/api/expenses"))
               .andExpect(status().isOk()) // Expect HTTP 200 status
               .andExpect(jsonPath("$", hasSize(2))) // Expect two items in the response
               .andExpect(jsonPath("$[0].id").value(1L)) // Verify properties of the first expense
               .andExpect(jsonPath("$[1].id").value(2L)); // Verify properties of the second expense
    
        // Verify that findAll was called exactly once
        verify(expenseRepository, times(1)).findAll();
    }

    @Test
    public void testGetExpenseById() throws Exception {
        // Create a mock user to associate with the expense
        User mockUser = new User();
        mockUser.setId(1L);
    
        // Create a mock category to associate with the expenses
        Category mockCategory = new Category();
        mockCategory.setId(1L);

        // Create a mock expense object and set its properties
        Expense expense = new Expense();
        expense.setId(1L);
        expense.setUser(mockUser);
        expense.setCategory(mockCategory);
        expense.setAmount(100.00);
        expense.setExpenseDate(new Date());
    
        // Mock the findById method of the expenseRepository to return the mock expense
        when(expenseRepository.findById(1L)).thenReturn(Optional.of(expense));
    
        // Perform a GET request to the '/api/expenses/1' endpoint and verify the response
        mockMvc.perform(get("/api/expenses/1"))
               .andExpect(status().isOk()) // Expect HTTP 200 status
               .andExpect(jsonPath("$.id").value(1L)); // Verify properties of the expense
    
        // Verify that findById was called exactly once with ID 1
        verify(expenseRepository, times(1)).findById(1L);
    }

    @Test
    public void testAddExpense() throws Exception {
        // Create mock User and Category
        User mockUser = new User();
        mockUser.setId(1L);

        Category mockCategory = new Category();
        mockCategory.setId(1L);
    
        // Create a mock Expense object and set its properties
        Expense expense = new Expense();
        expense.setId(1L);
        expense.setUser(mockUser);
        expense.setCategory(mockCategory);
        expense.setAmount(100.00);
        expense.setExpenseDate(new Date());
    
        // Mock the save method of the expenseRepository
        when(expenseRepository.save(any(Expense.class))).thenReturn(expense);
    
        // Perform a POST request to add the expense
        mockMvc.perform(post("/api/expenses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(expense)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1L));
    
        // Verify that the save method was called
        verify(expenseRepository, times(1)).save(any(Expense.class));
    }

    @Test
    public void testUpdateExpense() throws Exception {
        // Create a mock user to associate with the expenses
        User mockUser = new User();
        mockUser.setId(1L);
    
        // Create a mock category to associate with the expenses
        Category mockCategory = new Category();
        mockCategory.setId(1L);

        // Create a mock expense object to represent the existing expense
        Expense existingExpense = new Expense();
        existingExpense.setId(1L);
        existingExpense.setUser(mockUser);
        existingExpense.setCategory(mockCategory);
        existingExpense.setAmount(100.00);
        existingExpense.setExpenseDate(new Date());
    
        // Create a mock expense object to represent the updated expense details
        Expense updatedExpenseDetails = new Expense();
        updatedExpenseDetails.setId(1L);
        updatedExpenseDetails.setUser(mockUser);
        updatedExpenseDetails.setCategory(mockCategory);
        updatedExpenseDetails.setAmount(150.00);
        updatedExpenseDetails.setExpenseDate(new Date());
    
        // Mock the findById and save methods of the expenseRepository
        when(expenseRepository.findById(1L)).thenReturn(Optional.of(existingExpense));
        when(expenseRepository.save(any(Expense.class))).thenReturn(existingExpense);
    
        // Perform a PUT request to update the expense and verify the response
        mockMvc.perform(put("/api/expenses/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedExpenseDetails)))
               .andExpect(status().isOk()) // Expect HTTP 200 status
               .andExpect(jsonPath("$.amount").value(150.00)); // Verify updated properties of the expense
    
        // Verify that findById and save were called with the correct parameters
        verify(expenseRepository, times(1)).findById(1L);
        verify(expenseRepository, times(1)).save(any(Expense.class));
    }

    @Test
    public void testDeleteExpense() throws Exception {
        // Mock the findById method of the expenseRepository to return an Optional of a new Expense
        // This simulates finding an expense by ID before deletion
        when(expenseRepository.findById(1L)).thenReturn(Optional.of(new Expense()));
    
        // Perform a DELETE request to the '/api/expenses/1' endpoint to delete an expense
        // The endpoint is expected to handle the deletion of the expense with ID 1
        mockMvc.perform(delete("/api/expenses/1"))
               .andExpect(status().isOk()); // Verify that the response status is HTTP 200 OK
    
        // Verify that findById was called exactly once with ID 1
        // This checks that the controller correctly calls the repository method to find the expense
        verify(expenseRepository, times(1)).findById(1L);
    
        // Verify that delete was called exactly once with any Expense object
        // This ensures that the controller correctly calls the repository method to delete the expense
        verify(expenseRepository, times(1)).delete(any(Expense.class));
    }

    @Test
    public void testGetExpensesByCriteria() throws Exception {
        // Create a mock user to associate with the expenses
        User mockUser = new User();
        mockUser.setId(1L); // Set a user ID for the mock user
    
        // Create a mock category to associate with the expenses
        Category mockCategory = new Category();
        mockCategory.setId(1L);

        // Create two mock expense objects and set their properties
        Expense expense1 = new Expense();
        expense1.setId(1L);
        expense1.setUser(mockUser);
        expense1.setCategory(mockCategory);
        expense1.setAmount(100.00);
        expense1.setExpenseDate(new Date());
    
        Expense expense2 = new Expense();
        expense2.setId(2L);
        expense2.setUser(mockUser);
        expense2.setCategory(mockCategory);
        expense2.setAmount(200.00);
        expense2.setExpenseDate(new Date());
    
        // Mock the findAll method of the expenseRepository with a Specification and Sort
        when(expenseRepository.findAll(any(Specification.class), any(Sort.class)))
            .thenReturn(Arrays.asList(expense1, expense2));
    
        // Perform a GET request with filter parameters to the '/api/expenses/filter' endpoint
        mockMvc.perform(get("/api/expenses/filter")
                .param("minAmount", "50.00")
                .param("maxAmount", "250.00"))
               .andExpect(status().isOk()) // Expect HTTP 200 status
               .andExpect(jsonPath("$", hasSize(2))); // Expect two items in the response
    
        // Verify that findAll was called exactly once with the specified Specification and Sort
        verify(expenseRepository, times(1))
            .findAll(any(Specification.class), any(Sort.class));
    }

    @Test
    public void testGetExpenseAggregates() throws Exception {
        // Creating a Calendar instance to manipulate dates
        Calendar calendar = Calendar.getInstance();

        // Expense from the last month
        Expense expenseLastMonth = new Expense();
        calendar.add(Calendar.MONTH, -1);  // Set the date to one month ago
        expenseLastMonth.setExpenseDate(calendar.getTime());
        expenseLastMonth.setAmount(50.00);

        // Expense from the last quarter
        Expense expenseLastQuarter = new Expense();
        calendar.add(Calendar.MONTH, -2);  // Set the date to three months ago
        expenseLastQuarter.setExpenseDate(calendar.getTime());
        expenseLastQuarter.setAmount(100.00);

        // Expense from the last year
        Expense expenseLastYear = new Expense();
        calendar.add(Calendar.YEAR, -1);   // Set the date to one year ago
        expenseLastYear.setExpenseDate(calendar.getTime());
        expenseLastYear.setAmount(150.00);

        // Calculating sums based on the mock expenses
        double lastMonthSum = expenseLastMonth.getAmount();
        double lastQuarterSum = expenseLastMonth.getAmount() + expenseLastQuarter.getAmount();
        double lastYearSum = expenseLastMonth.getAmount() + expenseLastQuarter.getAmount() + expenseLastYear.getAmount();

        // Mocking repository method responses with the calculated sums
        when(expenseRepository.sumAmountLastMonth()).thenReturn(lastMonthSum);
        when(expenseRepository.sumAmountLastQuarter()).thenReturn(lastQuarterSum);
        when(expenseRepository.sumAmountLastYear()).thenReturn(lastYearSum);

        // Perform a GET request and assert the expected aggregate values
        mockMvc.perform(get("/api/expenses/aggregates"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.lastMonth").value(lastMonthSum))
            .andExpect(jsonPath("$.lastQuarter").value(lastQuarterSum))
            .andExpect(jsonPath("$.lastYear").value(lastYearSum));

        // Verify that the repository methods were called
        verify(expenseRepository, times(1)).sumAmountLastMonth();
        verify(expenseRepository, times(1)).sumAmountLastQuarter();
        verify(expenseRepository, times(1)).sumAmountLastYear();
    }
}
