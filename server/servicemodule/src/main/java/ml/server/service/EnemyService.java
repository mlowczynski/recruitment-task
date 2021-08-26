package ml.server.service;

import ml.server.model.Enemy;
import ml.server.repository.EnemyRepository;
import ml.server.model.exception.EnemyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class EnemyService {

    private final EnemyRepository enemyRepository;

    @Autowired
    public EnemyService(EnemyRepository enemyRepository) {
        this.enemyRepository = enemyRepository;
    }

    public Enemy addEnemy(Enemy enemy) {
        return enemyRepository.save(enemy);
    }

    public List<Enemy> getEnemies() {
        return StreamSupport
                .stream(enemyRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Enemy getEnemy(Long id) {
        return enemyRepository.findById(id).orElseThrow(() ->
                new EnemyNotFoundException(id));
    }

    public Enemy deleteEnemy(Long id) {
        Enemy enemy = getEnemy(id);
        enemyRepository.delete(enemy);
        return enemy;
    }

    @Transactional
    public Enemy editEnemy(Long id, Enemy enemy) {
        Enemy enemyToEdit = getEnemy(id);
        enemyToEdit.setName(enemy.getName());
        return enemyToEdit;
    }

    public List<Enemy> searchEnemies(String keyword) {
        return enemyRepository.search(keyword);
    }
}
