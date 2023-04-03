package students.platform.andqxai.uz.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import students.platform.andqxai.uz.domain.Apartment;
import students.platform.andqxai.uz.repository.ApartmentRepository;
import students.platform.andqxai.uz.service.criteria.ApartmentCriteria;
import students.platform.andqxai.uz.service.dto.ApartmentDTO;
import students.platform.andqxai.uz.service.mapper.ApartmentMapper;

/**
 * Integration tests for the {@link ApartmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ApartmentResourceIT {

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;
    private static final Double SMALLER_PRICE = 1D - 1D;

    private static final String DEFAULT_REQUIREMENTS = "AAAAAAAAAA";
    private static final String UPDATED_REQUIREMENTS = "BBBBBBBBBB";

    private static final Integer DEFAULT_ROOM_FIT = 1;
    private static final Integer UPDATED_ROOM_FIT = 2;
    private static final Integer SMALLER_ROOM_FIT = 1 - 1;

    private static final String ENTITY_API_URL = "/api/apartments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private ApartmentMapper apartmentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApartmentMockMvc;

    private Apartment apartment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Apartment createEntity(EntityManager em) {
        Apartment apartment = new Apartment()
            .location(DEFAULT_LOCATION)
            .phone(DEFAULT_PHONE)
            .description(DEFAULT_DESCRIPTION)
            .price(DEFAULT_PRICE)
            .requirements(DEFAULT_REQUIREMENTS)
            .roomFit(DEFAULT_ROOM_FIT);
        return apartment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Apartment createUpdatedEntity(EntityManager em) {
        Apartment apartment = new Apartment()
            .location(UPDATED_LOCATION)
            .phone(UPDATED_PHONE)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .requirements(UPDATED_REQUIREMENTS)
            .roomFit(UPDATED_ROOM_FIT);
        return apartment;
    }

    @BeforeEach
    public void initTest() {
        apartment = createEntity(em);
    }

    @Test
    @Transactional
    void createApartment() throws Exception {
        int databaseSizeBeforeCreate = apartmentRepository.findAll().size();
        // Create the Apartment
        ApartmentDTO apartmentDTO = apartmentMapper.toDto(apartment);
        restApartmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apartmentDTO)))
            .andExpect(status().isCreated());

        // Validate the Apartment in the database
        List<Apartment> apartmentList = apartmentRepository.findAll();
        assertThat(apartmentList).hasSize(databaseSizeBeforeCreate + 1);
        Apartment testApartment = apartmentList.get(apartmentList.size() - 1);
        assertThat(testApartment.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testApartment.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testApartment.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testApartment.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testApartment.getRequirements()).isEqualTo(DEFAULT_REQUIREMENTS);
        assertThat(testApartment.getRoomFit()).isEqualTo(DEFAULT_ROOM_FIT);
    }

    @Test
    @Transactional
    void createApartmentWithExistingId() throws Exception {
        // Create the Apartment with an existing ID
        apartment.setId(1L);
        ApartmentDTO apartmentDTO = apartmentMapper.toDto(apartment);

        int databaseSizeBeforeCreate = apartmentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApartmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apartmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Apartment in the database
        List<Apartment> apartmentList = apartmentRepository.findAll();
        assertThat(apartmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllApartments() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartmentList
        restApartmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(apartment.getId().intValue())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].requirements").value(hasItem(DEFAULT_REQUIREMENTS)))
            .andExpect(jsonPath("$.[*].roomFit").value(hasItem(DEFAULT_ROOM_FIT)));
    }

    @Test
    @Transactional
    void getApartment() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get the apartment
        restApartmentMockMvc
            .perform(get(ENTITY_API_URL_ID, apartment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(apartment.getId().intValue()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.requirements").value(DEFAULT_REQUIREMENTS))
            .andExpect(jsonPath("$.roomFit").value(DEFAULT_ROOM_FIT));
    }

    @Test
    @Transactional
    void getApartmentsByIdFiltering() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        Long id = apartment.getId();

        defaultApartmentShouldBeFound("id.equals=" + id);
        defaultApartmentShouldNotBeFound("id.notEquals=" + id);

        defaultApartmentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultApartmentShouldNotBeFound("id.greaterThan=" + id);

        defaultApartmentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultApartmentShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllApartmentsByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartmentList where location equals to DEFAULT_LOCATION
        defaultApartmentShouldBeFound("location.equals=" + DEFAULT_LOCATION);

        // Get all the apartmentList where location equals to UPDATED_LOCATION
        defaultApartmentShouldNotBeFound("location.equals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllApartmentsByLocationIsInShouldWork() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartmentList where location in DEFAULT_LOCATION or UPDATED_LOCATION
        defaultApartmentShouldBeFound("location.in=" + DEFAULT_LOCATION + "," + UPDATED_LOCATION);

        // Get all the apartmentList where location equals to UPDATED_LOCATION
        defaultApartmentShouldNotBeFound("location.in=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllApartmentsByLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartmentList where location is not null
        defaultApartmentShouldBeFound("location.specified=true");

        // Get all the apartmentList where location is null
        defaultApartmentShouldNotBeFound("location.specified=false");
    }

    @Test
    @Transactional
    void getAllApartmentsByLocationContainsSomething() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartmentList where location contains DEFAULT_LOCATION
        defaultApartmentShouldBeFound("location.contains=" + DEFAULT_LOCATION);

        // Get all the apartmentList where location contains UPDATED_LOCATION
        defaultApartmentShouldNotBeFound("location.contains=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllApartmentsByLocationNotContainsSomething() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartmentList where location does not contain DEFAULT_LOCATION
        defaultApartmentShouldNotBeFound("location.doesNotContain=" + DEFAULT_LOCATION);

        // Get all the apartmentList where location does not contain UPDATED_LOCATION
        defaultApartmentShouldBeFound("location.doesNotContain=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllApartmentsByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartmentList where phone equals to DEFAULT_PHONE
        defaultApartmentShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the apartmentList where phone equals to UPDATED_PHONE
        defaultApartmentShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllApartmentsByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartmentList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultApartmentShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the apartmentList where phone equals to UPDATED_PHONE
        defaultApartmentShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllApartmentsByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartmentList where phone is not null
        defaultApartmentShouldBeFound("phone.specified=true");

        // Get all the apartmentList where phone is null
        defaultApartmentShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    void getAllApartmentsByPhoneContainsSomething() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartmentList where phone contains DEFAULT_PHONE
        defaultApartmentShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the apartmentList where phone contains UPDATED_PHONE
        defaultApartmentShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllApartmentsByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartmentList where phone does not contain DEFAULT_PHONE
        defaultApartmentShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the apartmentList where phone does not contain UPDATED_PHONE
        defaultApartmentShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllApartmentsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartmentList where description equals to DEFAULT_DESCRIPTION
        defaultApartmentShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the apartmentList where description equals to UPDATED_DESCRIPTION
        defaultApartmentShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllApartmentsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartmentList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultApartmentShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the apartmentList where description equals to UPDATED_DESCRIPTION
        defaultApartmentShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllApartmentsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartmentList where description is not null
        defaultApartmentShouldBeFound("description.specified=true");

        // Get all the apartmentList where description is null
        defaultApartmentShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllApartmentsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartmentList where description contains DEFAULT_DESCRIPTION
        defaultApartmentShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the apartmentList where description contains UPDATED_DESCRIPTION
        defaultApartmentShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllApartmentsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartmentList where description does not contain DEFAULT_DESCRIPTION
        defaultApartmentShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the apartmentList where description does not contain UPDATED_DESCRIPTION
        defaultApartmentShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllApartmentsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartmentList where price equals to DEFAULT_PRICE
        defaultApartmentShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the apartmentList where price equals to UPDATED_PRICE
        defaultApartmentShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllApartmentsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartmentList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultApartmentShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the apartmentList where price equals to UPDATED_PRICE
        defaultApartmentShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllApartmentsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartmentList where price is not null
        defaultApartmentShouldBeFound("price.specified=true");

        // Get all the apartmentList where price is null
        defaultApartmentShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    void getAllApartmentsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartmentList where price is greater than or equal to DEFAULT_PRICE
        defaultApartmentShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the apartmentList where price is greater than or equal to UPDATED_PRICE
        defaultApartmentShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllApartmentsByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartmentList where price is less than or equal to DEFAULT_PRICE
        defaultApartmentShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the apartmentList where price is less than or equal to SMALLER_PRICE
        defaultApartmentShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllApartmentsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartmentList where price is less than DEFAULT_PRICE
        defaultApartmentShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the apartmentList where price is less than UPDATED_PRICE
        defaultApartmentShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllApartmentsByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartmentList where price is greater than DEFAULT_PRICE
        defaultApartmentShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the apartmentList where price is greater than SMALLER_PRICE
        defaultApartmentShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllApartmentsByRequirementsIsEqualToSomething() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartmentList where requirements equals to DEFAULT_REQUIREMENTS
        defaultApartmentShouldBeFound("requirements.equals=" + DEFAULT_REQUIREMENTS);

        // Get all the apartmentList where requirements equals to UPDATED_REQUIREMENTS
        defaultApartmentShouldNotBeFound("requirements.equals=" + UPDATED_REQUIREMENTS);
    }

    @Test
    @Transactional
    void getAllApartmentsByRequirementsIsInShouldWork() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartmentList where requirements in DEFAULT_REQUIREMENTS or UPDATED_REQUIREMENTS
        defaultApartmentShouldBeFound("requirements.in=" + DEFAULT_REQUIREMENTS + "," + UPDATED_REQUIREMENTS);

        // Get all the apartmentList where requirements equals to UPDATED_REQUIREMENTS
        defaultApartmentShouldNotBeFound("requirements.in=" + UPDATED_REQUIREMENTS);
    }

    @Test
    @Transactional
    void getAllApartmentsByRequirementsIsNullOrNotNull() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartmentList where requirements is not null
        defaultApartmentShouldBeFound("requirements.specified=true");

        // Get all the apartmentList where requirements is null
        defaultApartmentShouldNotBeFound("requirements.specified=false");
    }

    @Test
    @Transactional
    void getAllApartmentsByRequirementsContainsSomething() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartmentList where requirements contains DEFAULT_REQUIREMENTS
        defaultApartmentShouldBeFound("requirements.contains=" + DEFAULT_REQUIREMENTS);

        // Get all the apartmentList where requirements contains UPDATED_REQUIREMENTS
        defaultApartmentShouldNotBeFound("requirements.contains=" + UPDATED_REQUIREMENTS);
    }

    @Test
    @Transactional
    void getAllApartmentsByRequirementsNotContainsSomething() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartmentList where requirements does not contain DEFAULT_REQUIREMENTS
        defaultApartmentShouldNotBeFound("requirements.doesNotContain=" + DEFAULT_REQUIREMENTS);

        // Get all the apartmentList where requirements does not contain UPDATED_REQUIREMENTS
        defaultApartmentShouldBeFound("requirements.doesNotContain=" + UPDATED_REQUIREMENTS);
    }

    @Test
    @Transactional
    void getAllApartmentsByRoomFitIsEqualToSomething() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartmentList where roomFit equals to DEFAULT_ROOM_FIT
        defaultApartmentShouldBeFound("roomFit.equals=" + DEFAULT_ROOM_FIT);

        // Get all the apartmentList where roomFit equals to UPDATED_ROOM_FIT
        defaultApartmentShouldNotBeFound("roomFit.equals=" + UPDATED_ROOM_FIT);
    }

    @Test
    @Transactional
    void getAllApartmentsByRoomFitIsInShouldWork() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartmentList where roomFit in DEFAULT_ROOM_FIT or UPDATED_ROOM_FIT
        defaultApartmentShouldBeFound("roomFit.in=" + DEFAULT_ROOM_FIT + "," + UPDATED_ROOM_FIT);

        // Get all the apartmentList where roomFit equals to UPDATED_ROOM_FIT
        defaultApartmentShouldNotBeFound("roomFit.in=" + UPDATED_ROOM_FIT);
    }

    @Test
    @Transactional
    void getAllApartmentsByRoomFitIsNullOrNotNull() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartmentList where roomFit is not null
        defaultApartmentShouldBeFound("roomFit.specified=true");

        // Get all the apartmentList where roomFit is null
        defaultApartmentShouldNotBeFound("roomFit.specified=false");
    }

    @Test
    @Transactional
    void getAllApartmentsByRoomFitIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartmentList where roomFit is greater than or equal to DEFAULT_ROOM_FIT
        defaultApartmentShouldBeFound("roomFit.greaterThanOrEqual=" + DEFAULT_ROOM_FIT);

        // Get all the apartmentList where roomFit is greater than or equal to UPDATED_ROOM_FIT
        defaultApartmentShouldNotBeFound("roomFit.greaterThanOrEqual=" + UPDATED_ROOM_FIT);
    }

    @Test
    @Transactional
    void getAllApartmentsByRoomFitIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartmentList where roomFit is less than or equal to DEFAULT_ROOM_FIT
        defaultApartmentShouldBeFound("roomFit.lessThanOrEqual=" + DEFAULT_ROOM_FIT);

        // Get all the apartmentList where roomFit is less than or equal to SMALLER_ROOM_FIT
        defaultApartmentShouldNotBeFound("roomFit.lessThanOrEqual=" + SMALLER_ROOM_FIT);
    }

    @Test
    @Transactional
    void getAllApartmentsByRoomFitIsLessThanSomething() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartmentList where roomFit is less than DEFAULT_ROOM_FIT
        defaultApartmentShouldNotBeFound("roomFit.lessThan=" + DEFAULT_ROOM_FIT);

        // Get all the apartmentList where roomFit is less than UPDATED_ROOM_FIT
        defaultApartmentShouldBeFound("roomFit.lessThan=" + UPDATED_ROOM_FIT);
    }

    @Test
    @Transactional
    void getAllApartmentsByRoomFitIsGreaterThanSomething() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        // Get all the apartmentList where roomFit is greater than DEFAULT_ROOM_FIT
        defaultApartmentShouldNotBeFound("roomFit.greaterThan=" + DEFAULT_ROOM_FIT);

        // Get all the apartmentList where roomFit is greater than SMALLER_ROOM_FIT
        defaultApartmentShouldBeFound("roomFit.greaterThan=" + SMALLER_ROOM_FIT);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultApartmentShouldBeFound(String filter) throws Exception {
        restApartmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(apartment.getId().intValue())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].requirements").value(hasItem(DEFAULT_REQUIREMENTS)))
            .andExpect(jsonPath("$.[*].roomFit").value(hasItem(DEFAULT_ROOM_FIT)));

        // Check, that the count call also returns 1
        restApartmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultApartmentShouldNotBeFound(String filter) throws Exception {
        restApartmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restApartmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingApartment() throws Exception {
        // Get the apartment
        restApartmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingApartment() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        int databaseSizeBeforeUpdate = apartmentRepository.findAll().size();

        // Update the apartment
        Apartment updatedApartment = apartmentRepository.findById(apartment.getId()).get();
        // Disconnect from session so that the updates on updatedApartment are not directly saved in db
        em.detach(updatedApartment);
        updatedApartment
            .location(UPDATED_LOCATION)
            .phone(UPDATED_PHONE)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .requirements(UPDATED_REQUIREMENTS)
            .roomFit(UPDATED_ROOM_FIT);
        ApartmentDTO apartmentDTO = apartmentMapper.toDto(updatedApartment);

        restApartmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, apartmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(apartmentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Apartment in the database
        List<Apartment> apartmentList = apartmentRepository.findAll();
        assertThat(apartmentList).hasSize(databaseSizeBeforeUpdate);
        Apartment testApartment = apartmentList.get(apartmentList.size() - 1);
        assertThat(testApartment.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testApartment.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testApartment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testApartment.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testApartment.getRequirements()).isEqualTo(UPDATED_REQUIREMENTS);
        assertThat(testApartment.getRoomFit()).isEqualTo(UPDATED_ROOM_FIT);
    }

    @Test
    @Transactional
    void putNonExistingApartment() throws Exception {
        int databaseSizeBeforeUpdate = apartmentRepository.findAll().size();
        apartment.setId(count.incrementAndGet());

        // Create the Apartment
        ApartmentDTO apartmentDTO = apartmentMapper.toDto(apartment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApartmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, apartmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(apartmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Apartment in the database
        List<Apartment> apartmentList = apartmentRepository.findAll();
        assertThat(apartmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApartment() throws Exception {
        int databaseSizeBeforeUpdate = apartmentRepository.findAll().size();
        apartment.setId(count.incrementAndGet());

        // Create the Apartment
        ApartmentDTO apartmentDTO = apartmentMapper.toDto(apartment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApartmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(apartmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Apartment in the database
        List<Apartment> apartmentList = apartmentRepository.findAll();
        assertThat(apartmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApartment() throws Exception {
        int databaseSizeBeforeUpdate = apartmentRepository.findAll().size();
        apartment.setId(count.incrementAndGet());

        // Create the Apartment
        ApartmentDTO apartmentDTO = apartmentMapper.toDto(apartment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApartmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apartmentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Apartment in the database
        List<Apartment> apartmentList = apartmentRepository.findAll();
        assertThat(apartmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApartmentWithPatch() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        int databaseSizeBeforeUpdate = apartmentRepository.findAll().size();

        // Update the apartment using partial update
        Apartment partialUpdatedApartment = new Apartment();
        partialUpdatedApartment.setId(apartment.getId());

        partialUpdatedApartment.phone(UPDATED_PHONE).description(UPDATED_DESCRIPTION).price(UPDATED_PRICE);

        restApartmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApartment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApartment))
            )
            .andExpect(status().isOk());

        // Validate the Apartment in the database
        List<Apartment> apartmentList = apartmentRepository.findAll();
        assertThat(apartmentList).hasSize(databaseSizeBeforeUpdate);
        Apartment testApartment = apartmentList.get(apartmentList.size() - 1);
        assertThat(testApartment.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testApartment.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testApartment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testApartment.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testApartment.getRequirements()).isEqualTo(DEFAULT_REQUIREMENTS);
        assertThat(testApartment.getRoomFit()).isEqualTo(DEFAULT_ROOM_FIT);
    }

    @Test
    @Transactional
    void fullUpdateApartmentWithPatch() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        int databaseSizeBeforeUpdate = apartmentRepository.findAll().size();

        // Update the apartment using partial update
        Apartment partialUpdatedApartment = new Apartment();
        partialUpdatedApartment.setId(apartment.getId());

        partialUpdatedApartment
            .location(UPDATED_LOCATION)
            .phone(UPDATED_PHONE)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .requirements(UPDATED_REQUIREMENTS)
            .roomFit(UPDATED_ROOM_FIT);

        restApartmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApartment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApartment))
            )
            .andExpect(status().isOk());

        // Validate the Apartment in the database
        List<Apartment> apartmentList = apartmentRepository.findAll();
        assertThat(apartmentList).hasSize(databaseSizeBeforeUpdate);
        Apartment testApartment = apartmentList.get(apartmentList.size() - 1);
        assertThat(testApartment.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testApartment.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testApartment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testApartment.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testApartment.getRequirements()).isEqualTo(UPDATED_REQUIREMENTS);
        assertThat(testApartment.getRoomFit()).isEqualTo(UPDATED_ROOM_FIT);
    }

    @Test
    @Transactional
    void patchNonExistingApartment() throws Exception {
        int databaseSizeBeforeUpdate = apartmentRepository.findAll().size();
        apartment.setId(count.incrementAndGet());

        // Create the Apartment
        ApartmentDTO apartmentDTO = apartmentMapper.toDto(apartment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApartmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, apartmentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(apartmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Apartment in the database
        List<Apartment> apartmentList = apartmentRepository.findAll();
        assertThat(apartmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApartment() throws Exception {
        int databaseSizeBeforeUpdate = apartmentRepository.findAll().size();
        apartment.setId(count.incrementAndGet());

        // Create the Apartment
        ApartmentDTO apartmentDTO = apartmentMapper.toDto(apartment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApartmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(apartmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Apartment in the database
        List<Apartment> apartmentList = apartmentRepository.findAll();
        assertThat(apartmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApartment() throws Exception {
        int databaseSizeBeforeUpdate = apartmentRepository.findAll().size();
        apartment.setId(count.incrementAndGet());

        // Create the Apartment
        ApartmentDTO apartmentDTO = apartmentMapper.toDto(apartment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApartmentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(apartmentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Apartment in the database
        List<Apartment> apartmentList = apartmentRepository.findAll();
        assertThat(apartmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApartment() throws Exception {
        // Initialize the database
        apartmentRepository.saveAndFlush(apartment);

        int databaseSizeBeforeDelete = apartmentRepository.findAll().size();

        // Delete the apartment
        restApartmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, apartment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Apartment> apartmentList = apartmentRepository.findAll();
        assertThat(apartmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
