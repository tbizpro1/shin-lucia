package com.shin.lucia.service;

import com.shin.lucia.dto.LuciaResponseRequest;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @Transactional
    public LuciaResponseResponse create(LuciaResponseRequest request) {
        try {
            LuciaIdea idea = ideaRepository.findById(request.getIdeaId())
                    .orElseThrow(() -> new EntityNotFoundException("Ideia não encontrada"));

            LuciaResponse response = LuciaResponseMapper.toEntity(request, idea);
            return LuciaResponseMapper.toResponse(repository.save(response));
        } catch (Exception e) {
            log.error("Erro ao criar resposta: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao criar resposta");
        }
    }

    @Transactional
    public LuciaResponseResponse createWithFile(LuciaResponseRequest request, MultipartFile file, String username) {
        try {
            LuciaIdea idea = ideaRepository.findById(request.getIdeaId())
                    .orElseThrow(() -> new EntityNotFoundException("Ideia não encontrada"));

            String ideaTitle = idea.getTitle();
            String fileUrl = s3StorageService.uploadLuciaFile(file, username, "responses", ideaTitle);

            request.setObjectName(file.getOriginalFilename());
            request.setUrlHistory(fileUrl);

            LuciaResponse response = LuciaResponseMapper.toEntity(request, idea);
            return LuciaResponseMapper.toResponse(repository.save(response));
        } catch (Exception e) {
            log.error("Erro ao criar resposta com upload: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao criar resposta com upload");
        }
    }


    @Transactional
    public LuciaResponseResponse update(Long id, LuciaResponseRequest request) {
        try {
            LuciaResponse response = repository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Resposta não encontrada"));

            if (request.getContent() != null) response.setContent(request.getContent());
            if (request.getRelatedStep() != null) response.setRelatedStep(request.getRelatedStep());
            if (request.getAuthor() != null) response.setAuthor(request.getAuthor());
            if (request.getObjectName() != null) response.setObjectName(request.getObjectName());
            if (request.getUrlHistory() != null) response.setUrlHistory(request.getUrlHistory());

            return LuciaResponseMapper.toResponse(repository.save(response));
        } catch (Exception e) {
            log.error("Erro ao atualizar resposta: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao atualizar resposta");
        }
    }

    @Transactional
    public LuciaResponseResponse updateWithFile(Long id, LuciaResponseRequest request, MultipartFile file, String username) {
        try {
            LuciaResponse response = repository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Resposta não encontrada"));

            LuciaIdea idea = response.getIdea();
            String ideaTitle = idea.getTitle();

            s3StorageService.deleteFile(response.getUrlHistory());
            String fileUrl = s3StorageService.uploadLuciaFile(file, username, "responses", ideaTitle);

            response.setObjectName(file.getOriginalFilename());
            response.setUrlHistory(fileUrl);

            if (request != null) {
                if (request.getContent() != null) response.setContent(request.getContent());
                if (request.getRelatedStep() != null) response.setRelatedStep(request.getRelatedStep());
                if (request.getAuthor() != null) response.setAuthor(request.getAuthor());
            }

            return LuciaResponseMapper.toResponse(repository.save(response));
        } catch (Exception e) {
            log.error("Erro ao atualizar resposta com novo arquivo: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao atualizar resposta com novo arquivo");
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
