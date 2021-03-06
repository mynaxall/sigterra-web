package itomy.sigterra.service;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.*;
import itomy.sigterra.config.JHipsterProperties;
import itomy.sigterra.domain.User;
import itomy.sigterra.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.UUID;

/**
 * Service for AWS S3 Bucket.
 * Default storage for profile icons.
 */
@Service
@Transactional
public class AWSS3BucketService {
    public static final String USER_PROFILE_ICON = "user_profile_icon_";
    protected static AmazonS3 s3Client = null;
    private final Logger log = LoggerFactory.getLogger(AWSS3BucketService.class);
    @Inject
    private UserService userService;
    @Inject
    private UserRepository userRepository;
    @Inject
    private JHipsterProperties hipsterProperties;

    @PostConstruct
    private void init() {
        try {
            Resource resource = new ClassPathResource(hipsterProperties.getAwss3Bucket().getCredentials());
            s3Client = new AmazonS3Client(new PropertiesCredentials(resource.getFile()));
            Region usRegion = Region.getRegion(Regions.fromName(hipsterProperties.getAwss3Bucket().getRegion()));
            s3Client.setRegion(usRegion);
            s3Client.setS3ClientOptions(S3ClientOptions.builder().setAccelerateModeEnabled(true).build());
        } catch (IOException e) {
            log.error("Unable to initialize the AWS S3 Bucket Service");
        }
    }


