package itomy.sigterra.web.rest;

import itomy.sigterra.SigterraWebApp;

import itomy.sigterra.domain.TabType;
import itomy.sigterra.repository.TabTypeRepository;

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
 * Test class for the TabTypeResource REST controller.
 *
 * @see TabTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SigterraWebApp.class)
public class TabTypeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    @Inject
    private TabTypeRepository tabTypeRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restTabTypeMockMvc;

    private TabType tabType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TabTypeResource tabTypeResource = new TabTypeResource();
        ReflectionTestUtils.setField(tabTypeResource, "tabTypeRepository", tabTypeRepository);
        this.restTabTypeMockMvc = MockMvcBuilders.standaloneSetup(tabTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TabType createEntity(EntityManager em) {
        TabType tabType = new TabType()
                .name(DEFAULT_NAME)
                .type(DEFAULT_TYPE)
                .path(DEFAULT_PATH);
        return tabType;
    }

    @Before
    public void initTest() {
        tabType = createEntity(em);
    }

    @Test
    @Transactional
    public void createTabType() throws Exception {
        int databaseSizeBeforeCreate = tabTypeRepository.findAll().size();

        // Create the TabType

        restTabTypeMockMvc.perform(post("/api/tab-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tabType)))
                .andExpect(status().isCreated());

        // Validate the TabType in the database
        List<TabType> tabTypes = tabTypeRepository.findAll();
        assertThat(tabTypes).hasSize(databaseSizeBeforeCreate + 1);
        TabType testTabType = tabTypes.get(tabTypes.size() - 1);
        assertThat(testTabType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTabType.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testTabType.getPath()).isEqualTo(DEFAULT_PATH);
    }

    @Test
    @Transactional
    public void getAllTabTypes() throws Exception {
        // Initialize the database
        tabTypeRepository.saveAndFlush(tabType);

        // Get all the tabTypes
        restTabTypeMockMvc.perform(get("/api/tab-types?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(tabType.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH.toString())));
    }

    @Test
    @Transactional
    public void getTabType() throws Exception {
        // Initialize the database
        tabTypeRepository.saveAndFlush(tabType);

        // Get the tabType
        restTabTypeMockMvc.perform(get("/api/tab-types/{id}", tabType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tabType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTabType() throws Exception {
        // Get the tabType
        restTabTypeMockMvc.perform(get("/api/tab-types/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTabType() throws Exception {
        // Initialize the database
        tabTypeRepository.saveAndFlush(tabType);
        int databaseSizeBeforeUpdate = tabTypeRepository.findAll().size();

        // Update the tabType
        TabType updatedTabType = tabTypeRepository.findOne(tabType.getId());
        updatedTabType
                .name(UPDATED_NAME)
                .type(UPDATED_TYPE)
                .path(UPDATED_PATH);

        restTabTypeMockMvc.perform(put("/api/tab-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTabType)))
                .andExpect(status().isOk());

        // Validate the TabType in the database
        List<TabType> tabTypes = tabTypeRepository.findAll();
        assertThat(tabTypes).hasSize(databaseSizeBeforeUpdate);
        TabType testTabType = tabTypes.get(tabTypes.size() - 1);
        assertThat(testTabType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTabType.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTabType.getPath()).isEqualTo(UPDATED_PATH);
    }

    @Test
    @Transactional
    public void deleteTabType() throws Exception {
        // Initialize the database
        tabTypeRepository.saveAndFlush(tabType);
        int databaseSizeBeforeDelete = tabTypeRepository.findAll().size();

        // Get the tabType
        restTabTypeMockMvc.perform(delete("/api/tab-types/{id}", tabType.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TabType> tabTypes = tabTypeRepository.findAll();
        assertThat(tabTypes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
