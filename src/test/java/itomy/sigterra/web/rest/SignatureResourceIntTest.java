package itomy.sigterra.web.rest;

import itomy.sigterra.SigterraWebApp;

import itomy.sigterra.domain.Signature;
import itomy.sigterra.repository.SignatureRepository;

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
 * Test class for the SignatureResource REST controller.
 *
 * @see SignatureResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SigterraWebApp.class)
public class SignatureResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    @Inject
    private SignatureRepository signatureRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restSignatureMockMvc;

    private Signature signature;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SignatureResource signatureResource = new SignatureResource();
        ReflectionTestUtils.setField(signatureResource, "signatureRepository", signatureRepository);
        this.restSignatureMockMvc = MockMvcBuilders.standaloneSetup(signatureResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Signature createEntity(EntityManager em) {
        Signature signature = new Signature()
                .name(DEFAULT_NAME)
                .path(DEFAULT_PATH);
        return signature;
    }

    @Before
    public void initTest() {
        signature = createEntity(em);
    }

    @Test
    @Transactional
    public void createSignature() throws Exception {
        int databaseSizeBeforeCreate = signatureRepository.findAll().size();

        // Create the Signature

        restSignatureMockMvc.perform(post("/api/signatures")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(signature)))
                .andExpect(status().isCreated());

        // Validate the Signature in the database
        List<Signature> signatures = signatureRepository.findAll();
        assertThat(signatures).hasSize(databaseSizeBeforeCreate + 1);
        Signature testSignature = signatures.get(signatures.size() - 1);
        assertThat(testSignature.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSignature.getPath()).isEqualTo(DEFAULT_PATH);
    }

    @Test
    @Transactional
    public void getAllSignatures() throws Exception {
        // Initialize the database
        signatureRepository.saveAndFlush(signature);

        // Get all the signatures
        restSignatureMockMvc.perform(get("/api/signatures?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(signature.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH.toString())));
    }

    @Test
    @Transactional
    public void getSignature() throws Exception {
        // Initialize the database
        signatureRepository.saveAndFlush(signature);

        // Get the signature
        restSignatureMockMvc.perform(get("/api/signatures/{id}", signature.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(signature.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSignature() throws Exception {
        // Get the signature
        restSignatureMockMvc.perform(get("/api/signatures/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSignature() throws Exception {
        // Initialize the database
        signatureRepository.saveAndFlush(signature);
        int databaseSizeBeforeUpdate = signatureRepository.findAll().size();

        // Update the signature
        Signature updatedSignature = signatureRepository.findOne(signature.getId());
        updatedSignature
                .name(UPDATED_NAME)
                .path(UPDATED_PATH);

        restSignatureMockMvc.perform(put("/api/signatures")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSignature)))
                .andExpect(status().isOk());

        // Validate the Signature in the database
        List<Signature> signatures = signatureRepository.findAll();
        assertThat(signatures).hasSize(databaseSizeBeforeUpdate);
        Signature testSignature = signatures.get(signatures.size() - 1);
        assertThat(testSignature.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSignature.getPath()).isEqualTo(UPDATED_PATH);
    }

    @Test
    @Transactional
    public void deleteSignature() throws Exception {
        // Initialize the database
        signatureRepository.saveAndFlush(signature);
        int databaseSizeBeforeDelete = signatureRepository.findAll().size();

        // Get the signature
        restSignatureMockMvc.perform(delete("/api/signatures/{id}", signature.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Signature> signatures = signatureRepository.findAll();
        assertThat(signatures).hasSize(databaseSizeBeforeDelete - 1);
    }
}
