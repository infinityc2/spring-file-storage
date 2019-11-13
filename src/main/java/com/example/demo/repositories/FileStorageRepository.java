package com.example.demo.repositories;

import java.util.Optional;

import com.example.demo.entities.FileStorage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface FileStorageRepository extends JpaRepository<FileStorage, Long>{

    Optional<FileStorage> findByFileName(String fileName);
}