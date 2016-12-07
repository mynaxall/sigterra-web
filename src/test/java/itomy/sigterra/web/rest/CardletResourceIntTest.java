package itomy.sigterra.web.rest;

import itomy.sigterra.SigterraWebApp;

import itomy.sigterra.domain.Cardlet;
import itomy.sigterra.repository.CardletRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CardletResource REST controller.
 *
 * @see CardletResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SigterraWebApp.class)
public class CardletResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_MODIFIED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MODIFIED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Inject
    private CardletRepository cardletRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCardletMockMvc;

    private Cardlet cardlet;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CardletResource cardletResource = new CardletResource();
        ReflectionTestUtils.setField(cardletResource, "cardletRepository", cardletRepository);
        this.restCardletMockMvc = MockMvcBuilders.standaloneSetup(cardletResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cardlet createEntity(EntityManager em) {
        Cardlet cardlet = new Cardlet()
                .name(DEFAULT_NAME)
                .createdDate(DEFAULT_CREATED_DATE)
                .modifiedDate(DEFAULT_MODIFIED_DATE)
                .active(DEFAULT_ACTIVE);
        return cardlet;
    }

    @Before
    public void initTest() {
        cardlet = createEntity(em);
    }

    @Test
    @Transactional
    public void createCardlet() throws Exception {
        int databaseSizeBeforeCreate = cardletRepository.findAll().size();

        // Create the Cardlet

        restCardletMockMvc.perform(post("/api/cardlets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cardlet)))
                .andExpect(status().isCreated());

        // Validate the Cardlet in the database
        List<Cardlet> cardlets = cardletRepository.findAll();
        assertThat(cardlets).hasSize(databaseSizeBeforeCreate + 1);
        Cardlet testCardlet = cardlets.get(cardlets.size() - 1);
        assertThat(testCardlet.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCardlet.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testCardlet.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
        assertThat(testCardlet.isActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllCardlets() throws Exception {
        // Initialize the database
        cardletRepository.saveAndFlush(cardlet);

        // Get all the cardlets
        restCardletMockMvc.perform(get("/api/cardlets?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(cardlet.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
                .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())))
                .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void getCardlet() throws Exception {
        // Initialize the database
        cardletRepository.saveAndFlush(cardlet);

        // Get the cardlet
        restCardletMockMvc.perform(get("/api/cardlets/{id}", cardlet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cardlet.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCardlet() throws Exception {
        // Get the cardlet
        restCardletMockMvc.perform(get("/api/cardlets/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCardlet() throws Exception {
        // Initialize the database
        cardletRepository.saveAndFlush(cardlet);
        int databaseSizeBeforeUpdate = cardletRepository.findAll().size();

        // Update the cardlet
        Cardlet updatedCardlet = cardletRepository.findOne(cardlet.getId());
        updatedCardlet
                .name(UPDATED_NAME)
                .createdDate(UPDATED_CREATED_DATE)
                .modifiedDate(UPDATED_MODIFIED_DATE)
                .active(UPDATED_ACTIVE);

        restCardletMockMvc.perform(put("/api/cardlets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCardlet)))
                .andExpect(status().isOk());

        // Validate the Cardlet in the database
        List<Cardlet> cardlets = cardletRepository.findAll();
        assertThat(cardlets).hasSize(databaseSizeBeforeUpdate);
        Cardlet testCardlet = cardlets.get(cardlets.size() - 1);
        assertThat(testCardlet.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCardlet.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testCardlet.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
        assertThat(testCardlet.isActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void deleteCardlet() throws Exception {
        // Initialize the database
        cardletRepository.saveAndFlush(cardlet);
        int databaseSizeBeforeDelete = cardletRepository.findAll().size();

        // Get the cardlet
        restCardletMockMvc.perform(delete("/api/cardlets/{id}", cardlet.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Cardlet> cardlets = cardletRepository.findAll();
        assertThat(cardlets).hasSize(databaseSizeBeforeDelete - 1);
    }
}
