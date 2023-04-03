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
import students.platform.andqxai.uz.repository.ApartmentRepository;
import students.platform.andqxai.uz.service.ApartmentQueryService;
import students.platform.andqxai.uz.service.ApartmentService;
import students.platform.andqxai.uz.service.criteria.ApartmentCriteria;
import students.platform.andqxai.uz.service.dto.ApartmentDTO;
import students.platform.andqxai.uz.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link students.platform.andqxai.uz.domain.Apartment}.
 */
@RestController
@RequestMapping("/api")
public class ApartmentResource {

    private final Logger log = LoggerFactory.getLogger(ApartmentResource.class);

    private static final String ENTITY_NAME = "apartment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApartmentService apartmentService;

    private final ApartmentRepository apartmentRepository;

    private final ApartmentQueryService apartmentQueryService;

    public ApartmentResource(
        ApartmentService apartmentService,
        ApartmentRepository apartmentRepository,
        ApartmentQueryService apartmentQueryService
    ) {
        this.apartmentService = apartmentService;
        this.apartmentRepository = apartmentRepository;
        this.apartmentQueryService = apartmentQueryService;
    }

    /**
     * {@code POST  /apartments} : Create a new apartment.
     *
     * @param apartmentDTO the apartmentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new apartmentDTO, or with status {@code 400 (Bad Request)} if the apartment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/apartments")
    public ResponseEntity<ApartmentDTO> createApartment(@RequestBody ApartmentDTO apartmentDTO) throws URISyntaxException {
        log.debug("REST request to save Apartment : {}", apartmentDTO);
        if (apartmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new apartment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ApartmentDTO result = apartmentService.save(apartmentDTO);
        return ResponseEntity
            .created(new URI("/api/apartments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /apartments/:id} : Updates an existing apartment.
     *
     * @param id the id of the apartmentDTO to save.
     * @param apartmentDTO the apartmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apartmentDTO,
     * or with status {@code 400 (Bad Request)} if the apartmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the apartmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/apartments/{id}")
    public ResponseEntity<ApartmentDTO> updateApartment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ApartmentDTO apartmentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Apartment : {}, {}", id, apartmentDTO);
        if (apartmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, apartmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!apartmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ApartmentDTO result = apartmentService.update(apartmentDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, apartmentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /apartments/:id} : Partial updates given fields of an existing apartment, field will ignore if it is null
     *
     * @param id the id of the apartmentDTO to save.
     * @param apartmentDTO the apartmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apartmentDTO,
     * or with status {@code 400 (Bad Request)} if the apartmentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the apartmentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the apartmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/apartments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ApartmentDTO> partialUpdateApartment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ApartmentDTO apartmentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Apartment partially : {}, {}", id, apartmentDTO);
        if (apartmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, apartmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!apartmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ApartmentDTO> result = apartmentService.partialUpdate(apartmentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, apartmentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /apartments} : get all the apartments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of apartments in body.
     */
    @GetMapping("/apartments")
    public ResponseEntity<List<ApartmentDTO>> getAllApartments(
        ApartmentCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Apartments by criteria: {}", criteria);
        Page<ApartmentDTO> page = apartmentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /apartments/count} : count all the apartments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/apartments/count")
    public ResponseEntity<Long> countApartments(ApartmentCriteria criteria) {
        log.debug("REST request to count Apartments by criteria: {}", criteria);
        return ResponseEntity.ok().body(apartmentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /apartments/:id} : get the "id" apartment.
     *
     * @param id the id of the apartmentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the apartmentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/apartments/{id}")
    public ResponseEntity<ApartmentDTO> getApartment(@PathVariable Long id) {
        log.debug("REST request to get Apartment : {}", id);
        Optional<ApartmentDTO> apartmentDTO = apartmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(apartmentDTO);
    }

    /**
     * {@code DELETE  /apartments/:id} : delete the "id" apartment.
     *
     * @param id the id of the apartmentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/apartments/{id}")
    public ResponseEntity<Void> deleteApartment(@PathVariable Long id) {
        log.debug("REST request to delete Apartment : {}", id);
        apartmentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
