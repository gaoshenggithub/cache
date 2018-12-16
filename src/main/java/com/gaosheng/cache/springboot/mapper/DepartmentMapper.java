package com.gaosheng.cache.springboot.mapper;

import com.gaosheng.cache.springboot.bean.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DepartmentMapper {
    @Select("SELECT * FROM department WHERE id = #{id}")
    public Department getDeptById(Integer id);
}
