package ml.echelon133.controller;

import ml.echelon133.model.Room;
import ml.echelon133.model.form.RoomForm;
import ml.echelon133.service.IRoomService;
import ml.echelon133.service.ISpecialAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
public class AdminController {

    private IRoomService roomService;
    private ISpecialAuthorityService authorityService;

    @Autowired
    public AdminController(IRoomService roomService, ISpecialAuthorityService authorityService) {
        this.roomService = roomService;
        this.authorityService = authorityService;
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String getAdminPanel() {
        return "adminPanel";
    }

    @RequestMapping(value = "/admin/rooms", method = RequestMethod.GET)
    public String getAdminRoomsPanel(Model model) {
        RoomForm roomForm = new RoomForm();
        model.addAttribute("roomForm", roomForm);
        return "roomsPanel";
    }

    @RequestMapping(value = "/admin/rooms", method = RequestMethod.POST)
    public String postRoomForm(@Valid RoomForm roomForm, BindingResult result) {
        if (result.hasErrors()) {
            return "roomsPanel";
        }
        Room room = new Room(roomForm.getRoomName());
        roomService.save(room);
        return "redirect:/admin";
    }
}
