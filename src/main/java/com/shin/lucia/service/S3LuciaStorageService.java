package com.shin.lucia.service;

import com.shin.lucia.config.AwsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3LuciaStorageService {

    private final S3Client s3Client;
    private final AwsProperties awsProperties;
    private final String awsRegion;

    public String uploadLuciaFile(MultipartFile file, String username, String folder, String ideaTitle) throws IOException {
        String sanitizedTitle = sanitizeForS3Path(ideaTitle);
        String fileName = String.format("lucia/%s/%s/%s/%s", folder, username, sanitizedTitle, generateFileName(file.getOriginalFilename()));

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(awsProperties.getS3().getBucket())
                .key(fileName)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));

        return s3Client.utilities()
                .getUrl(b -> b
                        .bucket(awsProperties.getS3().getBucket())
                        .key(fileName))
                .toString();
    }


    public String uploadLuciaFileStep(MultipartFile file, String username, String stepFolder) throws IOException {
        String sanitizedStep = sanitizeForS3Path(stepFolder);
        String fileName = String.format("lucia/files/%s/%s/%s", username, sanitizedStep, generateFileName(file.getOriginalFilename()));

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(awsProperties.getS3().getBucket())
                .key(fileName)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));

        return s3Client.utilities()
                .getUrl(b -> b
                        .bucket(awsProperties.getS3().getBucket())
                        .key(fileName))
                .toString();
    }

    public String uploadLuciaGeneratedFile(byte[] content, String fileName, String username, String folder, String ideaTitle) {
        String sanitizedTitle = sanitizeForS3Path(ideaTitle);
        String key = String.format("lucia/%s/%s/%s/%s", folder, username, sanitizedTitle, fileName);

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(awsProperties.getS3().getBucket())
                .key(key)
                .contentType("text/plain")
                .build();

        s3Client.putObject(request, RequestBody.fromBytes(content));

        return s3Client.utilities()
                .getUrl(b -> b
                        .bucket(awsProperties.getS3().getBucket())
                        .key(key))
                .toString();
    }

    public String uploadLuciaResponseFile(byte[] content, String fileName, Long userId, Long ideaId) {
        String key = String.format("lucia/response/%s/%s/%s", userId, ideaId, fileName);

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(awsProperties.getS3().getBucket())
                .key(key)
                .contentType("text/plain")
                .build();

        s3Client.putObject(request, RequestBody.fromBytes(content));

        return s3Client.utilities()
                .getUrl(b -> b
                        .bucket(awsProperties.getS3().getBucket())
                        .key(key))
                .toString();
    }

    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank()) return;

        try {
            String key = new java.net.URI(fileUrl).getPath().substring(1);

            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(awsProperties.getS3().getBucket())
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteRequest);
            System.out.println("🗑️ Objeto deletado do S3 com key: " + key);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar arquivo do S3: " + fileUrl, e);
        }
    }


    private String generateFileName(String originalName) {
        return UUID.randomUUID() + "-" + originalName;
    }

    private String sanitizeForS3Path(String input) {
        return input.toLowerCase()
                .replaceAll("[^a-z0-9\\- ]", "")
                .replaceAll("[\\s]+", "-")
                .replaceAll("-{2,}", "-")
                .replaceAll("^-|-$", "");
    }

}
