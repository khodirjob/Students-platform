package students.platform.andqxai.uz.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import students.platform.andqxai.uz.repository.VacancyRepository;
import students.platform.andqxai.uz.service.VacancyQueryService;
import students.platform.andqxai.uz.service.VacancyService;
import students.platform.andqxai.uz.service.criteria.VacancyCriteria;
import students.platform.andqxai.uz.service.dto.VacancyDTO;
import students.platform.andqxai.uz.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link students.platform.andqxai.uz.domain.Vacancy}.
 */
@RestController
@RequestMapping("/api")
public class VacancyResource {

    private final Logger log = LoggerFactory.getLogger(VacancyResource.class);

    private static final String ENTITY_NAME = "vacancy";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VacancyService vacancyService;

    private final VacancyRepository vacancyRepository;

    private final VacancyQueryService vacancyQueryService;

    public VacancyResource(VacancyService vacancyService, VacancyRepository vacancyRepository, VacancyQueryService vacancyQueryService) {
        this.vacancyService = vacancyService;
        this.vacancyRepository = vacancyRepository;
        this.vacancyQueryService = vacancyQueryService;
    }

    /**
     * {@code POST  /vacancies} : Create a new vacancy.
     *
     * @param vacancyDTO the vacancyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vacancyDTO, or with status {@code 400 (Bad Request)} if the vacancy has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vacancies")
    public ResponseEntity<VacancyDTO> createVacancy(@RequestBody VacancyDTO vacancyDTO) throws URISyntaxException {
        log.debug("REST request to save Vacancy : {}", vacancyDTO);
        if (vacancyDTO.getId() != null) {
            throw new BadRequestAlertException("A new vacancy cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VacancyDTO result = vacancyService.save(vacancyDTO);
        return ResponseEntity
            .created(new URI("/api/vacancies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vacancies/:id} : Updates an existing vacancy.
     *
     * @param id the id of the vacancyDTO to save.
     * @param vacancyDTO the vacancyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vacancyDTO,
     * or with status {@code 400 (Bad Request)} if the vacancyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vacancyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vacancies/{id}")
    public ResponseEntity<VacancyDTO> updateVacancy(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VacancyDTO vacancyDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Vacancy : {}, {}", id, vacancyDTO);
        if (vacancyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vacancyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vacancyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VacancyDTO result = vacancyService.update(vacancyDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vacancyDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /vacancies/:id} : Partial updates given fields of an existing vacancy, field will ignore if it is null
     *
     * @param id the id of the vacancyDTO to save.
     * @param vacancyDTO the vacancyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vacancyDTO,
     * or with status {@code 400 (Bad Request)} if the vacancyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vacancyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vacancyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/vacancies/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VacancyDTO> partialUpdateVacancy(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VacancyDTO vacancyDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Vacancy partially : {}, {}", id, vacancyDTO);
        if (vacancyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vacancyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vacancyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VacancyDTO> result = vacancyService.partialUpdate(vacancyDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vacancyDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /vacancies} : get all the vacancies.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vacancies in body.
     */
    @GetMapping("/vacancies")
    public ResponseEntity<List<VacancyDTO>> getAllVacancies(
        VacancyCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Vacancies by criteria: {}", criteria);
        Page<VacancyDTO> page = vacancyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vacancies/count} : count all the vacancies.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/vacancies/count")
    public ResponseEntity<Long> countVacancies(VacancyCriteria criteria) {
        log.debug("REST request to count Vacancies by criteria: {}", criteria);
        return ResponseEntity.ok().body(vacancyQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /vacancies/:id} : get the "id" vacancy.
     *
     * @param id the id of the vacancyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vacancyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vacancies/{id}")
    public ResponseEntity<VacancyDTO> getVacancy(@PathVariable Long id) {
        log.debug("REST request to get Vacancy : {}", id);
        Optional<VacancyDTO> vacancyDTO = vacancyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vacancyDTO);
    }

    /**
     * {@code DELETE  /vacancies/:id} : delete the "id" vacancy.
     *
     * @param id the id of the vacancyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vacancies/{id}")
    public ResponseEntity<Void> deleteVacancy(@PathVariable Long id) {
        log.debug("REST request to delete Vacancy : {}", id);
        vacancyService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
