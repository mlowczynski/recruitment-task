package ml.server.repository;

import ml.server.model.Hero;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HeroRepository extends CrudRepository<Hero, Long> {
    @Query("SELECT h FROM Hero h WHERE h.name LIKE %?1%"
            + " OR CONCAT(h.id, '') LIKE %?1%")
    public List<Hero> search(String keyword);
}
