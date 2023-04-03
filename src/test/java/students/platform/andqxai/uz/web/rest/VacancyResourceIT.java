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
import students.platform.andqxai.uz.domain.Vacancy;
import students.platform.andqxai.uz.repository.VacancyRepository;
import students.platform.andqxai.uz.service.criteria.VacancyCriteria;
import students.platform.andqxai.uz.service.dto.VacancyDTO;
import students.platform.andqxai.uz.service.mapper.VacancyMapper;

/**
 * Integration tests for the {@link VacancyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VacancyResourceIT {

    private static final String DEFAULT_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_JOB_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_JOB_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_VACANCY_COUNT = 1;
    private static final Integer UPDATED_VACANCY_COUNT = 2;
    private static final Integer SMALLER_VACANCY_COUNT = 1 - 1;

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final Double DEFAULT_SALARY = 1D;
    private static final Double UPDATED_SALARY = 2D;
    private static final Double SMALLER_SALARY = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/vacancies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VacancyRepository vacancyRepository;

    @Autowired
    private VacancyMapper vacancyMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVacancyMockMvc;

    private Vacancy vacancy;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vacancy createEntity(EntityManager em) {
        Vacancy vacancy = new Vacancy()
            .companyName(DEFAULT_COMPANY_NAME)
            .jobType(DEFAULT_JOB_TYPE)
            .description(DEFAULT_DESCRIPTION)
            .vacancyCount(DEFAULT_VACANCY_COUNT)
            .location(DEFAULT_LOCATION)
            .phone(DEFAULT_PHONE)
            .salary(DEFAULT_SALARY);
        return vacancy;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vacancy createUpdatedEntity(EntityManager em) {
        Vacancy vacancy = new Vacancy()
            .companyName(UPDATED_COMPANY_NAME)
            .jobType(UPDATED_JOB_TYPE)
            .description(UPDATED_DESCRIPTION)
            .vacancyCount(UPDATED_VACANCY_COUNT)
            .location(UPDATED_LOCATION)
            .phone(UPDATED_PHONE)
            .salary(UPDATED_SALARY);
        return vacancy;
    }

    @BeforeEach
    public void initTest() {
        vacancy = createEntity(em);
    }

    @Test
    @Transactional
    void createVacancy() throws Exception {
        int databaseSizeBeforeCreate = vacancyRepository.findAll().size();
        // Create the Vacancy
        VacancyDTO vacancyDTO = vacancyMapper.toDto(vacancy);
        restVacancyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vacancyDTO)))
            .andExpect(status().isCreated());

        // Validate the Vacancy in the database
        List<Vacancy> vacancyList = vacancyRepository.findAll();
        assertThat(vacancyList).hasSize(databaseSizeBeforeCreate + 1);
        Vacancy testVacancy = vacancyList.get(vacancyList.size() - 1);
        assertThat(testVacancy.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testVacancy.getJobType()).isEqualTo(DEFAULT_JOB_TYPE);
        assertThat(testVacancy.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testVacancy.getVacancyCount()).isEqualTo(DEFAULT_VACANCY_COUNT);
        assertThat(testVacancy.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testVacancy.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testVacancy.getSalary()).isEqualTo(DEFAULT_SALARY);
    }

    @Test
    @Transactional
    void createVacancyWithExistingId() throws Exception {
        // Create the Vacancy with an existing ID
        vacancy.setId(1L);
        VacancyDTO vacancyDTO = vacancyMapper.toDto(vacancy);

        int databaseSizeBeforeCreate = vacancyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVacancyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vacancyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Vacancy in the database
        List<Vacancy> vacancyList = vacancyRepository.findAll();
        assertThat(vacancyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVacancies() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList
        restVacancyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vacancy.getId().intValue())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].jobType").value(hasItem(DEFAULT_JOB_TYPE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].vacancyCount").value(hasItem(DEFAULT_VACANCY_COUNT)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].salary").value(hasItem(DEFAULT_SALARY.doubleValue())));
    }

    @Test
    @Transactional
    void getVacancy() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get the vacancy
        restVacancyMockMvc
            .perform(get(ENTITY_API_URL_ID, vacancy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vacancy.getId().intValue()))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME))
            .andExpect(jsonPath("$.jobType").value(DEFAULT_JOB_TYPE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.vacancyCount").value(DEFAULT_VACANCY_COUNT))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.salary").value(DEFAULT_SALARY.doubleValue()));
    }

    @Test
    @Transactional
    void getVacanciesByIdFiltering() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        Long id = vacancy.getId();

        defaultVacancyShouldBeFound("id.equals=" + id);
        defaultVacancyShouldNotBeFound("id.notEquals=" + id);

        defaultVacancyShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultVacancyShouldNotBeFound("id.greaterThan=" + id);

        defaultVacancyShouldBeFound("id.lessThanOrEqual=" + id);
        defaultVacancyShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllVacanciesByCompanyNameIsEqualToSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where companyName equals to DEFAULT_COMPANY_NAME
        defaultVacancyShouldBeFound("companyName.equals=" + DEFAULT_COMPANY_NAME);

        // Get all the vacancyList where companyName equals to UPDATED_COMPANY_NAME
        defaultVacancyShouldNotBeFound("companyName.equals=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    void getAllVacanciesByCompanyNameIsInShouldWork() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where companyName in DEFAULT_COMPANY_NAME or UPDATED_COMPANY_NAME
        defaultVacancyShouldBeFound("companyName.in=" + DEFAULT_COMPANY_NAME + "," + UPDATED_COMPANY_NAME);

        // Get all the vacancyList where companyName equals to UPDATED_COMPANY_NAME
        defaultVacancyShouldNotBeFound("companyName.in=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    void getAllVacanciesByCompanyNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where companyName is not null
        defaultVacancyShouldBeFound("companyName.specified=true");

        // Get all the vacancyList where companyName is null
        defaultVacancyShouldNotBeFound("companyName.specified=false");
    }

    @Test
    @Transactional
    void getAllVacanciesByCompanyNameContainsSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where companyName contains DEFAULT_COMPANY_NAME
        defaultVacancyShouldBeFound("companyName.contains=" + DEFAULT_COMPANY_NAME);

        // Get all the vacancyList where companyName contains UPDATED_COMPANY_NAME
        defaultVacancyShouldNotBeFound("companyName.contains=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    void getAllVacanciesByCompanyNameNotContainsSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where companyName does not contain DEFAULT_COMPANY_NAME
        defaultVacancyShouldNotBeFound("companyName.doesNotContain=" + DEFAULT_COMPANY_NAME);

        // Get all the vacancyList where companyName does not contain UPDATED_COMPANY_NAME
        defaultVacancyShouldBeFound("companyName.doesNotContain=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    void getAllVacanciesByJobTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where jobType equals to DEFAULT_JOB_TYPE
        defaultVacancyShouldBeFound("jobType.equals=" + DEFAULT_JOB_TYPE);

        // Get all the vacancyList where jobType equals to UPDATED_JOB_TYPE
        defaultVacancyShouldNotBeFound("jobType.equals=" + UPDATED_JOB_TYPE);
    }

    @Test
    @Transactional
    void getAllVacanciesByJobTypeIsInShouldWork() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where jobType in DEFAULT_JOB_TYPE or UPDATED_JOB_TYPE
        defaultVacancyShouldBeFound("jobType.in=" + DEFAULT_JOB_TYPE + "," + UPDATED_JOB_TYPE);

        // Get all the vacancyList where jobType equals to UPDATED_JOB_TYPE
        defaultVacancyShouldNotBeFound("jobType.in=" + UPDATED_JOB_TYPE);
    }

    @Test
    @Transactional
    void getAllVacanciesByJobTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where jobType is not null
        defaultVacancyShouldBeFound("jobType.specified=true");

        // Get all the vacancyList where jobType is null
        defaultVacancyShouldNotBeFound("jobType.specified=false");
    }

    @Test
    @Transactional
    void getAllVacanciesByJobTypeContainsSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where jobType contains DEFAULT_JOB_TYPE
        defaultVacancyShouldBeFound("jobType.contains=" + DEFAULT_JOB_TYPE);

        // Get all the vacancyList where jobType contains UPDATED_JOB_TYPE
        defaultVacancyShouldNotBeFound("jobType.contains=" + UPDATED_JOB_TYPE);
    }

    @Test
    @Transactional
    void getAllVacanciesByJobTypeNotContainsSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where jobType does not contain DEFAULT_JOB_TYPE
        defaultVacancyShouldNotBeFound("jobType.doesNotContain=" + DEFAULT_JOB_TYPE);

        // Get all the vacancyList where jobType does not contain UPDATED_JOB_TYPE
        defaultVacancyShouldBeFound("jobType.doesNotContain=" + UPDATED_JOB_TYPE);
    }

    @Test
    @Transactional
    void getAllVacanciesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where description equals to DEFAULT_DESCRIPTION
        defaultVacancyShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the vacancyList where description equals to UPDATED_DESCRIPTION
        defaultVacancyShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllVacanciesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultVacancyShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the vacancyList where description equals to UPDATED_DESCRIPTION
        defaultVacancyShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllVacanciesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where description is not null
        defaultVacancyShouldBeFound("description.specified=true");

        // Get all the vacancyList where description is null
        defaultVacancyShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllVacanciesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where description contains DEFAULT_DESCRIPTION
        defaultVacancyShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the vacancyList where description contains UPDATED_DESCRIPTION
        defaultVacancyShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllVacanciesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where description does not contain DEFAULT_DESCRIPTION
        defaultVacancyShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the vacancyList where description does not contain UPDATED_DESCRIPTION
        defaultVacancyShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllVacanciesByVacancyCountIsEqualToSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where vacancyCount equals to DEFAULT_VACANCY_COUNT
        defaultVacancyShouldBeFound("vacancyCount.equals=" + DEFAULT_VACANCY_COUNT);

        // Get all the vacancyList where vacancyCount equals to UPDATED_VACANCY_COUNT
        defaultVacancyShouldNotBeFound("vacancyCount.equals=" + UPDATED_VACANCY_COUNT);
    }

    @Test
    @Transactional
    void getAllVacanciesByVacancyCountIsInShouldWork() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where vacancyCount in DEFAULT_VACANCY_COUNT or UPDATED_VACANCY_COUNT
        defaultVacancyShouldBeFound("vacancyCount.in=" + DEFAULT_VACANCY_COUNT + "," + UPDATED_VACANCY_COUNT);

        // Get all the vacancyList where vacancyCount equals to UPDATED_VACANCY_COUNT
        defaultVacancyShouldNotBeFound("vacancyCount.in=" + UPDATED_VACANCY_COUNT);
    }

    @Test
    @Transactional
    void getAllVacanciesByVacancyCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where vacancyCount is not null
        defaultVacancyShouldBeFound("vacancyCount.specified=true");

        // Get all the vacancyList where vacancyCount is null
        defaultVacancyShouldNotBeFound("vacancyCount.specified=false");
    }

    @Test
    @Transactional
    void getAllVacanciesByVacancyCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where vacancyCount is greater than or equal to DEFAULT_VACANCY_COUNT
        defaultVacancyShouldBeFound("vacancyCount.greaterThanOrEqual=" + DEFAULT_VACANCY_COUNT);

        // Get all the vacancyList where vacancyCount is greater than or equal to UPDATED_VACANCY_COUNT
        defaultVacancyShouldNotBeFound("vacancyCount.greaterThanOrEqual=" + UPDATED_VACANCY_COUNT);
    }

    @Test
    @Transactional
    void getAllVacanciesByVacancyCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where vacancyCount is less than or equal to DEFAULT_VACANCY_COUNT
        defaultVacancyShouldBeFound("vacancyCount.lessThanOrEqual=" + DEFAULT_VACANCY_COUNT);

        // Get all the vacancyList where vacancyCount is less than or equal to SMALLER_VACANCY_COUNT
        defaultVacancyShouldNotBeFound("vacancyCount.lessThanOrEqual=" + SMALLER_VACANCY_COUNT);
    }

    @Test
    @Transactional
    void getAllVacanciesByVacancyCountIsLessThanSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where vacancyCount is less than DEFAULT_VACANCY_COUNT
        defaultVacancyShouldNotBeFound("vacancyCount.lessThan=" + DEFAULT_VACANCY_COUNT);

        // Get all the vacancyList where vacancyCount is less than UPDATED_VACANCY_COUNT
        defaultVacancyShouldBeFound("vacancyCount.lessThan=" + UPDATED_VACANCY_COUNT);
    }

    @Test
    @Transactional
    void getAllVacanciesByVacancyCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where vacancyCount is greater than DEFAULT_VACANCY_COUNT
        defaultVacancyShouldNotBeFound("vacancyCount.greaterThan=" + DEFAULT_VACANCY_COUNT);

        // Get all the vacancyList where vacancyCount is greater than SMALLER_VACANCY_COUNT
        defaultVacancyShouldBeFound("vacancyCount.greaterThan=" + SMALLER_VACANCY_COUNT);
    }

    @Test
    @Transactional
    void getAllVacanciesByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where location equals to DEFAULT_LOCATION
        defaultVacancyShouldBeFound("location.equals=" + DEFAULT_LOCATION);

        // Get all the vacancyList where location equals to UPDATED_LOCATION
        defaultVacancyShouldNotBeFound("location.equals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllVacanciesByLocationIsInShouldWork() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where location in DEFAULT_LOCATION or UPDATED_LOCATION
        defaultVacancyShouldBeFound("location.in=" + DEFAULT_LOCATION + "," + UPDATED_LOCATION);

        // Get all the vacancyList where location equals to UPDATED_LOCATION
        defaultVacancyShouldNotBeFound("location.in=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllVacanciesByLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where location is not null
        defaultVacancyShouldBeFound("location.specified=true");

        // Get all the vacancyList where location is null
        defaultVacancyShouldNotBeFound("location.specified=false");
    }

    @Test
    @Transactional
    void getAllVacanciesByLocationContainsSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where location contains DEFAULT_LOCATION
        defaultVacancyShouldBeFound("location.contains=" + DEFAULT_LOCATION);

        // Get all the vacancyList where location contains UPDATED_LOCATION
        defaultVacancyShouldNotBeFound("location.contains=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllVacanciesByLocationNotContainsSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where location does not contain DEFAULT_LOCATION
        defaultVacancyShouldNotBeFound("location.doesNotContain=" + DEFAULT_LOCATION);

        // Get all the vacancyList where location does not contain UPDATED_LOCATION
        defaultVacancyShouldBeFound("location.doesNotContain=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllVacanciesByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where phone equals to DEFAULT_PHONE
        defaultVacancyShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the vacancyList where phone equals to UPDATED_PHONE
        defaultVacancyShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllVacanciesByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultVacancyShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the vacancyList where phone equals to UPDATED_PHONE
        defaultVacancyShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllVacanciesByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where phone is not null
        defaultVacancyShouldBeFound("phone.specified=true");

        // Get all the vacancyList where phone is null
        defaultVacancyShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    void getAllVacanciesByPhoneContainsSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where phone contains DEFAULT_PHONE
        defaultVacancyShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the vacancyList where phone contains UPDATED_PHONE
        defaultVacancyShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllVacanciesByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where phone does not contain DEFAULT_PHONE
        defaultVacancyShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the vacancyList where phone does not contain UPDATED_PHONE
        defaultVacancyShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllVacanciesBySalaryIsEqualToSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where salary equals to DEFAULT_SALARY
        defaultVacancyShouldBeFound("salary.equals=" + DEFAULT_SALARY);

        // Get all the vacancyList where salary equals to UPDATED_SALARY
        defaultVacancyShouldNotBeFound("salary.equals=" + UPDATED_SALARY);
    }

    @Test
    @Transactional
    void getAllVacanciesBySalaryIsInShouldWork() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where salary in DEFAULT_SALARY or UPDATED_SALARY
        defaultVacancyShouldBeFound("salary.in=" + DEFAULT_SALARY + "," + UPDATED_SALARY);

        // Get all the vacancyList where salary equals to UPDATED_SALARY
        defaultVacancyShouldNotBeFound("salary.in=" + UPDATED_SALARY);
    }

    @Test
    @Transactional
    void getAllVacanciesBySalaryIsNullOrNotNull() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where salary is not null
        defaultVacancyShouldBeFound("salary.specified=true");

        // Get all the vacancyList where salary is null
        defaultVacancyShouldNotBeFound("salary.specified=false");
    }

    @Test
    @Transactional
    void getAllVacanciesBySalaryIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where salary is greater than or equal to DEFAULT_SALARY
        defaultVacancyShouldBeFound("salary.greaterThanOrEqual=" + DEFAULT_SALARY);

        // Get all the vacancyList where salary is greater than or equal to UPDATED_SALARY
        defaultVacancyShouldNotBeFound("salary.greaterThanOrEqual=" + UPDATED_SALARY);
    }

    @Test
    @Transactional
    void getAllVacanciesBySalaryIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where salary is less than or equal to DEFAULT_SALARY
        defaultVacancyShouldBeFound("salary.lessThanOrEqual=" + DEFAULT_SALARY);

        // Get all the vacancyList where salary is less than or equal to SMALLER_SALARY
        defaultVacancyShouldNotBeFound("salary.lessThanOrEqual=" + SMALLER_SALARY);
    }

    @Test
    @Transactional
    void getAllVacanciesBySalaryIsLessThanSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where salary is less than DEFAULT_SALARY
        defaultVacancyShouldNotBeFound("salary.lessThan=" + DEFAULT_SALARY);

        // Get all the vacancyList where salary is less than UPDATED_SALARY
        defaultVacancyShouldBeFound("salary.lessThan=" + UPDATED_SALARY);
    }

    @Test
    @Transactional
    void getAllVacanciesBySalaryIsGreaterThanSomething() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList where salary is greater than DEFAULT_SALARY
        defaultVacancyShouldNotBeFound("salary.greaterThan=" + DEFAULT_SALARY);

        // Get all the vacancyList where salary is greater than SMALLER_SALARY
        defaultVacancyShouldBeFound("salary.greaterThan=" + SMALLER_SALARY);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVacancyShouldBeFound(String filter) throws Exception {
        restVacancyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vacancy.getId().intValue())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].jobType").value(hasItem(DEFAULT_JOB_TYPE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].vacancyCount").value(hasItem(DEFAULT_VACANCY_COUNT)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].salary").value(hasItem(DEFAULT_SALARY.doubleValue())));

        // Check, that the count call also returns 1
        restVacancyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVacancyShouldNotBeFound(String filter) throws Exception {
        restVacancyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVacancyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingVacancy() throws Exception {
        // Get the vacancy
        restVacancyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVacancy() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        int databaseSizeBeforeUpdate = vacancyRepository.findAll().size();

        // Update the vacancy
        Vacancy updatedVacancy = vacancyRepository.findById(vacancy.getId()).get();
        // Disconnect from session so that the updates on updatedVacancy are not directly saved in db
        em.detach(updatedVacancy);
        updatedVacancy
            .companyName(UPDATED_COMPANY_NAME)
            .jobType(UPDATED_JOB_TYPE)
            .description(UPDATED_DESCRIPTION)
            .vacancyCount(UPDATED_VACANCY_COUNT)
            .location(UPDATED_LOCATION)
            .phone(UPDATED_PHONE)
            .salary(UPDATED_SALARY);
        VacancyDTO vacancyDTO = vacancyMapper.toDto(updatedVacancy);

        restVacancyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vacancyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vacancyDTO))
            )
            .andExpect(status().isOk());

        // Validate the Vacancy in the database
        List<Vacancy> vacancyList = vacancyRepository.findAll();
        assertThat(vacancyList).hasSize(databaseSizeBeforeUpdate);
        Vacancy testVacancy = vacancyList.get(vacancyList.size() - 1);
        assertThat(testVacancy.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testVacancy.getJobType()).isEqualTo(UPDATED_JOB_TYPE);
        assertThat(testVacancy.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testVacancy.getVacancyCount()).isEqualTo(UPDATED_VACANCY_COUNT);
        assertThat(testVacancy.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testVacancy.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testVacancy.getSalary()).isEqualTo(UPDATED_SALARY);
    }

    @Test
    @Transactional
    void putNonExistingVacancy() throws Exception {
        int databaseSizeBeforeUpdate = vacancyRepository.findAll().size();
        vacancy.setId(count.incrementAndGet());

        // Create the Vacancy
        VacancyDTO vacancyDTO = vacancyMapper.toDto(vacancy);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVacancyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vacancyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vacancyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vacancy in the database
        List<Vacancy> vacancyList = vacancyRepository.findAll();
        assertThat(vacancyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVacancy() throws Exception {
        int databaseSizeBeforeUpdate = vacancyRepository.findAll().size();
        vacancy.setId(count.incrementAndGet());

        // Create the Vacancy
        VacancyDTO vacancyDTO = vacancyMapper.toDto(vacancy);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVacancyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vacancyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vacancy in the database
        List<Vacancy> vacancyList = vacancyRepository.findAll();
        assertThat(vacancyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVacancy() throws Exception {
        int databaseSizeBeforeUpdate = vacancyRepository.findAll().size();
        vacancy.setId(count.incrementAndGet());

        // Create the Vacancy
        VacancyDTO vacancyDTO = vacancyMapper.toDto(vacancy);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVacancyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vacancyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vacancy in the database
        List<Vacancy> vacancyList = vacancyRepository.findAll();
        assertThat(vacancyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVacancyWithPatch() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        int databaseSizeBeforeUpdate = vacancyRepository.findAll().size();

        // Update the vacancy using partial update
        Vacancy partialUpdatedVacancy = new Vacancy();
        partialUpdatedVacancy.setId(vacancy.getId());

        partialUpdatedVacancy.vacancyCount(UPDATED_VACANCY_COUNT);

        restVacancyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVacancy.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVacancy))
            )
            .andExpect(status().isOk());

        // Validate the Vacancy in the database
        List<Vacancy> vacancyList = vacancyRepository.findAll();
        assertThat(vacancyList).hasSize(databaseSizeBeforeUpdate);
        Vacancy testVacancy = vacancyList.get(vacancyList.size() - 1);
        assertThat(testVacancy.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testVacancy.getJobType()).isEqualTo(DEFAULT_JOB_TYPE);
        assertThat(testVacancy.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testVacancy.getVacancyCount()).isEqualTo(UPDATED_VACANCY_COUNT);
        assertThat(testVacancy.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testVacancy.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testVacancy.getSalary()).isEqualTo(DEFAULT_SALARY);
    }

    @Test
    @Transactional
    void fullUpdateVacancyWithPatch() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        int databaseSizeBeforeUpdate = vacancyRepository.findAll().size();

        // Update the vacancy using partial update
        Vacancy partialUpdatedVacancy = new Vacancy();
        partialUpdatedVacancy.setId(vacancy.getId());

        partialUpdatedVacancy
            .companyName(UPDATED_COMPANY_NAME)
            .jobType(UPDATED_JOB_TYPE)
            .description(UPDATED_DESCRIPTION)
            .vacancyCount(UPDATED_VACANCY_COUNT)
            .location(UPDATED_LOCATION)
            .phone(UPDATED_PHONE)
            .salary(UPDATED_SALARY);

        restVacancyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVacancy.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVacancy))
            )
            .andExpect(status().isOk());

        // Validate the Vacancy in the database
        List<Vacancy> vacancyList = vacancyRepository.findAll();
        assertThat(vacancyList).hasSize(databaseSizeBeforeUpdate);
        Vacancy testVacancy = vacancyList.get(vacancyList.size() - 1);
        assertThat(testVacancy.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testVacancy.getJobType()).isEqualTo(UPDATED_JOB_TYPE);
        assertThat(testVacancy.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testVacancy.getVacancyCount()).isEqualTo(UPDATED_VACANCY_COUNT);
        assertThat(testVacancy.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testVacancy.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testVacancy.getSalary()).isEqualTo(UPDATED_SALARY);
    }

    @Test
    @Transactional
    void patchNonExistingVacancy() throws Exception {
        int databaseSizeBeforeUpdate = vacancyRepository.findAll().size();
        vacancy.setId(count.incrementAndGet());

        // Create the Vacancy
        VacancyDTO vacancyDTO = vacancyMapper.toDto(vacancy);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVacancyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vacancyDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vacancyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vacancy in the database
        List<Vacancy> vacancyList = vacancyRepository.findAll();
        assertThat(vacancyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVacancy() throws Exception {
        int databaseSizeBeforeUpdate = vacancyRepository.findAll().size();
        vacancy.setId(count.incrementAndGet());

        // Create the Vacancy
        VacancyDTO vacancyDTO = vacancyMapper.toDto(vacancy);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVacancyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vacancyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vacancy in the database
        List<Vacancy> vacancyList = vacancyRepository.findAll();
        assertThat(vacancyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVacancy() throws Exception {
        int databaseSizeBeforeUpdate = vacancyRepository.findAll().size();
        vacancy.setId(count.incrementAndGet());

        // Create the Vacancy
        VacancyDTO vacancyDTO = vacancyMapper.toDto(vacancy);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVacancyMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(vacancyDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vacancy in the database
        List<Vacancy> vacancyList = vacancyRepository.findAll();
        assertThat(vacancyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVacancy() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        int databaseSizeBeforeDelete = vacancyRepository.findAll().size();

        // Delete the vacancy
        restVacancyMockMvc
            .perform(delete(ENTITY_API_URL_ID, vacancy.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vacancy> vacancyList = vacancyRepository.findAll();
        assertThat(vacancyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
