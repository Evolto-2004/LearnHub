package xyz.learnhub.common.types;

import lombok.Data;

/**
 *
 * @create 2023/3/24 14:35
 */
@Data
public class UploadFileInfo {
    private String originalName;
    private String extension;
    private long size;
    private String saveName;
    private String resourceType;
    private String savePath;
    private String disk;
}
