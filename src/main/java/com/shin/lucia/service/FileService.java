package com.shin.lucia.service;

import com.shin.lucia.dto.FileRequest;
import com.shin.lucia.dto.FileResponse;
import com.shin.lucia.entity.File;
import com.shin.lucia.exception.ResourceNotFoundException;
import com.shin.lucia.mapper.FileMapper;
import com.shin.lucia.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository repository;
    private final FileMapper mapper;

    public FileResponse upload(FileRequest request, Long userId) {
        File file = mapper.toEntity(request, userId);
        return mapper.toResponse(repository.save(file));
    }

    public List<FileResponse> getByUser(Long userId) {
        return repository.findAllByUserId(userId)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<FileResponse> getByUserAndStep(Long userId, Double step) {
        return repository.findAllByUserIdAndStep(userId, step)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Arquivo não encontrado para ID: " + id);
        }
        repository.deleteById(id);
    }
}
