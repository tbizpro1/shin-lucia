package com.shin.lucia.controller;

import com.shin.lucia.client.UserClient;
import com.shin.lucia.dto.FileRequest;
import com.shin.lucia.dto.FileResponse;
import com.shin.lucia.security.JwtService;
import com.shin.lucia.service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lucia/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;
    private final JwtService jwtService;
    private final UserClient userClient;

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public FileResponse upload(@RequestBody FileRequest request, HttpServletRequest req) {
        String token = req.getHeader(HttpHeaders.AUTHORIZATION);
        String username = jwtService.extractUsername(token);
        Long userId = userClient.findIdByUsername(username);
        return fileService.upload(request, userId);
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
