package com.hestia.booking_service.service;


import com.hestia.booking_service.core.model.Room;

import java.util.List;

public interface IRoomService {
    List<Room> getAllRooms();
    Room getRoomByID(Long id);
    Room createRoom(Room room);
}
