package com.example.prj.netflix_analyzer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

@Service
public class UploadService {

    @Value("${upload.path}")
    private String uploadPath;

    public List<String> upload(MultipartFile file) {
        try {
            Path filePath = Path.of(uploadPath, String.format("%s_%s", System.currentTimeMillis() , file.getOriginalFilename()));
            saveFile(file, filePath);
            return getFileLines(filePath);
        }
        catch (Exception e) {
            throw new RuntimeException("File seems to be corrupted or issue in processing");
        }
    }

    private List<String> getFileLines(Path filePath) {
        if (Files.exists(filePath)) {
            try {
                return Files.readAllLines(filePath);
            } catch (Exception e) {
                throw new RuntimeException("File seems to be corrupted or issue in processing");
            }
        } else {
            return Collections.emptyList();
        }
    }

    void saveFile(MultipartFile file, Path pathToZipFile) {
        try {
            InputStream in = file.getInputStream();
            OutputStream out = Files.newOutputStream(pathToZipFile);
            in.transferTo(out);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
