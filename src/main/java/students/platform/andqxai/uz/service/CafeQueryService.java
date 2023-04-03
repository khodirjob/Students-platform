package students.platform.andqxai.uz.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import students.platform.andqxai.uz.domain.*; // for static metamodels
import students.platform.andqxai.uz.domain.Cafe;
import students.platform.andqxai.uz.repository.CafeRepository;
import students.platform.andqxai.uz.service.criteria.CafeCriteria;
import students.platform.andqxai.uz.service.dto.CafeDTO;
import students.platform.andqxai.uz.service.mapper.CafeMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Cafe} entities in the database.
 * The main input is a {@link CafeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CafeDTO} or a {@link Page} of {@link CafeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CafeQueryService extends QueryService<Cafe> {

    private final Logger log = LoggerFactory.getLogger(CafeQueryService.class);

    private final CafeRepository cafeRepository;

    private final CafeMapper cafeMapper;

    public CafeQueryService(CafeRepository cafeRepository, CafeMapper cafeMapper) {
        this.cafeRepository = cafeRepository;
        this.cafeMapper = cafeMapper;
    }

    /**
     * Return a {@link List} of {@link CafeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CafeDTO> findByCriteria(CafeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Cafe> specification = createSpecification(criteria);
        return cafeMapper.toDto(cafeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CafeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CafeDTO> findByCriteria(CafeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Cafe> specification = createSpecification(criteria);
        return cafeRepository.findAll(specification, page).map(cafeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CafeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Cafe> specification = createSpecification(criteria);
        return cafeRepository.count(specification);
    }

    /**
     * Function to convert {@link CafeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Cafe> createSpecification(CafeCriteria criteria) {
        Specification<Cafe> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Cafe_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Cafe_.description));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Cafe_.name));
            }
            if (criteria.getLocation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocation(), Cafe_.location));
            }
            if (criteria.getOpeningTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOpeningTime(), Cafe_.openingTime));
            }
            if (criteria.getCloseTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCloseTime(), Cafe_.closeTime));
            }
        }
        return specification;
    }
}
