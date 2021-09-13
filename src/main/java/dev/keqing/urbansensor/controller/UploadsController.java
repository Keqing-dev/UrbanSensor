package dev.keqing.urbansensor.controller;

import dev.keqing.urbansensor.exception.CustomException;
import dev.keqing.urbansensor.service.FileStorageService;
import dev.keqing.urbansensor.utils.FileType;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Hidden
@RestController
@RequestMapping("uploads")
public class UploadsController {

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/{type}/{filename}")
    ResponseEntity<Resource> loadFile(@PathVariable String filename, @PathVariable String type, HttpServletRequest request) throws CustomException {
        Resource resource;

        switch (type) {
            case "avatar":
                resource = fileStorageService.loadFileAsResource(filename, FileType.AVATAR);
                break;
            case "file":
                resource = fileStorageService.loadFileAsResource(filename, FileType.FILE);
                break;
            default:
                throw new CustomException(HttpStatus.NOT_FOUND);
        }

        String contentType;

        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "No se pudo determinar el tipo de archivo");
        }

        if(contentType == null)
            contentType = "application/octet-stream";

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
    }
}
