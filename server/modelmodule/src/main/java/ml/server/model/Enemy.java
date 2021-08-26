package ml.server.model;

import ml.server.model.dto.EnemyDto;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "Enemy")
public class Enemy {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @ManyToOne
    private Hero hero;

    public static Enemy from(EnemyDto enemyDto) {
        Enemy enemy = new Enemy();
        enemy.setName(enemyDto.getName());
        return enemy;
    }
}
