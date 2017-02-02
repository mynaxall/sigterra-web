package itomy.sigterra.web.rest;

import itomy.sigterra.SigterraWebApp;

import itomy.sigterra.domain.ItemData;
import itomy.sigterra.repository.ItemDataRepository;

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
 * Test class for the ItemDataResource REST controller.
 *
 * @see ItemDataResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SigterraWebApp.class)
public class ItemDataResourceIntTest {

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_MODIFIED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MODIFIED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_FIRST_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_IMAGE = "BBBBBBBBBB";

    private static final String DEFAULT_SECOND_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_SECOND_IMAGE = "BBBBBBBBBB";

    private static final String DEFAULT_THIRD_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_THIRD_IMAGE = "BBBBBBBBBB";

    private static final String DEFAULT_LINK = "AAAAAAAAAA";
    private static final String UPDATED_LINK = "BBBBBBBBBB";

    @Inject
    private ItemDataRepository itemDataRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restItemDataMockMvc;

    private ItemData itemData;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ItemDataResource itemDataResource = new ItemDataResource();
        ReflectionTestUtils.setField(itemDataResource, "itemDataRepository", itemDataRepository);
        this.restItemDataMockMvc = MockMvcBuilders.standaloneSetup(itemDataResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemData createEntity(EntityManager em) {
        ItemData itemData = new ItemData()
                .createdDate(DEFAULT_CREATED_DATE)
                .modifiedDate(DEFAULT_MODIFIED_DATE)
                .firstImage(DEFAULT_FIRST_IMAGE)
                .secondImage(DEFAULT_SECOND_IMAGE)
                .thirdImage(DEFAULT_THIRD_IMAGE)
                .link(DEFAULT_LINK);
        return itemData;
    }

    @Before
    public void initTest() {
        itemData = createEntity(em);
    }

    @Test
    @Transactional
    public void createItemData() throws Exception {
        int databaseSizeBeforeCreate = itemDataRepository.findAll().size();

        // Create the ItemData

        restItemDataMockMvc.perform(post("/api/item-data")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(itemData)))
                .andExpect(status().isCreated());

        // Validate the ItemData in the database
        List<ItemData> itemData = itemDataRepository.findAll();
        assertThat(itemData).hasSize(databaseSizeBeforeCreate + 1);
        ItemData testItemData = itemData.get(itemData.size() - 1);
        assertThat(testItemData.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testItemData.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
        assertThat(testItemData.getFirstImage()).isEqualTo(DEFAULT_FIRST_IMAGE);
        assertThat(testItemData.getSecondImage()).isEqualTo(DEFAULT_SECOND_IMAGE);
        assertThat(testItemData.getThirdImage()).isEqualTo(DEFAULT_THIRD_IMAGE);
        assertThat(testItemData.getLink()).isEqualTo(DEFAULT_LINK);
    }

    @Test
    @Transactional
    public void getAllItemData() throws Exception {
        // Initialize the database
        itemDataRepository.saveAndFlush(itemData);

        // Get all the itemData
        restItemDataMockMvc.perform(get("/api/item-data?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(itemData.getId().intValue())))
                .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
                .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())))
                .andExpect(jsonPath("$.[*].firstImage").value(hasItem(DEFAULT_FIRST_IMAGE.toString())))
                .andExpect(jsonPath("$.[*].secondImage").value(hasItem(DEFAULT_SECOND_IMAGE.toString())))
                .andExpect(jsonPath("$.[*].thirdImage").value(hasItem(DEFAULT_THIRD_IMAGE.toString())))
                .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK.toString())));
    }

    @Test
    @Transactional
    public void getItemData() throws Exception {
        // Initialize the database
        itemDataRepository.saveAndFlush(itemData);

        // Get the itemData
        restItemDataMockMvc.perform(get("/api/item-data/{id}", itemData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(itemData.getId().intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.firstImage").value(DEFAULT_FIRST_IMAGE.toString()))
            .andExpect(jsonPath("$.secondImage").value(DEFAULT_SECOND_IMAGE.toString()))
            .andExpect(jsonPath("$.thirdImage").value(DEFAULT_THIRD_IMAGE.toString()))
            .andExpect(jsonPath("$.link").value(DEFAULT_LINK.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingItemData() throws Exception {
        // Get the itemData
        restItemDataMockMvc.perform(get("/api/item-data/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateItemData() throws Exception {
        // Initialize the database
        itemDataRepository.saveAndFlush(itemData);
        int databaseSizeBeforeUpdate = itemDataRepository.findAll().size();

        // Update the itemData
        ItemData updatedItemData = itemDataRepository.findOne(itemData.getId());
        updatedItemData
                .createdDate(UPDATED_CREATED_DATE)
                .modifiedDate(UPDATED_MODIFIED_DATE)
                .firstImage(UPDATED_FIRST_IMAGE)
                .secondImage(UPDATED_SECOND_IMAGE)
                .thirdImage(UPDATED_THIRD_IMAGE)
                .link(UPDATED_LINK);

        restItemDataMockMvc.perform(put("/api/item-data")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedItemData)))
                .andExpect(status().isOk());

        // Validate the ItemData in the database
        List<ItemData> itemData = itemDataRepository.findAll();
        assertThat(itemData).hasSize(databaseSizeBeforeUpdate);
        ItemData testItemData = itemData.get(itemData.size() - 1);
        assertThat(testItemData.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testItemData.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
        assertThat(testItemData.getFirstImage()).isEqualTo(UPDATED_FIRST_IMAGE);
        assertThat(testItemData.getSecondImage()).isEqualTo(UPDATED_SECOND_IMAGE);
        assertThat(testItemData.getThirdImage()).isEqualTo(UPDATED_THIRD_IMAGE);
        assertThat(testItemData.getLink()).isEqualTo(UPDATED_LINK);
    }

    @Test
    @Transactional
    public void deleteItemData() throws Exception {
        // Initialize the database
        itemDataRepository.saveAndFlush(itemData);
        int databaseSizeBeforeDelete = itemDataRepository.findAll().size();

        // Get the itemData
        restItemDataMockMvc.perform(delete("/api/item-data/{id}", itemData.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ItemData> itemData = itemDataRepository.findAll();
        assertThat(itemData).hasSize(databaseSizeBeforeDelete - 1);
    }
}
