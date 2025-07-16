package com.example.reservation;

import com.example.hotel_service.HotelServiceApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = HotelServiceApplication.class)
@AutoConfigureMockMvc
class ReservationControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper  objectMapper = new ObjectMapper();

    @Test
    void testCreateAndGetReservation() throws Exception {
        ReservationDTO dto = new ReservationDTO(null, "Integration", "101", LocalDate.now(), LocalDate.now().plusDays(1));
        String json = objectMapper.writeValueAsString(dto);

        // Create
        String response = mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.guestName").value("Integration"))
                .andReturn().getResponse().getContentAsString();

        ReservationDTO created = objectMapper.readValue(response, ReservationDTO.class);

        // Get by ID
        mockMvc.perform(get("/api/reservations/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.guestName").value("Integration"));
    }

    @Test
    void testGetAllReservations() throws Exception {
        // Add a reservation
        ReservationDTO dto = new ReservationDTO(null, "AllReservations", "102", LocalDate.now(), LocalDate.now().plusDays(1));
        String json = objectMapper.writeValueAsString(dto);
        mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());

        // Get all
        mockMvc.perform(get("/api/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists());
    }

    @Test
    void testUpdateReservation() throws Exception {
        // Create
        ReservationDTO dto = new ReservationDTO(null, "ToUpdate", "103", LocalDate.now(), LocalDate.now().plusDays(1));
        String json = objectMapper.writeValueAsString(dto);
        String response = mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andReturn().getResponse().getContentAsString();
        ReservationDTO created = objectMapper.readValue(response, ReservationDTO.class);

        // Update
        ReservationDTO update = new ReservationDTO(null, "Updated", "104", LocalDate.now(), LocalDate.now().plusDays(2));
        String updateJson = objectMapper.writeValueAsString(update);
        mockMvc.perform(put("/api/reservations/" + created.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.guestName").value("Updated"));
    }

    @Test
    void testDeleteReservation() throws Exception {
        // Create
        ReservationDTO dto = new ReservationDTO(null, "ToDelete", "105", LocalDate.now(), LocalDate.now().plusDays(1));
        String json = objectMapper.writeValueAsString(dto);
        String response = mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andReturn().getResponse().getContentAsString();
        ReservationDTO created = objectMapper.readValue(response, ReservationDTO.class);

        // Delete
        mockMvc.perform(delete("/api/reservations/" + created.getId()))
                .andExpect(status().isNoContent());

        // Verify not found
        mockMvc.perform(get("/api/reservations/" + created.getId()))
                .andExpect(status().isNotFound());
    }
}