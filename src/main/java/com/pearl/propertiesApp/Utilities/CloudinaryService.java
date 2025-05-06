package com.pearl.propertiesApp.Utilities;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    public CloudinaryService(@Value("${cloudinary.cloud-name}") String cloudName,
                             @Value("${cloudinary.api-key}") String apiKey,
                             @Value("${cloudinary.api-secret}") String apiSecret) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }

    @Async
    public void uploadVideo(MultipartFile file, String ID) throws IOException {
        Map<?, ?> uploadResult = cloudinary.uploader().uploadLarge(file.getBytes(),
                ObjectUtils.asMap(
                        "public_id", ID,
                        "resource_type", "video",
                        "folder", "PropertyVideos"));
        log.info((String) uploadResult.get("public_id"));


    }

    @Async
    public void uploadFile(MultipartFile file, String ID) throws IOException {
        Map<?, ?> uploadResult = cloudinary.uploader().uploadLarge(file.getBytes(),
                ObjectUtils.asMap("public_id", ID,
                        "resource_type", "image",
                        "folder", "PropertyPhotos"));
//        String publicId = (String) uploadResult.get("public_id");
        log.info((String) uploadResult.get("public_id"));
        //       return cloudinary.url().transformation(new Transformation<>()
        //                       .fetchFormat("auto")
        //                       .quality("auto")
        //                       .crop("scale").width(500))
        //               .generate());
    }

    public String getPhotoUrl(String id) {
        return String.format
                ("http://res.cloudinary.com/%s/image/upload/c_scale,f_auto,q_auto,w_500/v1/PropertyPhotos/%s",
                        cloudName, id);
    }

    public String getVideoUrl(String id) {
        return String.format("https://res.cloudinary.com/%s/video/upload/v1/PropertyPhotos/%s",
                cloudName, id);
    }
}
