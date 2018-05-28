package ml.echelon133.service;

import ml.echelon133.model.Room;
import ml.echelon133.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService implements IRoomService {

    private RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public List<Room> getAllRooms() {
        List<Room> allRooms = new ArrayList<>();
        roomRepository.findAll().forEach(allRooms::add);
        return allRooms;
    }

    @Override
    public Room getById(String id) {
        Optional<Room> room = roomRepository.findById(id);
        if (room.isPresent()) {
            return room.get();
        }
        return null;
    }

    @Override
    public Room getByName(String name) {
        return roomRepository.getByName(name);
    }

    @Override
    public boolean deleteById(String id) {
        roomRepository.deleteById(id);
        return !roomRepository.existsById(id);
    }

    @Override
    public void save(Room room) {
        roomRepository.save(room);
    }
}
