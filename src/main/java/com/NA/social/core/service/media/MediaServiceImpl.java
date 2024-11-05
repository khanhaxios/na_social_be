package com.NA.social.core.service.media;

import com.NA.social.core.entity.Media;
import com.NA.social.core.repository.MediaRepository;
import com.NA.social.core.ultis.ApiResponse;
import com.NA.social.core.ultis.MediaHelper;
import com.NA.social.core.ultis.Responser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class MediaServiceImpl implements MediaService {
    private final MediaRepository mediaRepository;
    private final Path fileUploadDirectory = Paths.get("uploads").toAbsolutePath().normalize();

    public void init() throws Exception {
        if (!Files.exists(fileUploadDirectory)) {
            Files.createDirectories(fileUploadDirectory);
        }
    }

    @Override
    public ResponseEntity<ApiResponse> uploadFile(List<MultipartFile> files) throws Exception {
        init();
        List<Media> prepareMedia = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileName = UUID.randomUUID().toString().concat(StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename())));
            if (fileName.contains("..")) {
                return Responser.badRequest(List.of("Invalid file"));
            }
            String fileType = MediaHelper.getMediaType(file);
            if (fileType == null) {
                return Responser.badRequest(List.of("Media type not supported!"));
            }
            Path targetLocation = this.fileUploadDirectory.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            Media media = new Media();
            media.setType(fileType);
            media.setPath(fileName);
            media.setSize(file.getSize());
            media.setName(fileName);
            prepareMedia.add(media);
        }
        // save all media to database
        return Responser.success(mediaRepository.saveAll(prepareMedia));
    }

    @Override
    public ResponseEntity<?> downloadFile(Long id) throws Exception {
        Media media = mediaRepository.findById(id).orElse(null);
        if (media == null) return Responser.notFound();
        Path filePath = this.fileUploadDirectory.resolve(media.getPath());
        Resource resource = new UrlResource(filePath.toUri());
        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + media.getName() + "\"").body(Files.readAllBytes(filePath));
        }
        return Responser.notFound();
    }

    @Override
    public ResponseEntity<?> getById(Long id) throws Exception {
        return this.downloadFile(id);
    }
}
