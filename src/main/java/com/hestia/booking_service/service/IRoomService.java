package com.hestia.booking_service.service;


import com.hestia.booking_service.core.model.Room;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IRoomService {
    List<Room> getAllRooms();
    Room getRoomByID(Long id);
    Room createRoom(Room room,List<MultipartFile> files);
    void deleteRoom(Long roomId);
}
