package com.tae.Etickette.concert.infra;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.tae.Etickette.concert.command.domain.ImageUploader;
import com.tae.Etickette.global.config.S3Config;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3ImageUploader implements ImageUploader {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${local.location.image}")
    private String localLocation;

    private final S3Config s3Config;
    @Override
    public String upload(MultipartFile multipartFile){

        //파일명과 확장자
        String fileName = multipartFile.getOriginalFilename();
        String ext = fileName.substring(fileName.indexOf("."));
        //UUID를 사용한 파일명 작성
        String uuidFileName = UUID.randomUUID() + ext;
        String localPath = localLocation + uuidFileName;
        //로컬환경에 이미지를 저장한다.
        File localFile = new File(localPath);

        try {
            multipartFile.transferTo(localFile);

            //S3에 이미지를 업로드 한다.
            s3Config.amazonS3Client().putObject(
                    new PutObjectRequest(bucket, uuidFileName, localFile)
                            .withCannedAcl(CannedAccessControlList.PublicRead));

            return s3Config.amazonS3Client().getUrl(bucket, uuidFileName).toString();
        } catch (IOException e) {
            throw new ImageUploadException("이미지 업로드 실패", e);
        }finally {
            //로컬환경의 이미지를 삭제한다.
            localFile.delete();
        }
    }
}
