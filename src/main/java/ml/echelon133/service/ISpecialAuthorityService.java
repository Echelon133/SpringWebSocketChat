package ml.echelon133.service;

import ml.echelon133.model.SpecialAuthority;

import java.util.List;

public interface ISpecialAuthorityService {
    SpecialAuthority save(SpecialAuthority authority);
    List<SpecialAuthority> getAll();
    List<SpecialAuthority> getAllByUsername(String username);
    boolean deleteById(String id);
}
