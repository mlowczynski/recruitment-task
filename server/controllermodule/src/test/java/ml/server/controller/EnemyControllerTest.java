package ml.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ml.server.model.Enemy;
import ml.server.model.exception.EnemyNotFoundException;
import ml.server.service.EnemyService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(EnemyControllerTest.class)
@ContextConfiguration(classes = EnemyController.class)
public class EnemyControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @MockBean
    EnemyService enemyService;

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
    }

    @Test
    public void  SHOULD_return_all_enemies_WHEN_getEnemies() throws Exception {
        List<Enemy> enemies = new ArrayList<>(Arrays.asList(ENEMY_1, ENEMY_2, ENEMY_3));

        Mockito.when(enemyService.getEnemies()).thenReturn(enemies);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/enemies")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[1].name", is("Killer")));
    }

    @Test
    public void  SHOULD_return_enemy_WHEN_getEnemy() throws Exception {
        Mockito.when(enemyService.getEnemy(ENEMY_1.getId())).thenReturn(ENEMY_1);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/enemies/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("Bad guy")));
    }

    @Test
    public void  SHOULD_conflict_WHEN_enemy_not_found() throws Exception {
        Mockito.when(enemyService.getEnemy(10l)).thenThrow(new EnemyNotFoundException(10l));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/enemies/10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$", is("Enemy not found")));
    }

    @Test
    public void  SHOULD_return_created_enemy_WHEN_addEnemy() throws Exception {
        Mockito.when(enemyService.addEnemy(Mockito.any(Enemy.class))).thenReturn(ENEMY_3);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post("/enemies")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(ENEMY_3));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("Thief")));
    }

    @Test
    public void  SHOULD_return_edited_enemy_WHEN_editEnemy() throws Exception {
        Enemy editedEnemy = new Enemy();
        editedEnemy.setName("Not bad guy");
        editedEnemy.setId(1l);

        Mockito.when(enemyService.editEnemy(Mockito.any(Long.class), Mockito.any(Enemy.class))).thenReturn(editedEnemy);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .put("/enemies/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(ENEMY_1));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Not bad guy")));
    }

    @Test
    public void  SHOULD_delete_enemy_WHEN_deleteEnemy() throws Exception {
        Mockito.when(enemyService.deleteEnemy(ENEMY_2.getId())).thenReturn(ENEMY_2);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/enemies/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void SHOULD_return_enemies_with_i_inside_WHEN_search_i() throws Exception {
        List<Enemy> enemies = new ArrayList<>(Arrays.asList(ENEMY_2, ENEMY_3));
        Mockito.when(enemyService.searchEnemies("i")).thenReturn(enemies);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .get("/enemies/search")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("q", "i");

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(2)))
                .andExpect(jsonPath("$[0].name", is("Killer")));
    }

    @Test
    public void SHOULD_return_enemies_with_1_inside_id_WHEN_search_1() throws Exception {
        List<Enemy> enemies = new ArrayList<>(Arrays.asList(ENEMY_1));
        Mockito.when(enemyService.searchEnemies("1")).thenReturn(enemies);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .get("/enemies/search")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("q", "1");

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Bad guy")));
    }
}