package com.wevel.wevel_server.deepapi;

import com.wevel.wevel_server.deepapi.model.TranslationRequest;
import com.wevel.wevel_server.deepapi.model.TranslationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@RestController
@RequestMapping("/api")
class TranslationController {

    private final DeepLConfig deepLConfig;

    @Autowired
    public TranslationController(DeepLConfig deepLConfig) {
        this.deepLConfig = deepLConfig;
    }

    private final String deeplApiUrl = "https://api-free.deepl.com/v2/translate";

    @PostMapping("/translate")
    public ResponseEntity<TranslationResponse> translate(@RequestBody TranslationRequest request) {
        String authKey = deepLConfig.getAuthKey();
        String targetLang = request.getTarget_lang();

        String apiUrl = deeplApiUrl + "?target_lang=" + targetLang;
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "DeepL-Auth-Key " + authKey);

        TranslationResponse response = restTemplate.postForObject(apiUrl, createHttpEntity(request, headers), TranslationResponse.class);

        return ResponseEntity.ok(response);
    }

    private HttpEntity<TranslationRequest> createHttpEntity(TranslationRequest request, HttpHeaders headers) {
        return new HttpEntity<>(request, headers);
    }
}