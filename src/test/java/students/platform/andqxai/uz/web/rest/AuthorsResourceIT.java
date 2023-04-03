package students.platform.andqxai.uz.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import students.platform.andqxai.uz.IntegrationTest;
import students.platform.andqxai.uz.domain.Authors;
import students.platform.andqxai.uz.domain.Book;
import students.platform.andqxai.uz.repository.AuthorsRepository;
import students.platform.andqxai.uz.service.criteria.AuthorsCriteria;
import students.platform.andqxai.uz.service.dto.AuthorsDTO;
import students.platform.andqxai.uz.service.mapper.AuthorsMapper;

/**
 * Integration tests for the {@link AuthorsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AuthorsResourceIT {

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_BIRTH_DAY = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BIRTH_DAY = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DED_DAY = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DED_DAY = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/authors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AuthorsRepository authorsRepository;

    @Autowired
    private AuthorsMapper authorsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAuthorsMockMvc;

    private Authors authors;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Authors createEntity(EntityManager em) {
        Authors authors = new Authors().fullName(DEFAULT_FULL_NAME).birthDay(DEFAULT_BIRTH_DAY).dedDay(DEFAULT_DED_DAY);
        return authors;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Authors createUpdatedEntity(EntityManager em) {
        Authors authors = new Authors().fullName(UPDATED_FULL_NAME).birthDay(UPDATED_BIRTH_DAY).dedDay(UPDATED_DED_DAY);
        return authors;
    }

    @BeforeEach
    public void initTest() {
        authors = createEntity(em);
    }

    @Test
    @Transactional
    void createAuthors() throws Exception {
        int databaseSizeBeforeCreate = authorsRepository.findAll().size();
        // Create the Authors
        AuthorsDTO authorsDTO = authorsMapper.toDto(authors);
        restAuthorsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(authorsDTO)))
            .andExpect(status().isCreated());

        // Validate the Authors in the database
        List<Authors> authorsList = authorsRepository.findAll();
        assertThat(authorsList).hasSize(databaseSizeBeforeCreate + 1);
        Authors testAuthors = authorsList.get(authorsList.size() - 1);
        assertThat(testAuthors.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testAuthors.getBirthDay()).isEqualTo(DEFAULT_BIRTH_DAY);
        assertThat(testAuthors.getDedDay()).isEqualTo(DEFAULT_DED_DAY);
    }

    @Test
    @Transactional
    void createAuthorsWithExistingId() throws Exception {
        // Create the Authors with an existing ID
        authors.setId(1L);
        AuthorsDTO authorsDTO = authorsMapper.toDto(authors);

        int databaseSizeBeforeCreate = authorsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAuthorsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(authorsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Authors in the database
        List<Authors> authorsList = authorsRepository.findAll();
        assertThat(authorsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAuthors() throws Exception {
        // Initialize the database
        authorsRepository.saveAndFlush(authors);

        // Get all the authorsList
        restAuthorsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(authors.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].birthDay").value(hasItem(DEFAULT_BIRTH_DAY.toString())))
            .andExpect(jsonPath("$.[*].dedDay").value(hasItem(DEFAULT_DED_DAY.toString())));
    }

    @Test
    @Transactional
    void getAuthors() throws Exception {
        // Initialize the database
        authorsRepository.saveAndFlush(authors);

        // Get the authors
        restAuthorsMockMvc
            .perform(get(ENTITY_API_URL_ID, authors.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(authors.getId().intValue()))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.birthDay").value(DEFAULT_BIRTH_DAY.toString()))
            .andExpect(jsonPath("$.dedDay").value(DEFAULT_DED_DAY.toString()));
    }

    @Test
    @Transactional
    void getAuthorsByIdFiltering() throws Exception {
        // Initialize the database
        authorsRepository.saveAndFlush(authors);

        Long id = authors.getId();

        defaultAuthorsShouldBeFound("id.equals=" + id);
        defaultAuthorsShouldNotBeFound("id.notEquals=" + id);

        defaultAuthorsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAuthorsShouldNotBeFound("id.greaterThan=" + id);

        defaultAuthorsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAuthorsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAuthorsByFullNameIsEqualToSomething() throws Exception {
        // Initialize the database
        authorsRepository.saveAndFlush(authors);

        // Get all the authorsList where fullName equals to DEFAULT_FULL_NAME
        defaultAuthorsShouldBeFound("fullName.equals=" + DEFAULT_FULL_NAME);

        // Get all the authorsList where fullName equals to UPDATED_FULL_NAME
        defaultAuthorsShouldNotBeFound("fullName.equals=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllAuthorsByFullNameIsInShouldWork() throws Exception {
        // Initialize the database
        authorsRepository.saveAndFlush(authors);

        // Get all the authorsList where fullName in DEFAULT_FULL_NAME or UPDATED_FULL_NAME
        defaultAuthorsShouldBeFound("fullName.in=" + DEFAULT_FULL_NAME + "," + UPDATED_FULL_NAME);

        // Get all the authorsList where fullName equals to UPDATED_FULL_NAME
        defaultAuthorsShouldNotBeFound("fullName.in=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllAuthorsByFullNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        authorsRepository.saveAndFlush(authors);

        // Get all the authorsList where fullName is not null
        defaultAuthorsShouldBeFound("fullName.specified=true");

        // Get all the authorsList where fullName is null
        defaultAuthorsShouldNotBeFound("fullName.specified=false");
    }

    @Test
    @Transactional
    void getAllAuthorsByFullNameContainsSomething() throws Exception {
        // Initialize the database
        authorsRepository.saveAndFlush(authors);

        // Get all the authorsList where fullName contains DEFAULT_FULL_NAME
        defaultAuthorsShouldBeFound("fullName.contains=" + DEFAULT_FULL_NAME);

        // Get all the authorsList where fullName contains UPDATED_FULL_NAME
        defaultAuthorsShouldNotBeFound("fullName.contains=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllAuthorsByFullNameNotContainsSomething() throws Exception {
        // Initialize the database
        authorsRepository.saveAndFlush(authors);

        // Get all the authorsList where fullName does not contain DEFAULT_FULL_NAME
        defaultAuthorsShouldNotBeFound("fullName.doesNotContain=" + DEFAULT_FULL_NAME);

        // Get all the authorsList where fullName does not contain UPDATED_FULL_NAME
        defaultAuthorsShouldBeFound("fullName.doesNotContain=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllAuthorsByBirthDayIsEqualToSomething() throws Exception {
        // Initialize the database
        authorsRepository.saveAndFlush(authors);

        // Get all the authorsList where birthDay equals to DEFAULT_BIRTH_DAY
        defaultAuthorsShouldBeFound("birthDay.equals=" + DEFAULT_BIRTH_DAY);

        // Get all the authorsList where birthDay equals to UPDATED_BIRTH_DAY
        defaultAuthorsShouldNotBeFound("birthDay.equals=" + UPDATED_BIRTH_DAY);
    }

    @Test
    @Transactional
    void getAllAuthorsByBirthDayIsInShouldWork() throws Exception {
        // Initialize the database
        authorsRepository.saveAndFlush(authors);

        // Get all the authorsList where birthDay in DEFAULT_BIRTH_DAY or UPDATED_BIRTH_DAY
        defaultAuthorsShouldBeFound("birthDay.in=" + DEFAULT_BIRTH_DAY + "," + UPDATED_BIRTH_DAY);

        // Get all the authorsList where birthDay equals to UPDATED_BIRTH_DAY
        defaultAuthorsShouldNotBeFound("birthDay.in=" + UPDATED_BIRTH_DAY);
    }

    @Test
    @Transactional
    void getAllAuthorsByBirthDayIsNullOrNotNull() throws Exception {
        // Initialize the database
        authorsRepository.saveAndFlush(authors);

        // Get all the authorsList where birthDay is not null
        defaultAuthorsShouldBeFound("birthDay.specified=true");

        // Get all the authorsList where birthDay is null
        defaultAuthorsShouldNotBeFound("birthDay.specified=false");
    }

    @Test
    @Transactional
    void getAllAuthorsByDedDayIsEqualToSomething() throws Exception {
        // Initialize the database
        authorsRepository.saveAndFlush(authors);

        // Get all the authorsList where dedDay equals to DEFAULT_DED_DAY
        defaultAuthorsShouldBeFound("dedDay.equals=" + DEFAULT_DED_DAY);

        // Get all the authorsList where dedDay equals to UPDATED_DED_DAY
        defaultAuthorsShouldNotBeFound("dedDay.equals=" + UPDATED_DED_DAY);
    }

    @Test
    @Transactional
    void getAllAuthorsByDedDayIsInShouldWork() throws Exception {
        // Initialize the database
        authorsRepository.saveAndFlush(authors);

        // Get all the authorsList where dedDay in DEFAULT_DED_DAY or UPDATED_DED_DAY
        defaultAuthorsShouldBeFound("dedDay.in=" + DEFAULT_DED_DAY + "," + UPDATED_DED_DAY);

        // Get all the authorsList where dedDay equals to UPDATED_DED_DAY
        defaultAuthorsShouldNotBeFound("dedDay.in=" + UPDATED_DED_DAY);
    }

    @Test
    @Transactional
    void getAllAuthorsByDedDayIsNullOrNotNull() throws Exception {
        // Initialize the database
        authorsRepository.saveAndFlush(authors);

        // Get all the authorsList where dedDay is not null
        defaultAuthorsShouldBeFound("dedDay.specified=true");

        // Get all the authorsList where dedDay is null
        defaultAuthorsShouldNotBeFound("dedDay.specified=false");
    }

    @Test
    @Transactional
    void getAllAuthorsByBookIsEqualToSomething() throws Exception {
        Book book;
        if (TestUtil.findAll(em, Book.class).isEmpty()) {
            authorsRepository.saveAndFlush(authors);
            book = BookResourceIT.createEntity(em);
        } else {
            book = TestUtil.findAll(em, Book.class).get(0);
        }
        em.persist(book);
        em.flush();
        authors.addBook(book);
        authorsRepository.saveAndFlush(authors);
        Long bookId = book.getId();

        // Get all the authorsList where book equals to bookId
        defaultAuthorsShouldBeFound("bookId.equals=" + bookId);

        // Get all the authorsList where book equals to (bookId + 1)
        defaultAuthorsShouldNotBeFound("bookId.equals=" + (bookId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAuthorsShouldBeFound(String filter) throws Exception {
        restAuthorsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(authors.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].birthDay").value(hasItem(DEFAULT_BIRTH_DAY.toString())))
            .andExpect(jsonPath("$.[*].dedDay").value(hasItem(DEFAULT_DED_DAY.toString())));

        // Check, that the count call also returns 1
        restAuthorsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAuthorsShouldNotBeFound(String filter) throws Exception {
        restAuthorsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAuthorsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAuthors() throws Exception {
        // Get the authors
        restAuthorsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAuthors() throws Exception {
        // Initialize the database
        authorsRepository.saveAndFlush(authors);

        int databaseSizeBeforeUpdate = authorsRepository.findAll().size();

        // Update the authors
        Authors updatedAuthors = authorsRepository.findById(authors.getId()).get();
        // Disconnect from session so that the updates on updatedAuthors are not directly saved in db
        em.detach(updatedAuthors);
        updatedAuthors.fullName(UPDATED_FULL_NAME).birthDay(UPDATED_BIRTH_DAY).dedDay(UPDATED_DED_DAY);
        AuthorsDTO authorsDTO = authorsMapper.toDto(updatedAuthors);

        restAuthorsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, authorsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(authorsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Authors in the database
        List<Authors> authorsList = authorsRepository.findAll();
        assertThat(authorsList).hasSize(databaseSizeBeforeUpdate);
        Authors testAuthors = authorsList.get(authorsList.size() - 1);
        assertThat(testAuthors.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testAuthors.getBirthDay()).isEqualTo(UPDATED_BIRTH_DAY);
        assertThat(testAuthors.getDedDay()).isEqualTo(UPDATED_DED_DAY);
    }

    @Test
    @Transactional
    void putNonExistingAuthors() throws Exception {
        int databaseSizeBeforeUpdate = authorsRepository.findAll().size();
        authors.setId(count.incrementAndGet());

        // Create the Authors
        AuthorsDTO authorsDTO = authorsMapper.toDto(authors);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuthorsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, authorsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(authorsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Authors in the database
        List<Authors> authorsList = authorsRepository.findAll();
        assertThat(authorsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAuthors() throws Exception {
        int databaseSizeBeforeUpdate = authorsRepository.findAll().size();
        authors.setId(count.incrementAndGet());

        // Create the Authors
        AuthorsDTO authorsDTO = authorsMapper.toDto(authors);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuthorsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(authorsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Authors in the database
        List<Authors> authorsList = authorsRepository.findAll();
        assertThat(authorsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAuthors() throws Exception {
        int databaseSizeBeforeUpdate = authorsRepository.findAll().size();
        authors.setId(count.incrementAndGet());

        // Create the Authors
        AuthorsDTO authorsDTO = authorsMapper.toDto(authors);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuthorsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(authorsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Authors in the database
        List<Authors> authorsList = authorsRepository.findAll();
        assertThat(authorsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAuthorsWithPatch() throws Exception {
        // Initialize the database
        authorsRepository.saveAndFlush(authors);

        int databaseSizeBeforeUpdate = authorsRepository.findAll().size();

        // Update the authors using partial update
        Authors partialUpdatedAuthors = new Authors();
        partialUpdatedAuthors.setId(authors.getId());

        partialUpdatedAuthors.fullName(UPDATED_FULL_NAME).birthDay(UPDATED_BIRTH_DAY);

        restAuthorsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAuthors.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAuthors))
            )
            .andExpect(status().isOk());

        // Validate the Authors in the database
        List<Authors> authorsList = authorsRepository.findAll();
        assertThat(authorsList).hasSize(databaseSizeBeforeUpdate);
        Authors testAuthors = authorsList.get(authorsList.size() - 1);
        assertThat(testAuthors.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testAuthors.getBirthDay()).isEqualTo(UPDATED_BIRTH_DAY);
        assertThat(testAuthors.getDedDay()).isEqualTo(DEFAULT_DED_DAY);
    }

    @Test
    @Transactional
    void fullUpdateAuthorsWithPatch() throws Exception {
        // Initialize the database
        authorsRepository.saveAndFlush(authors);

        int databaseSizeBeforeUpdate = authorsRepository.findAll().size();

        // Update the authors using partial update
        Authors partialUpdatedAuthors = new Authors();
        partialUpdatedAuthors.setId(authors.getId());

        partialUpdatedAuthors.fullName(UPDATED_FULL_NAME).birthDay(UPDATED_BIRTH_DAY).dedDay(UPDATED_DED_DAY);

        restAuthorsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAuthors.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAuthors))
            )
            .andExpect(status().isOk());

        // Validate the Authors in the database
        List<Authors> authorsList = authorsRepository.findAll();
        assertThat(authorsList).hasSize(databaseSizeBeforeUpdate);
        Authors testAuthors = authorsList.get(authorsList.size() - 1);
        assertThat(testAuthors.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testAuthors.getBirthDay()).isEqualTo(UPDATED_BIRTH_DAY);
        assertThat(testAuthors.getDedDay()).isEqualTo(UPDATED_DED_DAY);
    }

    @Test
    @Transactional
    void patchNonExistingAuthors() throws Exception {
        int databaseSizeBeforeUpdate = authorsRepository.findAll().size();
        authors.setId(count.incrementAndGet());

        // Create the Authors
        AuthorsDTO authorsDTO = authorsMapper.toDto(authors);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuthorsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, authorsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(authorsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Authors in the database
        List<Authors> authorsList = authorsRepository.findAll();
        assertThat(authorsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAuthors() throws Exception {
        int databaseSizeBeforeUpdate = authorsRepository.findAll().size();
        authors.setId(count.incrementAndGet());

        // Create the Authors
        AuthorsDTO authorsDTO = authorsMapper.toDto(authors);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuthorsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(authorsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Authors in the database
        List<Authors> authorsList = authorsRepository.findAll();
        assertThat(authorsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAuthors() throws Exception {
        int databaseSizeBeforeUpdate = authorsRepository.findAll().size();
        authors.setId(count.incrementAndGet());

        // Create the Authors
        AuthorsDTO authorsDTO = authorsMapper.toDto(authors);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuthorsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(authorsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Authors in the database
        List<Authors> authorsList = authorsRepository.findAll();
        assertThat(authorsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAuthors() throws Exception {
        // Initialize the database
        authorsRepository.saveAndFlush(authors);

        int databaseSizeBeforeDelete = authorsRepository.findAll().size();

        // Delete the authors
        restAuthorsMockMvc
            .perform(delete(ENTITY_API_URL_ID, authors.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Authors> authorsList = authorsRepository.findAll();
        assertThat(authorsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
