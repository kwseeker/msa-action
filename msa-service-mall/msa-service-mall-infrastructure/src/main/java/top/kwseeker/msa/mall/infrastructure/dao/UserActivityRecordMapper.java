package top.kwseeker.msa.mall.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.kwseeker.msa.mall.infrastructure.po.UserActivityRecordPO;

/**
 * 用户活动参与记录表 Mapper 接口
 */
@Mapper
public interface UserActivityRecordMapper extends BaseMapper<UserActivityRecordPO> {
}
