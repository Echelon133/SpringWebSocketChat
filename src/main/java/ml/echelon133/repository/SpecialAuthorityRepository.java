package ml.echelon133.repository;

import ml.echelon133.model.SpecialAuthority;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SpecialAuthorityRepository extends CrudRepository<SpecialAuthority, String> {
    List<SpecialAuthority> findAllByUsername(String username);
}
