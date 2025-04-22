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
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FileResponse uploadFile(
            @RequestPart("data") FileRequest request,
            @RequestPart("file") MultipartFile file,
            HttpServletRequest req
    ) throws IOException {
        String token = req.getHeader(HttpHeaders.AUTHORIZATION);
        String username = jwtService.extractUsername(token);
        Long userId = userClient.findIdByUsername(username);
        return fileService.uploadFile(request, file, username, userId);
    }

    @PutMapping("/{id}/data")
    @PreAuthorize("isAuthenticated()")
    public FileResponse updateFileData(
            @PathVariable Long id,
            @RequestBody FileRequest request
    ) {
        return fileService.updateFileData(id, request);
    }

    @PutMapping("/{id}/only-file")
    @PreAuthorize("isAuthenticated()")
    public FileResponse updateOnlyFile(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file,
            HttpServletRequest req
    ) throws IOException {
        String token = req.getHeader(HttpHeaders.AUTHORIZATION);
        String username = jwtService.extractUsername(token);
        return fileService.updateFileOnlyFile(id, file, username);
    }




    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public List<FileResponse> listAll(HttpServletRequest req) {
        String token = req.getHeader(HttpHeaders.AUTHORIZATION);
        String username = jwtService.extractUsername(token);
        Long userId = userClient.findIdByUsername(username);
        return fileService.getByUser(userId);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/step/{step}")
    public List<FileResponse> listByStep(@PathVariable Double step, HttpServletRequest req) {
        String token = req.getHeader(HttpHeaders.AUTHORIZATION);
        String username = jwtService.extractUsername(token);
        Long userId = userClient.findIdByUsername(username);
        return fileService.getByUserAndStep(userId, step);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        fileService.delete(id);
    }
}
