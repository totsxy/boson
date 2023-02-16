package org.boson.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OperationEnum {
    /**
     * 新增或修改操作
     */
    SaveOrUpdate("新增或修改"),
    /**
     * 新增操作
     */
    Save("新增"),
    /**
     * 修改操作
     */
    Update("修改"),
    /**
     * 删除操作
     */
    Remove("删除"),
    /**
     * 上传操作
     */
    Upload("上传");

    private String type;
}
