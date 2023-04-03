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
import students.platform.andqxai.uz.domain.Authors;
import students.platform.andqxai.uz.repository.AuthorsRepository;
import students.platform.andqxai.uz.service.criteria.AuthorsCriteria;
import students.platform.andqxai.uz.service.dto.AuthorsDTO;
import students.platform.andqxai.uz.service.mapper.AuthorsMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Authors} entities in the database.
 * The main input is a {@link AuthorsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AuthorsDTO} or a {@link Page} of {@link AuthorsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AuthorsQueryService extends QueryService<Authors> {

    private final Logger log = LoggerFactory.getLogger(AuthorsQueryService.class);

    private final AuthorsRepository authorsRepository;

    private final AuthorsMapper authorsMapper;

    public AuthorsQueryService(AuthorsRepository authorsRepository, AuthorsMapper authorsMapper) {
        this.authorsRepository = authorsRepository;
        this.authorsMapper = authorsMapper;
    }

    /**
     * Return a {@link List} of {@link AuthorsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AuthorsDTO> findByCriteria(AuthorsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Authors> specification = createSpecification(criteria);
        return authorsMapper.toDto(authorsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AuthorsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AuthorsDTO> findByCriteria(AuthorsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Authors> specification = createSpecification(criteria);
        return authorsRepository.findAll(specification, page).map(authorsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AuthorsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Authors> specification = createSpecification(criteria);
        return authorsRepository.count(specification);
    }

    /**
     * Function to convert {@link AuthorsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Authors> createSpecification(AuthorsCriteria criteria) {
        Specification<Authors> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Authors_.id));
            }
            if (criteria.getFullName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFullName(), Authors_.fullName));
            }
            if (criteria.getBirthDay() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBirthDay(), Authors_.birthDay));
            }
            if (criteria.getDedDay() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDedDay(), Authors_.dedDay));
            }
            if (criteria.getBookId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getBookId(), root -> root.join(Authors_.books, JoinType.LEFT).get(Book_.id))
                    );
            }
        }
        return specification;
    }
}
