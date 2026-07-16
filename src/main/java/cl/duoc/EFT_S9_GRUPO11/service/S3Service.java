package cl.duoc.EFT_S9_GRUPO11.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Service
public class S3Service {

@Value("${aws.s3.bucket-name}")
private String bucketName;

@Value("${aws.s3.region}")
private String region;

@Value("${aws.accessKeyId}")
private String accessKey;

@Value("${aws.secretAccessKey}")
private String secretKey;

@Value("${aws.sessionToken}")
private String sessionToken;

public String subirArchivo(MultipartFile file) throws IOException {
        // 1. Autenticación con AWS Academy
        S3Client s3 = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsSessionCredentials.create(accessKey, secretKey, sessionToken)
                ))
                .build();

        // 2. Generamos un nombre único para no sobreescribir archivos
        String key = "materiales/" + System.currentTimeMillis() + "_" + file.getOriginalFilename();

        // 3. Preparamos la petición de subida
        PutObjectRequest putOb = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build();

        // 4. Ejecutamos la subida a la nube
        s3.putObject(putOb, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        // 5. Retornamos la URL pública para guardarla en Oracle (Ej: en la tabla Curso o Examen)
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, key);
}
}