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
import students.platform.andqxai.uz.domain.Vacancy;
import students.platform.andqxai.uz.repository.VacancyRepository;
import students.platform.andqxai.uz.service.criteria.VacancyCriteria;
import students.platform.andqxai.uz.service.dto.VacancyDTO;
import students.platform.andqxai.uz.service.mapper.VacancyMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Vacancy} entities in the database.
 * The main input is a {@link VacancyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link VacancyDTO} or a {@link Page} of {@link VacancyDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VacancyQueryService extends QueryService<Vacancy> {

    private final Logger log = LoggerFactory.getLogger(VacancyQueryService.class);

    private final VacancyRepository vacancyRepository;

    private final VacancyMapper vacancyMapper;

    public VacancyQueryService(VacancyRepository vacancyRepository, VacancyMapper vacancyMapper) {
        this.vacancyRepository = vacancyRepository;
        this.vacancyMapper = vacancyMapper;
    }

    /**
     * Return a {@link List} of {@link VacancyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<VacancyDTO> findByCriteria(VacancyCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Vacancy> specification = createSpecification(criteria);
        return vacancyMapper.toDto(vacancyRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link VacancyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<VacancyDTO> findByCriteria(VacancyCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Vacancy> specification = createSpecification(criteria);
        return vacancyRepository.findAll(specification, page).map(vacancyMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VacancyCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Vacancy> specification = createSpecification(criteria);
        return vacancyRepository.count(specification);
    }

    /**
     * Function to convert {@link VacancyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Vacancy> createSpecification(VacancyCriteria criteria) {
        Specification<Vacancy> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Vacancy_.id));
            }
            if (criteria.getCompanyName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCompanyName(), Vacancy_.companyName));
            }
            if (criteria.getJobType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getJobType(), Vacancy_.jobType));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Vacancy_.description));
            }
            if (criteria.getVacancyCount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVacancyCount(), Vacancy_.vacancyCount));
            }
            if (criteria.getLocation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocation(), Vacancy_.location));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), Vacancy_.phone));
            }
            if (criteria.getSalary() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSalary(), Vacancy_.salary));
            }
        }
        return specification;
    }
}
