package com.apps.pochak.common;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static com.apps.pochak.common.BaseResponseStatus.*;

@RequiredArgsConstructor
@Component
@Service
public class AwsS3Service {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile multipartFile, String dirName) throws BaseException, IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new BaseException(Convert_File_Error));
        return upload(uploadFile, dirName);
    }

    private String upload(File uploadFile, String dirName) throws BaseException {
        String fileName = dirName + "/" + UUID.randomUUID() + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);
        deleteFile(uploadFile);
        return uploadImageUrl;
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(System.getProperty("user.dir") + "/" + file.getOriginalFilename());
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

    private String putS3(File uploadFile, String fileName) throws BaseException {
        try {
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
            return amazonS3Client.getUrl(bucket, fileName).toString();
        } catch (AmazonS3Exception e) {
            throw new BaseException(S3_Upload_Error);
        }
    }

    private void deleteFile(File targetFile) throws BaseException {
        try {
            targetFile.delete();
        } catch (AmazonServiceException e) {
            throw new BaseException(Delete_File_Error);
        }
    }
}
