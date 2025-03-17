package com.NA.social.core.service.media;

import com.NA.social.core.ultis.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface MediaService {
    ResponseEntity<ApiResponse> uploadFile(List<MultipartFile> files) throws Exception;

    ResponseEntity<?> downloadFile(Long id, Map<String, String> params) throws Exception;

    ResponseEntity<?> getById(Long id, Map<String, String> params) throws Exception;
}
