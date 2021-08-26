package ml.server.repository;

import ml.server.model.Enemy;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnemyRepository extends CrudRepository<Enemy, Long> {
    @Query("SELECT e FROM Enemy e WHERE e.name LIKE %?1%"
            + " OR CONCAT(e.id, '') LIKE %?1%")
    public List<Enemy> search(String keyword);
}
