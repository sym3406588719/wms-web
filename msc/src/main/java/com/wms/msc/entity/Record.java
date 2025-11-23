package com.wms.msc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serial;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Record对象", description="商品出入库记录表")
public class Record implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "货物ID（关联商品表）")
    @TableField("goods_id") // 数据库实际字段是 goods_id，这里注解对齐
    private Integer goodsId;

    @ApiModelProperty(value = "取货人/补货人ID")
    @TableField("userId") // 数据库是 userId（注意大小写，若数据库是 userid 小写，这里要改成 @TableField("userid")）
    private Integer userId;

    @ApiModelProperty(value = "操作人ID")
    @TableField("admin_id") // 数据库实际字段 admin_id
    private Integer adminId;

    @ApiModelProperty(value = "数量（正数为入库，负数为出库）")
    private Integer count;

    @ApiModelProperty(value = "操作时间（默认当前时间）")
    @TableField("create_time") // 数据库实际字段 create_time
    private LocalDateTime createTime;

    @ApiModelProperty(value = "备注（允许为空）")
    private String remark;
}
