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
import students.platform.andqxai.uz.repository.FoodRepository;
import students.platform.andqxai.uz.service.FoodQueryService;
import students.platform.andqxai.uz.service.FoodService;
import students.platform.andqxai.uz.service.criteria.FoodCriteria;
import students.platform.andqxai.uz.service.dto.FoodDTO;
import students.platform.andqxai.uz.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link students.platform.andqxai.uz.domain.Food}.
 */
@RestController
@RequestMapping("/api")
public class FoodResource {

    private final Logger log = LoggerFactory.getLogger(FoodResource.class);

    private static final String ENTITY_NAME = "food";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FoodService foodService;

    private final FoodRepository foodRepository;

    private final FoodQueryService foodQueryService;

    public FoodResource(FoodService foodService, FoodRepository foodRepository, FoodQueryService foodQueryService) {
        this.foodService = foodService;
        this.foodRepository = foodRepository;
        this.foodQueryService = foodQueryService;
    }

    /**
     * {@code POST  /foods} : Create a new food.
     *
     * @param foodDTO the foodDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new foodDTO, or with status {@code 400 (Bad Request)} if the food has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/foods")
    public ResponseEntity<FoodDTO> createFood(@RequestBody FoodDTO foodDTO) throws URISyntaxException {
        log.debug("REST request to save Food : {}", foodDTO);
        if (foodDTO.getId() != null) {
            throw new BadRequestAlertException("A new food cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FoodDTO result = foodService.save(foodDTO);
        return ResponseEntity
            .created(new URI("/api/foods/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /foods/:id} : Updates an existing food.
     *
     * @param id the id of the foodDTO to save.
     * @param foodDTO the foodDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated foodDTO,
     * or with status {@code 400 (Bad Request)} if the foodDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the foodDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/foods/{id}")
    public ResponseEntity<FoodDTO> updateFood(@PathVariable(value = "id", required = false) final Long id, @RequestBody FoodDTO foodDTO)
        throws URISyntaxException {
        log.debug("REST request to update Food : {}, {}", id, foodDTO);
        if (foodDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, foodDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!foodRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FoodDTO result = foodService.update(foodDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, foodDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /foods/:id} : Partial updates given fields of an existing food, field will ignore if it is null
     *
     * @param id the id of the foodDTO to save.
     * @param foodDTO the foodDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated foodDTO,
     * or with status {@code 400 (Bad Request)} if the foodDTO is not valid,
     * or with status {@code 404 (Not Found)} if the foodDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the foodDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/foods/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FoodDTO> partialUpdateFood(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FoodDTO foodDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Food partially : {}, {}", id, foodDTO);
        if (foodDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, foodDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!foodRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FoodDTO> result = foodService.partialUpdate(foodDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, foodDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /foods} : get all the foods.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of foods in body.
     */
    @GetMapping("/foods")
    public ResponseEntity<List<FoodDTO>> getAllFoods(
        FoodCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Foods by criteria: {}", criteria);
        Page<FoodDTO> page = foodQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /foods/count} : count all the foods.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/foods/count")
    public ResponseEntity<Long> countFoods(FoodCriteria criteria) {
        log.debug("REST request to count Foods by criteria: {}", criteria);
        return ResponseEntity.ok().body(foodQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /foods/:id} : get the "id" food.
     *
     * @param id the id of the foodDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the foodDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/foods/{id}")
    public ResponseEntity<FoodDTO> getFood(@PathVariable Long id) {
        log.debug("REST request to get Food : {}", id);
        Optional<FoodDTO> foodDTO = foodService.findOne(id);
        return ResponseUtil.wrapOrNotFound(foodDTO);
    }

    /**
     * {@code DELETE  /foods/:id} : delete the "id" food.
     *
     * @param id the id of the foodDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/foods/{id}")
    public ResponseEntity<Void> deleteFood(@PathVariable Long id) {
        log.debug("REST request to delete Food : {}", id);
        foodService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
