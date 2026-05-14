package com.hestia.booking_service.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hestia.booking_service.core.enums.RoomType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roomNumber;
    @Enumerated(EnumType.STRING)
    private RoomType roomType;
    private BigDecimal priceByNight;
    @Column(name = "is_available") // Pour la base de données
    @JsonProperty("isAvailable")  // Pour le JSON (Jackson)
    private boolean isAvailable;
    private String description;
    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;
    @OneToMany(mappedBy = "room")
    private List<Booking> bookings;
    @ElementCollection
    @CollectionTable(name= "room_images", joinColumns= @JoinColumn(name = "room_id"))
    @Column(name="image_url")
    private List<String> imageUrls = new ArrayList<>();
}

