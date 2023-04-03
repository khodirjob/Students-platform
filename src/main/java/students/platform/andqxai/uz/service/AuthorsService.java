package students.platform.andqxai.uz.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import students.platform.andqxai.uz.domain.Authors;
import students.platform.andqxai.uz.repository.AuthorsRepository;
import students.platform.andqxai.uz.service.dto.AuthorsDTO;
import students.platform.andqxai.uz.service.mapper.AuthorsMapper;

/**
 * Service Implementation for managing {@link Authors}.
 */
@Service
@Transactional
public class AuthorsService {

    private final Logger log = LoggerFactory.getLogger(AuthorsService.class);

    private final AuthorsRepository authorsRepository;

    private final AuthorsMapper authorsMapper;

    public AuthorsService(AuthorsRepository authorsRepository, AuthorsMapper authorsMapper) {
        this.authorsRepository = authorsRepository;
        this.authorsMapper = authorsMapper;
    }

    /**
     * Save a authors.
     *
     * @param authorsDTO the entity to save.
     * @return the persisted entity.
     */
    public AuthorsDTO save(AuthorsDTO authorsDTO) {
        log.debug("Request to save Authors : {}", authorsDTO);
        Authors authors = authorsMapper.toEntity(authorsDTO);
        authors = authorsRepository.save(authors);
        return authorsMapper.toDto(authors);
    }

    /**
     * Update a authors.
     *
     * @param authorsDTO the entity to save.
     * @return the persisted entity.
     */
    public AuthorsDTO update(AuthorsDTO authorsDTO) {
        log.debug("Request to update Authors : {}", authorsDTO);
        Authors authors = authorsMapper.toEntity(authorsDTO);
        authors = authorsRepository.save(authors);
        return authorsMapper.toDto(authors);
    }

    /**
     * Partially update a authors.
     *
     * @param authorsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AuthorsDTO> partialUpdate(AuthorsDTO authorsDTO) {
        log.debug("Request to partially update Authors : {}", authorsDTO);

        return authorsRepository
            .findById(authorsDTO.getId())
            .map(existingAuthors -> {
                authorsMapper.partialUpdate(existingAuthors, authorsDTO);

                return existingAuthors;
            })
            .map(authorsRepository::save)
            .map(authorsMapper::toDto);
    }

    /**
     * Get all the authors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AuthorsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Authors");
        return authorsRepository.findAll(pageable).map(authorsMapper::toDto);
    }

    /**
     * Get one authors by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AuthorsDTO> findOne(Long id) {
        log.debug("Request to get Authors : {}", id);
        return authorsRepository.findById(id).map(authorsMapper::toDto);
    }

    /**
     * Delete the authors by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Authors : {}", id);
        authorsRepository.deleteById(id);
    }
}
