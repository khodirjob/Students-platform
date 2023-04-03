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
import students.platform.andqxai.uz.repository.CafeRepository;
import students.platform.andqxai.uz.service.CafeQueryService;
import students.platform.andqxai.uz.service.CafeService;
import students.platform.andqxai.uz.service.criteria.CafeCriteria;
import students.platform.andqxai.uz.service.dto.CafeDTO;
import students.platform.andqxai.uz.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link students.platform.andqxai.uz.domain.Cafe}.
 */
@RestController
@RequestMapping("/api")
public class CafeResource {

    private final Logger log = LoggerFactory.getLogger(CafeResource.class);

    private static final String ENTITY_NAME = "cafe";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CafeService cafeService;

    private final CafeRepository cafeRepository;

    private final CafeQueryService cafeQueryService;

    public CafeResource(CafeService cafeService, CafeRepository cafeRepository, CafeQueryService cafeQueryService) {
        this.cafeService = cafeService;
        this.cafeRepository = cafeRepository;
        this.cafeQueryService = cafeQueryService;
    }

    /**
     * {@code POST  /cafes} : Create a new cafe.
     *
     * @param cafeDTO the cafeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cafeDTO, or with status {@code 400 (Bad Request)} if the cafe has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cafes")
    public ResponseEntity<CafeDTO> createCafe(@RequestBody CafeDTO cafeDTO) throws URISyntaxException {
        log.debug("REST request to save Cafe : {}", cafeDTO);
        if (cafeDTO.getId() != null) {
            throw new BadRequestAlertException("A new cafe cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CafeDTO result = cafeService.save(cafeDTO);
        return ResponseEntity
            .created(new URI("/api/cafes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cafes/:id} : Updates an existing cafe.
     *
     * @param id the id of the cafeDTO to save.
     * @param cafeDTO the cafeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cafeDTO,
     * or with status {@code 400 (Bad Request)} if the cafeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cafeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cafes/{id}")
    public ResponseEntity<CafeDTO> updateCafe(@PathVariable(value = "id", required = false) final Long id, @RequestBody CafeDTO cafeDTO)
        throws URISyntaxException {
        log.debug("REST request to update Cafe : {}, {}", id, cafeDTO);
        if (cafeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cafeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cafeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CafeDTO result = cafeService.update(cafeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cafeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cafes/:id} : Partial updates given fields of an existing cafe, field will ignore if it is null
     *
     * @param id the id of the cafeDTO to save.
     * @param cafeDTO the cafeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cafeDTO,
     * or with status {@code 400 (Bad Request)} if the cafeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cafeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cafeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cafes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CafeDTO> partialUpdateCafe(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CafeDTO cafeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Cafe partially : {}, {}", id, cafeDTO);
        if (cafeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cafeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cafeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CafeDTO> result = cafeService.partialUpdate(cafeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cafeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cafes} : get all the cafes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cafes in body.
     */
    @GetMapping("/cafes")
    public ResponseEntity<List<CafeDTO>> getAllCafes(
        CafeCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Cafes by criteria: {}", criteria);
        Page<CafeDTO> page = cafeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cafes/count} : count all the cafes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/cafes/count")
    public ResponseEntity<Long> countCafes(CafeCriteria criteria) {
        log.debug("REST request to count Cafes by criteria: {}", criteria);
        return ResponseEntity.ok().body(cafeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cafes/:id} : get the "id" cafe.
     *
     * @param id the id of the cafeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cafeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cafes/{id}")
    public ResponseEntity<CafeDTO> getCafe(@PathVariable Long id) {
        log.debug("REST request to get Cafe : {}", id);
        Optional<CafeDTO> cafeDTO = cafeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cafeDTO);
    }

    /**
     * {@code DELETE  /cafes/:id} : delete the "id" cafe.
     *
     * @param id the id of the cafeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cafes/{id}")
    public ResponseEntity<Void> deleteCafe(@PathVariable Long id) {
        log.debug("REST request to delete Cafe : {}", id);
        cafeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
