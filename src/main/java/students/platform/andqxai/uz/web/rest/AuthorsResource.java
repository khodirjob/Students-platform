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
import students.platform.andqxai.uz.repository.AuthorsRepository;
import students.platform.andqxai.uz.service.AuthorsQueryService;
import students.platform.andqxai.uz.service.AuthorsService;
import students.platform.andqxai.uz.service.criteria.AuthorsCriteria;
import students.platform.andqxai.uz.service.dto.AuthorsDTO;
import students.platform.andqxai.uz.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link students.platform.andqxai.uz.domain.Authors}.
 */
@RestController
@RequestMapping("/api")
public class AuthorsResource {

    private final Logger log = LoggerFactory.getLogger(AuthorsResource.class);

    private static final String ENTITY_NAME = "authors";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AuthorsService authorsService;

    private final AuthorsRepository authorsRepository;

    private final AuthorsQueryService authorsQueryService;

    public AuthorsResource(AuthorsService authorsService, AuthorsRepository authorsRepository, AuthorsQueryService authorsQueryService) {
        this.authorsService = authorsService;
        this.authorsRepository = authorsRepository;
        this.authorsQueryService = authorsQueryService;
    }

    /**
     * {@code POST  /authors} : Create a new authors.
     *
     * @param authorsDTO the authorsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new authorsDTO, or with status {@code 400 (Bad Request)} if the authors has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/authors")
    public ResponseEntity<AuthorsDTO> createAuthors(@RequestBody AuthorsDTO authorsDTO) throws URISyntaxException {
        log.debug("REST request to save Authors : {}", authorsDTO);
        if (authorsDTO.getId() != null) {
            throw new BadRequestAlertException("A new authors cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AuthorsDTO result = authorsService.save(authorsDTO);
        return ResponseEntity
            .created(new URI("/api/authors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /authors/:id} : Updates an existing authors.
     *
     * @param id the id of the authorsDTO to save.
     * @param authorsDTO the authorsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated authorsDTO,
     * or with status {@code 400 (Bad Request)} if the authorsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the authorsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/authors/{id}")
    public ResponseEntity<AuthorsDTO> updateAuthors(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AuthorsDTO authorsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Authors : {}, {}", id, authorsDTO);
        if (authorsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, authorsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!authorsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AuthorsDTO result = authorsService.update(authorsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, authorsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /authors/:id} : Partial updates given fields of an existing authors, field will ignore if it is null
     *
     * @param id the id of the authorsDTO to save.
     * @param authorsDTO the authorsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated authorsDTO,
     * or with status {@code 400 (Bad Request)} if the authorsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the authorsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the authorsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/authors/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AuthorsDTO> partialUpdateAuthors(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AuthorsDTO authorsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Authors partially : {}, {}", id, authorsDTO);
        if (authorsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, authorsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!authorsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AuthorsDTO> result = authorsService.partialUpdate(authorsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, authorsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /authors} : get all the authors.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of authors in body.
     */
    @GetMapping("/authors")
    public ResponseEntity<List<AuthorsDTO>> getAllAuthors(
        AuthorsCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Authors by criteria: {}", criteria);
        Page<AuthorsDTO> page = authorsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /authors/count} : count all the authors.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/authors/count")
    public ResponseEntity<Long> countAuthors(AuthorsCriteria criteria) {
        log.debug("REST request to count Authors by criteria: {}", criteria);
        return ResponseEntity.ok().body(authorsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /authors/:id} : get the "id" authors.
     *
     * @param id the id of the authorsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the authorsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/authors/{id}")
    public ResponseEntity<AuthorsDTO> getAuthors(@PathVariable Long id) {
        log.debug("REST request to get Authors : {}", id);
        Optional<AuthorsDTO> authorsDTO = authorsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(authorsDTO);
    }

    /**
     * {@code DELETE  /authors/:id} : delete the "id" authors.
     *
     * @param id the id of the authorsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/authors/{id}")
    public ResponseEntity<Void> deleteAuthors(@PathVariable Long id) {
        log.debug("REST request to delete Authors : {}", id);
        authorsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
