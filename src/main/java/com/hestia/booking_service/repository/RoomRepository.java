package com.hestia.booking_service.repository;

import com.hestia.booking_service.core.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room,Long> {
    boolean existsByRoomNumber(String roomNumber);
}
