package students.platform.andqxai.uz.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import students.platform.andqxai.uz.domain.Vacancy;
import students.platform.andqxai.uz.repository.VacancyRepository;
import students.platform.andqxai.uz.service.dto.VacancyDTO;
import students.platform.andqxai.uz.service.mapper.VacancyMapper;

/**
 * Service Implementation for managing {@link Vacancy}.
 */
@Service
@Transactional
public class VacancyService {

    private final Logger log = LoggerFactory.getLogger(VacancyService.class);

    private final VacancyRepository vacancyRepository;

    private final VacancyMapper vacancyMapper;

    public VacancyService(VacancyRepository vacancyRepository, VacancyMapper vacancyMapper) {
        this.vacancyRepository = vacancyRepository;
        this.vacancyMapper = vacancyMapper;
    }

    /**
     * Save a vacancy.
     *
     * @param vacancyDTO the entity to save.
     * @return the persisted entity.
     */
    public VacancyDTO save(VacancyDTO vacancyDTO) {
        log.debug("Request to save Vacancy : {}", vacancyDTO);
        Vacancy vacancy = vacancyMapper.toEntity(vacancyDTO);
        vacancy = vacancyRepository.save(vacancy);
        return vacancyMapper.toDto(vacancy);
    }

    /**
     * Update a vacancy.
     *
     * @param vacancyDTO the entity to save.
     * @return the persisted entity.
     */
    public VacancyDTO update(VacancyDTO vacancyDTO) {
        log.debug("Request to update Vacancy : {}", vacancyDTO);
        Vacancy vacancy = vacancyMapper.toEntity(vacancyDTO);
        vacancy = vacancyRepository.save(vacancy);
        return vacancyMapper.toDto(vacancy);
    }

    /**
     * Partially update a vacancy.
     *
     * @param vacancyDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<VacancyDTO> partialUpdate(VacancyDTO vacancyDTO) {
        log.debug("Request to partially update Vacancy : {}", vacancyDTO);

        return vacancyRepository
            .findById(vacancyDTO.getId())
            .map(existingVacancy -> {
                vacancyMapper.partialUpdate(existingVacancy, vacancyDTO);

                return existingVacancy;
            })
            .map(vacancyRepository::save)
            .map(vacancyMapper::toDto);
    }

    /**
     * Get all the vacancies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<VacancyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Vacancies");
        return vacancyRepository.findAll(pageable).map(vacancyMapper::toDto);
    }

    /**
     * Get one vacancy by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<VacancyDTO> findOne(Long id) {
        log.debug("Request to get Vacancy : {}", id);
        return vacancyRepository.findById(id).map(vacancyMapper::toDto);
    }

    /**
     * Delete the vacancy by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Vacancy : {}", id);
        vacancyRepository.deleteById(id);
    }
}
