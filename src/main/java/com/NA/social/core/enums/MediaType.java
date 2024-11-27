package com.NA.social.core.enums;

public enum MediaType {
    VIDEO("video", "video/mp4", "video/x-msvideo"),
    IMAGE("image", "image/jpeg", "image/png", "image/gif", "image/svg+xml", "image/webp","image/*"),
    FILE_DOCX("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
    FILE_PDF("pdf", "application/pdf"),
    FILE_COMPRESS("compress", "application/zip", "application/x-rar-compressed"),
    GIF("gif", "image/gif"),
    RAW("raw", "unknown");
    private final String type;
    private final String[] mimeTypes;

    MediaType(String type, String... mimeTypes) {
        this.type = type;
        this.mimeTypes = mimeTypes;
    }

    public String getType() {
        return type;
    }

    public String[] getMimeTypes() {
        return mimeTypes;
    }

    public boolean matchesMimeType(String mimeType) {
        for (String mt : mimeTypes) {
            if (mt.equalsIgnoreCase(mimeType)) {
                return true;
            }
        }
        return false;
    }
}
