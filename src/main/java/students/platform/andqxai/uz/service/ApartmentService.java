package students.platform.andqxai.uz.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import students.platform.andqxai.uz.domain.Apartment;
import students.platform.andqxai.uz.repository.ApartmentRepository;
import students.platform.andqxai.uz.service.dto.ApartmentDTO;
import students.platform.andqxai.uz.service.mapper.ApartmentMapper;

/**
 * Service Implementation for managing {@link Apartment}.
 */
@Service
@Transactional
public class ApartmentService {

    private final Logger log = LoggerFactory.getLogger(ApartmentService.class);

    private final ApartmentRepository apartmentRepository;

    private final ApartmentMapper apartmentMapper;

    public ApartmentService(ApartmentRepository apartmentRepository, ApartmentMapper apartmentMapper) {
        this.apartmentRepository = apartmentRepository;
        this.apartmentMapper = apartmentMapper;
    }

    /**
     * Save a apartment.
     *
     * @param apartmentDTO the entity to save.
     * @return the persisted entity.
     */
    public ApartmentDTO save(ApartmentDTO apartmentDTO) {
        log.debug("Request to save Apartment : {}", apartmentDTO);
        Apartment apartment = apartmentMapper.toEntity(apartmentDTO);
        apartment = apartmentRepository.save(apartment);
        return apartmentMapper.toDto(apartment);
    }

    /**
     * Update a apartment.
     *
     * @param apartmentDTO the entity to save.
     * @return the persisted entity.
     */
    public ApartmentDTO update(ApartmentDTO apartmentDTO) {
        log.debug("Request to update Apartment : {}", apartmentDTO);
        Apartment apartment = apartmentMapper.toEntity(apartmentDTO);
        apartment = apartmentRepository.save(apartment);
        return apartmentMapper.toDto(apartment);
    }

    /**
     * Partially update a apartment.
     *
     * @param apartmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ApartmentDTO> partialUpdate(ApartmentDTO apartmentDTO) {
        log.debug("Request to partially update Apartment : {}", apartmentDTO);

        return apartmentRepository
            .findById(apartmentDTO.getId())
            .map(existingApartment -> {
                apartmentMapper.partialUpdate(existingApartment, apartmentDTO);

                return existingApartment;
            })
            .map(apartmentRepository::save)
            .map(apartmentMapper::toDto);
    }

    /**
     * Get all the apartments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ApartmentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Apartments");
        return apartmentRepository.findAll(pageable).map(apartmentMapper::toDto);
    }

    /**
     * Get one apartment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ApartmentDTO> findOne(Long id) {
        log.debug("Request to get Apartment : {}", id);
        return apartmentRepository.findById(id).map(apartmentMapper::toDto);
    }

    /**
     * Delete the apartment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Apartment : {}", id);
        apartmentRepository.deleteById(id);
    }
}
