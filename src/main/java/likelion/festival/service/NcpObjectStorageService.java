package likelion.festival.service;

import likelion.festival.config.NcpStorageProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NcpObjectStorageService {

    private final S3Client s3Client;
    private final NcpStorageProperties properties;

    public String uploadImage(MultipartFile file) throws IOException {
        String datePath = LocalDate.now().toString();
        String key = "lost-items/" + datePath + "/" + UUID.randomUUID();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(properties.getBucket())
                .key(key)
                .contentType(file.getContentType())
                .acl("public-read")
                .build();

        s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        return getObjectUrl(key);
    }

    public String getObjectUrl(String objectKey) {
        return properties.getEndpoint() + "/" + properties.getBucket() + "/" + objectKey;
    }
}
