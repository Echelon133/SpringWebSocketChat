package ml.echelon133.controller;

import ml.echelon133.model.Room;
import ml.echelon133.model.SpecialAuthority;
import ml.echelon133.model.form.RoomForm;
import ml.echelon133.model.form.SpecialAuthorityForm;
import ml.echelon133.service.IRoomService;
import ml.echelon133.service.ISpecialAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

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

    @RequestMapping(value = "/admin/rooms/{id}", method = RequestMethod.DELETE)
    public @ResponseBody Map<String, Boolean> deleteRoomWithId(@PathVariable String id) {
        Map<String, Boolean> response = new HashMap<>();
        Boolean wasDeleted = roomService.deleteById(id);
        response.put("deleted", wasDeleted);
        return response;
    }

    @RequestMapping(value = "/admin/authorities", method = RequestMethod.GET)
    public String getAdminAuthoritiesPanel(Model model) {
        SpecialAuthorityForm specialAuthorityForm = new SpecialAuthorityForm();
        model.addAttribute("specialAuthorityForm", specialAuthorityForm);
        return "authoritiesPanel";
    }

    @RequestMapping(value = "/admin/authorities", method = RequestMethod.POST)
    public String postAuthoritiesForm(@Valid SpecialAuthorityForm specialAuthorityForm, BindingResult result) {
        if (result.hasErrors()) {
            return "authoritiesPanel";
        }
        String username = specialAuthorityForm.getUsername();
        String authority = specialAuthorityForm.getAuthority();
        SpecialAuthority specialAuthority = new SpecialAuthority(username, authority);
        authorityService.save(specialAuthority);
        return "redirect:/admin";
    }
}
