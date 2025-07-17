package com.example.reservation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReservationServiceTest {
    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateReservation() {
        ReservationDTO dto = new ReservationDTO(null, "John Doe", "101");
        Reservation saved = new Reservation();
        saved.setId(1L);
        saved.setGuestName("John Doe");
        saved.setRoomNumber("101");
        when(reservationRepository.save(any(Reservation.class))).thenReturn(saved);

        ReservationDTO result = reservationService.createReservation(dto);
        assertNotNull(result.getId());
        assertEquals("John Doe", result.getGuestName());
    }

    @Test
    void testGetAllReservations() {
        Reservation r1 = new Reservation();
        r1.setId(1L); r1.setGuestName("A"); r1.setRoomNumber("101");
        Reservation r2 = new Reservation();
        r2.setId(2L); r2.setGuestName("B"); r2.setRoomNumber("102");
        when(reservationRepository.findAll()).thenReturn(Arrays.asList(r1, r2));

        List<ReservationDTO> reservations = reservationService.getAllReservations();
        assertEquals(2, reservations.size());
    }

    @Test
    void testGetReservationById_Found() {
        Reservation r = new Reservation();
        r.setId(1L); r.setGuestName("A"); r.setRoomNumber("101");
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(r));

        Optional<ReservationDTO> result = reservationService.getReservationById(1L);
        assertTrue(result.isPresent());
        assertEquals("A", result.get().getGuestName());
    }

    @Test
    void testGetReservationById_NotFound() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<ReservationDTO> result = reservationService.getReservationById(1L);
        assertFalse(result.isPresent());
    }

    @Test
    void testUpdateReservation_Found() {
        Reservation r = new Reservation();
        r.setId(1L); r.setGuestName("A"); r.setRoomNumber("101");
        Reservation updated = new Reservation();
        updated.setId(1L); updated.setGuestName("B"); updated.setRoomNumber("102");
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(r));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(updated);

        ReservationDTO dto = new ReservationDTO(null, "B", "102");
        Optional<ReservationDTO> result = reservationService.updateReservation(1L, dto);
        assertTrue(result.isPresent());
        assertEquals("B", result.get().getGuestName());
    }

    @Test
    void testUpdateReservation_NotFound() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.empty());
        ReservationDTO dto = new ReservationDTO(null, "B", "102");
        Optional<ReservationDTO> result = reservationService.updateReservation(1L, dto);
        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteReservation_Found() {
        when(reservationRepository.existsById(1L)).thenReturn(true);
        doNothing().when(reservationRepository).deleteById(1L);
        assertTrue(reservationService.deleteReservation(1L));
    }

    @Test
    void testDeleteReservation_NotFound() {
        when(reservationRepository.existsById(1L)).thenReturn(false);
        assertFalse(reservationService.deleteReservation(1L));
    }
} 