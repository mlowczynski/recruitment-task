package ml.server.model.dto;

import ml.server.model.Hero;
import lombok.Data;

@Data
public class HeroNoEnemiesDto {
    private Long id;
    private String name;

    public static HeroNoEnemiesDto from(Hero hero) {
        HeroNoEnemiesDto heroNoEnemiesDto = new HeroNoEnemiesDto();
        heroNoEnemiesDto.setId(hero.getId());
        heroNoEnemiesDto.setName(hero.getName());
        return heroNoEnemiesDto;
    }
}
