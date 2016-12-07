package itomy.sigterra.web.rest;

import itomy.sigterra.SigterraWebApp;

import itomy.sigterra.domain.Business;
import itomy.sigterra.repository.BusinessRepository;

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
 * Test class for the BusinessResource REST controller.
 *
 * @see BusinessResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SigterraWebApp.class)
public class BusinessResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_MODIFIED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MODIFIED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_PISITION = 1;
    private static final Integer UPDATED_PISITION = 2;

    private static final String DEFAULT_ICON = "AAAAAAAAAA";
    private static final String UPDATED_ICON = "BBBBBBBBBB";

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_JOB_POSITION = "AAAAAAAAAA";
    private static final String UPDATED_JOB_POSITION = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_SITE = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_SITE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_TWITTER = "AAAAAAAAAA";
    private static final String UPDATED_TWITTER = "BBBBBBBBBB";

    private static final String DEFAULT_FACEBOOK = "AAAAAAAAAA";
    private static final String UPDATED_FACEBOOK = "BBBBBBBBBB";

    private static final String DEFAULT_GOOGLE = "AAAAAAAAAA";
    private static final String UPDATED_GOOGLE = "BBBBBBBBBB";

    private static final String DEFAULT_LINKED_IN = "AAAAAAAAAA";
    private static final String UPDATED_LINKED_IN = "BBBBBBBBBB";

    private static final String DEFAULT_PHOTO = "AAAAAAAAAA";
    private static final String UPDATED_PHOTO = "BBBBBBBBBB";

    private static final String DEFAULT_MAIN_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_MAIN_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_COLOR = "BBBBBBBBBB";

    @Inject
    private BusinessRepository businessRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restBusinessMockMvc;

    private Business business;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BusinessResource businessResource = new BusinessResource();
        ReflectionTestUtils.setField(businessResource, "businessRepository", businessRepository);
        this.restBusinessMockMvc = MockMvcBuilders.standaloneSetup(businessResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Business createEntity(EntityManager em) {
        Business business = new Business()
                .name(DEFAULT_NAME)
                .createdDate(DEFAULT_CREATED_DATE)
                .modifiedDate(DEFAULT_MODIFIED_DATE)
                .pisition(DEFAULT_PISITION)
                .icon(DEFAULT_ICON)
                .userName(DEFAULT_USER_NAME)
                .jobPosition(DEFAULT_JOB_POSITION)
                .companyName(DEFAULT_COMPANY_NAME)
                .companySite(DEFAULT_COMPANY_SITE)
                .email(DEFAULT_EMAIL)
                .phone(DEFAULT_PHONE)
                .address(DEFAULT_ADDRESS)
                .twitter(DEFAULT_TWITTER)
                .facebook(DEFAULT_FACEBOOK)
                .google(DEFAULT_GOOGLE)
                .linkedIn(DEFAULT_LINKED_IN)
                .photo(DEFAULT_PHOTO)
                .mainColor(DEFAULT_MAIN_COLOR)
                .color(DEFAULT_COLOR);
        return business;
    }

    @Before
    public void initTest() {
        business = createEntity(em);
    }

    @Test
    @Transactional
    public void createBusiness() throws Exception {
        int databaseSizeBeforeCreate = businessRepository.findAll().size();

        // Create the Business

        restBusinessMockMvc.perform(post("/api/businesses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(business)))
                .andExpect(status().isCreated());

        // Validate the Business in the database
        List<Business> businesses = businessRepository.findAll();
        assertThat(businesses).hasSize(databaseSizeBeforeCreate + 1);
        Business testBusiness = businesses.get(businesses.size() - 1);
        assertThat(testBusiness.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBusiness.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testBusiness.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
        assertThat(testBusiness.getPisition()).isEqualTo(DEFAULT_PISITION);
        assertThat(testBusiness.getIcon()).isEqualTo(DEFAULT_ICON);
        assertThat(testBusiness.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testBusiness.getJobPosition()).isEqualTo(DEFAULT_JOB_POSITION);
        assertThat(testBusiness.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testBusiness.getCompanySite()).isEqualTo(DEFAULT_COMPANY_SITE);
        assertThat(testBusiness.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testBusiness.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testBusiness.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testBusiness.getTwitter()).isEqualTo(DEFAULT_TWITTER);
        assertThat(testBusiness.getFacebook()).isEqualTo(DEFAULT_FACEBOOK);
        assertThat(testBusiness.getGoogle()).isEqualTo(DEFAULT_GOOGLE);
        assertThat(testBusiness.getLinkedIn()).isEqualTo(DEFAULT_LINKED_IN);
        assertThat(testBusiness.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testBusiness.getMainColor()).isEqualTo(DEFAULT_MAIN_COLOR);
        assertThat(testBusiness.getColor()).isEqualTo(DEFAULT_COLOR);
    }

    @Test
    @Transactional
    public void getAllBusinesses() throws Exception {
        // Initialize the database
        businessRepository.saveAndFlush(business);

        // Get all the businesses
        restBusinessMockMvc.perform(get("/api/businesses?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(business.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
                .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())))
                .andExpect(jsonPath("$.[*].pisition").value(hasItem(DEFAULT_PISITION)))
                .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON.toString())))
                .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME.toString())))
                .andExpect(jsonPath("$.[*].jobPosition").value(hasItem(DEFAULT_JOB_POSITION.toString())))
                .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME.toString())))
                .andExpect(jsonPath("$.[*].companySite").value(hasItem(DEFAULT_COMPANY_SITE.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
                .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
                .andExpect(jsonPath("$.[*].twitter").value(hasItem(DEFAULT_TWITTER.toString())))
                .andExpect(jsonPath("$.[*].facebook").value(hasItem(DEFAULT_FACEBOOK.toString())))
                .andExpect(jsonPath("$.[*].google").value(hasItem(DEFAULT_GOOGLE.toString())))
                .andExpect(jsonPath("$.[*].linkedIn").value(hasItem(DEFAULT_LINKED_IN.toString())))
                .andExpect(jsonPath("$.[*].photo").value(hasItem(DEFAULT_PHOTO.toString())))
                .andExpect(jsonPath("$.[*].mainColor").value(hasItem(DEFAULT_MAIN_COLOR.toString())))
                .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR.toString())));
    }

    @Test
    @Transactional
    public void getBusiness() throws Exception {
        // Initialize the database
        businessRepository.saveAndFlush(business);

        // Get the business
        restBusinessMockMvc.perform(get("/api/businesses/{id}", business.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(business.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.pisition").value(DEFAULT_PISITION))
            .andExpect(jsonPath("$.icon").value(DEFAULT_ICON.toString()))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME.toString()))
            .andExpect(jsonPath("$.jobPosition").value(DEFAULT_JOB_POSITION.toString()))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME.toString()))
            .andExpect(jsonPath("$.companySite").value(DEFAULT_COMPANY_SITE.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.twitter").value(DEFAULT_TWITTER.toString()))
            .andExpect(jsonPath("$.facebook").value(DEFAULT_FACEBOOK.toString()))
            .andExpect(jsonPath("$.google").value(DEFAULT_GOOGLE.toString()))
            .andExpect(jsonPath("$.linkedIn").value(DEFAULT_LINKED_IN.toString()))
            .andExpect(jsonPath("$.photo").value(DEFAULT_PHOTO.toString()))
            .andExpect(jsonPath("$.mainColor").value(DEFAULT_MAIN_COLOR.toString()))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBusiness() throws Exception {
        // Get the business
        restBusinessMockMvc.perform(get("/api/businesses/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBusiness() throws Exception {
        // Initialize the database
        businessRepository.saveAndFlush(business);
        int databaseSizeBeforeUpdate = businessRepository.findAll().size();

        // Update the business
        Business updatedBusiness = businessRepository.findOne(business.getId());
        updatedBusiness
                .name(UPDATED_NAME)
                .createdDate(UPDATED_CREATED_DATE)
                .modifiedDate(UPDATED_MODIFIED_DATE)
                .pisition(UPDATED_PISITION)
                .icon(UPDATED_ICON)
                .userName(UPDATED_USER_NAME)
                .jobPosition(UPDATED_JOB_POSITION)
                .companyName(UPDATED_COMPANY_NAME)
                .companySite(UPDATED_COMPANY_SITE)
                .email(UPDATED_EMAIL)
                .phone(UPDATED_PHONE)
                .address(UPDATED_ADDRESS)
                .twitter(UPDATED_TWITTER)
                .facebook(UPDATED_FACEBOOK)
                .google(UPDATED_GOOGLE)
                .linkedIn(UPDATED_LINKED_IN)
                .photo(UPDATED_PHOTO)
                .mainColor(UPDATED_MAIN_COLOR)
                .color(UPDATED_COLOR);

        restBusinessMockMvc.perform(put("/api/businesses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedBusiness)))
                .andExpect(status().isOk());

        // Validate the Business in the database
        List<Business> businesses = businessRepository.findAll();
        assertThat(businesses).hasSize(databaseSizeBeforeUpdate);
        Business testBusiness = businesses.get(businesses.size() - 1);
        assertThat(testBusiness.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBusiness.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testBusiness.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
        assertThat(testBusiness.getPisition()).isEqualTo(UPDATED_PISITION);
        assertThat(testBusiness.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testBusiness.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testBusiness.getJobPosition()).isEqualTo(UPDATED_JOB_POSITION);
        assertThat(testBusiness.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testBusiness.getCompanySite()).isEqualTo(UPDATED_COMPANY_SITE);
        assertThat(testBusiness.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testBusiness.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testBusiness.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testBusiness.getTwitter()).isEqualTo(UPDATED_TWITTER);
        assertThat(testBusiness.getFacebook()).isEqualTo(UPDATED_FACEBOOK);
        assertThat(testBusiness.getGoogle()).isEqualTo(UPDATED_GOOGLE);
        assertThat(testBusiness.getLinkedIn()).isEqualTo(UPDATED_LINKED_IN);
        assertThat(testBusiness.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testBusiness.getMainColor()).isEqualTo(UPDATED_MAIN_COLOR);
        assertThat(testBusiness.getColor()).isEqualTo(UPDATED_COLOR);
    }

    @Test
    @Transactional
    public void deleteBusiness() throws Exception {
        // Initialize the database
        businessRepository.saveAndFlush(business);
        int databaseSizeBeforeDelete = businessRepository.findAll().size();

        // Get the business
        restBusinessMockMvc.perform(delete("/api/businesses/{id}", business.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Business> businesses = businessRepository.findAll();
        assertThat(businesses).hasSize(databaseSizeBeforeDelete - 1);
    }
}
