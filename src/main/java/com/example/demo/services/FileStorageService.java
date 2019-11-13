package com.example.demo.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collection;

import com.example.demo.entities.FileStorage;
import com.example.demo.repositories.FileStorageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    @Autowired
    private FileStorageRepository fileStorageRepository;

    public FileStorage findById(Long id) {
        return fileStorageRepository.getOne(id);
    }

    public Collection<FileStorage> findAll() {
        return fileStorageRepository.findAll();
    }

    public FileStorage store(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (fileName.contains("..")) return null;

            FileStorage fileStorage = FileStorage
                    .builder()
                    .fileName(fileName)
                    .fileType(file.getContentType())
                    .data(file.getBytes())
                    .build();
            return fileStorageRepository.save(fileStorage);

        } catch (IOException e) {
            e.getStackTrace();
            return null;
        }
    }

    public ResponseEntity<ByteArrayResource> download(Long id) {
        FileStorage fileStorage = findById(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileStorage.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileStorage.getFileName() + "\"")
                .body(new ByteArrayResource(fileStorage.getData()));
    }

    public ResponseEntity<InputStreamResource> preview(Long id) {
        FileStorage fileStorage = findById(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileStorage.getFileType()))
                .body(new InputStreamResource(new ByteArrayInputStream(fileStorage.getData())));
    }

}