package ml.server.repository;

import ml.server.model.Enemy;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EnemyRepository extends CrudRepository<Enemy, Long> {
    @Query("SELECT e FROM Enemy e WHERE e.name LIKE %?1%"
            + " OR CONCAT(e.id, '') LIKE %?1%")
    public List<Enemy> search(String keyword);
}
