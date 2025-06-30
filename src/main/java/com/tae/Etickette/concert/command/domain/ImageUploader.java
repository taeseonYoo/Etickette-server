package com.tae.Etickette.concert.command.domain;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.IOException;

public interface ImageUploader {
    String upload(MultipartFile request) throws IOException;
}
