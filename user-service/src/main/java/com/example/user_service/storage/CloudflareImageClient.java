package com.example.user_service.storage;

import com.example.security.exception.BadRequestException;
import com.example.security.exception.ServiceUnavailableException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class CloudflareImageClient {

    private final RestClient restClient = RestClient.create();

    @Value("${app.cloudflare.images-url:}")
    private String imagesUrl;

    @Value("${app.cloudflare.api-token:}")
    private String apiToken;

    public String upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Immagine mancante");
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BadRequestException("Il file deve essere un'immagine");
        }
        if (imagesUrl == null || imagesUrl.isBlank() || apiToken == null || apiToken.isBlank()) {
            throw new ServiceUnavailableException("Caricamento immagini non configurato");
        }

        byte[] bytes;
        try {
            bytes = file.getBytes();
        } catch (IOException ex) {
            throw new ServiceUnavailableException("Lettura dell'immagine non riuscita");
        }
        String filename = file.getOriginalFilename() == null || file.getOriginalFilename().isBlank()
                ? "foto"
                : file.getOriginalFilename();
        ByteArrayResource resource = new ByteArrayResource(bytes) {
            @Override
            public String getFilename() {
                return filename;
            }
        };

        LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", resource);

        JsonNode response = restClient.post()
                .uri(imagesUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiToken)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(body)
                .retrieve()
                .body(JsonNode.class);

        if (response == null) {
            throw new ServiceUnavailableException("Caricamento immagine non riuscito");
        }
        JsonNode variants = response.path("result").path("variants");
        if (variants.isArray() && !variants.isEmpty() && !variants.get(0).asText().isBlank()) {
            return variants.get(0).asText();
        }
        String url = response.path("result").path("url").asText("");
        if (url.isBlank()) {
            throw new ServiceUnavailableException("Caricamento immagine non riuscito");
        }
        return url;
    }
}
