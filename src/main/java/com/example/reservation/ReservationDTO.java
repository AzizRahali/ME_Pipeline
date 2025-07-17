package com.example.reservation;

public class ReservationDTO {
    private Long id;
    private String guestName;
    private String roomNumber;

    public ReservationDTO() {}

    public ReservationDTO(Long id, String guestName, String roomNumber) {
        this.id = id;
        this.guestName = guestName;
        this.roomNumber = roomNumber;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getGuestName() { return guestName; }
    public void setGuestName(String guestName) { this.guestName = guestName; }
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
} 