package com.shin.lucia.service;

import com.shin.lucia.dto.FileRequest;
import com.shin.lucia.dto.FileResponse;
import com.shin.lucia.entity.File;
import com.shin.lucia.exception.ResourceNotFoundException;
import com.shin.lucia.mapper.FileMapper;
import com.shin.lucia.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final S3LuciaStorageService s3StorageService;
    private final FileRepository repository;
    private final FileMapper mapper;

    @Transactional
    public FileResponse uploadFile(FileRequest request, MultipartFile file, Long userId) throws IOException {
        try {
            String fileUrl = s3StorageService.uploadLuciaFileByIdea(file, request.getIdeaId());

            File fileEntity = File.builder()
                    .fileUrl(fileUrl)
                    .type(request.getType())
                    .author(request.getAuthor())
                    .name(file.getOriginalFilename())
                    .ideaId(request.getIdeaId())
                    .userId(userId)
                    .build();

            return mapper.toResponse(repository.save(fileEntity));
        } catch (Exception e) {
            log.error("Erro ao fazer upload de arquivo: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao enviar arquivo");
        }
    }



    @Transactional
    public List<FileResponse> getByUser(Long userId) {
        return repository.findAllByUserId(userId)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FileResponse getById(Long id) {
        File file = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Arquivo não encontrado para ID: " + id));
        return mapper.toResponse(file);
    }


    @Transactional
    public FileResponse updateFileData(Long id, FileRequest request) {
        File file = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Arquivo não encontrado para ID: " + id));

        file.setAuthor(request.getAuthor());
        file.setType(request.getType());
        file.setName(request.getName());

        return mapper.toResponse(repository.save(file));
    }

    @Transactional
    public FileResponse updateFileOnlyFile(Long id, MultipartFile file, Long userId, Long ideaId) throws IOException {
        File fileEntity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Arquivo não encontrado para ID: " + id));

        s3StorageService.deleteFile(fileEntity.getFileUrl());

        String fileUrl = s3StorageService.uploadLuciaFileByIdea(file, ideaId);

        fileEntity.setFileUrl(fileUrl);
        fileEntity.setName(file.getOriginalFilename());
        fileEntity.setIdeaId(ideaId);

        return mapper.toResponse(repository.save(fileEntity));
    }


    @Transactional(readOnly = true)
    public List<FileResponse> getByIdeaId(Long ideaId) {
        return repository.findAllByIdeaId(ideaId)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FileResponse> getAllFiles() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        File file = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Arquivo não encontrado para ID: " + id));

        try {
            s3StorageService.deleteFile(file.getFileUrl());
        } catch (Exception e) {
            log.warn("Não foi possível deletar arquivo do S3: {}", e.getMessage());
        }

        repository.delete(file);
    }

}
