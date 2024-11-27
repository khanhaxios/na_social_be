package com.NA.social.core.api;

import com.NA.social.core.service.media.MediaService;
import com.NA.social.core.ultis.ApiResponse;
import com.NA.social.core.ultis.Responser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@CrossOrigin
public class MediaApi {
    private final MediaService mediaService;

    @PostMapping("/uploads")
    public ResponseEntity<ApiResponse> uploadFile(@RequestParam(name = "files") List<MultipartFile> files) {
        try {
            return mediaService.uploadFile(files);
        } catch (Exception e) {
            return Responser.serverError(e.getMessage());
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadFile(@PathVariable(name = "id") long id) {
        try {
            return mediaService.downloadFile(id);
        } catch (Exception e) {
            return Responser.serverError(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable(name = "id") long id) {
        try {
            return mediaService.getById(id);
        } catch (Exception e) {
            return Responser.serverError(e.getMessage());
        }
    }
}
