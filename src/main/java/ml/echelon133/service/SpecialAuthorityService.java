package ml.echelon133.service;

import ml.echelon133.model.SpecialAuthority;
import ml.echelon133.repository.SpecialAuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SpecialAuthorityService implements ISpecialAuthorityService {

    private SpecialAuthorityRepository authorityRepository;

    @Autowired
    public SpecialAuthorityService(SpecialAuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Override
    public SpecialAuthority save(SpecialAuthority authority) {
        return authorityRepository.save(authority);
    }

    @Override
    public List<SpecialAuthority> getAll() {
        List<SpecialAuthority> authorities = new ArrayList<>();
        authorityRepository.findAll().forEach(authorities::add);
        return authorities;
    }

    @Override
    public List<SpecialAuthority> getAllByUsername(String username) {
        List<SpecialAuthority> authorities = new ArrayList<>();
        authorityRepository.findAllByUsername(username).forEach(authorities::add);
        return authorities;
    }

    @Override
    public boolean deleteById(String id) {
        authorityRepository.deleteById(id);
        return !authorityRepository.existsById(id);
    }
}
