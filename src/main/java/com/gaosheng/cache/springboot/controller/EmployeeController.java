package com.gaosheng.cache.springboot.controller;

import com.gaosheng.cache.springboot.bean.Employee;
import com.gaosheng.cache.springboot.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @GetMapping("/emp/{id}")
    public Employee employee(@PathVariable("id")Integer id){
        Employee employee = employeeService.getEmployee(id);
        return employee;
    }

    @GetMapping("/emp")
    public Employee udpate(Employee employee){
        Employee emp = employeeService.updateEmployee(employee);
        return emp;
    }


    @GetMapping("/delemp")
    public String delete(Integer id){
        employeeService.delEmployee(id);
        return "success";
    }

    @GetMapping("/emp/lastName/{lastName}")
    public Employee getEmpByLastName(@PathVariable("lastName") String lastName){
        Employee employee = employeeService.getEmployeeByLastName(lastName);
        return employee;
    }
}
