package com.molla.admin.util;

import org.apache.juli.logging.Log;
import org.slf4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadUtil {

    public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);

        }
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new IOException("could not save file " + fileName, ex);

        }
    }
    public static void cleanDir(String dir) {
        Path dirPath = Paths.get(dir);
        try {
            Files.list(dirPath).forEach(file -> {
                if(!Files.isDirectory(file)) {
                    try {
                        Files.delete(file);
                    } catch (IOException ex) {
                        System.out.println("could not delete file" + file);

                    }
                }
            });
        } catch (IOException ex) {
            System.out.println("could not list directory" + dirPath);
        }
    }

    public static void removeDir(String Dir) {
        cleanDir(Dir);
        try {
            Files.delete(Paths.get(Dir));
        } catch (IOException e){
            //LOGGER.error("could not remove directory " + Dir);
        }
    }

    public static void cleanDir(String uploadDir, String fileName, MultipartFile multipartFile) {
    }
}
