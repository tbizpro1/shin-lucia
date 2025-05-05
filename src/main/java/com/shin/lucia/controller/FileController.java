package com.shin.lucia.controller;

import com.shin.lucia.dto.FileRequest;
import com.shin.lucia.dto.FileResponse;
import com.shin.lucia.service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/lucia/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/idea/{ideaId}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FileResponse uploadFile(
            @PathVariable Long ideaId,
            @RequestPart("data") FileRequest request,
            @RequestPart("file") MultipartFile file
    ) throws IOException {
        request.setIdeaId(ideaId);
        return fileService.uploadFile(request, file);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}/data")
    public FileResponse updateFileData(
            @PathVariable Long id,
            @RequestBody FileRequest request
    ) {
        return fileService.updateFileData(id, request);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}/idea/{ideaId}/only-file")
    public FileResponse updateOnlyFile(
            @PathVariable Long id,
            @PathVariable Long ideaId,
            @RequestPart("file") MultipartFile file
    ) throws IOException {
        return fileService.updateFileOnlyFile(id, file);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public FileResponse getById(@PathVariable Long id) {
        return fileService.getById(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/idea/{ideaId}")
    public List<FileResponse> getByIdeaId(@PathVariable Long ideaId) {
        return fileService.getByIdeaId(ideaId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<FileResponse> getAll() {
        return fileService.getAllFiles();
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        fileService.delete(id);
    }
}
