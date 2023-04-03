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
import students.platform.andqxai.uz.domain.Cafe;
import students.platform.andqxai.uz.repository.CafeRepository;
import students.platform.andqxai.uz.service.criteria.CafeCriteria;
import students.platform.andqxai.uz.service.dto.CafeDTO;
import students.platform.andqxai.uz.service.mapper.CafeMapper;

/**
 * Integration tests for the {@link CafeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CafeResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final Instant DEFAULT_OPENING_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_OPENING_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CLOSE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CLOSE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/cafes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CafeRepository cafeRepository;

    @Autowired
    private CafeMapper cafeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCafeMockMvc;

    private Cafe cafe;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cafe createEntity(EntityManager em) {
        Cafe cafe = new Cafe()
            .description(DEFAULT_DESCRIPTION)
            .name(DEFAULT_NAME)
            .location(DEFAULT_LOCATION)
            .openingTime(DEFAULT_OPENING_TIME)
            .closeTime(DEFAULT_CLOSE_TIME);
        return cafe;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cafe createUpdatedEntity(EntityManager em) {
        Cafe cafe = new Cafe()
            .description(UPDATED_DESCRIPTION)
            .name(UPDATED_NAME)
            .location(UPDATED_LOCATION)
            .openingTime(UPDATED_OPENING_TIME)
            .closeTime(UPDATED_CLOSE_TIME);
        return cafe;
    }

    @BeforeEach
    public void initTest() {
        cafe = createEntity(em);
    }

    @Test
    @Transactional
    void createCafe() throws Exception {
        int databaseSizeBeforeCreate = cafeRepository.findAll().size();
        // Create the Cafe
        CafeDTO cafeDTO = cafeMapper.toDto(cafe);
        restCafeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cafeDTO)))
            .andExpect(status().isCreated());

        // Validate the Cafe in the database
        List<Cafe> cafeList = cafeRepository.findAll();
        assertThat(cafeList).hasSize(databaseSizeBeforeCreate + 1);
        Cafe testCafe = cafeList.get(cafeList.size() - 1);
        assertThat(testCafe.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCafe.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCafe.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testCafe.getOpeningTime()).isEqualTo(DEFAULT_OPENING_TIME);
        assertThat(testCafe.getCloseTime()).isEqualTo(DEFAULT_CLOSE_TIME);
    }

    @Test
    @Transactional
    void createCafeWithExistingId() throws Exception {
        // Create the Cafe with an existing ID
        cafe.setId(1L);
        CafeDTO cafeDTO = cafeMapper.toDto(cafe);

        int databaseSizeBeforeCreate = cafeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCafeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cafeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cafe in the database
        List<Cafe> cafeList = cafeRepository.findAll();
        assertThat(cafeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCafes() throws Exception {
        // Initialize the database
        cafeRepository.saveAndFlush(cafe);

        // Get all the cafeList
        restCafeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cafe.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].openingTime").value(hasItem(DEFAULT_OPENING_TIME.toString())))
            .andExpect(jsonPath("$.[*].closeTime").value(hasItem(DEFAULT_CLOSE_TIME.toString())));
    }

    @Test
    @Transactional
    void getCafe() throws Exception {
        // Initialize the database
        cafeRepository.saveAndFlush(cafe);

        // Get the cafe
        restCafeMockMvc
            .perform(get(ENTITY_API_URL_ID, cafe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cafe.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.openingTime").value(DEFAULT_OPENING_TIME.toString()))
            .andExpect(jsonPath("$.closeTime").value(DEFAULT_CLOSE_TIME.toString()));
    }

    @Test
    @Transactional
    void getCafesByIdFiltering() throws Exception {
        // Initialize the database
        cafeRepository.saveAndFlush(cafe);

        Long id = cafe.getId();

        defaultCafeShouldBeFound("id.equals=" + id);
        defaultCafeShouldNotBeFound("id.notEquals=" + id);

        defaultCafeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCafeShouldNotBeFound("id.greaterThan=" + id);

        defaultCafeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCafeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCafesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        cafeRepository.saveAndFlush(cafe);

        // Get all the cafeList where description equals to DEFAULT_DESCRIPTION
        defaultCafeShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the cafeList where description equals to UPDATED_DESCRIPTION
        defaultCafeShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCafesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        cafeRepository.saveAndFlush(cafe);

        // Get all the cafeList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultCafeShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the cafeList where description equals to UPDATED_DESCRIPTION
        defaultCafeShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCafesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        cafeRepository.saveAndFlush(cafe);

        // Get all the cafeList where description is not null
        defaultCafeShouldBeFound("description.specified=true");

        // Get all the cafeList where description is null
        defaultCafeShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllCafesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        cafeRepository.saveAndFlush(cafe);

        // Get all the cafeList where description contains DEFAULT_DESCRIPTION
        defaultCafeShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the cafeList where description contains UPDATED_DESCRIPTION
        defaultCafeShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCafesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        cafeRepository.saveAndFlush(cafe);

        // Get all the cafeList where description does not contain DEFAULT_DESCRIPTION
        defaultCafeShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the cafeList where description does not contain UPDATED_DESCRIPTION
        defaultCafeShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCafesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        cafeRepository.saveAndFlush(cafe);

        // Get all the cafeList where name equals to DEFAULT_NAME
        defaultCafeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the cafeList where name equals to UPDATED_NAME
        defaultCafeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCafesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        cafeRepository.saveAndFlush(cafe);

        // Get all the cafeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCafeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the cafeList where name equals to UPDATED_NAME
        defaultCafeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCafesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        cafeRepository.saveAndFlush(cafe);

        // Get all the cafeList where name is not null
        defaultCafeShouldBeFound("name.specified=true");

        // Get all the cafeList where name is null
        defaultCafeShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllCafesByNameContainsSomething() throws Exception {
        // Initialize the database
        cafeRepository.saveAndFlush(cafe);

        // Get all the cafeList where name contains DEFAULT_NAME
        defaultCafeShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the cafeList where name contains UPDATED_NAME
        defaultCafeShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCafesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        cafeRepository.saveAndFlush(cafe);

        // Get all the cafeList where name does not contain DEFAULT_NAME
        defaultCafeShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the cafeList where name does not contain UPDATED_NAME
        defaultCafeShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCafesByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        cafeRepository.saveAndFlush(cafe);

        // Get all the cafeList where location equals to DEFAULT_LOCATION
        defaultCafeShouldBeFound("location.equals=" + DEFAULT_LOCATION);

        // Get all the cafeList where location equals to UPDATED_LOCATION
        defaultCafeShouldNotBeFound("location.equals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllCafesByLocationIsInShouldWork() throws Exception {
        // Initialize the database
        cafeRepository.saveAndFlush(cafe);

        // Get all the cafeList where location in DEFAULT_LOCATION or UPDATED_LOCATION
        defaultCafeShouldBeFound("location.in=" + DEFAULT_LOCATION + "," + UPDATED_LOCATION);

        // Get all the cafeList where location equals to UPDATED_LOCATION
        defaultCafeShouldNotBeFound("location.in=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllCafesByLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        cafeRepository.saveAndFlush(cafe);

        // Get all the cafeList where location is not null
        defaultCafeShouldBeFound("location.specified=true");

        // Get all the cafeList where location is null
        defaultCafeShouldNotBeFound("location.specified=false");
    }

    @Test
    @Transactional
    void getAllCafesByLocationContainsSomething() throws Exception {
        // Initialize the database
        cafeRepository.saveAndFlush(cafe);

        // Get all the cafeList where location contains DEFAULT_LOCATION
        defaultCafeShouldBeFound("location.contains=" + DEFAULT_LOCATION);

        // Get all the cafeList where location contains UPDATED_LOCATION
        defaultCafeShouldNotBeFound("location.contains=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllCafesByLocationNotContainsSomething() throws Exception {
        // Initialize the database
        cafeRepository.saveAndFlush(cafe);

        // Get all the cafeList where location does not contain DEFAULT_LOCATION
        defaultCafeShouldNotBeFound("location.doesNotContain=" + DEFAULT_LOCATION);

        // Get all the cafeList where location does not contain UPDATED_LOCATION
        defaultCafeShouldBeFound("location.doesNotContain=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllCafesByOpeningTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        cafeRepository.saveAndFlush(cafe);

        // Get all the cafeList where openingTime equals to DEFAULT_OPENING_TIME
        defaultCafeShouldBeFound("openingTime.equals=" + DEFAULT_OPENING_TIME);

        // Get all the cafeList where openingTime equals to UPDATED_OPENING_TIME
        defaultCafeShouldNotBeFound("openingTime.equals=" + UPDATED_OPENING_TIME);
    }

    @Test
    @Transactional
    void getAllCafesByOpeningTimeIsInShouldWork() throws Exception {
        // Initialize the database
        cafeRepository.saveAndFlush(cafe);

        // Get all the cafeList where openingTime in DEFAULT_OPENING_TIME or UPDATED_OPENING_TIME
        defaultCafeShouldBeFound("openingTime.in=" + DEFAULT_OPENING_TIME + "," + UPDATED_OPENING_TIME);

        // Get all the cafeList where openingTime equals to UPDATED_OPENING_TIME
        defaultCafeShouldNotBeFound("openingTime.in=" + UPDATED_OPENING_TIME);
    }

    @Test
    @Transactional
    void getAllCafesByOpeningTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        cafeRepository.saveAndFlush(cafe);

        // Get all the cafeList where openingTime is not null
        defaultCafeShouldBeFound("openingTime.specified=true");

        // Get all the cafeList where openingTime is null
        defaultCafeShouldNotBeFound("openingTime.specified=false");
    }

    @Test
    @Transactional
    void getAllCafesByCloseTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        cafeRepository.saveAndFlush(cafe);

        // Get all the cafeList where closeTime equals to DEFAULT_CLOSE_TIME
        defaultCafeShouldBeFound("closeTime.equals=" + DEFAULT_CLOSE_TIME);

        // Get all the cafeList where closeTime equals to UPDATED_CLOSE_TIME
        defaultCafeShouldNotBeFound("closeTime.equals=" + UPDATED_CLOSE_TIME);
    }

    @Test
    @Transactional
    void getAllCafesByCloseTimeIsInShouldWork() throws Exception {
        // Initialize the database
        cafeRepository.saveAndFlush(cafe);

        // Get all the cafeList where closeTime in DEFAULT_CLOSE_TIME or UPDATED_CLOSE_TIME
        defaultCafeShouldBeFound("closeTime.in=" + DEFAULT_CLOSE_TIME + "," + UPDATED_CLOSE_TIME);

        // Get all the cafeList where closeTime equals to UPDATED_CLOSE_TIME
        defaultCafeShouldNotBeFound("closeTime.in=" + UPDATED_CLOSE_TIME);
    }

    @Test
    @Transactional
    void getAllCafesByCloseTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        cafeRepository.saveAndFlush(cafe);

        // Get all the cafeList where closeTime is not null
        defaultCafeShouldBeFound("closeTime.specified=true");

        // Get all the cafeList where closeTime is null
        defaultCafeShouldNotBeFound("closeTime.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCafeShouldBeFound(String filter) throws Exception {
        restCafeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cafe.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].openingTime").value(hasItem(DEFAULT_OPENING_TIME.toString())))
            .andExpect(jsonPath("$.[*].closeTime").value(hasItem(DEFAULT_CLOSE_TIME.toString())));

        // Check, that the count call also returns 1
        restCafeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCafeShouldNotBeFound(String filter) throws Exception {
        restCafeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCafeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCafe() throws Exception {
        // Get the cafe
        restCafeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCafe() throws Exception {
        // Initialize the database
        cafeRepository.saveAndFlush(cafe);

        int databaseSizeBeforeUpdate = cafeRepository.findAll().size();

        // Update the cafe
        Cafe updatedCafe = cafeRepository.findById(cafe.getId()).get();
        // Disconnect from session so that the updates on updatedCafe are not directly saved in db
        em.detach(updatedCafe);
        updatedCafe
            .description(UPDATED_DESCRIPTION)
            .name(UPDATED_NAME)
            .location(UPDATED_LOCATION)
            .openingTime(UPDATED_OPENING_TIME)
            .closeTime(UPDATED_CLOSE_TIME);
        CafeDTO cafeDTO = cafeMapper.toDto(updatedCafe);

        restCafeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cafeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cafeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Cafe in the database
        List<Cafe> cafeList = cafeRepository.findAll();
        assertThat(cafeList).hasSize(databaseSizeBeforeUpdate);
        Cafe testCafe = cafeList.get(cafeList.size() - 1);
        assertThat(testCafe.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCafe.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCafe.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testCafe.getOpeningTime()).isEqualTo(UPDATED_OPENING_TIME);
        assertThat(testCafe.getCloseTime()).isEqualTo(UPDATED_CLOSE_TIME);
    }

    @Test
    @Transactional
    void putNonExistingCafe() throws Exception {
        int databaseSizeBeforeUpdate = cafeRepository.findAll().size();
        cafe.setId(count.incrementAndGet());

        // Create the Cafe
        CafeDTO cafeDTO = cafeMapper.toDto(cafe);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCafeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cafeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cafeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cafe in the database
        List<Cafe> cafeList = cafeRepository.findAll();
        assertThat(cafeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCafe() throws Exception {
        int databaseSizeBeforeUpdate = cafeRepository.findAll().size();
        cafe.setId(count.incrementAndGet());

        // Create the Cafe
        CafeDTO cafeDTO = cafeMapper.toDto(cafe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCafeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cafeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cafe in the database
        List<Cafe> cafeList = cafeRepository.findAll();
        assertThat(cafeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCafe() throws Exception {
        int databaseSizeBeforeUpdate = cafeRepository.findAll().size();
        cafe.setId(count.incrementAndGet());

        // Create the Cafe
        CafeDTO cafeDTO = cafeMapper.toDto(cafe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCafeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cafeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cafe in the database
        List<Cafe> cafeList = cafeRepository.findAll();
        assertThat(cafeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCafeWithPatch() throws Exception {
        // Initialize the database
        cafeRepository.saveAndFlush(cafe);

        int databaseSizeBeforeUpdate = cafeRepository.findAll().size();

        // Update the cafe using partial update
        Cafe partialUpdatedCafe = new Cafe();
        partialUpdatedCafe.setId(cafe.getId());

        partialUpdatedCafe.description(UPDATED_DESCRIPTION).name(UPDATED_NAME).location(UPDATED_LOCATION).closeTime(UPDATED_CLOSE_TIME);

        restCafeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCafe.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCafe))
            )
            .andExpect(status().isOk());

        // Validate the Cafe in the database
        List<Cafe> cafeList = cafeRepository.findAll();
        assertThat(cafeList).hasSize(databaseSizeBeforeUpdate);
        Cafe testCafe = cafeList.get(cafeList.size() - 1);
        assertThat(testCafe.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCafe.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCafe.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testCafe.getOpeningTime()).isEqualTo(DEFAULT_OPENING_TIME);
        assertThat(testCafe.getCloseTime()).isEqualTo(UPDATED_CLOSE_TIME);
    }

    @Test
    @Transactional
    void fullUpdateCafeWithPatch() throws Exception {
        // Initialize the database
        cafeRepository.saveAndFlush(cafe);

        int databaseSizeBeforeUpdate = cafeRepository.findAll().size();

        // Update the cafe using partial update
        Cafe partialUpdatedCafe = new Cafe();
        partialUpdatedCafe.setId(cafe.getId());

        partialUpdatedCafe
            .description(UPDATED_DESCRIPTION)
            .name(UPDATED_NAME)
            .location(UPDATED_LOCATION)
            .openingTime(UPDATED_OPENING_TIME)
            .closeTime(UPDATED_CLOSE_TIME);

        restCafeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCafe.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCafe))
            )
            .andExpect(status().isOk());

        // Validate the Cafe in the database
        List<Cafe> cafeList = cafeRepository.findAll();
        assertThat(cafeList).hasSize(databaseSizeBeforeUpdate);
        Cafe testCafe = cafeList.get(cafeList.size() - 1);
        assertThat(testCafe.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCafe.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCafe.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testCafe.getOpeningTime()).isEqualTo(UPDATED_OPENING_TIME);
        assertThat(testCafe.getCloseTime()).isEqualTo(UPDATED_CLOSE_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingCafe() throws Exception {
        int databaseSizeBeforeUpdate = cafeRepository.findAll().size();
        cafe.setId(count.incrementAndGet());

        // Create the Cafe
        CafeDTO cafeDTO = cafeMapper.toDto(cafe);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCafeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cafeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cafeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cafe in the database
        List<Cafe> cafeList = cafeRepository.findAll();
        assertThat(cafeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCafe() throws Exception {
        int databaseSizeBeforeUpdate = cafeRepository.findAll().size();
        cafe.setId(count.incrementAndGet());

        // Create the Cafe
        CafeDTO cafeDTO = cafeMapper.toDto(cafe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCafeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cafeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cafe in the database
        List<Cafe> cafeList = cafeRepository.findAll();
        assertThat(cafeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCafe() throws Exception {
        int databaseSizeBeforeUpdate = cafeRepository.findAll().size();
        cafe.setId(count.incrementAndGet());

        // Create the Cafe
        CafeDTO cafeDTO = cafeMapper.toDto(cafe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCafeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cafeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cafe in the database
        List<Cafe> cafeList = cafeRepository.findAll();
        assertThat(cafeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCafe() throws Exception {
        // Initialize the database
        cafeRepository.saveAndFlush(cafe);

        int databaseSizeBeforeDelete = cafeRepository.findAll().size();

        // Delete the cafe
        restCafeMockMvc
            .perform(delete(ENTITY_API_URL_ID, cafe.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cafe> cafeList = cafeRepository.findAll();
        assertThat(cafeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
