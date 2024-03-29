package top.kwseeker.msa.mall.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import top.kwseeker.msa.mall.infrastructure.po.UserActivityRecordPO;
import top.kwseeker.msa.mybatis.core.mapper.BaseMapperX;

/**
 * 用户活动参与记录表 Mapper 接口
 */
@Mapper
public interface UserActivityRecordMapper extends BaseMapperX<UserActivityRecordPO> {
}
