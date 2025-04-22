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
    public FileResponse uploadFile(FileRequest request, MultipartFile file, String username, Long userId) throws IOException {
        try {
            String stepFolder = "step-" + String.valueOf(request.getStep()).replace(".", "-");

            String fileUrl = s3StorageService.uploadLuciaFileStep(file, username, stepFolder);


            File fileEntity = File.builder()
                    .fileUrl(fileUrl)
                    .type(request.getType())
                    .author(request.getAuthor())
                    .step(request.getStep())
                    .name(file.getOriginalFilename())
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

    @Transactional
    public List<FileResponse> getByUserAndStep(Long userId, Double step) {
        return repository.findAllByUserIdAndStep(userId, step)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public FileResponse updateFileData(Long id, FileRequest request) {
        File file = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Arquivo não encontrado para ID: " + id));

        file.setAuthor(request.getAuthor());
        file.setStep(request.getStep());
        file.setType(request.getType());
        file.setName(request.getName());

        return mapper.toResponse(repository.save(file));
    }

    @Transactional
    public FileResponse updateFileOnlyFile(Long id, MultipartFile file, String username) throws IOException {
        File fileEntity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Arquivo não encontrado para ID: " + id));

        s3StorageService.deleteFile(fileEntity.getFileUrl());

        String stepFolder = "step-" + String.valueOf(fileEntity.getStep()).replace(".", "-");
        String fileUrl = s3StorageService.uploadLuciaFileStep(file, username, stepFolder);

        fileEntity.setFileUrl(fileUrl);
        fileEntity.setName(file.getOriginalFilename());

        return mapper.toResponse(repository.save(fileEntity));
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
