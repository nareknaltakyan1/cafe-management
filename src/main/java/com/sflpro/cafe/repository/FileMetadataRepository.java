package com.sflpro.cafe.repository;

import com.sflpro.cafe.entity.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileMetadataRepository extends JpaRepository<FileMetadata, Long>
{
	FileMetadata findByGuid(String guid);

	boolean existsByGuid(String guid);
}