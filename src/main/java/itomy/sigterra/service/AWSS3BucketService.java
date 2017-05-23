package itomy.sigterra.service;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
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

/**
 * Service for AWS S3 Bucket.
 * Default storage for profile icons.
 */
@Service
@Transactional
public class AWSS3BucketService {
    private final Logger log = LoggerFactory.getLogger(AWSS3BucketService.class);

    @Inject
    private UserService userService;

    @Inject
    private UserRepository userRepository;

    @Inject
    private JHipsterProperties hipsterProperties;

    protected static AmazonS3 s3Client = null;

    @PostConstruct
    private void init() {
        try {
            Resource resource = new ClassPathResource(hipsterProperties.getAwss3Bucket().getCredentials());
            s3Client = new AmazonS3Client(new PropertiesCredentials(resource.getFile()));
            Region usRegion = Region.getRegion(Regions.fromName(hipsterProperties.getAwss3Bucket().getRegion()));
            s3Client.setRegion(usRegion);
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
                if(StringUtils.isNotBlank(imageUrl)) {
                    String imageKey = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
                    s3Client.deleteObject(new DeleteObjectRequest(bucketName+"/accounts/"+user.getId(), imageKey));
                }

                String name = "user_profile_icon_" + user.getId();
                String originalFilename = file.getOriginalFilename();
                if (originalFilename.contains(".")) {
                    name += originalFilename.substring(originalFilename.lastIndexOf("."));
                }

                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(file.getContentType());
                PutObjectRequest putObject = new PutObjectRequest(bucketName+"/accounts/"+user.getId(), name, file.getInputStream(), metadata);
                putObject.withCannedAcl(CannedAccessControlList.PublicRead);
                s3Client.putObject(putObject);
                GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucketName+"/accounts/"+user.getId(), name);
                URL url = s3Client.generatePresignedUrl(urlRequest);
                uri = new URI(url.toURI().getScheme(), url.toURI().getAuthority(), url.toURI()
                                                                                      .getPath(), null, url.toURI()
                                                                                                           .getFragment());



                user.setImageUrl(uri.toString() + "?" + System.currentTimeMillis());
                log.info(uri.toString());
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


                if(StringUtils.isNotBlank(id)) {

                    s3Client.deleteObject(new DeleteObjectRequest(bucketName+"/bussinessCardTab/"+user.getId(), id));
                }

                String name = "user_profile_icon_" + user.getId() + "_" + System.currentTimeMillis();
                String originalFilename = file.getOriginalFilename();
                if (originalFilename.contains(".")) {
                    name += originalFilename.substring(originalFilename.lastIndexOf("."));
                }

                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(file.getContentType());
                PutObjectRequest putObject = new PutObjectRequest(bucketName+"/bussinessCardTab/"+user.getId(), name, file.getInputStream(), metadata);
                putObject.withCannedAcl(CannedAccessControlList.PublicRead);
                s3Client.putObject(putObject);
                GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucketName+"/bussinessCardTab/"+user.getId(), name);
                URL url = s3Client.generatePresignedUrl(urlRequest);
                uri = new URI(url.toURI().getScheme(), url.toURI().getAuthority(), url.toURI()
                    .getPath(), null, url.toURI()
                    .getFragment());

            } catch (Exception e) {
                log.error("Error occurred while uploading the profile icon file", e);
            }
        }
        return uri;
    }

    public URI uploadSignatureImage(MultipartFile file, String cardletId, String fileName) {
        URI uri = null;
        if (file != null && !file.isEmpty()) {
            try {
                String bucketName = hipsterProperties.getAwss3Bucket().getName();
                User user = userService.getUserWithAuthorities();


                if(StringUtils.isNotBlank(cardletId)) {

                    s3Client.deleteObject(new DeleteObjectRequest(bucketName+"/signature/"+user.getId()+"/"+cardletId, fileName));
                }

                String name = fileName;
                String originalFilename = file.getOriginalFilename();
                if (originalFilename.contains(".")) {
                    name += originalFilename.substring(originalFilename.lastIndexOf("."));
                }

                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(file.getContentType());
                PutObjectRequest putObject = new PutObjectRequest(bucketName+"/signature/"+user.getId()+"/"+cardletId, name, file.getInputStream(), metadata);
                putObject.withCannedAcl(CannedAccessControlList.PublicRead);
                s3Client.putObject(putObject);
                GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucketName+"/signature/"+user.getId()+"/"+cardletId, name);
                URL url = s3Client.generatePresignedUrl(urlRequest);
                uri = new URI(url.toURI().getScheme(), url.toURI().getAuthority(), url.toURI()
                    .getPath(), null, url.toURI()
                    .getFragment());

            } catch (Exception e) {
                log.error("Error occurred while uploading the profile icon file", e);
            }
        }
        return uri;
    }
}

