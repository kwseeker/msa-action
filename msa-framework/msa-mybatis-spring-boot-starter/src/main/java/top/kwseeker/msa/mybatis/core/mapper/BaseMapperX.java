package top.kwseeker.msa.mybatis.core.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;

import java.util.List;

/**
 * BaseMapper 拓展，存放通用的拓展方法
 */
public interface BaseMapperX<T> extends BaseMapper<T> {

    default boolean save(T entity) {
        return SqlHelper.retBool(insert(entity));
    }

    default T selectOne(SFunction<T, ?> field, Object value) {
        return selectOne(new LambdaQueryWrapper<T>().eq(field, value));
    }

    default List<T> selectList(SFunction<T, ?> field, Object value) {
        return selectList(new LambdaQueryWrapper<T>().eq(field, value));
    }

    default List<T> selectListByMulti(SFunction<T, ?> field, List<?> values) {
        return selectList(new LambdaQueryWrapper<T>().in(field, values));
    }
}
