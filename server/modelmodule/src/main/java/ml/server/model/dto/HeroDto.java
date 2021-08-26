package ml.server.model.dto;

import ml.server.model.Hero;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class HeroDto {
    private Long id;
    private String name;
    private List<EnemyDto> enemiesDto = new ArrayList<>();

    public static HeroDto from(Hero hero) {
        HeroDto heroDto = new HeroDto();
        heroDto.setId(hero.getId());
        heroDto.setName(hero.getName());
        heroDto.setEnemiesDto(hero.getEnemies().stream().map(EnemyDto::from).collect(Collectors.toList()));
        return heroDto;
    }
}
