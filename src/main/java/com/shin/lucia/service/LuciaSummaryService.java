package com.shin.lucia.service;

import com.shin.lucia.dto.LuciaSummaryRequest;
import com.shin.lucia.dto.LuciaSummaryResponse;
import com.shin.lucia.entity.LuciaIdea;
import com.shin.lucia.entity.LuciaSummaryIdeas;
import com.shin.lucia.mapper.LuciaSummaryMapper;
import com.shin.lucia.repository.LuciaIdeaRepository;
import com.shin.lucia.repository.LuciaSummaryIdeasRepository;
import com.shin.lucia.security.JwtService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@Service
@RequiredArgsConstructor
public class LuciaSummaryService {

    private final S3LuciaStorageService s3StorageService;
    private final JwtService jwtService;
    private final LuciaSummaryIdeasRepository repository;
    private final LuciaIdeaRepository ideaRepository;

    @Transactional
    public LuciaSummaryResponse createOrUpdate(LuciaSummaryRequest request) {
        try {
            LuciaIdea idea = ideaRepository.findById(request.getIdeaId())
                    .orElseThrow(() -> new EntityNotFoundException("Ideia não encontrada"));

            LuciaSummaryIdeas summary = repository.findByIdea(idea)
                    .map(existing -> {
                        existing.setObjectName(request.getObjectName());
                        existing.setUrlFile(request.getUrlFile());
                        return existing;
                    }).orElseGet(() -> LuciaSummaryMapper.toEntity(request, idea));

            return LuciaSummaryMapper.toResponse(repository.save(summary));
        } catch (Exception e) {
            log.error("Erro ao criar ou atualizar sumário: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao criar ou atualizar sumário");
        }
    }

    @Transactional
    public LuciaSummaryResponse createOrUpdateWithFile(LuciaSummaryRequest request, MultipartFile file, String username) {
        try {
            LuciaIdea idea = ideaRepository.findById(request.getIdeaId())
                    .orElseThrow(() -> new EntityNotFoundException("Ideia não encontrada"));

            String ideaTitle = idea.getTitle();
            String fileUrl = s3StorageService.uploadLuciaFile(file, username, "summaries", ideaTitle);

            request.setObjectName(file.getOriginalFilename());
            request.setUrlFile(fileUrl);

            LuciaSummaryIdeas summary = repository.findByIdea(idea)
                    .map(existing -> {
                        existing.setObjectName(request.getObjectName());
                        existing.setUrlFile(request.getUrlFile());
                        return existing;
                    }).orElseGet(() -> LuciaSummaryMapper.toEntity(request, idea));

            return LuciaSummaryMapper.toResponse(repository.save(summary));
        } catch (Exception e) {
            log.error("Erro ao criar ou atualizar sumário com upload: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao criar ou atualizar sumário com upload");
        }
    }


    @Transactional
    public LuciaSummaryResponse update(Long id, LuciaSummaryRequest request) {
        try {
            LuciaSummaryIdeas summary = repository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Sumário não encontrado"));

            if (request.getUrlFile() != null) summary.setUrlFile(request.getUrlFile());
            if (request.getObjectName() != null) summary.setObjectName(request.getObjectName());

            return LuciaSummaryMapper.toResponse(repository.save(summary));
        } catch (Exception e) {
            log.error("Erro ao atualizar sumário: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao atualizar sumário");
        }
    }

    @Transactional
    public LuciaSummaryResponse updateWithFile(Long id, LuciaSummaryRequest request, MultipartFile file, String username) {
        try {
            LuciaSummaryIdeas summary = repository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Sumário não encontrado"));

            LuciaIdea idea = summary.getIdea();
            String ideaTitle = idea.getTitle();

            s3StorageService.deleteFile(summary.getUrlFile());
            String fileUrl = s3StorageService.uploadLuciaFile(file, username, "summaries", ideaTitle);

            summary.setObjectName(file.getOriginalFilename());
            summary.setUrlFile(fileUrl);

            if (request != null && request.getObjectName() != null) {
                summary.setObjectName(request.getObjectName());
            }

            return LuciaSummaryMapper.toResponse(repository.save(summary));
        } catch (Exception e) {
            log.error("Erro ao atualizar sumário com novo arquivo: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao atualizar sumário com novo arquivo");
        }
    }

    @Transactional
    public void delete(Long id) {
        try {
            LuciaSummaryIdeas summary = repository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Resumo não encontrado"));

            s3StorageService.deleteFile(summary.getUrlFile());
            repository.delete(summary);
        } catch (Exception e) {
            log.error("Erro ao deletar sumário: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao deletar sumário");
        }
    }

    @Transactional
    public LuciaSummaryResponse getByIdeaId(Long ideaId) {
        try {
            LuciaIdea idea = ideaRepository.findById(ideaId)
                    .orElseThrow(() -> new EntityNotFoundException("Ideia não encontrada"));

            return repository.findByIdea(idea)
                    .map(LuciaSummaryMapper::toResponse)
                    .orElseThrow(() -> new EntityNotFoundException("Sumário não encontrado"));
        } catch (Exception e) {
            log.error("Erro ao buscar sumário da ideia: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao buscar sumário da ideia");
        }
    }
}
