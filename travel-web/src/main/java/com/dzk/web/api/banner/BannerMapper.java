package com.dzk.web.api.banner;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dzk
 * @since 2025-04-18
 */
@Mapper

public interface BannerMapper extends BaseMapper<Banner> {
    @Select("SELECT MAX(sort) FROM banner")
    Integer getMaxSort();

    /**
     * 更新Banner排序
     * @param bannerId Banner ID
     * @param sort 排序值
     * @return 影响行数
     */
    @Update("UPDATE banner SET sort = #{sort} WHERE id = #{bannerId}")
    int updateSort(@Param("bannerId") Integer bannerId, @Param("sort") Integer sort);

    /**
     * 删除Banner后更新其他Banner的排序值
     * @param deletedSort 被删除的Banner的排序值
     * @return 是否更新成功
     */
    @Update("UPDATE banner SET sort = sort - 1 WHERE sort > #{deletedSort}")
    boolean updateSortAfterDelete(@Param("deletedSort") Integer deletedSort);

    /**
     * 获取Banner列表，按sort排序
     * @return Banner列表
     */
    @Select("SELECT id, image, type,is_active, text, content_id, sort FROM banner ORDER BY sort ASC")
    List<Banner> selectList();

    int update(Banner banner);
}
