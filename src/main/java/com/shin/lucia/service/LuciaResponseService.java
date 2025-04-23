package com.shin.lucia.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shin.lucia.client.UserClient;
import com.shin.lucia.dto.LuciaResponseResponse;
import com.shin.lucia.entity.LuciaIdea;
import com.shin.lucia.entity.LuciaResponse;
import com.shin.lucia.mapper.LuciaResponseMapper;
import com.shin.lucia.repository.LuciaIdeaRepository;
import com.shin.lucia.repository.LuciaResponseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LuciaResponseService {

    private final LuciaResponseRepository repository;
    private final LuciaIdeaRepository ideaRepository;
    private final S3LuciaStorageService s3StorageService;
    private final UserClient userClient;


    @Transactional
    public LuciaResponseResponse uploadResponseAsTxt(Long ideaId, Double step, List<Map<String, Object>> data, String username) {
        try {
            LuciaIdea idea = ideaRepository.findById(ideaId)
                    .orElseThrow(() -> new EntityNotFoundException("Ideia não encontrada"));

            Long userId = userClient.findIdByUsername(username);

            ObjectMapper mapper = new ObjectMapper();
            String content = mapper.writeValueAsString(data);

            String fileName = "step-" + step + ".txt";
            byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);

            repository.findByIdeaAndRelatedStep(idea, step).ifPresent(existing -> {
                if (existing.getUrlHistory() != null) {
                    s3StorageService.deleteFile(existing.getUrlHistory());
                }
            });

            String fileUrl = s3StorageService.uploadLuciaResponseFile(contentBytes, fileName, userId, ideaId);

            LuciaResponse response = repository.findByIdeaAndRelatedStep(idea, step)
                    .orElseGet(() -> LuciaResponse.builder()
                            .idea(idea)
                            .relatedStep(step)
                            .build());

            response.setUrlHistory(fileUrl);
            response.setObjectName(fileName);
            response.setContent(content); // JSON como String
            response.setAuthor(username);

            return LuciaResponseMapper.toResponse(repository.save(response));

        } catch (Exception e) {
            log.error("Erro ao salvar resposta como txt: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao salvar resposta");
        }
    }


    @Transactional
    public void delete(Long id) {
        try {
            LuciaResponse response = repository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Resposta não encontrada"));

            s3StorageService.deleteFile(response.getUrlHistory());
            repository.delete(response);
        } catch (Exception e) {
            log.error("Erro ao deletar resposta: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao deletar resposta");
        }
    }

    public LuciaResponseResponse getById(Long id) {
        try {
            return repository.findById(id)
                    .map(LuciaResponseMapper::toResponse)
                    .orElseThrow(() -> new EntityNotFoundException("Resposta não encontrada"));
        } catch (Exception e) {
            log.error("Erro ao buscar resposta: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao buscar resposta");
        }
    }

    public List<LuciaResponseResponse> getByIdeaId(Long ideaId) {
        try {
            LuciaIdea idea = ideaRepository.findById(ideaId)
                    .orElseThrow(() -> new EntityNotFoundException("Ideia não encontrada"));

            return repository.findByIdea(idea).stream()
                    .map(LuciaResponseMapper::toResponse)
                    .toList();
        } catch (Exception e) {
            log.error("Erro ao buscar respostas da ideia: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao buscar respostas da ideia");
        }
    }

    public Map<Double, String> getStepSummaries(Long ideaId) {
        try {
            return repository.findAllByIdea_Id(ideaId).stream()
                    .collect(Collectors.toMap(
                            LuciaResponse::getRelatedStep,
                            LuciaResponse::getContent,
                            (existing, replacement) -> replacement
                    ));
        } catch (Exception e) {
            log.error("Erro ao buscar step summaries: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao buscar step summaries");
        }
    }
}
