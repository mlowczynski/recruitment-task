package ml.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ml.server.model.Enemy;
import ml.server.model.Hero;
import ml.server.model.exception.EnemyIsAlreadyAssignedException;
import ml.server.model.exception.EnemyNotFoundException;
import ml.server.model.exception.HeroNotFoundException;
import ml.server.service.HeroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HeroControllerTest.class)
@ContextConfiguration(classes = HeroController.class)
public class HeroControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @MockBean
    HeroService heroService;

    Hero HERO_1 = new Hero();
    Hero HERO_2 = new Hero();
    Hero HERO_3 = new Hero();
    Enemy ENEMY_1 = new Enemy();
    Enemy ENEMY_2 = new Enemy();
    Enemy ENEMY_3 = new Enemy();

    @BeforeEach
    public void init() {
        ENEMY_1.setId(1l);
        ENEMY_1.setName("Bad guy");

        ENEMY_2.setId(2l);
        ENEMY_2.setName("Killer");

        ENEMY_3.setId(3l);
        ENEMY_3.setName("Thief");

        HERO_1.setId(4l);
        HERO_1.setName("Wonder Woman");

        HERO_2.setId(5l);
        HERO_2.setName("Wolverine");

        HERO_3.setId(6l);
        HERO_3.setName("Spider-Man");
    }

    @Test
    public void  SHOULD_return_all_heroes_WHEN_getHeroes() throws Exception {
        List<Hero> heroes = new ArrayList<>(Arrays.asList(HERO_1, HERO_2, HERO_3));

        Mockito.when(heroService.getHeroes()).thenReturn(heroes);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/heroes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[1].name", is("Wolverine")));
    }

    @Test
    public void  SHOULD_return_hero_WHEN_getHero() throws Exception {
        Mockito.when(heroService.getHero(HERO_1.getId())).thenReturn(HERO_1);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/heroes/4")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("Wonder Woman")));
    }

    @Test
    public void  SHOULD_conflict_WHEN_hero_not_found() throws Exception {
        Mockito.when(heroService.getHero(10l)).thenThrow(new HeroNotFoundException(10l));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/heroes/10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$", is("Hero not found")));
    }

    @Test
    public void  SHOULD_return_created_hero_WHEN_addHero() throws Exception {
        Mockito.when(heroService.addHero(Mockito.any(Hero.class))).thenReturn(HERO_3);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post("/heroes")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(ENEMY_3));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("Spider-Man")));
    }

    @Test
    public void  SHOULD_return_edited_hero_WHEN_editHero() throws Exception {
        Hero editedHero = new Hero();
        editedHero.setName("Harley Quinn");
        editedHero.setId(4l);

        Mockito.when(heroService.editHero(Mockito.any(Long.class), Mockito.any(Hero.class))).thenReturn(editedHero);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .put("/heroes/4")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(editedHero));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id", is(4)))
                .andExpect(jsonPath("$.name", is("Harley Quinn")));
    }

    @Test
    public void  SHOULD_delete_hero_WHEN_deleteHero() throws Exception {
        Mockito.when(heroService.deleteHero(HERO_2.getId())).thenReturn(HERO_2);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/heroes/5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void  SHOULD_add_enemy_to_hero_WHEN_addEnemyToHero() throws Exception {
        ENEMY_3.setHero(HERO_3);
        HERO_3.addEnemy(ENEMY_3);
        Mockito.when(heroService.addEnemyToHero(HERO_3.getId(), ENEMY_3.getId())).thenReturn(HERO_3);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/heroes/6/enemies/3/add")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id", is(6)))
                .andExpect(jsonPath("$.name", is("Spider-Man")))
                .andExpect(jsonPath("$.enemiesDto[0].id", is(3)))
                .andExpect(jsonPath("$.enemiesDto[0].name", is("Thief")));
    }

    @Test
    public void  SHOULD_remove_enemy_from_hero_WHEN_removeEnemyFromHero() throws Exception {
        Mockito.when(heroService.removeEnemyFromHero(HERO_1.getId(), ENEMY_1.getId())).thenReturn(HERO_2);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/heroes/4/enemies/1/remove")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void  SHOULD_conflict_WHEN_enemy_not_found() throws Exception {
        Mockito.when(heroService.addEnemyToHero(Mockito.any(Long.class), Mockito.any(Long.class))).thenThrow(new EnemyNotFoundException(11l));

        mockMvc.perform(MockMvcRequestBuilders
                .post("/heroes/10/enemies/11/add")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$", is("Enemy not found")));
    }

    @Test
    public void  SHOULD_conflict_WHEN_enemy_already_assigned() throws Exception {
        Mockito.when(heroService.addEnemyToHero(Mockito.any(Long.class), Mockito.any(Long.class))).thenThrow(new EnemyIsAlreadyAssignedException(1l, 4l));

        mockMvc.perform(MockMvcRequestBuilders
                .post("/heroes/4/enemies/1/add")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$", is("Enemy is already assigned to this hero")));
    }

    @Test
    public void SHOULD_return_heroes_with_der_inside_WHEN_search_der() throws Exception {
        List<Hero> heroes = new ArrayList<>(Arrays.asList(HERO_1, HERO_3));
        Mockito.when(heroService.searchHeroes("der")).thenReturn(heroes);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .get("/heroes/search")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("q", "der");

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(4)))
                .andExpect(jsonPath("$[0].name", is("Wonder Woman")));
    }

    @Test
    public void SHOULD_return_heroes_with_5_inside_id_WHEN_search_5() throws Exception {
        List<Hero> heroes = new ArrayList<>(Arrays.asList(HERO_2));
        Mockito.when(heroService.searchHeroes("5")).thenReturn(heroes);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .get("/heroes/search")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("q", "5");

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(5)))
                .andExpect(jsonPath("$[0].name", is("Wolverine")));
    }
}
