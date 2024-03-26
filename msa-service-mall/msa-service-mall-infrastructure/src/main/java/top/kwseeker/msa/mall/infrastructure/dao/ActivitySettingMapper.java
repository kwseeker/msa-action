package top.kwseeker.msa.mall.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import top.kwseeker.msa.mall.infrastructure.po.ActivitySettingPO;
import top.kwseeker.msa.mybatis.core.mapper.BaseMapperX;

/**
 * 活动配置表 Mapper 接口
 */
@Mapper
public interface ActivitySettingMapper extends BaseMapperX<ActivitySettingPO> {
}
