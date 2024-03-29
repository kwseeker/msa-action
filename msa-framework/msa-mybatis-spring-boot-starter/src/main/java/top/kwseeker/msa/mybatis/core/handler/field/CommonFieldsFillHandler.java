package top.kwseeker.msa.mybatis.core.handler.field;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import top.kwseeker.msa.mybatis.core.po.TimeBasePO;

import java.time.LocalDateTime;

/**
 * 通用字段（所有表都包含的一些公用字段）填充处理器
 * 详细参考: <a href="https://baomidou.com/pages/4c6bcf/">自动填充功能</a>
 */
public class CommonFieldsFillHandler implements MetaObjectHandler {

    /**
     * 插入时填充
     * 我们的通用字段都是通过 BasePO定义的，所以这里
     * 需要先判断原始参数对象是否是BasePO类型，是的话，再判断各个通用字段是否为空，是则填充
     * @param metaObject 原SQL参数的元对象，包括原始的参数对象 originalObject，还包括 MetaClass（比如UserPO的元数据信息，包括一些注解信息）、
     *                   MetaObject（指向外部的指针）
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        if (metaObject == null || !(metaObject.getOriginalObject() instanceof TimeBasePO)) {
            return;
        }
        //creator还是通过传参传吧，也可以使用ThreadLocal但是小心异步执行器
        LocalDateTime current = LocalDateTime.now();
        TimeBasePO timeBasePO = (TimeBasePO) metaObject.getOriginalObject();
        if (timeBasePO.getCreateTime() == null) {
            timeBasePO.setCreateTime(current);
        }
        if (timeBasePO.getUpdateTime() == null) {
            timeBasePO.setUpdateTime(current);
        }
    }

    /**
     * 更新时填充
     * @param metaObject 原SQL参数的元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        strictUpdateFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
    }
}
