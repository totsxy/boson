package org.boson.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文件扩展名枚举
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Getter
@AllArgsConstructor
public enum FileExtEnum {
    /**
     * jpg文件
     */
    JPG(".jpg", "jpg文件"),
    /**
     * png文件
     */
    PNG(".png", "png文件"),
    /**
     * Jpeg文件
     */
    JPEG(".jpeg", "jpeg文件"),
    /**
     * wav文件
     */
    WAV(".wav", "wav文件"),
    /**
     * md文件
     */
    MD(".md", "markdown文件"),
    /**
     * txt文件
     */
    TXT(".txt", "txt文件");

    /**
     * 获取文件格式枚举
     *
     * @param extName 扩展名
     * @return {@link FileExtEnum} 文件格式枚举
     */
    public static FileExtEnum getFileExt(String extName) {
        for (FileExtEnum value : FileExtEnum.values()) {
            if (value.getExt().equalsIgnoreCase(extName)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 扩展名
     */
    private final String ext;

    /**
     * 描述
     */
    private final String desc;

}
