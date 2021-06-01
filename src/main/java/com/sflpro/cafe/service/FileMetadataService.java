package com.sflpro.cafe.service;


import com.sflpro.cafe.entity.FileMetadata;
import com.sflpro.cafe.repository.FileMetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FileMetadataService {

    private final FileMetadataRepository fileMetadataRepository;

    @Autowired
    public FileMetadataService(FileMetadataRepository fileMetadataRepository) {
        this.fileMetadataRepository = fileMetadataRepository;
    }

    @Transactional
    public FileMetadata saveFile(String originalName, String guid, String type, String path, String context, Long createdBy) {
        FileMetadata file = new FileMetadata();
        file.setOriginalName(originalName);
        file.setGuid(guid);
        file.setType(type);
        file.setContext(context);
        file.setPath(path);

        fileMetadataRepository.save(file);

        return file;
    }

    public FileMetadata getFile(String guid) {
        return fileMetadataRepository.findByGuid(guid);
    }

    public boolean isGuidExists(String guid) {
        return fileMetadataRepository.existsByGuid(guid);
    }
}
