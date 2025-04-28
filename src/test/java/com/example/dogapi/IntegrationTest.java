package com.example.dogapi;

import com.example.dogapi.dao.request.FavoriteImageRequest;
import com.example.dogapi.entity.FavoriteImage;
import com.example.dogapi.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-data-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void addFavoriteImage_ShouldCallRepositoryMethods() throws Exception {
        // 1. Подготовка тестовых данных
        FavoriteImageRequest request = new FavoriteImageRequest(
                "testUser",
                "https://image.com/dog.jpg",
                "breed"
        );

        mockMvc.perform(post("/api/dogs/favorite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void getFavoriteImages_ShouldReturnFormattedResponse() throws Exception {
        User user = new User("username");
        user.add(new FavoriteImage("https://image.com/dog.jpg", "username"));
        mockMvc.perform(get("/api/dogs/username/favorite", "username"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.breeds.length()").value(1))
                .andExpect(jsonPath("$.breeds[0].images.length()").value(1))
                .andExpect(jsonPath("$.breeds[?(@.name == 'breed')].images[0]").value("https://image.com/dog.jpg"));
    }
}