package ml.echelon133.controller;

import ml.echelon133.service.IRoomService;
import ml.echelon133.service.ISpecialAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
}
