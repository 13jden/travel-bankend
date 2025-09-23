package com.dzk.web.api.attraction;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface AttractionImageMapper extends BaseMapper<AttractionImage> {
    @Select("select * from attraction_image where attraction_id = #{id} order by sort ASC")
    List<AttractionImage> selectListByAttractionId(Long id);
}