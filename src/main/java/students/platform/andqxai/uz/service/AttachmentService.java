package students.platform.andqxai.uz.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import students.platform.andqxai.uz.domain.Attachment;
import students.platform.andqxai.uz.repository.AttachmentRepository;
import students.platform.andqxai.uz.service.dto.AttachmentDTO;
import students.platform.andqxai.uz.service.mapper.AttachmentMapper;

/**
 * Service Implementation for managing {@link Attachment}.
 */
@Service
@Transactional
public class AttachmentService {

    private static final String uploadDirectory = "src/main/resources/attachments";

    private final Logger log = LoggerFactory.getLogger(AttachmentService.class);

    private final AttachmentRepository attachmentRepository;

    private final AttachmentMapper attachmentMapper;

    public AttachmentService(AttachmentRepository attachmentRepository, AttachmentMapper attachmentMapper) {
        this.attachmentRepository = attachmentRepository;
        this.attachmentMapper = attachmentMapper;
    }

    /**
     * Save a attachment.
     *
     * @param attachmentDTO the entity to save.
     * @return the persisted entity.
     */
    public AttachmentDTO save(AttachmentDTO attachmentDTO) {
        log.debug("Request to save Attachment : {}", attachmentDTO);
        Attachment attachment = attachmentMapper.toEntity(attachmentDTO);
        attachment = attachmentRepository.save(attachment);
        return attachmentMapper.toDto(attachment);
    }

    /**
     * Update a attachment.
     *
     * @param attachmentDTO the entity to save.
     * @return the persisted entity.
     */
    public AttachmentDTO update(AttachmentDTO attachmentDTO) {
        log.debug("Request to update Attachment : {}", attachmentDTO);
        Attachment attachment = attachmentMapper.toEntity(attachmentDTO);
        attachment = attachmentRepository.save(attachment);
        return attachmentMapper.toDto(attachment);
    }

    /**
     * Partially update a attachment.
     *
     * @param attachmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AttachmentDTO> partialUpdate(AttachmentDTO attachmentDTO) {
        log.debug("Request to partially update Attachment : {}", attachmentDTO);

        return attachmentRepository
            .findById(attachmentDTO.getId())
            .map(existingAttachment -> {
                attachmentMapper.partialUpdate(existingAttachment, attachmentDTO);

                return existingAttachment;
            })
            .map(attachmentRepository::save)
            .map(attachmentMapper::toDto);
    }

    /**
     * Get all the attachments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AttachmentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Attachments");
        return attachmentRepository.findAll(pageable).map(attachmentMapper::toDto);
    }

    /**
     * Get one attachment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AttachmentDTO> findOne(Long id) {
        log.debug("Request to get Attachment : {}", id);
        return attachmentRepository.findById(id).map(attachmentMapper::toDto);
    }

    /**
     * Delete the attachment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Attachment : {}", id);
        attachmentRepository.deleteById(id);
    }

    public List<String> upload(MultipartHttpServletRequest request) {
        List<String> uploadFilesName = new ArrayList<>();

        Iterator<String> fileNames = request.getFileNames();
        while (fileNames.hasNext()) {
            List<MultipartFile> files = request.getFiles(fileNames.next());
            int a;
            for (MultipartFile file : files) {
                String originalFilename = file.getOriginalFilename();

                Attachment attachment = new Attachment();

                attachment.setFileOriginalName(originalFilename);
                attachment.setAttachSize(file.getSize());
                attachment.setContentType(file.getContentType());

                String[] split = originalFilename.split("\\.");

                String name = UUID.randomUUID().toString() + "." + split[split.length - 1];

                attachment.setName(name);

                Path path = Paths.get(uploadDirectory + "/" + name);
                try {
                    Files.copy(file.getInputStream(), path);
                    Attachment save = attachmentRepository.save(attachment);
                    uploadFilesName.add(name);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return uploadFilesName;
    }

    public void downloadFile(String name, HttpServletResponse response) {
        Optional<Attachment> optionalAttachment = attachmentRepository.findAttachmentByName(name);
        if (optionalAttachment.isPresent()) {
            Attachment attachment = optionalAttachment.get();
            response.setHeader("Content-Disposition", "attachment; filename=" + attachment.getFileOriginalName());
            response.setContentType(attachment.getContentType());

            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(uploadDirectory + "/" + attachment.getName());
                FileCopyUtils.copy(fileInputStream, response.getOutputStream());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
