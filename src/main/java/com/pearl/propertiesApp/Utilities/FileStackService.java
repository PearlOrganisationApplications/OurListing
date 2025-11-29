package com.pearl.propertiesApp.Utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStackService {


    @Value("${aws.bucketName}")
    private String bucketName;

    @Autowired
    private S3Client s3Client;

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename(); // Get actual file name
        if (fileName == null || fileName.isEmpty()) {
            throw new IOException("Invalid file name");
        }

        File dir = new File("upload");
        if (!dir.exists()) {
            if (!dir.mkdirs())
                return null;
        }

        String filePath = "upload" + File.separator + fileName;// Use actual filename
        Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
        return "https://propertyapp.ddns.net/download?fileName=" + fileName;
    }

}
