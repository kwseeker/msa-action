package top.kwseeker.msa.mall.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import top.kwseeker.msa.mall.infrastructure.po.ItemPO;
import top.kwseeker.msa.mybatis.core.mapper.BaseMapperX;

/**
 * 物品表 Mapper 接口
 */
@Mapper
public interface ItemMapper extends BaseMapperX<ItemPO> {
}