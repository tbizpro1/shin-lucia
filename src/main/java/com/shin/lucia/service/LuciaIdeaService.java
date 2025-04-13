package com.shin.lucia.service;

import com.shin.lucia.dto.LuciaIdeaRequest;
import com.shin.lucia.dto.LuciaIdeaResponse;
import com.shin.lucia.entity.LuciaIdea;
import com.shin.lucia.mapper.LuciaIdeaMapper;
import com.shin.lucia.repository.LuciaIdeaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LuciaIdeaService {

    private final LuciaIdeaRepository repository;

    public LuciaIdeaResponse create(LuciaIdeaRequest request, Long userId) {
        LuciaIdea idea = LuciaIdeaMapper.toEntity(request, userId);
        return LuciaIdeaMapper.toResponse(repository.save(idea));
    }

    public LuciaIdeaResponse getById(Long id) {
        return repository.findById(id)
                .map(LuciaIdeaMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Ideia não encontrada"));
    }

    public List<LuciaIdeaResponse> getByUserId(Long userId) {
        return repository.findByUserId(userId).stream()
                .map(LuciaIdeaMapper::toResponse)
                .toList();
    }

    public LuciaIdeaResponse update(Long id, LuciaIdeaRequest request) {
        LuciaIdea idea = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ideia não encontrada"));

        if (request.getTitle() != null) idea.setTitle(request.getTitle());
        if (request.getDescription() != null) idea.setDescription(request.getDescription());
        if (request.getStep() != null) idea.setStep(request.getStep());
        if (request.getProblem() != null) idea.setProblem(request.getProblem());
        if (request.getSolution() != null) idea.setSolution(request.getSolution());
        if (request.getWhoIs() != null) idea.setWhoIs(request.getWhoIs());

        return LuciaIdeaMapper.toResponse(repository.save(idea));
    }

    public void delete(Long id) {
        LuciaIdea idea = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ideia não encontrada"));
        repository.delete(idea);
    }
}
