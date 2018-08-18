package com.evanhayes.evanhayes.models.Images;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

public class fileImages {
    public static BufferedImage autoRotate(MultipartFile image) throws ImageProcessingException, IOException {
        File convFile = new File(image.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(image.getBytes());
        fos.close();

        BufferedImage src = ImageIO.read(new ByteArrayInputStream(image.getBytes()));

        //get metadata, specifically orientation
        Metadata metadata = ImageMetadataReader.readMetadata(convFile);
        ExifIFD0Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);

        int orientation = 1;
        try {
            orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        int width = src.getWidth();
        int height = src.getHeight();
        if (orientation == 3 || orientation == 6 || orientation == 8) {
            AffineTransform affineTransform = RotateImage.getTransform(orientation, width, height);
            AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BILINEAR);
            BufferedImage destinationImage = new BufferedImage(height, width, src.getType());
            destinationImage = affineTransformOp.filter(src, destinationImage);
            return destinationImage;
        } else {
            return src;
        }
    }
    public static String uploadImage(BufferedImage image, String fileName, String ext) {
        String bucketName = "evanhayesimages";

        try {
//            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
//                    .withRegion(clientRegion)
//                    .withCredentials(new ProfileCredentialsProvider())
//                    .build();
            AmazonS3 s3Client = s3client.s3();

            String path = "images/" + fileName;

            ByteArrayOutputStream outstream = new ByteArrayOutputStream();
            ImageIO.write(image, ext, outstream);
            byte[] buffer = outstream.toByteArray();
            InputStream is = new ByteArrayInputStream(buffer);
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentType("image/" + ext);
            meta.setContentLength(buffer.length);
            s3Client.putObject(new PutObjectRequest(bucketName, path, is, meta).withCannedAcl(CannedAccessControlList.PublicRead));
            outstream.flush();
        }
        catch(AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        }
        catch(SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //http(s)://s3.amazonaws.com/<bucket>/<object>
        String fullPath = "https://" + bucketName + ".s3.amazonaws.com/images/" + fileName;
        return fullPath;
    }
}