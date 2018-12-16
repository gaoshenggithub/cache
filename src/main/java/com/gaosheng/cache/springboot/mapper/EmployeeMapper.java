package com.gaosheng.cache.springboot.mapper;

import com.gaosheng.cache.springboot.bean.Employee;
import org.apache.ibatis.annotations.*;

@Mapper
public interface EmployeeMapper {

    @Insert("INSERT INTO employee(lastName,email,gender,d_id}) values(#{lastName},#{email},#{gender},#{dId})")
    public void insertEmp(Employee employee);

    @Delete("DELETE FROM employee where id = #{id}")
    public void deleteEmp(Integer id );

    @Update("update employee set lastName=#{lastName},email=#{email},gender=#{gender} WHERE id = #{id}")
    public void updateEmp(Employee employee);

    @Select("SELECT * FROM employee WHERE id = #{id}")
    public Employee getEmpById(Integer id);

    @Select("SELECT * FROM employee WHERE lastName = #{lastName}")
    public Employee getEmpByLastName(String lastName);


}
