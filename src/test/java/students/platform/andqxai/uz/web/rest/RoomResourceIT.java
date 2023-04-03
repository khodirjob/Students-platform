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
import students.platform.andqxai.uz.domain.Room;
import students.platform.andqxai.uz.repository.RoomRepository;
import students.platform.andqxai.uz.service.criteria.RoomCriteria;
import students.platform.andqxai.uz.service.dto.RoomDTO;
import students.platform.andqxai.uz.service.mapper.RoomMapper;

/**
 * Integration tests for the {@link RoomResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RoomResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_BUILDING = "AAAAAAAAAA";
    private static final String UPDATED_BUILDING = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/rooms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRoomMockMvc;

    private Room room;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Room createEntity(EntityManager em) {
        Room room = new Room().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).building(DEFAULT_BUILDING);
        return room;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Room createUpdatedEntity(EntityManager em) {
        Room room = new Room().name(UPDATED_NAME).description(UPDATED_DESCRIPTION).building(UPDATED_BUILDING);
        return room;
    }

    @BeforeEach
    public void initTest() {
        room = createEntity(em);
    }

    @Test
    @Transactional
    void createRoom() throws Exception {
        int databaseSizeBeforeCreate = roomRepository.findAll().size();
        // Create the Room
        RoomDTO roomDTO = roomMapper.toDto(room);
        restRoomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roomDTO)))
            .andExpect(status().isCreated());

        // Validate the Room in the database
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeCreate + 1);
        Room testRoom = roomList.get(roomList.size() - 1);
        assertThat(testRoom.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRoom.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRoom.getBuilding()).isEqualTo(DEFAULT_BUILDING);
    }

    @Test
    @Transactional
    void createRoomWithExistingId() throws Exception {
        // Create the Room with an existing ID
        room.setId(1L);
        RoomDTO roomDTO = roomMapper.toDto(room);

        int databaseSizeBeforeCreate = roomRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roomDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Room in the database
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRooms() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get all the roomList
        restRoomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(room.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].building").value(hasItem(DEFAULT_BUILDING)));
    }

    @Test
    @Transactional
    void getRoom() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get the room
        restRoomMockMvc
            .perform(get(ENTITY_API_URL_ID, room.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(room.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.building").value(DEFAULT_BUILDING));
    }

    @Test
    @Transactional
    void getRoomsByIdFiltering() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        Long id = room.getId();

        defaultRoomShouldBeFound("id.equals=" + id);
        defaultRoomShouldNotBeFound("id.notEquals=" + id);

        defaultRoomShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRoomShouldNotBeFound("id.greaterThan=" + id);

        defaultRoomShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRoomShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRoomsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get all the roomList where name equals to DEFAULT_NAME
        defaultRoomShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the roomList where name equals to UPDATED_NAME
        defaultRoomShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRoomsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get all the roomList where name in DEFAULT_NAME or UPDATED_NAME
        defaultRoomShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the roomList where name equals to UPDATED_NAME
        defaultRoomShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRoomsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get all the roomList where name is not null
        defaultRoomShouldBeFound("name.specified=true");

        // Get all the roomList where name is null
        defaultRoomShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllRoomsByNameContainsSomething() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get all the roomList where name contains DEFAULT_NAME
        defaultRoomShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the roomList where name contains UPDATED_NAME
        defaultRoomShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRoomsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get all the roomList where name does not contain DEFAULT_NAME
        defaultRoomShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the roomList where name does not contain UPDATED_NAME
        defaultRoomShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRoomsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get all the roomList where description equals to DEFAULT_DESCRIPTION
        defaultRoomShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the roomList where description equals to UPDATED_DESCRIPTION
        defaultRoomShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRoomsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get all the roomList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultRoomShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the roomList where description equals to UPDATED_DESCRIPTION
        defaultRoomShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRoomsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get all the roomList where description is not null
        defaultRoomShouldBeFound("description.specified=true");

        // Get all the roomList where description is null
        defaultRoomShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllRoomsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get all the roomList where description contains DEFAULT_DESCRIPTION
        defaultRoomShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the roomList where description contains UPDATED_DESCRIPTION
        defaultRoomShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRoomsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get all the roomList where description does not contain DEFAULT_DESCRIPTION
        defaultRoomShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the roomList where description does not contain UPDATED_DESCRIPTION
        defaultRoomShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRoomsByBuildingIsEqualToSomething() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get all the roomList where building equals to DEFAULT_BUILDING
        defaultRoomShouldBeFound("building.equals=" + DEFAULT_BUILDING);

        // Get all the roomList where building equals to UPDATED_BUILDING
        defaultRoomShouldNotBeFound("building.equals=" + UPDATED_BUILDING);
    }

    @Test
    @Transactional
    void getAllRoomsByBuildingIsInShouldWork() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get all the roomList where building in DEFAULT_BUILDING or UPDATED_BUILDING
        defaultRoomShouldBeFound("building.in=" + DEFAULT_BUILDING + "," + UPDATED_BUILDING);

        // Get all the roomList where building equals to UPDATED_BUILDING
        defaultRoomShouldNotBeFound("building.in=" + UPDATED_BUILDING);
    }

    @Test
    @Transactional
    void getAllRoomsByBuildingIsNullOrNotNull() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get all the roomList where building is not null
        defaultRoomShouldBeFound("building.specified=true");

        // Get all the roomList where building is null
        defaultRoomShouldNotBeFound("building.specified=false");
    }

    @Test
    @Transactional
    void getAllRoomsByBuildingContainsSomething() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get all the roomList where building contains DEFAULT_BUILDING
        defaultRoomShouldBeFound("building.contains=" + DEFAULT_BUILDING);

        // Get all the roomList where building contains UPDATED_BUILDING
        defaultRoomShouldNotBeFound("building.contains=" + UPDATED_BUILDING);
    }

    @Test
    @Transactional
    void getAllRoomsByBuildingNotContainsSomething() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get all the roomList where building does not contain DEFAULT_BUILDING
        defaultRoomShouldNotBeFound("building.doesNotContain=" + DEFAULT_BUILDING);

        // Get all the roomList where building does not contain UPDATED_BUILDING
        defaultRoomShouldBeFound("building.doesNotContain=" + UPDATED_BUILDING);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRoomShouldBeFound(String filter) throws Exception {
        restRoomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(room.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].building").value(hasItem(DEFAULT_BUILDING)));

        // Check, that the count call also returns 1
        restRoomMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRoomShouldNotBeFound(String filter) throws Exception {
        restRoomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRoomMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRoom() throws Exception {
        // Get the room
        restRoomMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRoom() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        int databaseSizeBeforeUpdate = roomRepository.findAll().size();

        // Update the room
        Room updatedRoom = roomRepository.findById(room.getId()).get();
        // Disconnect from session so that the updates on updatedRoom are not directly saved in db
        em.detach(updatedRoom);
        updatedRoom.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).building(UPDATED_BUILDING);
        RoomDTO roomDTO = roomMapper.toDto(updatedRoom);

        restRoomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roomDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roomDTO))
            )
            .andExpect(status().isOk());

        // Validate the Room in the database
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeUpdate);
        Room testRoom = roomList.get(roomList.size() - 1);
        assertThat(testRoom.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRoom.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRoom.getBuilding()).isEqualTo(UPDATED_BUILDING);
    }

    @Test
    @Transactional
    void putNonExistingRoom() throws Exception {
        int databaseSizeBeforeUpdate = roomRepository.findAll().size();
        room.setId(count.incrementAndGet());

        // Create the Room
        RoomDTO roomDTO = roomMapper.toDto(room);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roomDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Room in the database
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRoom() throws Exception {
        int databaseSizeBeforeUpdate = roomRepository.findAll().size();
        room.setId(count.incrementAndGet());

        // Create the Room
        RoomDTO roomDTO = roomMapper.toDto(room);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Room in the database
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRoom() throws Exception {
        int databaseSizeBeforeUpdate = roomRepository.findAll().size();
        room.setId(count.incrementAndGet());

        // Create the Room
        RoomDTO roomDTO = roomMapper.toDto(room);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roomDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Room in the database
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRoomWithPatch() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        int databaseSizeBeforeUpdate = roomRepository.findAll().size();

        // Update the room using partial update
        Room partialUpdatedRoom = new Room();
        partialUpdatedRoom.setId(room.getId());

        partialUpdatedRoom.description(UPDATED_DESCRIPTION).building(UPDATED_BUILDING);

        restRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRoom))
            )
            .andExpect(status().isOk());

        // Validate the Room in the database
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeUpdate);
        Room testRoom = roomList.get(roomList.size() - 1);
        assertThat(testRoom.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRoom.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRoom.getBuilding()).isEqualTo(UPDATED_BUILDING);
    }

    @Test
    @Transactional
    void fullUpdateRoomWithPatch() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        int databaseSizeBeforeUpdate = roomRepository.findAll().size();

        // Update the room using partial update
        Room partialUpdatedRoom = new Room();
        partialUpdatedRoom.setId(room.getId());

        partialUpdatedRoom.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).building(UPDATED_BUILDING);

        restRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRoom))
            )
            .andExpect(status().isOk());

        // Validate the Room in the database
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeUpdate);
        Room testRoom = roomList.get(roomList.size() - 1);
        assertThat(testRoom.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRoom.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRoom.getBuilding()).isEqualTo(UPDATED_BUILDING);
    }

    @Test
    @Transactional
    void patchNonExistingRoom() throws Exception {
        int databaseSizeBeforeUpdate = roomRepository.findAll().size();
        room.setId(count.incrementAndGet());

        // Create the Room
        RoomDTO roomDTO = roomMapper.toDto(room);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, roomDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Room in the database
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRoom() throws Exception {
        int databaseSizeBeforeUpdate = roomRepository.findAll().size();
        room.setId(count.incrementAndGet());

        // Create the Room
        RoomDTO roomDTO = roomMapper.toDto(room);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Room in the database
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRoom() throws Exception {
        int databaseSizeBeforeUpdate = roomRepository.findAll().size();
        room.setId(count.incrementAndGet());

        // Create the Room
        RoomDTO roomDTO = roomMapper.toDto(room);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(roomDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Room in the database
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRoom() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        int databaseSizeBeforeDelete = roomRepository.findAll().size();

        // Delete the room
        restRoomMockMvc
            .perform(delete(ENTITY_API_URL_ID, room.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
