package com.wms.msc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 商品表
 * </p>
 *
 * @author wms
 * @since 2025-10-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Goods对象", description="商品表")
public class Goods implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "货名")
    private String name;

    @ApiModelProperty(value = "仓库（关联storage表id）")
    private Integer storage;

    @ApiModelProperty(value = "分类（关联goodsType表id）")
    @TableField("goodsType") // 显式指定数据库列名与实体字段一致
    private Integer goodsType;

    @ApiModelProperty(value = "数量（默认0，避免空值）")
    private Integer count;

    @ApiModelProperty(value = "备注（允许为空，灵活适配业务）")
    private String remark;


}
