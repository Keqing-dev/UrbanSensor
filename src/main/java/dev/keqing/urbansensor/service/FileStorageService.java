package dev.keqing.urbansensor.service;

import dev.keqing.urbansensor.config.GeneralConfig;
import dev.keqing.urbansensor.exception.CustomException;
import dev.keqing.urbansensor.utils.FileType;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {
    private final Path avatarLocation;
    private final Path fileLocation;

    @Autowired
    public FileStorageService(GeneralConfig generalConfig) throws CustomException {
        this.avatarLocation = Paths.get("").toAbsolutePath().getParent().resolve(generalConfig.getUploadDir()).resolve(generalConfig.getAvatarDir()).normalize();
        this.fileLocation = Paths.get("").toAbsolutePath().getParent().resolve(generalConfig.getUploadDir()).resolve(generalConfig.getFileDir()).normalize();

        try {
            Files.createDirectories(this.avatarLocation);
            Files.createDirectories(this.fileLocation);
        } catch (IOException ex) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not create the directories", ex);
        }
    }

    public String storeFile(MultipartFile file, FileType type, String oldFilename) throws CustomException {
        String filename = UUID.randomUUID() + "." + FilenameUtils.getExtension(file.getOriginalFilename());

        try {
            Path targetLocation = getTargetLocation(filename, type);

            Files.copy(file.getInputStream(), targetLocation);

            if(oldFilename != null && !oldFilename.isBlank())
                deleteFile(oldFilename, type);

            return filename;
        } catch (IOException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo almacenar el archivo " + file.getOriginalFilename() + ". Por favor intenta nuevamente");
        }
    }


    public void deleteFile(String filename, FileType type) throws CustomException {
        try {
            Path targetLocation = getTargetLocation(filename, type);

            Files.delete(targetLocation);
        } catch (IOException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo eliminar el archivo " + filename + ". Por favor intenta nuevamente");
        }
    }

    private Path getTargetLocation(String filename, FileType type) {
        switch(type) {
            case AVATAR:
                return this.avatarLocation.resolve(filename);
            case FILE:
                return this.fileLocation.resolve(filename);
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }
}
