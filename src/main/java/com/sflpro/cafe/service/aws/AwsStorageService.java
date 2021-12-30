package com.sflpro.cafe.service.aws;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.sflpro.cafe.config.CloudStorageConfig;
import com.sflpro.cafe.dto.FileMetadataDTO;
import com.sflpro.cafe.exception.AppIllegalStateException;
import com.sflpro.cafe.util.FileUtil;
import com.sflpro.cafe.validator.FileValidator;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service("AwsStorageService")
public class AwsStorageService implements StorageService
{

	private static final String PATH_UPLOADS = "uploads";
	private static final String PATH_VIDEO = "/video";
	private static final String PATH_DOCUMENTS = "/documents";
	private static final String PATH_PHOTO = "/photo";
	private static final String PERSONAL = "/personal";
	private static final String PHOTO_EXTENSION = "jpg";
	private static final String EXCEL_EXTENSION = "xlsx";
	private static final String PHOTO_PATH = "uploads/personal/";
	private static final String EXCEL_PATH = "xlsx";

	private final AwsGateway awsGateway;
	private final CloudStorageConfig cloudStorageConfig;
	private final FileValidator fileValidator;
	private final ResourceLoader resourceLoader;

	public AwsStorageService(AwsGateway awsGateway, CloudStorageConfig cloudStorageConfig, FileValidator fileValidator, ResourceLoader resourceLoader)
	{
		this.awsGateway = awsGateway;
		this.cloudStorageConfig = cloudStorageConfig;
		this.fileValidator = fileValidator;
		this.resourceLoader = resourceLoader;
	}

	@Override
	public FileMetadataDTO store(MultipartFile file, String guid, Boolean personal)
	{
		String extension = FileUtil.getFileExtension(file);
		if (StringUtils.isEmpty(extension))
		{
			throw new AppIllegalStateException("Unknown file extension");
		}
		String filename = FileUtil.getFilenameWithExtension(guid, extension);
		String contentType = FileUtil.getFileContentType(file);

		if (!fileValidator.validateFileType(contentType, extension))
		{
			throw new AppIllegalStateException("Wrong file type");
		}

		String folderKey;
		if (contentType.startsWith("image"))
		{
			folderKey = PATH_PHOTO;
		}
		else
			if (contentType.startsWith("video"))
			{
				folderKey = PATH_VIDEO;
			}
			else
				if (contentType.startsWith("application/pdf"))
				{
					folderKey = PATH_DOCUMENTS;
				}
				else
					if (contentType.startsWith("text/html"))
					{
						folderKey = PATH_DOCUMENTS;
					}
					else
					{
						throw new AppIllegalStateException("unknown type");
					}
		String key = PATH_UPLOADS + (personal ? PERSONAL : folderKey) + "/" + filename;
		Map<String, String> metadata = new HashMap<>();
		metadata.put("originalName", file.getName());
		try
		{
			PutObjectResult result = awsGateway.uploadFile(cloudStorageConfig.getStorage().getBucketName(), key, FileUtil.getFileContentType(file),
				file.getInputStream(), metadata, personal);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}

		FileMetadataDTO fileMetadata = new FileMetadataDTO();
		fileMetadata.setOriginalName(file.getOriginalFilename());
		fileMetadata.setGuid(filename);
		fileMetadata.setType(contentType);
		fileMetadata.setPath(awsGateway.getResourceUrl(cloudStorageConfig.getStorage().getBucketName(), key).toString());
		return fileMetadata;
	}

	@Override
	public void removeFile(String file)
	{
	}

	@Override
	public Path load(String filename)
	{
		return null;
	}

	@Override
	public Resource loadAsResource(String filename)
	{
		return resourceLoader.getResource(filename);
	}

	public URL generatePreSignUrl(String objectKey)
	{
		String finalObjectKey = objectKey;
		String type = Optional.ofNullable(objectKey).filter(f -> f.contains(".")).map(f -> f.substring(finalObjectKey.lastIndexOf(".") + 1))
			.orElse(null);

		if (Objects.equals(type, PHOTO_EXTENSION))
		{
			objectKey = PHOTO_PATH + objectKey;
		}
		else
			if (Objects.equals(type, EXCEL_EXTENSION))
			{
				// TODO
				objectKey = EXCEL_PATH + objectKey;
			}

		java.util.Date expiration = new java.util.Date();
		long expTimeMillis = expiration.getTime();
		expTimeMillis += 1000 * 60 * 60;
		expiration.setTime(expTimeMillis);
		return awsGateway.generatePreSingUrl(cloudStorageConfig.getStorage().getBucketName(), objectKey, expiration, HttpMethod.GET);
	}
}
