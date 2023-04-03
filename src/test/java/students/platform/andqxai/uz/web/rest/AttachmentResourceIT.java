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
import students.platform.andqxai.uz.domain.Attachment;
import students.platform.andqxai.uz.domain.enumeration.AttachmentType;
import students.platform.andqxai.uz.repository.AttachmentRepository;
import students.platform.andqxai.uz.service.criteria.AttachmentCriteria;
import students.platform.andqxai.uz.service.dto.AttachmentDTO;
import students.platform.andqxai.uz.service.mapper.AttachmentMapper;

/**
 * Integration tests for the {@link AttachmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AttachmentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_ORIGINAL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_ORIGINAL_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_ATTACH_SIZE = 1L;
    private static final Long UPDATED_ATTACH_SIZE = 2L;
    private static final Long SMALLER_ATTACH_SIZE = 1L - 1L;

    private static final String DEFAULT_CONTENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_TYPE = "BBBBBBBBBB";

    private static final AttachmentType DEFAULT_ATTACHMENT_TYPE = AttachmentType.FOOD;
    private static final AttachmentType UPDATED_ATTACHMENT_TYPE = AttachmentType.CAFE;

    private static final Long DEFAULT_OBJECT_ID = 1L;
    private static final Long UPDATED_OBJECT_ID = 2L;
    private static final Long SMALLER_OBJECT_ID = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/attachments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private AttachmentMapper attachmentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAttachmentMockMvc;

    private Attachment attachment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Attachment createEntity(EntityManager em) {
        Attachment attachment = new Attachment()
            .name(DEFAULT_NAME)
            .fileOriginalName(DEFAULT_FILE_ORIGINAL_NAME)
            .attachSize(DEFAULT_ATTACH_SIZE)
            .contentType(DEFAULT_CONTENT_TYPE)
            .attachmentType(DEFAULT_ATTACHMENT_TYPE)
            .objectId(DEFAULT_OBJECT_ID);
        return attachment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Attachment createUpdatedEntity(EntityManager em) {
        Attachment attachment = new Attachment()
            .name(UPDATED_NAME)
            .fileOriginalName(UPDATED_FILE_ORIGINAL_NAME)
            .attachSize(UPDATED_ATTACH_SIZE)
            .contentType(UPDATED_CONTENT_TYPE)
            .attachmentType(UPDATED_ATTACHMENT_TYPE)
            .objectId(UPDATED_OBJECT_ID);
        return attachment;
    }

    @BeforeEach
    public void initTest() {
        attachment = createEntity(em);
    }

    @Test
    @Transactional
    void createAttachment() throws Exception {
        int databaseSizeBeforeCreate = attachmentRepository.findAll().size();
        // Create the Attachment
        AttachmentDTO attachmentDTO = attachmentMapper.toDto(attachment);
        restAttachmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attachmentDTO)))
            .andExpect(status().isCreated());

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeCreate + 1);
        Attachment testAttachment = attachmentList.get(attachmentList.size() - 1);
        assertThat(testAttachment.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAttachment.getFileOriginalName()).isEqualTo(DEFAULT_FILE_ORIGINAL_NAME);
        assertThat(testAttachment.getAttachSize()).isEqualTo(DEFAULT_ATTACH_SIZE);
        assertThat(testAttachment.getContentType()).isEqualTo(DEFAULT_CONTENT_TYPE);
        assertThat(testAttachment.getAttachmentType()).isEqualTo(DEFAULT_ATTACHMENT_TYPE);
        assertThat(testAttachment.getObjectId()).isEqualTo(DEFAULT_OBJECT_ID);
    }

    @Test
    @Transactional
    void createAttachmentWithExistingId() throws Exception {
        // Create the Attachment with an existing ID
        attachment.setId(1L);
        AttachmentDTO attachmentDTO = attachmentMapper.toDto(attachment);

        int databaseSizeBeforeCreate = attachmentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttachmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attachmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAttachmentTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = attachmentRepository.findAll().size();
        // set the field null
        attachment.setAttachmentType(null);

        // Create the Attachment, which fails.
        AttachmentDTO attachmentDTO = attachmentMapper.toDto(attachment);

        restAttachmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attachmentDTO)))
            .andExpect(status().isBadRequest());

        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAttachments() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList
        restAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attachment.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].fileOriginalName").value(hasItem(DEFAULT_FILE_ORIGINAL_NAME)))
            .andExpect(jsonPath("$.[*].attachSize").value(hasItem(DEFAULT_ATTACH_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].contentType").value(hasItem(DEFAULT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].attachmentType").value(hasItem(DEFAULT_ATTACHMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].objectId").value(hasItem(DEFAULT_OBJECT_ID.intValue())));
    }

    @Test
    @Transactional
    void getAttachment() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get the attachment
        restAttachmentMockMvc
            .perform(get(ENTITY_API_URL_ID, attachment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(attachment.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.fileOriginalName").value(DEFAULT_FILE_ORIGINAL_NAME))
            .andExpect(jsonPath("$.attachSize").value(DEFAULT_ATTACH_SIZE.intValue()))
            .andExpect(jsonPath("$.contentType").value(DEFAULT_CONTENT_TYPE))
            .andExpect(jsonPath("$.attachmentType").value(DEFAULT_ATTACHMENT_TYPE.toString()))
            .andExpect(jsonPath("$.objectId").value(DEFAULT_OBJECT_ID.intValue()));
    }

    @Test
    @Transactional
    void getAttachmentsByIdFiltering() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        Long id = attachment.getId();

        defaultAttachmentShouldBeFound("id.equals=" + id);
        defaultAttachmentShouldNotBeFound("id.notEquals=" + id);

        defaultAttachmentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAttachmentShouldNotBeFound("id.greaterThan=" + id);

        defaultAttachmentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAttachmentShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAttachmentsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where name equals to DEFAULT_NAME
        defaultAttachmentShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the attachmentList where name equals to UPDATED_NAME
        defaultAttachmentShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAttachmentsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where name in DEFAULT_NAME or UPDATED_NAME
        defaultAttachmentShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the attachmentList where name equals to UPDATED_NAME
        defaultAttachmentShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAttachmentsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where name is not null
        defaultAttachmentShouldBeFound("name.specified=true");

        // Get all the attachmentList where name is null
        defaultAttachmentShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllAttachmentsByNameContainsSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where name contains DEFAULT_NAME
        defaultAttachmentShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the attachmentList where name contains UPDATED_NAME
        defaultAttachmentShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAttachmentsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where name does not contain DEFAULT_NAME
        defaultAttachmentShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the attachmentList where name does not contain UPDATED_NAME
        defaultAttachmentShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAttachmentsByFileOriginalNameIsEqualToSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where fileOriginalName equals to DEFAULT_FILE_ORIGINAL_NAME
        defaultAttachmentShouldBeFound("fileOriginalName.equals=" + DEFAULT_FILE_ORIGINAL_NAME);

        // Get all the attachmentList where fileOriginalName equals to UPDATED_FILE_ORIGINAL_NAME
        defaultAttachmentShouldNotBeFound("fileOriginalName.equals=" + UPDATED_FILE_ORIGINAL_NAME);
    }

    @Test
    @Transactional
    void getAllAttachmentsByFileOriginalNameIsInShouldWork() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where fileOriginalName in DEFAULT_FILE_ORIGINAL_NAME or UPDATED_FILE_ORIGINAL_NAME
        defaultAttachmentShouldBeFound("fileOriginalName.in=" + DEFAULT_FILE_ORIGINAL_NAME + "," + UPDATED_FILE_ORIGINAL_NAME);

        // Get all the attachmentList where fileOriginalName equals to UPDATED_FILE_ORIGINAL_NAME
        defaultAttachmentShouldNotBeFound("fileOriginalName.in=" + UPDATED_FILE_ORIGINAL_NAME);
    }

    @Test
    @Transactional
    void getAllAttachmentsByFileOriginalNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where fileOriginalName is not null
        defaultAttachmentShouldBeFound("fileOriginalName.specified=true");

        // Get all the attachmentList where fileOriginalName is null
        defaultAttachmentShouldNotBeFound("fileOriginalName.specified=false");
    }

    @Test
    @Transactional
    void getAllAttachmentsByFileOriginalNameContainsSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where fileOriginalName contains DEFAULT_FILE_ORIGINAL_NAME
        defaultAttachmentShouldBeFound("fileOriginalName.contains=" + DEFAULT_FILE_ORIGINAL_NAME);

        // Get all the attachmentList where fileOriginalName contains UPDATED_FILE_ORIGINAL_NAME
        defaultAttachmentShouldNotBeFound("fileOriginalName.contains=" + UPDATED_FILE_ORIGINAL_NAME);
    }

    @Test
    @Transactional
    void getAllAttachmentsByFileOriginalNameNotContainsSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where fileOriginalName does not contain DEFAULT_FILE_ORIGINAL_NAME
        defaultAttachmentShouldNotBeFound("fileOriginalName.doesNotContain=" + DEFAULT_FILE_ORIGINAL_NAME);

        // Get all the attachmentList where fileOriginalName does not contain UPDATED_FILE_ORIGINAL_NAME
        defaultAttachmentShouldBeFound("fileOriginalName.doesNotContain=" + UPDATED_FILE_ORIGINAL_NAME);
    }

    @Test
    @Transactional
    void getAllAttachmentsByAttachSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where attachSize equals to DEFAULT_ATTACH_SIZE
        defaultAttachmentShouldBeFound("attachSize.equals=" + DEFAULT_ATTACH_SIZE);

        // Get all the attachmentList where attachSize equals to UPDATED_ATTACH_SIZE
        defaultAttachmentShouldNotBeFound("attachSize.equals=" + UPDATED_ATTACH_SIZE);
    }

    @Test
    @Transactional
    void getAllAttachmentsByAttachSizeIsInShouldWork() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where attachSize in DEFAULT_ATTACH_SIZE or UPDATED_ATTACH_SIZE
        defaultAttachmentShouldBeFound("attachSize.in=" + DEFAULT_ATTACH_SIZE + "," + UPDATED_ATTACH_SIZE);

        // Get all the attachmentList where attachSize equals to UPDATED_ATTACH_SIZE
        defaultAttachmentShouldNotBeFound("attachSize.in=" + UPDATED_ATTACH_SIZE);
    }

    @Test
    @Transactional
    void getAllAttachmentsByAttachSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where attachSize is not null
        defaultAttachmentShouldBeFound("attachSize.specified=true");

        // Get all the attachmentList where attachSize is null
        defaultAttachmentShouldNotBeFound("attachSize.specified=false");
    }

    @Test
    @Transactional
    void getAllAttachmentsByAttachSizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where attachSize is greater than or equal to DEFAULT_ATTACH_SIZE
        defaultAttachmentShouldBeFound("attachSize.greaterThanOrEqual=" + DEFAULT_ATTACH_SIZE);

        // Get all the attachmentList where attachSize is greater than or equal to UPDATED_ATTACH_SIZE
        defaultAttachmentShouldNotBeFound("attachSize.greaterThanOrEqual=" + UPDATED_ATTACH_SIZE);
    }

    @Test
    @Transactional
    void getAllAttachmentsByAttachSizeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where attachSize is less than or equal to DEFAULT_ATTACH_SIZE
        defaultAttachmentShouldBeFound("attachSize.lessThanOrEqual=" + DEFAULT_ATTACH_SIZE);

        // Get all the attachmentList where attachSize is less than or equal to SMALLER_ATTACH_SIZE
        defaultAttachmentShouldNotBeFound("attachSize.lessThanOrEqual=" + SMALLER_ATTACH_SIZE);
    }

    @Test
    @Transactional
    void getAllAttachmentsByAttachSizeIsLessThanSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where attachSize is less than DEFAULT_ATTACH_SIZE
        defaultAttachmentShouldNotBeFound("attachSize.lessThan=" + DEFAULT_ATTACH_SIZE);

        // Get all the attachmentList where attachSize is less than UPDATED_ATTACH_SIZE
        defaultAttachmentShouldBeFound("attachSize.lessThan=" + UPDATED_ATTACH_SIZE);
    }

    @Test
    @Transactional
    void getAllAttachmentsByAttachSizeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where attachSize is greater than DEFAULT_ATTACH_SIZE
        defaultAttachmentShouldNotBeFound("attachSize.greaterThan=" + DEFAULT_ATTACH_SIZE);

        // Get all the attachmentList where attachSize is greater than SMALLER_ATTACH_SIZE
        defaultAttachmentShouldBeFound("attachSize.greaterThan=" + SMALLER_ATTACH_SIZE);
    }

    @Test
    @Transactional
    void getAllAttachmentsByContentTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where contentType equals to DEFAULT_CONTENT_TYPE
        defaultAttachmentShouldBeFound("contentType.equals=" + DEFAULT_CONTENT_TYPE);

        // Get all the attachmentList where contentType equals to UPDATED_CONTENT_TYPE
        defaultAttachmentShouldNotBeFound("contentType.equals=" + UPDATED_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void getAllAttachmentsByContentTypeIsInShouldWork() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where contentType in DEFAULT_CONTENT_TYPE or UPDATED_CONTENT_TYPE
        defaultAttachmentShouldBeFound("contentType.in=" + DEFAULT_CONTENT_TYPE + "," + UPDATED_CONTENT_TYPE);

        // Get all the attachmentList where contentType equals to UPDATED_CONTENT_TYPE
        defaultAttachmentShouldNotBeFound("contentType.in=" + UPDATED_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void getAllAttachmentsByContentTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where contentType is not null
        defaultAttachmentShouldBeFound("contentType.specified=true");

        // Get all the attachmentList where contentType is null
        defaultAttachmentShouldNotBeFound("contentType.specified=false");
    }

    @Test
    @Transactional
    void getAllAttachmentsByContentTypeContainsSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where contentType contains DEFAULT_CONTENT_TYPE
        defaultAttachmentShouldBeFound("contentType.contains=" + DEFAULT_CONTENT_TYPE);

        // Get all the attachmentList where contentType contains UPDATED_CONTENT_TYPE
        defaultAttachmentShouldNotBeFound("contentType.contains=" + UPDATED_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void getAllAttachmentsByContentTypeNotContainsSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where contentType does not contain DEFAULT_CONTENT_TYPE
        defaultAttachmentShouldNotBeFound("contentType.doesNotContain=" + DEFAULT_CONTENT_TYPE);

        // Get all the attachmentList where contentType does not contain UPDATED_CONTENT_TYPE
        defaultAttachmentShouldBeFound("contentType.doesNotContain=" + UPDATED_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void getAllAttachmentsByAttachmentTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where attachmentType equals to DEFAULT_ATTACHMENT_TYPE
        defaultAttachmentShouldBeFound("attachmentType.equals=" + DEFAULT_ATTACHMENT_TYPE);

        // Get all the attachmentList where attachmentType equals to UPDATED_ATTACHMENT_TYPE
        defaultAttachmentShouldNotBeFound("attachmentType.equals=" + UPDATED_ATTACHMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllAttachmentsByAttachmentTypeIsInShouldWork() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where attachmentType in DEFAULT_ATTACHMENT_TYPE or UPDATED_ATTACHMENT_TYPE
        defaultAttachmentShouldBeFound("attachmentType.in=" + DEFAULT_ATTACHMENT_TYPE + "," + UPDATED_ATTACHMENT_TYPE);

        // Get all the attachmentList where attachmentType equals to UPDATED_ATTACHMENT_TYPE
        defaultAttachmentShouldNotBeFound("attachmentType.in=" + UPDATED_ATTACHMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllAttachmentsByAttachmentTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where attachmentType is not null
        defaultAttachmentShouldBeFound("attachmentType.specified=true");

        // Get all the attachmentList where attachmentType is null
        defaultAttachmentShouldNotBeFound("attachmentType.specified=false");
    }

    @Test
    @Transactional
    void getAllAttachmentsByObjectIdIsEqualToSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where objectId equals to DEFAULT_OBJECT_ID
        defaultAttachmentShouldBeFound("objectId.equals=" + DEFAULT_OBJECT_ID);

        // Get all the attachmentList where objectId equals to UPDATED_OBJECT_ID
        defaultAttachmentShouldNotBeFound("objectId.equals=" + UPDATED_OBJECT_ID);
    }

    @Test
    @Transactional
    void getAllAttachmentsByObjectIdIsInShouldWork() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where objectId in DEFAULT_OBJECT_ID or UPDATED_OBJECT_ID
        defaultAttachmentShouldBeFound("objectId.in=" + DEFAULT_OBJECT_ID + "," + UPDATED_OBJECT_ID);

        // Get all the attachmentList where objectId equals to UPDATED_OBJECT_ID
        defaultAttachmentShouldNotBeFound("objectId.in=" + UPDATED_OBJECT_ID);
    }

    @Test
    @Transactional
    void getAllAttachmentsByObjectIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where objectId is not null
        defaultAttachmentShouldBeFound("objectId.specified=true");

        // Get all the attachmentList where objectId is null
        defaultAttachmentShouldNotBeFound("objectId.specified=false");
    }

    @Test
    @Transactional
    void getAllAttachmentsByObjectIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where objectId is greater than or equal to DEFAULT_OBJECT_ID
        defaultAttachmentShouldBeFound("objectId.greaterThanOrEqual=" + DEFAULT_OBJECT_ID);

        // Get all the attachmentList where objectId is greater than or equal to UPDATED_OBJECT_ID
        defaultAttachmentShouldNotBeFound("objectId.greaterThanOrEqual=" + UPDATED_OBJECT_ID);
    }

    @Test
    @Transactional
    void getAllAttachmentsByObjectIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where objectId is less than or equal to DEFAULT_OBJECT_ID
        defaultAttachmentShouldBeFound("objectId.lessThanOrEqual=" + DEFAULT_OBJECT_ID);

        // Get all the attachmentList where objectId is less than or equal to SMALLER_OBJECT_ID
        defaultAttachmentShouldNotBeFound("objectId.lessThanOrEqual=" + SMALLER_OBJECT_ID);
    }

    @Test
    @Transactional
    void getAllAttachmentsByObjectIdIsLessThanSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where objectId is less than DEFAULT_OBJECT_ID
        defaultAttachmentShouldNotBeFound("objectId.lessThan=" + DEFAULT_OBJECT_ID);

        // Get all the attachmentList where objectId is less than UPDATED_OBJECT_ID
        defaultAttachmentShouldBeFound("objectId.lessThan=" + UPDATED_OBJECT_ID);
    }

    @Test
    @Transactional
    void getAllAttachmentsByObjectIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachmentList where objectId is greater than DEFAULT_OBJECT_ID
        defaultAttachmentShouldNotBeFound("objectId.greaterThan=" + DEFAULT_OBJECT_ID);

        // Get all the attachmentList where objectId is greater than SMALLER_OBJECT_ID
        defaultAttachmentShouldBeFound("objectId.greaterThan=" + SMALLER_OBJECT_ID);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAttachmentShouldBeFound(String filter) throws Exception {
        restAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attachment.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].fileOriginalName").value(hasItem(DEFAULT_FILE_ORIGINAL_NAME)))
            .andExpect(jsonPath("$.[*].attachSize").value(hasItem(DEFAULT_ATTACH_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].contentType").value(hasItem(DEFAULT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].attachmentType").value(hasItem(DEFAULT_ATTACHMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].objectId").value(hasItem(DEFAULT_OBJECT_ID.intValue())));

        // Check, that the count call also returns 1
        restAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAttachmentShouldNotBeFound(String filter) throws Exception {
        restAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAttachment() throws Exception {
        // Get the attachment
        restAttachmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAttachment() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        int databaseSizeBeforeUpdate = attachmentRepository.findAll().size();

        // Update the attachment
        Attachment updatedAttachment = attachmentRepository.findById(attachment.getId()).get();
        // Disconnect from session so that the updates on updatedAttachment are not directly saved in db
        em.detach(updatedAttachment);
        updatedAttachment
            .name(UPDATED_NAME)
            .fileOriginalName(UPDATED_FILE_ORIGINAL_NAME)
            .attachSize(UPDATED_ATTACH_SIZE)
            .contentType(UPDATED_CONTENT_TYPE)
            .attachmentType(UPDATED_ATTACHMENT_TYPE)
            .objectId(UPDATED_OBJECT_ID);
        AttachmentDTO attachmentDTO = attachmentMapper.toDto(updatedAttachment);

        restAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, attachmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attachmentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeUpdate);
        Attachment testAttachment = attachmentList.get(attachmentList.size() - 1);
        assertThat(testAttachment.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAttachment.getFileOriginalName()).isEqualTo(UPDATED_FILE_ORIGINAL_NAME);
        assertThat(testAttachment.getAttachSize()).isEqualTo(UPDATED_ATTACH_SIZE);
        assertThat(testAttachment.getContentType()).isEqualTo(UPDATED_CONTENT_TYPE);
        assertThat(testAttachment.getAttachmentType()).isEqualTo(UPDATED_ATTACHMENT_TYPE);
        assertThat(testAttachment.getObjectId()).isEqualTo(UPDATED_OBJECT_ID);
    }

    @Test
    @Transactional
    void putNonExistingAttachment() throws Exception {
        int databaseSizeBeforeUpdate = attachmentRepository.findAll().size();
        attachment.setId(count.incrementAndGet());

        // Create the Attachment
        AttachmentDTO attachmentDTO = attachmentMapper.toDto(attachment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, attachmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attachmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAttachment() throws Exception {
        int databaseSizeBeforeUpdate = attachmentRepository.findAll().size();
        attachment.setId(count.incrementAndGet());

        // Create the Attachment
        AttachmentDTO attachmentDTO = attachmentMapper.toDto(attachment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attachmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAttachment() throws Exception {
        int databaseSizeBeforeUpdate = attachmentRepository.findAll().size();
        attachment.setId(count.incrementAndGet());

        // Create the Attachment
        AttachmentDTO attachmentDTO = attachmentMapper.toDto(attachment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttachmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attachmentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAttachmentWithPatch() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        int databaseSizeBeforeUpdate = attachmentRepository.findAll().size();

        // Update the attachment using partial update
        Attachment partialUpdatedAttachment = new Attachment();
        partialUpdatedAttachment.setId(attachment.getId());

        partialUpdatedAttachment.fileOriginalName(UPDATED_FILE_ORIGINAL_NAME).contentType(UPDATED_CONTENT_TYPE);

        restAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAttachment))
            )
            .andExpect(status().isOk());

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeUpdate);
        Attachment testAttachment = attachmentList.get(attachmentList.size() - 1);
        assertThat(testAttachment.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAttachment.getFileOriginalName()).isEqualTo(UPDATED_FILE_ORIGINAL_NAME);
        assertThat(testAttachment.getAttachSize()).isEqualTo(DEFAULT_ATTACH_SIZE);
        assertThat(testAttachment.getContentType()).isEqualTo(UPDATED_CONTENT_TYPE);
        assertThat(testAttachment.getAttachmentType()).isEqualTo(DEFAULT_ATTACHMENT_TYPE);
        assertThat(testAttachment.getObjectId()).isEqualTo(DEFAULT_OBJECT_ID);
    }

    @Test
    @Transactional
    void fullUpdateAttachmentWithPatch() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        int databaseSizeBeforeUpdate = attachmentRepository.findAll().size();

        // Update the attachment using partial update
        Attachment partialUpdatedAttachment = new Attachment();
        partialUpdatedAttachment.setId(attachment.getId());

        partialUpdatedAttachment
            .name(UPDATED_NAME)
            .fileOriginalName(UPDATED_FILE_ORIGINAL_NAME)
            .attachSize(UPDATED_ATTACH_SIZE)
            .contentType(UPDATED_CONTENT_TYPE)
            .attachmentType(UPDATED_ATTACHMENT_TYPE)
            .objectId(UPDATED_OBJECT_ID);

        restAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAttachment))
            )
            .andExpect(status().isOk());

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeUpdate);
        Attachment testAttachment = attachmentList.get(attachmentList.size() - 1);
        assertThat(testAttachment.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAttachment.getFileOriginalName()).isEqualTo(UPDATED_FILE_ORIGINAL_NAME);
        assertThat(testAttachment.getAttachSize()).isEqualTo(UPDATED_ATTACH_SIZE);
        assertThat(testAttachment.getContentType()).isEqualTo(UPDATED_CONTENT_TYPE);
        assertThat(testAttachment.getAttachmentType()).isEqualTo(UPDATED_ATTACHMENT_TYPE);
        assertThat(testAttachment.getObjectId()).isEqualTo(UPDATED_OBJECT_ID);
    }

    @Test
    @Transactional
    void patchNonExistingAttachment() throws Exception {
        int databaseSizeBeforeUpdate = attachmentRepository.findAll().size();
        attachment.setId(count.incrementAndGet());

        // Create the Attachment
        AttachmentDTO attachmentDTO = attachmentMapper.toDto(attachment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, attachmentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attachmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAttachment() throws Exception {
        int databaseSizeBeforeUpdate = attachmentRepository.findAll().size();
        attachment.setId(count.incrementAndGet());

        // Create the Attachment
        AttachmentDTO attachmentDTO = attachmentMapper.toDto(attachment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attachmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAttachment() throws Exception {
        int databaseSizeBeforeUpdate = attachmentRepository.findAll().size();
        attachment.setId(count.incrementAndGet());

        // Create the Attachment
        AttachmentDTO attachmentDTO = attachmentMapper.toDto(attachment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(attachmentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAttachment() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        int databaseSizeBeforeDelete = attachmentRepository.findAll().size();

        // Delete the attachment
        restAttachmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, attachment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Attachment> attachmentList = attachmentRepository.findAll();
        assertThat(attachmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
