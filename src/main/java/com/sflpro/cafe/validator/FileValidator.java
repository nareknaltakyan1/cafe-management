package com.sflpro.cafe.validator;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class FileValidator {

    private static final List<String> ACCEPTED_CONTENT_TYPES = Arrays.asList(
            "image/jpeg",
            "image/jpg",
            "image/png",
            "application/pdf",
            "video/mp4",
            "video/quicktime",
            "text/html",
            "application/xlsx"
    );

    private static final List<String> ACCEPTED_EXTENSIONS = Arrays.asList(
            "jpeg",
            "jpg",
            "png",
            "mpeg",
            "pdf",
            "mp4",
            "mov",
            "html",
            "xlsx"
    );

    public boolean validateFileType(String contentType, String extension) {
        boolean isContentTypeAcceptable = validateContentType(contentType);
        boolean isExtensionAcceptable = validateExtension(extension);

        return isContentTypeAcceptable && isExtensionAcceptable;
    }

    public boolean validateContentType(String fileContentType) {
        return ACCEPTED_CONTENT_TYPES.contains(fileContentType);
    }

    public boolean validateExtension(String extension) {
        return ACCEPTED_EXTENSIONS.contains(extension);
    }
}
