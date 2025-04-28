package com.pearl.propertiesApp.Utilities;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.Scanner;

@Service
public class FileStackService {

    private static final String FILESTACK_API_URL = "https://www.filestackapi.com/api/store/S3";
    @Value("${filestack.key}")
    private String filestackKey;

    public String uploadFile(Object File) throws IOException {
        if (File == null) return "";
        // Create a temporary file for the upload
        if (File instanceof MultipartFile) {
            MultipartFile file = (MultipartFile) File;
            java.io.File tempFile = java.io.File.createTempFile("upload-", file.getOriginalFilename());
            file.transferTo(tempFile);

            // Construct API URL with the API key
            String apiUrl = FILESTACK_API_URL + "?key=" + filestackKey;

            // Set up the HTTP connection
            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", Files.probeContentType(tempFile.toPath()));

            // Send the file as a binary stream
            Files.copy(tempFile.toPath(), connection.getOutputStream());
            connection.getOutputStream().flush();
            connection.getOutputStream().close();

            // Read the API response
            String jsonResponse;
            try (Scanner scanner = new Scanner(connection.getInputStream())) {
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
            }

            // Clean up the temporary file
            tempFile.delete();

            // Parse the JSON response to extract the URL
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonResponse);
            return jsonNode.get("url").asText(); // Extract and return the URL
        }
        return "";
    }

}
