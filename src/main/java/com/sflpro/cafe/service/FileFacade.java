package com.sflpro.cafe.service;

import com.sflpro.cafe.dto.FileMetadataDTO;
import com.sflpro.cafe.entity.FileMetadata;
import com.sflpro.cafe.exception.NotFoundException;
import com.sflpro.cafe.service.aws.StorageService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.UUID;

@Service
public class FileFacade
{

	private final FileMetadataService fileMetadataService;
	private final StorageService storageService;

	public FileFacade(FileMetadataService fileMetadataService, @Qualifier("AwsStorageService") StorageService storageService, UserService userService)
	{
		this.fileMetadataService = fileMetadataService;
		this.storageService = storageService;
	}

	public String storeFile(MultipartFile file, String context, Boolean personal)
	{
		String fileGuid = generateGuid();

		FileMetadataDTO fileMetadata = storageService.store(file, fileGuid, personal);
		try
		{
			fileMetadataService.saveFile(fileMetadata.getOriginalName(), fileMetadata.getGuid(), fileMetadata.getType(), fileMetadata.getPath(),
				context, null);
		}
		catch (Exception e)
		{
			rollbackFileCreation(fileMetadata.getPath());
			throw e;
		}

		return fileMetadata.getGuid();
	}

	private String generateGuid()
	{
		String fileGuid;
		do
		{
			fileGuid = generateRandomString();
		}
		while (fileMetadataService.isGuidExists(fileGuid));

		return fileGuid;
	}

	private String generateRandomString()
	{
		return UUID.randomUUID().toString();
	}

	private void rollbackFileCreation(String file)
	{
		storageService.removeFile(file);
	}

	public Resource loadFile(String guid)
	{
		FileMetadata file = fileMetadataService.getFile(guid);
		if (file == null)
		{
			throw new NotFoundException(guid);
		}

		return storageService.loadAsResource(file.getPath());
	}

	public URL generatePreSingURL(String objectKey)
	{
		return storageService.generatePreSignUrl(objectKey);
	}
}
