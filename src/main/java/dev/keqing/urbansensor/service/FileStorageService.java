package dev.keqing.urbansensor.service;

import dev.keqing.urbansensor.config.GeneralConfig;
import dev.keqing.urbansensor.exception.CustomException;
import dev.keqing.urbansensor.utils.FileType;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileStorageService {
    private final Path avatarLocation;
    private final Path fileLocation;

    @Autowired
    public FileStorageService(GeneralConfig generalConfig) throws CustomException {
        this.avatarLocation = Paths.get("").toAbsolutePath().resolve(generalConfig.getUploadDir()).resolve(generalConfig.getAvatarDir()).normalize();
        this.fileLocation = Paths.get("").toAbsolutePath().resolve(generalConfig.getUploadDir()).resolve(generalConfig.getFileDir()).normalize();

        try {
            Files.createDirectories(this.avatarLocation);
            Files.createDirectories(this.fileLocation);
        } catch (IOException ex) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Resource loadFileAsResource(String filename, FileType type) throws CustomException {
        try {
            Path filePath = getTargetLocation(filename, type);

            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists())
                return resource;
            else
                throw new CustomException(HttpStatus.NOT_FOUND);
        } catch (MalformedURLException e) {
            throw new CustomException(HttpStatus.NOT_FOUND);
        }
    }

    public String storeFile(MultipartFile file, FileType type, String oldFilename) throws CustomException {

//        switch (type) {
//            case AVATAR:
//                if(!Objects.equals(file.getContentType(), MimeTypeUtils.IMAGE_JPEG_VALUE) || !Objects.equals(file.getContentType(), MimeTypeUtils.IMAGE_PNG_VALUE))
//                    throw new CustomException(HttpStatus.BAD_REQUEST, "Formato de archivo invalido. Solo se aceptan formatos .png, .jpg y .jpeg");
//                break;
//            case FILE:
//                if (!Objects.equals(file.getContentType(), MimeTypeUtils.IMAGE_JPEG_VALUE) || !Objects.equals(file.getContentType(), MimeTypeUtils.IMAGE_PNG_VALUE) || !Objects.equals(file.getContentType(), "video/mp4"))
//                    throw new CustomException(HttpStatus.BAD_REQUEST, "Formato de archivo invalido. Solo se aceptan formatos .png, .jpg y .jpeg");
//                break;
//        }

        String filename = UUID.randomUUID() + "." + FilenameUtils.getExtension(file.getOriginalFilename());

        try {
            Path targetLocation = getTargetLocation(filename, type);

            Files.copy(file.getInputStream(), targetLocation);

            if (oldFilename != null && !oldFilename.isBlank())
                deleteFile(oldFilename, type);

            return filename;
        } catch (IOException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String storeFile(MultipartFile file, FileType type) throws CustomException {
        String filename = UUID.randomUUID() + "." + FilenameUtils.getExtension(file.getOriginalFilename());

        try {
            Path targetLocation = getTargetLocation(filename, type);

            Files.copy(file.getInputStream(), targetLocation);

            return filename;
        } catch (IOException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public void deleteFile(String filename, FileType type) throws CustomException {
        try {
            Path targetLocation = getTargetLocation(filename, type);

            Files.delete(targetLocation);
        } catch (IOException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Path getTargetLocation(String filename, FileType type) {
        switch (type) {
            case AVATAR:
                return this.avatarLocation.resolve(filename);
            case FILE:
                return this.fileLocation.resolve(filename);
            default:
                throw new IllegalStateException("Valor Inesperado: " + type);
        }
    }
}
