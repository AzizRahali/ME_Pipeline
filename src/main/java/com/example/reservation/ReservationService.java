package com.example.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public ReservationDTO createReservation(ReservationDTO dto) {
        Reservation reservation = new Reservation();
        reservation.setGuestName(dto.getGuestName());
        reservation.setRoomNumber(dto.getRoomNumber());
        Reservation saved = reservationRepository.save(reservation);
        return toDTO(saved);
    }

    public List<ReservationDTO> getAllReservations() {
        return reservationRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Optional<ReservationDTO> getReservationById(Long id) {
        return reservationRepository.findById(id).map(this::toDTO);
    }

    public Optional<ReservationDTO> updateReservation(Long id, ReservationDTO dto) {
        return reservationRepository.findById(id).map(reservation -> {
            reservation.setGuestName(dto.getGuestName());
            reservation.setRoomNumber(dto.getRoomNumber());
            return toDTO(reservationRepository.save(reservation));
        });
    }

    public boolean deleteReservation(Long id) {
        if (reservationRepository.existsById(id)) {
            reservationRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private ReservationDTO toDTO(Reservation reservation) {
        return new ReservationDTO(reservation.getId(), reservation.getGuestName(), reservation.getRoomNumber());
    }
} 