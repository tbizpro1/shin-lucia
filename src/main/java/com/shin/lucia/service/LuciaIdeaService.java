package com.shin.lucia.service;

import com.shin.lucia.dto.LuciaIdeaRequest;
import com.shin.lucia.dto.LuciaIdeaResponse;
import com.shin.lucia.entity.LuciaIdea;
import com.shin.lucia.mapper.LuciaIdeaMapper;
import com.shin.lucia.repository.LuciaIdeaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LuciaIdeaService {

    private final LuciaIdeaRepository repository;

    @Transactional
    public LuciaIdeaResponse create(LuciaIdeaRequest request) {

        if (request.getTitle() == null || request.getDescription() == null) {
            throw new IllegalArgumentException("Título e descrição são obrigatórios");
        }
        if (request.getStep() == null) {
            throw new IllegalArgumentException("Etapa é obrigatória");
        }
        if (request.getProblem() == null) {
            throw new IllegalArgumentException("Problema é obrigatório");
        }
        if (request.getSolution() == null) {
            throw new IllegalArgumentException("Solução é obrigatória");
        }
        if (request.getWhoIs() == null) {
            throw new IllegalArgumentException("Quem é obrigatório");
        }
        if (request.getCompanyId() == null) {
            throw new IllegalArgumentException("Company ID é obrigatório");
        }

        LuciaIdea idea = LuciaIdeaMapper.toEntity(request, request.getCompanyId());
        return LuciaIdeaMapper.toResponse(repository.save(idea));
    }


    @Transactional
    public List<LuciaIdeaResponse> getByCompanyId(Long companyId) {
        return repository.findByCompanyId(companyId).stream()
                .map(LuciaIdeaMapper::toResponse)
                .toList();
    }

    @Transactional
    public LuciaIdeaResponse getById(Long id) {
        return repository.findById(id)
                .map(LuciaIdeaMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Ideia não encontrada"));
    }

    @Transactional
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
    @Transactional
    public void delete(Long id) {
        LuciaIdea idea = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ideia não encontrada"));
        repository.delete(idea);
    }
}
