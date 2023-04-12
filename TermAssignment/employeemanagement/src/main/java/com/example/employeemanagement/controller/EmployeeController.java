package com.example.employeemanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.repository.EmployeeRepository;

@RestController
public class EmployeeController {
	
    @Autowired
    private EmployeeRepository employeeRepository;

    @PostMapping("/employee")
    public Employee saveEmployee(@RequestBody Employee employee) {
        return employeeRepository.save(employee);
    }
    @GetMapping("/getAllEmployee")
    public List<Employee> getAllEmployees() {
        return employeeRepository.getAllEmployees();
    }
    
    @DeleteMapping("/delete/employee/{id}")
    public String deleteEmployee(@PathVariable("id") String employeeId) {
        return  employeeRepository.delete(employeeId);
    }

    @PutMapping("/update/employee/{id}")
    public Employee updateEmployee(@PathVariable("id") String employeeId, @RequestBody Employee employee) {
        return employeeRepository.update(employeeId,employee);
    }
    
    @PostMapping("/notify")
    public String sendNotification(@RequestBody String message) {
    	return employeeRepository.publishMessageTopic(message);
    }

}
