package itomy.sigterra.web.rest;

import itomy.sigterra.SigterraWebApp;

import itomy.sigterra.domain.AddressBook;
import itomy.sigterra.repository.AddressBookRepository;

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
 * Test class for the AddressBookResource REST controller.
 *
 * @see AddressBookResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SigterraWebApp.class)
public class AddressBookResourceIntTest {

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_MODIFIED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MODIFIED_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private AddressBookRepository addressBookRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restAddressBookMockMvc;

    private AddressBook addressBook;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AddressBookResource addressBookResource = new AddressBookResource();
        ReflectionTestUtils.setField(addressBookResource, "addressBookRepository", addressBookRepository);
        this.restAddressBookMockMvc = MockMvcBuilders.standaloneSetup(addressBookResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AddressBook createEntity(EntityManager em) {
        AddressBook addressBook = new AddressBook()
                .createdDate(DEFAULT_CREATED_DATE)
                .modifiedDate(DEFAULT_MODIFIED_DATE);
        return addressBook;
    }

    @Before
    public void initTest() {
        addressBook = createEntity(em);
    }

    @Test
    @Transactional
    public void createAddressBook() throws Exception {
        int databaseSizeBeforeCreate = addressBookRepository.findAll().size();

        // Create the AddressBook

        restAddressBookMockMvc.perform(post("/api/address-books")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(addressBook)))
                .andExpect(status().isCreated());

        // Validate the AddressBook in the database
        List<AddressBook> addressBooks = addressBookRepository.findAll();
        assertThat(addressBooks).hasSize(databaseSizeBeforeCreate + 1);
        AddressBook testAddressBook = addressBooks.get(addressBooks.size() - 1);
        assertThat(testAddressBook.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testAddressBook.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllAddressBooks() throws Exception {
        // Initialize the database
        addressBookRepository.saveAndFlush(addressBook);

        // Get all the addressBooks
        restAddressBookMockMvc.perform(get("/api/address-books?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(addressBook.getId().intValue())))
                .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
                .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    public void getAddressBook() throws Exception {
        // Initialize the database
        addressBookRepository.saveAndFlush(addressBook);

        // Get the addressBook
        restAddressBookMockMvc.perform(get("/api/address-books/{id}", addressBook.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(addressBook.getId().intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAddressBook() throws Exception {
        // Get the addressBook
        restAddressBookMockMvc.perform(get("/api/address-books/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAddressBook() throws Exception {
        // Initialize the database
        addressBookRepository.saveAndFlush(addressBook);
        int databaseSizeBeforeUpdate = addressBookRepository.findAll().size();

        // Update the addressBook
        AddressBook updatedAddressBook = addressBookRepository.findOne(addressBook.getId());
        updatedAddressBook
                .createdDate(UPDATED_CREATED_DATE)
                .modifiedDate(UPDATED_MODIFIED_DATE);

        restAddressBookMockMvc.perform(put("/api/address-books")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAddressBook)))
                .andExpect(status().isOk());

        // Validate the AddressBook in the database
        List<AddressBook> addressBooks = addressBookRepository.findAll();
        assertThat(addressBooks).hasSize(databaseSizeBeforeUpdate);
        AddressBook testAddressBook = addressBooks.get(addressBooks.size() - 1);
        assertThat(testAddressBook.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testAddressBook.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void deleteAddressBook() throws Exception {
        // Initialize the database
        addressBookRepository.saveAndFlush(addressBook);
        int databaseSizeBeforeDelete = addressBookRepository.findAll().size();

        // Get the addressBook
        restAddressBookMockMvc.perform(delete("/api/address-books/{id}", addressBook.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<AddressBook> addressBooks = addressBookRepository.findAll();
        assertThat(addressBooks).hasSize(databaseSizeBeforeDelete - 1);
    }
}
