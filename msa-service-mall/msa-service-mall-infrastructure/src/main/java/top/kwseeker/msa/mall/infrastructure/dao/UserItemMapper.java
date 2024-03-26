package top.kwseeker.msa.mall.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import top.kwseeker.msa.mall.infrastructure.po.UserItemPO;
import top.kwseeker.msa.mybatis.core.mapper.BaseMapperX;

/**
 * 用户物品表 Mapper 接口
 */
@Mapper
public interface UserItemMapper extends BaseMapperX<UserItemPO> {
}
