package org.boson.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 操作类型枚举
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Getter
@AllArgsConstructor
public enum OperationEnum {
    /**
     * 新增或修改
     */
    SaveOrUpdate("新增或修改"),
    /**
     * 新增
     */
    Save("新增"),
    /**
     * 修改
     */
    Update("修改"),
    /**
     * 删除
     */
    Delete("删除"),
    /**
     * 上传
     */
    Upload("上传");

    private String type;
}
