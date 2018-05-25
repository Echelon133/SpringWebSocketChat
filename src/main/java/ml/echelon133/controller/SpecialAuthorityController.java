package ml.echelon133.controller;

import ml.echelon133.model.SpecialAuthority;
import ml.echelon133.service.ISpecialAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SpecialAuthorityController {

    private ISpecialAuthorityService authorityService;

    @Autowired
    public SpecialAuthorityController(ISpecialAuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    @RequestMapping(value = "/json/special-authorities", method = RequestMethod.GET)
    public List<SpecialAuthority> getSpecialAuthorities() {
        return authorityService.getAll();
    }
}
