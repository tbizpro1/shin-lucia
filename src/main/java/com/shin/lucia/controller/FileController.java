package com.shin.lucia.controller;

import com.shin.lucia.client.UserClient;
import com.shin.lucia.dto.FileRequest;
import com.shin.lucia.dto.FileResponse;
import com.shin.lucia.security.JwtService;
import com.shin.lucia.service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
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
    private final JwtService jwtService;
    private final UserClient userClient;

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/idea/{ideaId}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FileResponse uploadFile(
            @PathVariable Long ideaId,
            @RequestPart("data") FileRequest request,
            @RequestPart("file") MultipartFile file,
            HttpServletRequest req
    ) throws IOException {
        String token = req.getHeader(HttpHeaders.AUTHORIZATION);
        String username = jwtService.extractUsername(token);
        Long userId = userClient.findIdByUsername(username);
        request.setIdeaId(ideaId);
        return fileService.uploadFile(request, file, userId);
    }

    @PutMapping("/{id}/data")
    @PreAuthorize("isAuthenticated()")
    public FileResponse updateFileData(
            @PathVariable Long id,
            @RequestBody FileRequest request
    ) {
        return fileService.updateFileData(id, request);
    }

    @PutMapping("/{id}/idea/{ideaId}/only-file")
    @PreAuthorize("isAuthenticated()")
    public FileResponse updateOnlyFile(
            @PathVariable Long id,
            @PathVariable Long ideaId,
            @RequestPart("file") MultipartFile file,
            HttpServletRequest req
    ) throws IOException {
        String token = req.getHeader(HttpHeaders.AUTHORIZATION);
        String username = jwtService.extractUsername(token);
        Long userId = userClient.findIdByUsername(username);
        return fileService.updateFileOnlyFile(id, file, userId, ideaId);
    }

    @GetMapping("/my-documents")
    @PreAuthorize("isAuthenticated()")
    public List<FileResponse> listMyDocuments(HttpServletRequest req) {
        String token = req.getHeader(HttpHeaders.AUTHORIZATION);
        String username = jwtService.extractUsername(token);
        Long userId = userClient.findIdByUsername(username);
        return fileService.getByUser(userId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public FileResponse getById(@PathVariable Long id) {
        return fileService.getById(id);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("isAuthenticated()")
    public List<FileResponse> getByUserId(@PathVariable Long userId) {
        return fileService.getByUser(userId);
    }

    @GetMapping("/idea/{ideaId}")
    @PreAuthorize("isAuthenticated()")
    public List<FileResponse> getByIdeaId(@PathVariable Long ideaId) {
        return fileService.getByIdeaId(ideaId);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<FileResponse> getAll() {
        return fileService.getAllFiles();
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public void delete(@PathVariable Long id) {
        fileService.delete(id);
    }
}
