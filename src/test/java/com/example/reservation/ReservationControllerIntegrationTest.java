package com.example.reservation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ReservationControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testCreateAndGetReservation() throws Exception {
        ReservationDTO dto = new ReservationDTO(null, "Integration", "101");
        String json = objectMapper.writeValueAsString(dto);

        // Create
        String response = mockMvc.perform(post("/reservations/addReservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.guestName").value("Integration"))
                .andReturn().getResponse().getContentAsString();

        ReservationDTO created = objectMapper.readValue(response, ReservationDTO.class);

        // Get by ID
        mockMvc.perform(get("/reservations/getReservation/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.guestName").value("Integration"));
    }

    @Test
    void testGetAllReservations() throws Exception {
        // Add a reservation
        ReservationDTO dto = new ReservationDTO(null, "AllReservations", "102");
        String json = objectMapper.writeValueAsString(dto);
        mockMvc.perform(post("/reservations/addReservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        // Get all
        mockMvc.perform(get("/reservations/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists());
    }

    @Test
    void testUpdateReservation() throws Exception {
        // Create
        ReservationDTO dto = new ReservationDTO(null, "ToUpdate", "103");
        String json = objectMapper.writeValueAsString(dto);
        String response = mockMvc.perform(post("/reservations/addReservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn().getResponse().getContentAsString();
        ReservationDTO created = objectMapper.readValue(response, ReservationDTO.class);

        // Update
        ReservationDTO update = new ReservationDTO(null, "Updated", "104");
        String updateJson = objectMapper.writeValueAsString(update);
        mockMvc.perform(put("/reservations/update/" + created.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.guestName").value("Updated"));
    }

    @Test
    void testDeleteReservation() throws Exception {
        // Create
        ReservationDTO dto = new ReservationDTO(null, "ToDelete", "105");
        String json = objectMapper.writeValueAsString(dto);
        String response = mockMvc.perform(post("/reservations/addReservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn().getResponse().getContentAsString();
        ReservationDTO created = objectMapper.readValue(response, ReservationDTO.class);

        // Delete
        mockMvc.perform(delete("/reservations/delete/" + created.getId()))
                .andExpect(status().isNoContent());

        // Verify not found
        mockMvc.perform(get("/reservations/getReservation/" + created.getId()))
                .andExpect(status().isNotFound());
    }
}
