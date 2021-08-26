package ml.server.controller;

import ml.server.model.Hero;
import ml.server.model.dto.HeroDto;
import ml.server.model.exception.EnemyIsAlreadyAssignedException;
import ml.server.model.exception.EnemyNotFoundException;
import ml.server.model.exception.HeroNotFoundException;
import ml.server.service.HeroService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/heroes")
@CrossOrigin(origins = "http://localhost:4200")
public class HeroController {

    Logger logger = LoggerFactory.getLogger(EnemyController.class);
    private final HeroService heroService;

    @Autowired
    public HeroController(HeroService heroService) {
        this.heroService = heroService;
    }

    @PostMapping
    public ResponseEntity<HeroDto> addHero(@RequestBody final HeroDto heroDto) {
        logger.info("Add hero: " + heroDto.getName());
        Hero hero = heroService.addHero(Hero.from(heroDto));
        return new ResponseEntity<>(HeroDto.from(hero), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<HeroDto>> getHeroes() {
        logger.info("Get all heroes");
        List<Hero> heroes = heroService.getHeroes();
        List<HeroDto> heroesDto = heroes.stream().map(HeroDto::from).collect(Collectors.toList());
        return new ResponseEntity<>(heroesDto, HttpStatus.OK);
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<HeroDto> getHero(@PathVariable final Long id) {
        logger.info("Get hero: " + id);
        Hero hero = heroService.getHero(id);
        return new ResponseEntity<>(HeroDto.from(hero), HttpStatus.OK);
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<HeroDto> deleteHero(@PathVariable final Long id) {
        logger.info("Delete hero: " + id);
        Hero hero = heroService.deleteHero(id);
        return new ResponseEntity<>(HeroDto.from(hero), HttpStatus.OK);
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<HeroDto> editHero (@PathVariable final Long id,
                                             @RequestBody final HeroDto heroDto) {
        logger.info("Edit hero: " + id);
        Hero editedHero = heroService.editHero(id, Hero.from(heroDto));
        return new ResponseEntity<>(HeroDto.from(editedHero), HttpStatus.OK);
    }

    @PostMapping(value = "{heroId}/enemies/{enemyId}/add")
    public ResponseEntity<HeroDto> addEnemyToHero(@PathVariable final Long heroId,
                                                  @PathVariable final Long enemyId) {
        logger.info("Add enemy " + enemyId + " to hero " + heroId);
        Hero hero = heroService.addEnemyToHero(heroId, enemyId);
        return new ResponseEntity<>(HeroDto.from(hero), HttpStatus.OK);
    }

    @DeleteMapping(value = "{heroId}/enemies/{enemyId}/remove")
    public ResponseEntity<HeroDto> removeEnemyFromHero(@PathVariable final Long heroId,
                                                       @PathVariable final Long enemyId) {
        logger.info("Remove enemy " + enemyId + " from hero " + heroId);
        Hero hero = heroService.removeEnemyFromHero(heroId, enemyId);
        return new ResponseEntity<>(HeroDto.from(hero), HttpStatus.OK);
    }

    @ExceptionHandler(value = HeroNotFoundException.class)
    public ResponseEntity handleHeroNotFoundException(HeroNotFoundException e) {
        logger.error(e.getMessage());
        return new ResponseEntity("Hero not found", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = EnemyNotFoundException.class)
    public ResponseEntity handleEnemyNotFoundException(EnemyNotFoundException e) {
        logger.error(e.getMessage());
        return new ResponseEntity("Enemy not found", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = EnemyIsAlreadyAssignedException.class)
    public ResponseEntity EnemyIsAlreadyAssignedException(EnemyIsAlreadyAssignedException e) {
        logger.error(e.getMessage());
        return new ResponseEntity("Enemy is already assigned to this hero", HttpStatus.CONFLICT);
    }

    @GetMapping("search")
    public ResponseEntity<List<HeroDto>> searchHeroes(@RequestParam(value = "q") final String keyword) {
        logger.info("Searching for \"" + keyword + "\" among heroes");
        List<Hero> heroes = heroService.searchHeroes(keyword);
        List<HeroDto> heroesDto = heroes.stream().map(HeroDto::from).collect(Collectors.toList());
        return new ResponseEntity<>(heroesDto, HttpStatus.OK);
    }
}
