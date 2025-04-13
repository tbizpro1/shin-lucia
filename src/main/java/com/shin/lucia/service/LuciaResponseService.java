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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LuciaResponseService {

    private final LuciaResponseRepository repository;
    private final LuciaIdeaRepository ideaRepository;

    public LuciaResponseResponse create(LuciaResponseRequest request) {
        LuciaIdea idea = ideaRepository.findById(request.getIdeaId())
                .orElseThrow(() -> new EntityNotFoundException("Ideia não encontrada"));

        LuciaResponse response = LuciaResponseMapper.toEntity(request, idea);
        return LuciaResponseMapper.toResponse(repository.save(response));
    }

    public LuciaResponseResponse getById(Long id) {
        return repository.findById(id)
                .map(LuciaResponseMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Resposta não encontrada"));
    }

    public List<LuciaResponseResponse> getByIdeaId(Long ideaId) {
        LuciaIdea idea = ideaRepository.findById(ideaId)
                .orElseThrow(() -> new EntityNotFoundException("Ideia não encontrada"));

        return repository.findByIdea(idea).stream()
                .map(LuciaResponseMapper::toResponse)
                .toList();
    }

    public Map<Double, String> getSummariesByIdea(Long ideaId) {
        return repository.findAllByIdea_Id(ideaId).stream()
                .collect(Collectors.toMap(
                        LuciaResponse::getRelatedStep,
                        LuciaResponse::getContent
                ));
    }

    public Map<Double, String> getStepSummaries(Long ideaId) {
        return repository.findAllByIdea_Id(ideaId).stream()
                .collect(Collectors.toMap(
                        LuciaResponse::getRelatedStep,
                        LuciaResponse::getContent,
                        (existing, replacement) -> replacement
                ));
    }



}
