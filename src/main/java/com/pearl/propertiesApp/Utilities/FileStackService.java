package com.pearl.propertiesApp.Utilities;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStackService {
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
        return "https://propertyapp.ddns.net/file/download?fileName=" + fileName;
    }

}
