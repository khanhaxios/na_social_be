package com.NA.social.core.ultis;

import com.NA.social.core.enums.MediaType;
import org.springframework.web.multipart.MultipartFile;

public class MediaHelper {
    public static String getMediaType(MultipartFile multipartFile) {
        String mimeType = multipartFile.getContentType();
        for (MediaType mediaType : MediaType.values()) {
            if (mediaType.matchesMimeType(mimeType)) {
                return mediaType.getType();
            }
        }
        return null;
    }
}
