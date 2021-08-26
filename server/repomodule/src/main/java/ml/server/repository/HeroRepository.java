package ml.server.repository;

import ml.server.model.Hero;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HeroRepository extends CrudRepository<Hero, Long> {
    @Query("SELECT h FROM Hero h WHERE h.name LIKE %?1%"
            + " OR CONCAT(h.id, '') LIKE %?1%")
    public List<Hero> search(String keyword);
}
