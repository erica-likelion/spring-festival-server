package likelion.festival.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageService {
    private final String uploadDir = System.getProperty("user.dir") + "/uploads/";
    private final String baseUrl = "/images/";

    public String saveImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어있습니다.");
        }

        try {
            String originalFilename = file.getOriginalFilename();
            String filename = UUID.randomUUID() + "_" + originalFilename;
            Path savePath = Paths.get(uploadDir, filename);

            Files.createDirectories(savePath.getParent());
            file.transferTo(savePath);

            return baseUrl + filename;

        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 실패");
        }
    }

    private String getExtension(String originalFilename) {
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new IllegalArgumentException("원본 파일 이름이 null입니다.");
        }
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex == -1) {
            return ""; // 확장자가 없는 경우
        }
        return originalFilename.substring(dotIndex); // 예: ".jpg"
    }
}
