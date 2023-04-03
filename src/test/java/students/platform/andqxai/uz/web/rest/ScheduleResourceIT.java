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
import students.platform.andqxai.uz.domain.Schedule;
import students.platform.andqxai.uz.repository.ScheduleRepository;
import students.platform.andqxai.uz.service.criteria.ScheduleCriteria;
import students.platform.andqxai.uz.service.dto.ScheduleDTO;
import students.platform.andqxai.uz.service.mapper.ScheduleMapper;

/**
 * Integration tests for the {@link ScheduleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ScheduleResourceIT {

    private static final String DEFAULT_FACULTY = "AAAAAAAAAA";
    private static final String UPDATED_FACULTY = "BBBBBBBBBB";

    private static final Integer DEFAULT_LEVEL = 1;
    private static final Integer UPDATED_LEVEL = 2;
    private static final Integer SMALLER_LEVEL = 1 - 1;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/schedules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ScheduleMapper scheduleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restScheduleMockMvc;

    private Schedule schedule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Schedule createEntity(EntityManager em) {
        Schedule schedule = new Schedule().faculty(DEFAULT_FACULTY).level(DEFAULT_LEVEL).description(DEFAULT_DESCRIPTION);
        return schedule;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Schedule createUpdatedEntity(EntityManager em) {
        Schedule schedule = new Schedule().faculty(UPDATED_FACULTY).level(UPDATED_LEVEL).description(UPDATED_DESCRIPTION);
        return schedule;
    }

    @BeforeEach
    public void initTest() {
        schedule = createEntity(em);
    }

    @Test
    @Transactional
    void createSchedule() throws Exception {
        int databaseSizeBeforeCreate = scheduleRepository.findAll().size();
        // Create the Schedule
        ScheduleDTO scheduleDTO = scheduleMapper.toDto(schedule);
        restScheduleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(scheduleDTO)))
            .andExpect(status().isCreated());

        // Validate the Schedule in the database
        List<Schedule> scheduleList = scheduleRepository.findAll();
        assertThat(scheduleList).hasSize(databaseSizeBeforeCreate + 1);
        Schedule testSchedule = scheduleList.get(scheduleList.size() - 1);
        assertThat(testSchedule.getFaculty()).isEqualTo(DEFAULT_FACULTY);
        assertThat(testSchedule.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testSchedule.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createScheduleWithExistingId() throws Exception {
        // Create the Schedule with an existing ID
        schedule.setId(1L);
        ScheduleDTO scheduleDTO = scheduleMapper.toDto(schedule);

        int databaseSizeBeforeCreate = scheduleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restScheduleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(scheduleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Schedule in the database
        List<Schedule> scheduleList = scheduleRepository.findAll();
        assertThat(scheduleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSchedules() throws Exception {
        // Initialize the database
        scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList
        restScheduleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schedule.getId().intValue())))
            .andExpect(jsonPath("$.[*].faculty").value(hasItem(DEFAULT_FACULTY)))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getSchedule() throws Exception {
        // Initialize the database
        scheduleRepository.saveAndFlush(schedule);

        // Get the schedule
        restScheduleMockMvc
            .perform(get(ENTITY_API_URL_ID, schedule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(schedule.getId().intValue()))
            .andExpect(jsonPath("$.faculty").value(DEFAULT_FACULTY))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getSchedulesByIdFiltering() throws Exception {
        // Initialize the database
        scheduleRepository.saveAndFlush(schedule);

        Long id = schedule.getId();

        defaultScheduleShouldBeFound("id.equals=" + id);
        defaultScheduleShouldNotBeFound("id.notEquals=" + id);

        defaultScheduleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultScheduleShouldNotBeFound("id.greaterThan=" + id);

        defaultScheduleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultScheduleShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSchedulesByFacultyIsEqualToSomething() throws Exception {
        // Initialize the database
        scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where faculty equals to DEFAULT_FACULTY
        defaultScheduleShouldBeFound("faculty.equals=" + DEFAULT_FACULTY);

        // Get all the scheduleList where faculty equals to UPDATED_FACULTY
        defaultScheduleShouldNotBeFound("faculty.equals=" + UPDATED_FACULTY);
    }

    @Test
    @Transactional
    void getAllSchedulesByFacultyIsInShouldWork() throws Exception {
        // Initialize the database
        scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where faculty in DEFAULT_FACULTY or UPDATED_FACULTY
        defaultScheduleShouldBeFound("faculty.in=" + DEFAULT_FACULTY + "," + UPDATED_FACULTY);

        // Get all the scheduleList where faculty equals to UPDATED_FACULTY
        defaultScheduleShouldNotBeFound("faculty.in=" + UPDATED_FACULTY);
    }

    @Test
    @Transactional
    void getAllSchedulesByFacultyIsNullOrNotNull() throws Exception {
        // Initialize the database
        scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where faculty is not null
        defaultScheduleShouldBeFound("faculty.specified=true");

        // Get all the scheduleList where faculty is null
        defaultScheduleShouldNotBeFound("faculty.specified=false");
    }

    @Test
    @Transactional
    void getAllSchedulesByFacultyContainsSomething() throws Exception {
        // Initialize the database
        scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where faculty contains DEFAULT_FACULTY
        defaultScheduleShouldBeFound("faculty.contains=" + DEFAULT_FACULTY);

        // Get all the scheduleList where faculty contains UPDATED_FACULTY
        defaultScheduleShouldNotBeFound("faculty.contains=" + UPDATED_FACULTY);
    }

    @Test
    @Transactional
    void getAllSchedulesByFacultyNotContainsSomething() throws Exception {
        // Initialize the database
        scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where faculty does not contain DEFAULT_FACULTY
        defaultScheduleShouldNotBeFound("faculty.doesNotContain=" + DEFAULT_FACULTY);

        // Get all the scheduleList where faculty does not contain UPDATED_FACULTY
        defaultScheduleShouldBeFound("faculty.doesNotContain=" + UPDATED_FACULTY);
    }

    @Test
    @Transactional
    void getAllSchedulesByLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where level equals to DEFAULT_LEVEL
        defaultScheduleShouldBeFound("level.equals=" + DEFAULT_LEVEL);

        // Get all the scheduleList where level equals to UPDATED_LEVEL
        defaultScheduleShouldNotBeFound("level.equals=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllSchedulesByLevelIsInShouldWork() throws Exception {
        // Initialize the database
        scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where level in DEFAULT_LEVEL or UPDATED_LEVEL
        defaultScheduleShouldBeFound("level.in=" + DEFAULT_LEVEL + "," + UPDATED_LEVEL);

        // Get all the scheduleList where level equals to UPDATED_LEVEL
        defaultScheduleShouldNotBeFound("level.in=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllSchedulesByLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where level is not null
        defaultScheduleShouldBeFound("level.specified=true");

        // Get all the scheduleList where level is null
        defaultScheduleShouldNotBeFound("level.specified=false");
    }

    @Test
    @Transactional
    void getAllSchedulesByLevelIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where level is greater than or equal to DEFAULT_LEVEL
        defaultScheduleShouldBeFound("level.greaterThanOrEqual=" + DEFAULT_LEVEL);

        // Get all the scheduleList where level is greater than or equal to UPDATED_LEVEL
        defaultScheduleShouldNotBeFound("level.greaterThanOrEqual=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllSchedulesByLevelIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where level is less than or equal to DEFAULT_LEVEL
        defaultScheduleShouldBeFound("level.lessThanOrEqual=" + DEFAULT_LEVEL);

        // Get all the scheduleList where level is less than or equal to SMALLER_LEVEL
        defaultScheduleShouldNotBeFound("level.lessThanOrEqual=" + SMALLER_LEVEL);
    }

    @Test
    @Transactional
    void getAllSchedulesByLevelIsLessThanSomething() throws Exception {
        // Initialize the database
        scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where level is less than DEFAULT_LEVEL
        defaultScheduleShouldNotBeFound("level.lessThan=" + DEFAULT_LEVEL);

        // Get all the scheduleList where level is less than UPDATED_LEVEL
        defaultScheduleShouldBeFound("level.lessThan=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllSchedulesByLevelIsGreaterThanSomething() throws Exception {
        // Initialize the database
        scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where level is greater than DEFAULT_LEVEL
        defaultScheduleShouldNotBeFound("level.greaterThan=" + DEFAULT_LEVEL);

        // Get all the scheduleList where level is greater than SMALLER_LEVEL
        defaultScheduleShouldBeFound("level.greaterThan=" + SMALLER_LEVEL);
    }

    @Test
    @Transactional
    void getAllSchedulesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where description equals to DEFAULT_DESCRIPTION
        defaultScheduleShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the scheduleList where description equals to UPDATED_DESCRIPTION
        defaultScheduleShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSchedulesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultScheduleShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the scheduleList where description equals to UPDATED_DESCRIPTION
        defaultScheduleShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSchedulesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where description is not null
        defaultScheduleShouldBeFound("description.specified=true");

        // Get all the scheduleList where description is null
        defaultScheduleShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllSchedulesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where description contains DEFAULT_DESCRIPTION
        defaultScheduleShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the scheduleList where description contains UPDATED_DESCRIPTION
        defaultScheduleShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSchedulesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where description does not contain DEFAULT_DESCRIPTION
        defaultScheduleShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the scheduleList where description does not contain UPDATED_DESCRIPTION
        defaultScheduleShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultScheduleShouldBeFound(String filter) throws Exception {
        restScheduleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schedule.getId().intValue())))
            .andExpect(jsonPath("$.[*].faculty").value(hasItem(DEFAULT_FACULTY)))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restScheduleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultScheduleShouldNotBeFound(String filter) throws Exception {
        restScheduleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restScheduleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSchedule() throws Exception {
        // Get the schedule
        restScheduleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSchedule() throws Exception {
        // Initialize the database
        scheduleRepository.saveAndFlush(schedule);

        int databaseSizeBeforeUpdate = scheduleRepository.findAll().size();

        // Update the schedule
        Schedule updatedSchedule = scheduleRepository.findById(schedule.getId()).get();
        // Disconnect from session so that the updates on updatedSchedule are not directly saved in db
        em.detach(updatedSchedule);
        updatedSchedule.faculty(UPDATED_FACULTY).level(UPDATED_LEVEL).description(UPDATED_DESCRIPTION);
        ScheduleDTO scheduleDTO = scheduleMapper.toDto(updatedSchedule);

        restScheduleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, scheduleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scheduleDTO))
            )
            .andExpect(status().isOk());

        // Validate the Schedule in the database
        List<Schedule> scheduleList = scheduleRepository.findAll();
        assertThat(scheduleList).hasSize(databaseSizeBeforeUpdate);
        Schedule testSchedule = scheduleList.get(scheduleList.size() - 1);
        assertThat(testSchedule.getFaculty()).isEqualTo(UPDATED_FACULTY);
        assertThat(testSchedule.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testSchedule.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingSchedule() throws Exception {
        int databaseSizeBeforeUpdate = scheduleRepository.findAll().size();
        schedule.setId(count.incrementAndGet());

        // Create the Schedule
        ScheduleDTO scheduleDTO = scheduleMapper.toDto(schedule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScheduleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, scheduleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scheduleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Schedule in the database
        List<Schedule> scheduleList = scheduleRepository.findAll();
        assertThat(scheduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSchedule() throws Exception {
        int databaseSizeBeforeUpdate = scheduleRepository.findAll().size();
        schedule.setId(count.incrementAndGet());

        // Create the Schedule
        ScheduleDTO scheduleDTO = scheduleMapper.toDto(schedule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scheduleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Schedule in the database
        List<Schedule> scheduleList = scheduleRepository.findAll();
        assertThat(scheduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSchedule() throws Exception {
        int databaseSizeBeforeUpdate = scheduleRepository.findAll().size();
        schedule.setId(count.incrementAndGet());

        // Create the Schedule
        ScheduleDTO scheduleDTO = scheduleMapper.toDto(schedule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(scheduleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Schedule in the database
        List<Schedule> scheduleList = scheduleRepository.findAll();
        assertThat(scheduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateScheduleWithPatch() throws Exception {
        // Initialize the database
        scheduleRepository.saveAndFlush(schedule);

        int databaseSizeBeforeUpdate = scheduleRepository.findAll().size();

        // Update the schedule using partial update
        Schedule partialUpdatedSchedule = new Schedule();
        partialUpdatedSchedule.setId(schedule.getId());

        partialUpdatedSchedule.faculty(UPDATED_FACULTY);

        restScheduleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchedule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSchedule))
            )
            .andExpect(status().isOk());

        // Validate the Schedule in the database
        List<Schedule> scheduleList = scheduleRepository.findAll();
        assertThat(scheduleList).hasSize(databaseSizeBeforeUpdate);
        Schedule testSchedule = scheduleList.get(scheduleList.size() - 1);
        assertThat(testSchedule.getFaculty()).isEqualTo(UPDATED_FACULTY);
        assertThat(testSchedule.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testSchedule.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateScheduleWithPatch() throws Exception {
        // Initialize the database
        scheduleRepository.saveAndFlush(schedule);

        int databaseSizeBeforeUpdate = scheduleRepository.findAll().size();

        // Update the schedule using partial update
        Schedule partialUpdatedSchedule = new Schedule();
        partialUpdatedSchedule.setId(schedule.getId());

        partialUpdatedSchedule.faculty(UPDATED_FACULTY).level(UPDATED_LEVEL).description(UPDATED_DESCRIPTION);

        restScheduleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchedule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSchedule))
            )
            .andExpect(status().isOk());

        // Validate the Schedule in the database
        List<Schedule> scheduleList = scheduleRepository.findAll();
        assertThat(scheduleList).hasSize(databaseSizeBeforeUpdate);
        Schedule testSchedule = scheduleList.get(scheduleList.size() - 1);
        assertThat(testSchedule.getFaculty()).isEqualTo(UPDATED_FACULTY);
        assertThat(testSchedule.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testSchedule.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingSchedule() throws Exception {
        int databaseSizeBeforeUpdate = scheduleRepository.findAll().size();
        schedule.setId(count.incrementAndGet());

        // Create the Schedule
        ScheduleDTO scheduleDTO = scheduleMapper.toDto(schedule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScheduleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, scheduleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(scheduleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Schedule in the database
        List<Schedule> scheduleList = scheduleRepository.findAll();
        assertThat(scheduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSchedule() throws Exception {
        int databaseSizeBeforeUpdate = scheduleRepository.findAll().size();
        schedule.setId(count.incrementAndGet());

        // Create the Schedule
        ScheduleDTO scheduleDTO = scheduleMapper.toDto(schedule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(scheduleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Schedule in the database
        List<Schedule> scheduleList = scheduleRepository.findAll();
        assertThat(scheduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSchedule() throws Exception {
        int databaseSizeBeforeUpdate = scheduleRepository.findAll().size();
        schedule.setId(count.incrementAndGet());

        // Create the Schedule
        ScheduleDTO scheduleDTO = scheduleMapper.toDto(schedule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduleMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(scheduleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Schedule in the database
        List<Schedule> scheduleList = scheduleRepository.findAll();
        assertThat(scheduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSchedule() throws Exception {
        // Initialize the database
        scheduleRepository.saveAndFlush(schedule);

        int databaseSizeBeforeDelete = scheduleRepository.findAll().size();

        // Delete the schedule
        restScheduleMockMvc
            .perform(delete(ENTITY_API_URL_ID, schedule.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Schedule> scheduleList = scheduleRepository.findAll();
        assertThat(scheduleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
