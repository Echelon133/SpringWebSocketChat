package ml.echelon133.service;

import ml.echelon133.model.Room;

import java.util.List;

public interface IRoomService {
    List<Room> getAllRooms();
    Room getById(String id);
    Room getByName(String name);
    boolean deleteById(String id);
    void save(Room room);
}
