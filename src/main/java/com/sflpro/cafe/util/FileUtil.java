package com.sflpro.cafe.util;

import org.springframework.web.multipart.MultipartFile;

public final class FileUtil {

    private FileUtil() {}

    public static String getFileExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        return fileName == null ? null : fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public static String getFilenameWithExtension(String filename, String extension) {
        return filename + "." + extension;
    }

    public static String getFileContentType(MultipartFile file) {
        return file.getContentType();
    }
}
