package students.platform.andqxai.uz.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import students.platform.andqxai.uz.domain.Cafe;
import students.platform.andqxai.uz.repository.CafeRepository;
import students.platform.andqxai.uz.service.dto.CafeDTO;
import students.platform.andqxai.uz.service.mapper.CafeMapper;

/**
 * Service Implementation for managing {@link Cafe}.
 */
@Service
@Transactional
public class CafeService {

    private final Logger log = LoggerFactory.getLogger(CafeService.class);

    private final CafeRepository cafeRepository;

    private final CafeMapper cafeMapper;

    public CafeService(CafeRepository cafeRepository, CafeMapper cafeMapper) {
        this.cafeRepository = cafeRepository;
        this.cafeMapper = cafeMapper;
    }

    /**
     * Save a cafe.
     *
     * @param cafeDTO the entity to save.
     * @return the persisted entity.
     */
    public CafeDTO save(CafeDTO cafeDTO) {
        log.debug("Request to save Cafe : {}", cafeDTO);
        Cafe cafe = cafeMapper.toEntity(cafeDTO);
        cafe = cafeRepository.save(cafe);
        return cafeMapper.toDto(cafe);
    }

    /**
     * Update a cafe.
     *
     * @param cafeDTO the entity to save.
     * @return the persisted entity.
     */
    public CafeDTO update(CafeDTO cafeDTO) {
        log.debug("Request to update Cafe : {}", cafeDTO);
        Cafe cafe = cafeMapper.toEntity(cafeDTO);
        cafe = cafeRepository.save(cafe);
        return cafeMapper.toDto(cafe);
    }

    /**
     * Partially update a cafe.
     *
     * @param cafeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CafeDTO> partialUpdate(CafeDTO cafeDTO) {
        log.debug("Request to partially update Cafe : {}", cafeDTO);

        return cafeRepository
            .findById(cafeDTO.getId())
            .map(existingCafe -> {
                cafeMapper.partialUpdate(existingCafe, cafeDTO);

                return existingCafe;
            })
            .map(cafeRepository::save)
            .map(cafeMapper::toDto);
    }

    /**
     * Get all the cafes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CafeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Cafes");
        return cafeRepository.findAll(pageable).map(cafeMapper::toDto);
    }

    /**
     * Get one cafe by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CafeDTO> findOne(Long id) {
        log.debug("Request to get Cafe : {}", id);
        return cafeRepository.findById(id).map(cafeMapper::toDto);
    }

    /**
     * Delete the cafe by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Cafe : {}", id);
        cafeRepository.deleteById(id);
    }
}
