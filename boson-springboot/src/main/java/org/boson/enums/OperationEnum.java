package org.boson.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
    Remove("删除"),
    /**
     * 上传
     */
    Upload("上传");

    private String type;
}
