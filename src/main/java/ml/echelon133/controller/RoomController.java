package ml.echelon133.controller;

import ml.echelon133.model.Room;
import ml.echelon133.service.IRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class RoomController {

    private IRoomService roomService;

    @Autowired
    public RoomController(IRoomService roomService) {
        this.roomService = roomService;
    }

    @RequestMapping(value = "/json/rooms", method = RequestMethod.GET)
    public @ResponseBody List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }
}