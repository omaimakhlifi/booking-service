package com.hestia.booking_service.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hestia.booking_service.core.model.Room;
import com.hestia.booking_service.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final IRoomService roomService;
    private final ObjectMapper objectMapper;
    @GetMapping
    public List<Room> getRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        Room room = roomService.getRoomByID(id);
        return ResponseEntity.ok(room);
    }

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Room> addRoom(
            @RequestPart("room") String roomJson,
            @RequestPart("files") List<MultipartFile> files) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        Room room = mapper.readValue(roomJson, Room.class);

        return ResponseEntity.ok(roomService.createRoom(room, files));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }
}