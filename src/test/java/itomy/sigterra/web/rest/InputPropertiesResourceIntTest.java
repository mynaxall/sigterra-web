package itomy.sigterra.web.rest;

import itomy.sigterra.SigterraWebApp;

import itomy.sigterra.domain.InputProperties;
import itomy.sigterra.repository.InputPropertiesRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the InputPropertiesResource REST controller.
 *
 * @see InputPropertiesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SigterraWebApp.class)
public class InputPropertiesResourceIntTest {

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_BOLD = false;
    private static final Boolean UPDATED_BOLD = true;

    private static final Boolean DEFAULT_ITALIC = false;
    private static final Boolean UPDATED_ITALIC = true;

    private static final Boolean DEFAULT_UNDERLINE = false;
    private static final Boolean UPDATED_UNDERLINE = true;

    @Inject
    private InputPropertiesRepository inputPropertiesRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restInputPropertiesMockMvc;

    private InputProperties inputProperties;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InputPropertiesResource inputPropertiesResource = new InputPropertiesResource();
        ReflectionTestUtils.setField(inputPropertiesResource, "inputPropertiesRepository", inputPropertiesRepository);
        this.restInputPropertiesMockMvc = MockMvcBuilders.standaloneSetup(inputPropertiesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InputProperties createEntity(EntityManager em) {
        InputProperties inputProperties = new InputProperties()
                .value(DEFAULT_VALUE)
                .bold(DEFAULT_BOLD)
                .italic(DEFAULT_ITALIC)
                .underline(DEFAULT_UNDERLINE);
        return inputProperties;
    }

    @Before
    public void initTest() {
        inputProperties = createEntity(em);
    }

    @Test
    @Transactional
    public void createInputProperties() throws Exception {
        int databaseSizeBeforeCreate = inputPropertiesRepository.findAll().size();

        // Create the InputProperties

        restInputPropertiesMockMvc.perform(post("/api/input-properties")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(inputProperties)))
                .andExpect(status().isCreated());

        // Validate the InputProperties in the database
        List<InputProperties> inputProperties = inputPropertiesRepository.findAll();
        assertThat(inputProperties).hasSize(databaseSizeBeforeCreate + 1);
        InputProperties testInputProperties = inputProperties.get(inputProperties.size() - 1);
        assertThat(testInputProperties.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testInputProperties.isBold()).isEqualTo(DEFAULT_BOLD);
        assertThat(testInputProperties.isItalic()).isEqualTo(DEFAULT_ITALIC);
        assertThat(testInputProperties.isUnderline()).isEqualTo(DEFAULT_UNDERLINE);
    }

    @Test
    @Transactional
    public void getAllInputProperties() throws Exception {
        // Initialize the database
        inputPropertiesRepository.saveAndFlush(inputProperties);

        // Get all the inputProperties
        restInputPropertiesMockMvc.perform(get("/api/input-properties?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(inputProperties.getId().intValue())))
                .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())))
                .andExpect(jsonPath("$.[*].bold").value(hasItem(DEFAULT_BOLD.booleanValue())))
                .andExpect(jsonPath("$.[*].italic").value(hasItem(DEFAULT_ITALIC.booleanValue())))
                .andExpect(jsonPath("$.[*].underline").value(hasItem(DEFAULT_UNDERLINE.booleanValue())));
    }

    @Test
    @Transactional
    public void getInputProperties() throws Exception {
        // Initialize the database
        inputPropertiesRepository.saveAndFlush(inputProperties);

        // Get the inputProperties
        restInputPropertiesMockMvc.perform(get("/api/input-properties/{id}", inputProperties.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(inputProperties.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()))
            .andExpect(jsonPath("$.bold").value(DEFAULT_BOLD.booleanValue()))
            .andExpect(jsonPath("$.italic").value(DEFAULT_ITALIC.booleanValue()))
            .andExpect(jsonPath("$.underline").value(DEFAULT_UNDERLINE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingInputProperties() throws Exception {
        // Get the inputProperties
        restInputPropertiesMockMvc.perform(get("/api/input-properties/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInputProperties() throws Exception {
        // Initialize the database
        inputPropertiesRepository.saveAndFlush(inputProperties);
        int databaseSizeBeforeUpdate = inputPropertiesRepository.findAll().size();

        // Update the inputProperties
        InputProperties updatedInputProperties = inputPropertiesRepository.findOne(inputProperties.getId());
        updatedInputProperties
                .value(UPDATED_VALUE)
                .bold(UPDATED_BOLD)
                .italic(UPDATED_ITALIC)
                .underline(UPDATED_UNDERLINE);

        restInputPropertiesMockMvc.perform(put("/api/input-properties")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedInputProperties)))
                .andExpect(status().isOk());

        // Validate the InputProperties in the database
        List<InputProperties> inputProperties = inputPropertiesRepository.findAll();
        assertThat(inputProperties).hasSize(databaseSizeBeforeUpdate);
        InputProperties testInputProperties = inputProperties.get(inputProperties.size() - 1);
        assertThat(testInputProperties.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testInputProperties.isBold()).isEqualTo(UPDATED_BOLD);
        assertThat(testInputProperties.isItalic()).isEqualTo(UPDATED_ITALIC);
        assertThat(testInputProperties.isUnderline()).isEqualTo(UPDATED_UNDERLINE);
    }

    @Test
    @Transactional
    public void deleteInputProperties() throws Exception {
        // Initialize the database
        inputPropertiesRepository.saveAndFlush(inputProperties);
        int databaseSizeBeforeDelete = inputPropertiesRepository.findAll().size();

        // Get the inputProperties
        restInputPropertiesMockMvc.perform(delete("/api/input-properties/{id}", inputProperties.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<InputProperties> inputProperties = inputPropertiesRepository.findAll();
        assertThat(inputProperties).hasSize(databaseSizeBeforeDelete - 1);
    }
}
