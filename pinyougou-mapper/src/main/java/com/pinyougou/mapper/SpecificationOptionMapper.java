package com.pinyougou.mapper;

import com.pinyougou.pojo.Specification;
import com.pinyougou.pojo.SpecificationOption;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SpecificationOptionMapper  extends Mapper<SpecificationOption>{


    void save(Specification specification);

    List<SpecificationOption> findAllBySpecId(Long id);

    void deleteBySpecId(Long id);
}