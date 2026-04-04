package com.hestia.booking_service.service;

import com.hestia.booking_service.core.model.Room;
import com.hestia.booking_service.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements IRoomService{
    private final RoomRepository roomRepository;
    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public Room getRoomByID(Long id) {
        return roomRepository.findById(id).orElseThrow(()->new RuntimeException("room not found winth id:" + id ));
    }

    @Override
    public Room createRoom(Room room) {
        return null;
    }


}
