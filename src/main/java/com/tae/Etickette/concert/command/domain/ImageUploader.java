package com.tae.Etickette.concert.command.domain;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploader {
    String upload(MultipartFile request);
}
