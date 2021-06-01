package com.sflpro.cafe.web.rest;

import com.sflpro.cafe.dto.SingleFieldWrapper;
import com.sflpro.cafe.service.FileFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URL;

@RestController
@RequestMapping("/file")
@PreAuthorize("hasRole('ROLE_MANAGER')")
public class FileController {

    private final Logger log = LoggerFactory.getLogger(FileController.class);

    private final FileFacade fileFacade;

    public FileController(FileFacade fileFacade) {
        this.fileFacade = fileFacade;
    }

    @PostMapping(path = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<SingleFieldWrapper<String>> uploadFile(@RequestParam MultipartFile file) {
        log.debug("REST request to upload file : {}", file.getOriginalFilename());
        return ResponseEntity.ok(SingleFieldWrapper.of(fileFacade.storeFile(file, "photo", false)));
    }

    @GetMapping(path = "/download/{guid}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String guid, HttpServletRequest request) {
        Resource resource = fileFacade.loadFile(guid);

        String contentType = getContentType(resource, request);

        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType("application/pdf"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    private String getContentType(Resource resource, HttpServletRequest request) {
        String contentType;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getPath());
        } catch (IOException ex) {
            // Fallback to the default content type if type could not be determined
            contentType = "application/octet-stream";
        }
        return contentType;
    }

    @GetMapping(path = "/pre-sign-url")
    public ResponseEntity<SingleFieldWrapper<URL>> getPreSignUrl(@RequestParam String key) throws IOException {
        log.debug("REST request to get file pre sign url : {}", key);
        return ResponseEntity.ok(SingleFieldWrapper.of(fileFacade.generatePreSingURL(key)));
    }
}
