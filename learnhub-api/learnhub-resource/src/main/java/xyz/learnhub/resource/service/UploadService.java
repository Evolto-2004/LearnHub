package xyz.learnhub.resource.service;

import org.springframework.web.multipart.MultipartFile;
import xyz.learnhub.common.exception.ServiceException;
import xyz.learnhub.common.types.UploadFileInfo;
import xyz.learnhub.common.types.config.S3Config;
import xyz.learnhub.resource.domain.Resource;

/**
 *
 * @create 2023/3/8 14:02
 */
public interface UploadService {

    UploadFileInfo upload(S3Config s3Config, MultipartFile file, String dir)
            throws ServiceException;

    Resource storeBase64Image(
            S3Config s3Config, Integer adminId, String content, String categoryIds)
            throws ServiceException;
}
