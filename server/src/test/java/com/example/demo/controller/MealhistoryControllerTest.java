package com.example.demo.controller;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;




@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MealhistoryControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "testUser")
    public void testGetDailyMealhistoryList() throws Exception {
        LocalDate date = LocalDate.of(2000, 1, 1);

        ResultActions result = mockMvc.perform(get("/api/v1/mealhistory/{date}", date));

        result.andExpect(status().isOk())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(jsonPath("$[0].userId").value(1))
              .andExpect(jsonPath("$[0].food.id").value(1))
              .andExpect(jsonPath("$[0].food.name").value("Rice"))
              .andExpect(jsonPath("$[0].quantity").value(200))
              .andExpect(jsonPath("$[0].consumedAt").value("2000-01-01T20:15:00"));
              
    }

    @Test
    @WithMockUser(username = "testUser") 
    public void testBatchCreateMealhistories() throws Exception {
        String requestJson = """
            [
                {"foodId": 1, "quantity": 150, "consumedAt": "2000-01-02T08:00:00", "mealNumber": 1},
                {"foodId": 2, "quantity": 300, "consumedAt": "2000-01-02T12:00:00", "mealNumber": 2}
            ]
        """;

        ResultActions result = mockMvc.perform(post("/api/v1/mealhistory/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));

        result.andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].food.id").value(1))
            .andExpect(jsonPath("$[0].quantity").value(150))
            .andExpect(jsonPath("$[0].consumedAt").value("2000-01-02T08:00:00")) 
            .andExpect(jsonPath("$[0].mealNumber").value(1)) 
            .andExpect(jsonPath("$[1].food.id").value(2))
            .andExpect(jsonPath("$[1].quantity").value(300))
            .andExpect(jsonPath("$[1].consumedAt").value("2000-01-02T12:00:00")) 
            .andExpect(jsonPath("$[1].mealNumber").value(2)); 
    }

    
}
