package com.shin.lucia.service;

import com.shin.lucia.dto.LuciaSummaryRequest;
import com.shin.lucia.dto.LuciaSummaryResponse;
import com.shin.lucia.entity.LuciaIdea;
import com.shin.lucia.entity.LuciaSummaryIdeas;
import com.shin.lucia.mapper.LuciaSummaryMapper;
import com.shin.lucia.repository.LuciaIdeaRepository;
import com.shin.lucia.repository.LuciaSummaryIdeasRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LuciaSummaryService {

    private final LuciaSummaryIdeasRepository repository;
    private final LuciaIdeaRepository ideaRepository;

    public LuciaSummaryResponse createOrUpdate(LuciaSummaryRequest request) {
        LuciaIdea idea = ideaRepository.findById(request.getIdeaId())
                .orElseThrow(() -> new EntityNotFoundException("Ideia não encontrada"));

        LuciaSummaryIdeas summary = repository.findByIdea(idea)
                .map(existing -> {
                    existing.setObjectName(request.getObjectName());
                    existing.setUrlFile(request.getUrlFile());
                    return existing;
                }).orElseGet(() -> LuciaSummaryMapper.toEntity(request, idea));

        return LuciaSummaryMapper.toResponse(repository.save(summary));
    }

    public LuciaSummaryResponse getByIdeaId(Long ideaId) {
        LuciaIdea idea = ideaRepository.findById(ideaId)
                .orElseThrow(() -> new EntityNotFoundException("Ideia não encontrada"));

        return repository.findByIdea(idea)
                .map(LuciaSummaryMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Sumário não encontrado"));
    }
}
