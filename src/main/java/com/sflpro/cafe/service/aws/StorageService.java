package com.sflpro.cafe.service.aws;

import com.sflpro.cafe.dto.FileMetadataDTO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.nio.file.Path;

public interface StorageService
{
	FileMetadataDTO store(MultipartFile file, String guid, Boolean personal);

	void removeFile(String file);

	Path load(String filename);

	Resource loadAsResource(String filename);

	URL generatePreSignUrl(String objectKey);
}
