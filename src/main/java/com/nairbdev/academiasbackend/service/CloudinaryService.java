package com.nairbdev.academiasbackend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.Set;

@Service
public class CloudinaryService {

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg", "image/png", "image/webp"
    );

    private final Cloudinary cloudinary;
    private final String folder;

    public CloudinaryService(Cloudinary cloudinary,
                             @Value("${cloudinary.folder}") String folder) {
        this.cloudinary = cloudinary;
        this.folder = folder;
    }

    public String uploadAlumnoPhoto(byte[] fileBytes, String cedula) {
        if (fileBytes == null || fileBytes.length == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La imagen es obligatoria.");
        }

        // public_id: alumnos/<cedula>/<timestamp>
        String publicId = "alumno_" + sanitize(cedula) + "_" + Instant.now().toEpochMilli();

        try {
            Map<?, ?> result = cloudinary.uploader().upload(
                    fileBytes,
                    ObjectUtils.asMap(
                            "folder", folder,
                            "public_id", publicId,
                            "resource_type", "image",
                            "overwrite", true
                    )
            );

            Object secureUrl = result.get("secure_url");
            if (secureUrl == null) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Cloudinary no devolvió secure_url.");
            }
            return secureUrl.toString();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error subiendo imagen a Cloudinary.", e);
        }
    }

    private String sanitize(String s) {
        if (s == null) return "sin_cedula";
        return s.replaceAll("[^A-Za-z0-9_-]", "");
    }
}
