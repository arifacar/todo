package com.arifacar.service.common;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.*;
import com.arifacar.domain.model.constants.Constants;
import com.arifacar.domain.model.user.User;
import com.arifacar.domain.util.PropertyUtil;
import com.arifacar.service.util.StringUtil;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AwsFileService {

    private final String DOMAIN = "http://image.arifacar.com/";
    private final String BUCKET_NAME = "image.arifacar.com";
    private final String ACCESS_KEY = "ACCESS_KEY_123";
    private final String SECRET_KEY = "SECRET_KEY_1234567890";

    private final PropertyUtil propertyUtil;

    @Autowired
    public AwsFileService(PropertyUtil propertyUtil) {
        this.propertyUtil = propertyUtil;
    }

    public Map<MultipartFile, String> uploadFileList(User currentUser, List<MultipartFile> multipartFileList) {
        Map<MultipartFile, String> imageList = new HashMap<>();

        multipartFileList.forEach(multipartFile -> {
            try {
                String imageUrl = uploadSingleFile(currentUser, multipartFile, Constants.UPLOAD_OTHER);
                imageList.put(multipartFile, imageUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return imageList;
    }

    public String uploadSingleFile(User currentUser, MultipartFile multipartFile, String uploadProfile) throws IOException {
        File file = convertMultiPartToFile(multipartFile);

        Regions clientRegion = Regions.EU_WEST_1;
        String bucketName = getBucketName(currentUser, uploadProfile);
        String path = getPath(currentUser, uploadProfile);

        String extension = FilenameUtils.getExtension(file.getName());


        String keyName = getKeyName(currentUser, uploadProfile) + "." + extension;

        long contentLength = file.length();

        long partSize = 5 * 1024 * 1024; // Set part size to 5 MB. 

        try {

            BasicAWSCredentials creds = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);

            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(clientRegion)
                    .withCredentials(new AWSStaticCredentialsProvider(creds))
                    .build();

            // Create a list of ETag objects. You retrieve ETags for each object part uploaded,
            // then, after each individual part has been uploaded, pass the list of ETags to 
            // the request to complete the upload.
            List<PartETag> partETags = new ArrayList<PartETag>();

            // Initiate the multipart upload.
            InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucketName, keyName)
                    .withObjectMetadata(getObjectMetadata(extension));

            InitiateMultipartUploadResult initResponse = s3Client.initiateMultipartUpload(initRequest);

            // Upload the file parts.
            long filePosition = 0;
            for (int i = 1; filePosition < contentLength; i++) {
                // Because the last part could be less than 5 MB, adjust the part size as needed.
                partSize = Math.min(partSize, (contentLength - filePosition));

                // Create the request to upload a part.
                UploadPartRequest uploadRequest = new UploadPartRequest()
                        .withBucketName(bucketName)
                        .withKey(keyName)
                        .withUploadId(initResponse.getUploadId())
                        .withPartNumber(i)
                        .withFileOffset(filePosition)
                        .withFile(file)
                        .withPartSize(partSize);

                // Upload the part and add the response's ETag to our list.
                UploadPartResult uploadResult = s3Client.uploadPart(uploadRequest);
                partETags.add(uploadResult.getPartETag());

                filePosition += partSize;
            }

            // Complete the multipart upload.
            CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(bucketName, keyName,
                    initResponse.getUploadId(), partETags);

            s3Client.completeMultipartUpload(compRequest);

            return DOMAIN + path + "/" + keyName;
        } catch (AmazonServiceException e) {
            // TODO: Log here
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        } catch (SdkClientException e) {
            // TODO: Log here
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }
        return "";
    }

    public void deleteSingleFile(User currentUser, String url, String uploadProfile) {
        try {
            BasicAWSCredentials creds = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
            Regions clientRegion = Regions.EU_WEST_1;
            String bucketName = BUCKET_NAME;
            String path = getPath(url);

            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(clientRegion)
                    .withCredentials(new AWSStaticCredentialsProvider(creds))
                    .build();
            s3Client.deleteObject(bucketName, path);
        } catch (Exception e) {
            System.out.println("Dosya silinemedi..."); // TODO: Log here instead of message
        }

    }

    private ObjectMetadata getObjectMetadata(String extension) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(getContentType(extension));
        metadata.addUserMetadata(Headers.CONTENT_TYPE, getContentType(extension));
        return metadata;
    }

    private String getContentType(String extension) {
        String ext;
        switch (extension) {
            case "png":
                ext = "png";
                break;
            case "bmp":
                ext = "bmp";
                break;
            case "gif":
                ext = "gif";
                break;
            default:
                ext = "jpeg";
                break;
        }
        return "image/" + ext;
    }

    private String getKeyName(User currentUser, String uploadProfile) {
        if (Constants.UPLOAD_PROFILE.equalsIgnoreCase(uploadProfile)) {
            return currentUser.getUsername() + "_" + StringUtil.generateUniqueFileName();
        } else {
            return StringUtil.generateUniqueFileName();
        }
    }

    private String getBucketName(User currentUser, String uploadProfile) {
        String path = propertyUtil.getApplicationProperty("arifacar.file.upload.path");
        if (Constants.UPLOAD_PROFILE.equalsIgnoreCase(uploadProfile)) {
            return BUCKET_NAME + "/" + path + uploadProfile;
        } else {
            return BUCKET_NAME + "/" + path + uploadProfile + "/" + currentUser.getId();
        }
    }

    private String getPath(User currentUser, String uploadProfile) {
        String path = propertyUtil.getApplicationProperty("arifacar.file.upload.path");
        if (Constants.UPLOAD_PROFILE.equalsIgnoreCase(uploadProfile)) {
            return path + uploadProfile;
        } else {
            return path + uploadProfile + "/" + currentUser.getId();
        }
    }

    private String getPath(String url) {
        return url.replace(DOMAIN, "");
    }

    private File convertMultiPartToFile(MultipartFile multipartFile) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + multipartFile.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(multipartFile.getBytes());
        fos.close();
        return convFile;
    }
}