package com.shin.lucia.service;

import com.fasterxml.jackson.core.type.TypeReference;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shin.lucia.client.UserClient;


import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class LuciaSummaryService {

    private final S3LuciaStorageService s3StorageService;
    private final LuciaSummaryIdeasRepository repository;
    private final LuciaIdeaRepository ideaRepository;
    private final ObjectMapper objectMapper;
    private final UserClient userClient;



    @Transactional
    public LuciaSummaryResponse updateWithFile(Long ideaId, MultipartFile file, String username) {
        try {
            LuciaIdea idea = ideaRepository.findById(ideaId)
                    .orElseThrow(() -> new EntityNotFoundException("Ideia não encontrada"));

            LuciaSummaryIdeas summary = repository.findByIdea(idea)
                    .orElseGet(() -> LuciaSummaryIdeas.builder().idea(idea).build());

            s3StorageService.deleteFile(summary.getUrlFile());

            String fileUrl = s3StorageService.uploadLuciaFile(file, username, "summaries", idea.getTitle());

            summary.setObjectName(file.getOriginalFilename());
            summary.setUrlFile(fileUrl);

            return LuciaSummaryMapper.toResponse(repository.save(summary));
        } catch (Exception e) {
            log.error("Erro ao atualizar sumário acumulado: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao atualizar sumário acumulado");
        }
    }

    @Transactional
    public LuciaSummaryResponse generateAndUploadSummaryFile(Long ideaId, Map<String, String> steps, String username) {
        try {
            LuciaIdea idea = ideaRepository.findById(ideaId)
                    .orElseThrow(() -> new EntityNotFoundException("Ideia não encontrada"));

            Long userId = userClient.findIdByUsername(username);
            byte[] contentBytes = objectMapper.writeValueAsBytes(steps);

            LuciaSummaryIdeas summary = repository.findByIdea(idea)
                    .orElseGet(() -> LuciaSummaryIdeas.builder().idea(idea).build());

            s3StorageService.deleteFile(summary.getUrlFile()); // só se existir

            String fileUrl = s3StorageService.uploadLuciaJsonSummary(contentBytes, userId, ideaId);

            summary.setObjectName("summary.json");
            summary.setUrlFile(fileUrl);

            return LuciaSummaryMapper.toResponse(repository.save(summary));
        } catch (Exception e) {
            log.error("Erro ao atualizar resumo com JSON: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao atualizar resumo com JSON");
        }
    }



    @Transactional
    public LuciaSummaryResponse createSummaryFromJson(Long ideaId, Map<String, String> steps, String username) {
        try {
            LuciaIdea idea = ideaRepository.findById(ideaId)
                    .orElseThrow(() -> new EntityNotFoundException("Ideia não encontrada"));

            if (repository.findByIdea(idea).isPresent()) {
                throw new IllegalStateException("Resumo já existe para esta ideia.");
            }

            Long userId = userClient.findIdByUsername(username);
            byte[] contentBytes = objectMapper.writeValueAsBytes(steps);

            String fileUrl = s3StorageService.uploadLuciaJsonSummary(contentBytes, userId, ideaId);

            LuciaSummaryIdeas summary = LuciaSummaryIdeas.builder()
                    .idea(idea)
                    .objectName("summary.json")
                    .urlFile(fileUrl)
                    .build();

            return LuciaSummaryMapper.toResponse(repository.save(summary));
        } catch (Exception e) {
            log.error("Erro ao criar resumo com JSON: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao criar resumo com JSON");
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

    @Transactional(readOnly = true)
    public LuciaSummaryResponse getByIdeaId(Long ideaId) {
        try {
            LuciaIdea idea = ideaRepository.findById(ideaId)
                    .orElseThrow(() -> new EntityNotFoundException("Ideia não encontrada"));

            LuciaSummaryIdeas summary = repository.findByIdea(idea)
                    .orElseThrow(() -> new EntityNotFoundException("Sumário não encontrado"));

            Long userId = idea.getUserId();

            byte[] jsonBytes = s3StorageService.readSummaryJson(userId, ideaId);
            Map<String, String> contentMap = objectMapper.readValue(jsonBytes, new TypeReference<>() {});

            LuciaSummaryResponse response = LuciaSummaryMapper.toResponse(summary);
            response.setContent(contentMap);

            return response;

        } catch (Exception e) {
            log.error("Erro ao buscar conteúdo do sumário da ideia: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao buscar conteúdo do sumário");
        }
    }



}