    public URI uploadProfileImage(MultipartFile file) {
        URI uri = null;
        if (file != null && !file.isEmpty()) {
            try {
                String bucketName = hipsterProperties.getAwss3Bucket().getName();
                User user = userService.getUserWithAuthorities();

                String imageUrl = user.getImageUrl();
                String folderPath = "accounts/" + user.getId() + "/";
                if (StringUtils.isNotBlank(imageUrl)) {
                    String imageKey = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
                    s3Client.deleteObject(new DeleteObjectRequest(bucketName, folderPath + imageKey));
                }

                String name = USER_PROFILE_ICON + user.getId();
                String originalFilename = file.getOriginalFilename();
                if (originalFilename.contains(".")) {
                    name += originalFilename.substring(originalFilename.lastIndexOf("."));
                }

                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(file.getContentType());
                metadata.setContentLength(file.getSize());
                String fileKey = folderPath + name;
                PutObjectRequest putObject = new PutObjectRequest(bucketName, fileKey, file.getInputStream(), metadata);
                putObject.withCannedAcl(CannedAccessControlList.PublicRead);

                s3Client.putObject(putObject);

                GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucketName, fileKey);
                URL url = s3Client.generatePresignedUrl(urlRequest);
                uri = new URI(url.toURI().getScheme(),
                    url.toURI().getAuthority(),
                    url.toURI().getPath(),
                    null,
                    url.toURI().getFragment());


                uri = URI.create(uri.toString() + '?' + System.currentTimeMillis());
                user.setImageUrl(uri.toString());
                log.debug("Uploaded profile image to AWS S3 Bucket has URL: {}", uri.toString());
                userRepository.save(user);
            } catch (Exception e) {
                log.error("Error occurred while uploading the profile icon file", e);
            }
        }
        return uri;
    }

    public URI uploadBusinessImage(MultipartFile file, String id) {
        URI uri = null;
        if (file != null && !file.isEmpty()) {
            try {
                String bucketName = hipsterProperties.getAwss3Bucket().getName();
                User user = userService.getUserWithAuthorities();

                String folderPath = "bussinessCardTab/" + user.getId() + "/";
                if (StringUtils.isNotBlank(id)) {
                    s3Client.deleteObject(new DeleteObjectRequest(bucketName, folderPath + id));
                }

                String name = USER_PROFILE_ICON + user.getId() + "_" + System.currentTimeMillis();
                String originalFilename = file.getOriginalFilename();
                if (originalFilename.contains(".")) {
                    name += originalFilename.substring(originalFilename.lastIndexOf("."));
                }

                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(file.getContentType());
                metadata.setContentLength(file.getSize());
                String fileKey = folderPath + name;
                PutObjectRequest putObject = new PutObjectRequest(bucketName, fileKey, file.getInputStream(), metadata);
                putObject.withCannedAcl(CannedAccessControlList.PublicRead);

                s3Client.putObject(putObject);

                GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucketName, fileKey);
                URL url = s3Client.generatePresignedUrl(urlRequest);
                uri = new URI(url.toURI().getScheme(),
                    url.toURI().getAuthority(),
                    url.toURI().getPath(),
                    null,
                    url.toURI().getFragment());
                log.debug("Uploaded business image to AWS S3 Bucket has URL: {}", uri.toString());
            } catch (Exception e) {
                log.error("Error occurred while uploading the profile icon file", e);
            }
        }
        return uri;
    }

    public URI uploadSignatureImage(MultipartFile file, String cardletId, String name) {
        URI uri = null;
        if (file != null && !file.isEmpty()) {
            try {
                String bucketName = hipsterProperties.getAwss3Bucket().getName();
                User user = userService.getUserWithAuthorities();

                String folderPath = "signature/" + user.getId() + "/" + cardletId + "/";
                if (StringUtils.isNotBlank(cardletId)) {
                    s3Client.deleteObject(new DeleteObjectRequest(bucketName, folderPath + name));
                }

                String originalFilename = file.getOriginalFilename();
                if (originalFilename.contains(".")) {
                    name += originalFilename.substring(originalFilename.lastIndexOf("."));
                }

                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(file.getContentType());
                metadata.setContentLength(file.getSize());
                String fileKey = folderPath + name;
                PutObjectRequest putObject = new PutObjectRequest(bucketName, fileKey, file.getInputStream(), metadata);
                putObject.withCannedAcl(CannedAccessControlList.PublicRead);

                s3Client.putObject(putObject);

                GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucketName, fileKey);
                URL url = s3Client.generatePresignedUrl(urlRequest);
                uri = new URI(url.toURI().getScheme(),
                    url.toURI().getAuthority(),
                    url.toURI().getPath(),
                    null,
                    url.toURI().getFragment());

                uri = URI.create(uri.toString() + '?' + System.currentTimeMillis());
                log.debug("Uploaded signature image to AWS S3 Bucket has URL: {}", uri.toString());
            } catch (Exception e) {
                log.error("Error occurred while uploading the profile icon file", e);
            }
        }
        return uri;
    }

    /**
     * upload image to S3 bucket
     *
     * @param file     file
     * @param fullPath fileKey in bucket
     * @return new file URI
     */
    public URI uploadImage(MultipartFile file, String fullPath) {
        URI uri = null;
        if (file != null && !file.isEmpty()) {
            try {

                String bucketName = hipsterProperties.getAwss3Bucket().getName();
                User user = userService.getUserWithAuthorities();

                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(file.getContentType());
                metadata.setContentLength(file.getSize());

                PutObjectRequest putObject = new PutObjectRequest(bucketName, fullPath, file.getInputStream(), metadata);
                putObject.withCannedAcl(CannedAccessControlList.PublicRead);

                s3Client.putObject(putObject);

                GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucketName, fullPath);
                URL url = s3Client.generatePresignedUrl(urlRequest);
                uri = new URI(url.toURI().getScheme(),
                    url.toURI().getAuthority(),
                    url.toURI().getPath(),
                    null,
                    url.toURI().getFragment());

                uri = URI.create(uri.toString() + '?' + System.currentTimeMillis());
                log.debug("Uploaded image file to AWS S3 Bucket has URL: {}", url.toString());
            } catch (Exception e) {
                log.error("Error occurred while uploading the file", e);
            }
        }
        return uri;
    }

    /**
     * rename file in bucket. Don't delete source file, only copy
     *
     * @param source source fileKey in bucket
     * @param dest   dest fileKet in bucket.
     * @return
     */
    public URI renameFile(String source, String dest) {
        URI uri = null;
        try {
            String bucketName = hipsterProperties.getAwss3Bucket().getName();
            s3Client.copyObject(bucketName, source, bucketName, dest);
            s3Client.setObjectAcl(bucketName, dest, CannedAccessControlList.PublicRead);
            URL url = s3Client.generatePresignedUrl(new GeneratePresignedUrlRequest(bucketName, dest));
            uri = new URI(url.toURI().getScheme(),
                url.toURI().getAuthority(),
                url.toURI().getPath(),
                null,
                url.toURI().getFragment());

            uri = URI.create(uri.toString() + '?' + System.currentTimeMillis());
            log.debug("File copied " + source + "->" + dest);
        } catch (Exception e) {
            log.error("Error occurred while rename the profile icon file", e);

        }
        return uri;
    }

    /**
     * delete file in bucket
     *
     * @param path fileKey in bucket
     */
    public void deleteFile(String path) {
        try {
            String bucketName = hipsterProperties.getAwss3Bucket().getName();
            s3Client.deleteObject(bucketName, path);
            log.info("Deleted file: " + path);
        } catch (Exception e) {
            log.error("Error delete file " + path, e);
        }
    }

    public URI uploadImage(MultipartFile file, Long cardletId, String basePath) {
        URI uri = null;
        if (file != null && !file.isEmpty()) {
            try {
                String bucketName = hipsterProperties.getAwss3Bucket().getName();
                User user = userService.getUserWithAuthorities();

                String originalFilename = file.getOriginalFilename();
                String name = UUID.randomUUID().toString();

                String folderPath = basePath + user.getId() + "/" + cardletId + "/";
                if (StringUtils.isNotBlank(cardletId.toString())) {
                    s3Client.deleteObject(new DeleteObjectRequest(bucketName, folderPath + name));
                }

                if (originalFilename.contains(".")) {
                    name += originalFilename.substring(originalFilename.lastIndexOf("."));
                }

                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(file.getContentType());
                metadata.setContentLength(file.getSize());
                String fileKey = folderPath + name;
                PutObjectRequest putObject = new PutObjectRequest(bucketName, fileKey, file.getInputStream(), metadata);
                putObject.withCannedAcl(CannedAccessControlList.PublicRead);

                s3Client.putObject(putObject);

                GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucketName, fileKey);
                URL url = s3Client.generatePresignedUrl(urlRequest);
                uri = new URI(url.toURI().getScheme(),
                    url.toURI().getAuthority(),
                    url.toURI().getPath(),
                    null,
                    url.toURI().getFragment());

                uri = URI.create(uri.toString() + '?' + System.currentTimeMillis());
                log.debug("Uploaded signature image to AWS S3 Bucket has URL: {}", uri.toString());
            } catch (Exception e) {
                log.error("Error occurred while uploading the profile icon file", e);
            }
        }
        return uri;
    }
}

