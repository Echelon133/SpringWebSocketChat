package ml.echelon133.repository;

import ml.echelon133.model.Room;
import org.springframework.data.repository.CrudRepository;

public interface RoomRepository extends CrudRepository<Room, String> {
    Room getByName(String name);
}
