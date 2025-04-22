package com.shin.lucia.service;

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

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class LuciaSummaryService {

    private final S3LuciaStorageService s3StorageService;
    private final JwtService jwtService;
    private final LuciaSummaryIdeasRepository repository;
    private final LuciaIdeaRepository ideaRepository;


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

            StringBuilder builder = new StringBuilder();
            steps.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> {
                        builder.append("## ").append(entry.getKey().toUpperCase()).append("\n");
                        builder.append(entry.getValue()).append("\n\n");
                    });

            byte[] contentBytes = builder.toString().getBytes(StandardCharsets.UTF_8);

            String fileName = "summary-" + UUID.randomUUID() + ".txt";

            LuciaSummaryIdeas summary = repository.findByIdea(idea)
                    .orElseGet(() -> LuciaSummaryIdeas.builder().idea(idea).build());

            s3StorageService.deleteFile(summary.getUrlFile());

            String fileUrl = s3StorageService.uploadLuciaGeneratedFile(contentBytes, fileName, username, "summaries", idea.getTitle());

            summary.setObjectName(fileName);
            summary.setUrlFile(fileUrl);

            return LuciaSummaryMapper.toResponse(repository.save(summary));
        } catch (Exception e) {
            log.error("Erro ao gerar/uploadar arquivo de resumo: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao gerar resumo");
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

            StringBuilder builder = new StringBuilder();
            steps.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> {
                        builder.append("## ").append(entry.getKey().toUpperCase()).append("\n");
                        builder.append(entry.getValue()).append("\n\n");
                    });

            byte[] contentBytes = builder.toString().getBytes(StandardCharsets.UTF_8);
            String fileName = "summary-" + UUID.randomUUID() + ".txt";

            String fileUrl = s3StorageService.uploadLuciaGeneratedFile(contentBytes, fileName, username, "summaries", idea.getTitle());

            LuciaSummaryIdeas summary = LuciaSummaryIdeas.builder()
                    .idea(idea)
                    .objectName(fileName)
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
