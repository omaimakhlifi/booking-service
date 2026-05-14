package com.hestia.booking_service.service;

import com.hestia.booking_service.core.model.Room;
import com.hestia.booking_service.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements IRoomService{
    private final RoomRepository roomRepository;
    private final IFileStorageService fileStorageService;
    @Override
    public List<Room> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        rooms.forEach(room -> {
            if (room.getImageUrls() != null) {
                List<String> presignedUrls = room.getImageUrls().stream()
                        .map(name -> fileStorageService.generatePresignedUrl(name))
                        .toList();

                // On met à jour la liste dans l'objet avant de l'envoyer au front
                room.setImageUrls(presignedUrls);
            }
        });
        return rooms;
    }

    @Override
    public Room getRoomByID(Long id) {
        return roomRepository.findById(id).orElseThrow(()->new RuntimeException("room not found with id:" + id ));
    }

    @Override
    @Transactional
    public Room createRoom(Room room,List<MultipartFile> files) {

        if(roomRepository.existsByRoomNumber(room.getRoomNumber()))
            throw new RuntimeException("La chambre n°" + room.getRoomNumber() + "existe déja");
        if (files != null && !files.isEmpty()) {
            List<String> uploadedFileNames = files.stream()
                    .map(fileStorageService::saveFile) // Appelle ton MinioStorageService
                    .collect(Collectors.toList());

            room.setImageUrls(uploadedFileNames);
        }
        return roomRepository.save(room);
    }

    @Override
    public void deleteRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Chambre non trouvée"));
        if (room.getImageUrls() != null && !room.getImageUrls().isEmpty()) {
            for (String fileName : room.getImageUrls()) {
                fileStorageService.deleteFile(fileName);
            }
        }
        roomRepository.delete(room);
        System.out.println("Chambre " + roomId + " et ses images supprimées avec succès.");
    }


}
