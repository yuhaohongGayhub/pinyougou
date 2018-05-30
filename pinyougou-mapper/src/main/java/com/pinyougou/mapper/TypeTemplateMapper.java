package com.pinyougou.mapper;


import com.pinyougou.pojo.TypeTemplate;

import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface TypeTemplateMapper extends Mapper<TypeTemplate> {

    List<TypeTemplate> findAll(TypeTemplate typeTemplate);

    @Select("SELECT id, name FROM tb_type_template")
    List<Map<String,String>> finTypeTemplateIdAndName();
}