package com.vitdo82.sandbox.demoai.resources;

import com.vitdo82.sandbox.demoai.service.RagService;
import lombok.AllArgsConstructor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1")
@AllArgsConstructor
public class QuestionResource {

    private final RagService ragService;

    @PostMapping(value = "/chat")
    public ResponseEntity<String> question(@RequestBody Optional<String> message) throws Exception {
        Flux<ChatResponse> chatResponse = ragService.query(message.orElseThrow(() -> new Exception("The message is required")));

        String result = chatResponse
            .timeout(Duration.ofSeconds(30))
            .map(response -> response.getResult().getOutput().getContent())
            .collect(Collectors.joining())
            .block();

        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/upload")
    public ResponseEntity<String> uploadDocument(@RequestPart("file") Optional<MultipartFile[]> uploadFiles) throws Exception {
        ragService.processFiles(Arrays.stream(uploadFiles.orElseThrow(() -> new Exception("The file is required"))).toList());
        return ResponseEntity.ok("Finish file upload");
    }

    @GetMapping(value = "/document")
    public ResponseEntity<Collection<String>> question() {
        Set<String> documents = ragService.getUploadedFileName();
        return ResponseEntity.ok(documents);
    }
}
