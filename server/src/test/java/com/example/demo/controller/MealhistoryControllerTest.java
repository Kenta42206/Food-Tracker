package com.example.demo.controller;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.repository.FoodRepository;
import com.example.demo.service.MealhistoryService;


@SpringBootTest
@AutoConfigureMockMvc
public class MealhistoryControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MealhistoryService mealhistoryService;

    @Autowired
    private FoodRepository foodRepository;

    @Test
    public void testGetDailyMealhistoryList() throws Exception {
        LocalDate date = null;

        ResultActions result = mockMvc.perform(get("/api/v1/mealhistory/{date}", date));

        result.andExpect(status().isOk())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(jsonPath("$.id").value(1))
              .andExpect(jsonPath("$.userId").value(1))
              .andExpect(jsonPath("$.food.id").value(1))
              .andExpect(jsonPath("$.quantity").value(200))
              .andExpect(jsonPath("$.consumedAt").value("2024-12-04"));
              
    }

    @Test
    public void testCreateMealHistory() throws Exception{
        assertTrue(foodRepository.existsById(1L));

        // リクエストボディ
        String requestJson = "{\"foodId\":1, \"quantity\":200}";

        // 実行
        ResultActions result = mockMvc.perform(post("/api/v1/mealhistory")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));

        // 検証
        result.andExpect(status().isCreated())
            .andExpect(jsonPath("$.food.id").value(1L))
            .andExpect(jsonPath("$.food.name").value("Rice"))
            .andExpect(jsonPath("$.quantity").value(200));
    }

    
}
