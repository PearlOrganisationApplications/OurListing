package com.pearl.propertiesApp.Controllers;

import com.pearl.propertiesApp.DTOs.RequestDTO;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/file")
public class FileController {

    private static final String FILE_UPLOAD_PATH = "upload";

    @GetMapping("/files")
    public ResponseEntity<?> getFiles() {
        File folder = new File(FILE_UPLOAD_PATH);
        File[] files = folder.listFiles((dir, name) -> new File(dir, name).isFile());
        if (files == null || files.length == 0) {
            return new ResponseEntity<>("No Files Found", HttpStatus.NOT_FOUND);
        }
        List<String> fileNames = Arrays.stream(files).map(File::getName).toList();
        return ResponseEntity.ok(fileNames);
    }

    @GetMapping("/download")
    public ResponseEntity<?> downloadFile(@RequestParam String fileName) throws IOException {
        File folder = new File(FILE_UPLOAD_PATH);
        File[] files = folder.listFiles((dir, name) -> new File(dir, name).isFile());
        if (files == null || files.length == 0) {
            return new ResponseEntity<>("File Not Found", HttpStatus.NOT_FOUND);
        }

        File file = Arrays.stream(files).filter(f -> f.getName().equals(fileName)).findFirst().orElse(null);

        assert file != null;
        if (!file.exists() || file.isDirectory()) {
            return new ResponseEntity<>("File Not Found", HttpStatus.NOT_FOUND);
        }

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        String contentType = Files.probeContentType(file.toPath());
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        String headerValue = "attachment; filename=\"" + fileName + "\"";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(resource);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@ModelAttribute RequestDTO.registerRequestDTO requestDTO) {
        try {
            String filePath = UploadUtil.saveFile(FILE_UPLOAD_PATH, requestDTO.getFile());
            return ResponseEntity.ok("File uploaded successfully: " + filePath);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File upload failed: " + Arrays.toString(e.getStackTrace()));
        }
    }
}

@Component
class UploadUtil {

    public static String saveFile(String directory, MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename(); // Get actual file name
        if (fileName == null || fileName.isEmpty()) {
            throw new IOException("Invalid file name");
        }

        File dir = new File(directory);
        if (!dir.exists()) {
            if (!dir.mkdirs())
                return null;
        }

        String filePath = directory + File.separator + fileName;// Use actual filename
        Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
        return filePath;
    }
}

