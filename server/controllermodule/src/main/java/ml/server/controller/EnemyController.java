package ml.server.controller;

import ml.server.model.Enemy;
import ml.server.model.dto.EnemyDto;
import ml.server.model.exception.EnemyNotFoundException;
import ml.server.service.EnemyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/enemies")
@CrossOrigin(origins = "http://localhost:4200")
public class EnemyController {

    Logger logger = LoggerFactory.getLogger(EnemyController.class);
    private final EnemyService enemyService;

    public EnemyController(EnemyService enemyService) {
        this.enemyService = enemyService;
    }

    @PostMapping
    public ResponseEntity<EnemyDto> addEnemy(@RequestBody final EnemyDto enemyDto) {
        logger.info("Add enemy: " + enemyDto.getName());
        Enemy enemy = enemyService.addEnemy(Enemy.from(enemyDto));
        return new ResponseEntity<>(EnemyDto.from(enemy), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<EnemyDto>> getEnemies() {
        logger.info("Get all enemies");
        List<Enemy> enemies = enemyService.getEnemies();
        List<EnemyDto> enemiesDto = enemies.stream().map(EnemyDto::from).collect(Collectors.toList());
        return new ResponseEntity<>(enemiesDto, HttpStatus.OK);
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<EnemyDto> getEnemy(@PathVariable final Long id) {
        logger.info("Get enemy: " + id);
        Enemy enemy = enemyService.getEnemy(id);
        return new ResponseEntity<>(EnemyDto.from(enemy), HttpStatus.OK);
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<EnemyDto> deleteEnemy(@PathVariable final Long id) {
        logger.info("Delete enemy: " + id);
        Enemy enemy = enemyService.deleteEnemy(id);
        return new ResponseEntity<>(EnemyDto.from(enemy), HttpStatus.OK);
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<EnemyDto> editEnemy(@PathVariable final Long id,
                                              @RequestBody final EnemyDto enemyDto) {
        logger.info("Edit enemy: " + id);
        Enemy editedEnemy = enemyService.editEnemy(id, Enemy.from(enemyDto));
        return new ResponseEntity<>(EnemyDto.from(editedEnemy), HttpStatus.OK);
    }

    @ExceptionHandler(value = EnemyNotFoundException.class)
    public ResponseEntity<String> handleEnemyNotFoundException(EnemyNotFoundException e) {
        logger.error(e.getMessage());
        return new ResponseEntity<String>("Enemy not found", HttpStatus.CONFLICT);
    }

    @GetMapping("search")
    public ResponseEntity<List<EnemyDto>> searchEnemies(@RequestParam(value = "q") final String keyword) {
        logger.info("Searching for \"" + keyword + "\" among enemies");
        List<Enemy> enemies = enemyService.searchEnemies(keyword);
        List<EnemyDto> enemiesDto = enemies.stream().map(EnemyDto::from).collect(Collectors.toList());
        return new ResponseEntity<>(enemiesDto, HttpStatus.OK);
    }
}
