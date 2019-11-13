package com.example.demo.controllers;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.demo.entities.FileStorage;
import com.example.demo.services.FileStorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class FileStorageController {

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/files")
    public Collection<FileStorage> getAll() {
        return fileStorageService.findAll();
    }

    @GetMapping("/files/{id}")
    public FileStorage getOne(@PathVariable Long id) {
        return fileStorageService.findById(id);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<?> download(@PathVariable Long id) {
        return fileStorageService.download(id);
    }

    @GetMapping("/preview/{id}")
    public ResponseEntity<?> preview(@PathVariable Long id) {
        return fileStorageService.preview(id);
    }

    @PostMapping("/upload")
    public Map<String, Object> store(@RequestParam("file") MultipartFile file) {
        FileStorage fileStorage = fileStorageService.store(file);
        String fileDownloadUri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/download/")
                .path(fileStorage.getId().toString())
                .toUriString();
        
        Map<String, Object> fileMapping = new HashMap<>();
        fileMapping.put("fileName", fileStorage.getFileName());
        fileMapping.put("uri", fileDownloadUri);
        fileMapping.put("type", file.getContentType());
        fileMapping.put("size", file.getSize());
        return fileMapping;
    }

    @PostMapping("/uploads")
    public List<?> uploads(@RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files)
                .stream()
                .map(file -> store(file))
                .collect(Collectors.toList());
    }


}