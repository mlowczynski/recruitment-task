package ml.server.service;

import ml.server.model.Enemy;
import ml.server.model.Hero;
import ml.server.repository.HeroRepository;
import ml.server.model.exception.EnemyIsAlreadyAssignedException;
import ml.server.model.exception.HeroNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class HeroService {

    private final HeroRepository heroRepository;
    private final EnemyService enemyService;

    @Autowired
    public HeroService(HeroRepository heroRepository, EnemyService enemyService) {
        this.heroRepository = heroRepository;
        this.enemyService = enemyService;
    }

    public Hero addHero(Hero hero) {
        return heroRepository.save(hero);
    }

    public List<Hero> getHeroes() {
        return StreamSupport
                .stream(heroRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Hero getHero(Long id) {
        return heroRepository.findById(id).orElseThrow(() ->
                new HeroNotFoundException(id));
    }

    public Hero deleteHero(Long id) {
        Hero hero = getHero(id);
        heroRepository.delete(hero);
        return hero;
    }

    @Transactional
    public Hero editHero(Long id, Hero hero) {
        Hero heroToEdit = getHero(id);
        heroToEdit.setName(hero.getName());
        return heroToEdit;
    }

    @Transactional
    public Hero addEnemyToHero(Long heroId, Long enemyId) {
        Hero hero = getHero(heroId);
        Enemy enemy = enemyService.getEnemy(enemyId);
        if (Objects.nonNull(enemy.getHero())) {
            throw new EnemyIsAlreadyAssignedException(enemyId,
                    enemy.getHero().getId());
        }
        hero.addEnemy(enemy);
        enemy.setHero(hero);
        return hero;
    }

    @Transactional
    public Hero removeEnemyFromHero(Long heroId, Long enemyId) {
        Hero hero = getHero(heroId);
        Enemy enemy = enemyService.getEnemy(enemyId);
        hero.removeEnemy(enemy);
        return hero;
    }

    public List<Hero> searchHeroes(String keyword) {
        return heroRepository.search(keyword);
    }
}
