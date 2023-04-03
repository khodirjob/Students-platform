package students.platform.andqxai.uz.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import students.platform.andqxai.uz.domain.Schedule;
import students.platform.andqxai.uz.repository.ScheduleRepository;
import students.platform.andqxai.uz.service.dto.ScheduleDTO;
import students.platform.andqxai.uz.service.mapper.ScheduleMapper;

/**
 * Service Implementation for managing {@link Schedule}.
 */
@Service
@Transactional
public class ScheduleService {

    private final Logger log = LoggerFactory.getLogger(ScheduleService.class);

    private final ScheduleRepository scheduleRepository;

    private final ScheduleMapper scheduleMapper;

    public ScheduleService(ScheduleRepository scheduleRepository, ScheduleMapper scheduleMapper) {
        this.scheduleRepository = scheduleRepository;
        this.scheduleMapper = scheduleMapper;
    }

    /**
     * Save a schedule.
     *
     * @param scheduleDTO the entity to save.
     * @return the persisted entity.
     */
    public ScheduleDTO save(ScheduleDTO scheduleDTO) {
        log.debug("Request to save Schedule : {}", scheduleDTO);
        Schedule schedule = scheduleMapper.toEntity(scheduleDTO);
        schedule = scheduleRepository.save(schedule);
        return scheduleMapper.toDto(schedule);
    }

    /**
     * Update a schedule.
     *
     * @param scheduleDTO the entity to save.
     * @return the persisted entity.
     */
    public ScheduleDTO update(ScheduleDTO scheduleDTO) {
        log.debug("Request to update Schedule : {}", scheduleDTO);
        Schedule schedule = scheduleMapper.toEntity(scheduleDTO);
        schedule = scheduleRepository.save(schedule);
        return scheduleMapper.toDto(schedule);
    }

    /**
     * Partially update a schedule.
     *
     * @param scheduleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ScheduleDTO> partialUpdate(ScheduleDTO scheduleDTO) {
        log.debug("Request to partially update Schedule : {}", scheduleDTO);

        return scheduleRepository
            .findById(scheduleDTO.getId())
            .map(existingSchedule -> {
                scheduleMapper.partialUpdate(existingSchedule, scheduleDTO);

                return existingSchedule;
            })
            .map(scheduleRepository::save)
            .map(scheduleMapper::toDto);
    }

    /**
     * Get all the schedules.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ScheduleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Schedules");
        return scheduleRepository.findAll(pageable).map(scheduleMapper::toDto);
    }

    /**
     * Get one schedule by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ScheduleDTO> findOne(Long id) {
        log.debug("Request to get Schedule : {}", id);
        return scheduleRepository.findById(id).map(scheduleMapper::toDto);
    }

    /**
     * Delete the schedule by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Schedule : {}", id);
        scheduleRepository.deleteById(id);
    }
}
