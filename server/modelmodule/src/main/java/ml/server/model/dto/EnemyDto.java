package ml.server.model.dto;

import ml.server.model.Enemy;
import lombok.Data;

import java.util.Objects;

@Data
public class EnemyDto {
    private Long id;
    private String name;
    private HeroNoEnemiesDto heroNoEnemiesDto;

    public static EnemyDto from(Enemy enemy) {
        EnemyDto enemyDto = new EnemyDto();
        enemyDto.setId(enemy.getId());
        enemyDto.setName(enemy.getName());
        if (Objects.nonNull(enemy.getHero())) {
            enemyDto.setHeroNoEnemiesDto(HeroNoEnemiesDto.from(enemy.getHero()));
        }
        return enemyDto;
    }
}
