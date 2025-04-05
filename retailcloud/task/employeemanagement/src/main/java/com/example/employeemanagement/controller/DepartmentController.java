package com.example.employeemanagement.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.employeemanagement.model.Department;
import com.example.employeemanagement.service.DepartmentService;

@RestController
@RequestMapping("/department")
public class DepartmentController
{
    @Autowired
    private DepartmentService departmentService;


    //Add Department
    @PostMapping("/create")
    public ResponseEntity<?> addDepartment(@RequestBody Department department)
    {
        return departmentService.createDepartment(department);
    }


    //Delete Department
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteDepartment(@PathVariable Long id)
    {
        return departmentService.removeDepartment(id);
    }


    //Update Department
    @PatchMapping("/update/{id}")
    public ResponseEntity<?> upadteDepartment(@PathVariable Long id,@RequestBody Department updatedDepartment)
    {
        return departmentService.updateDepartment(id, updatedDepartment);
    }



    //Fetch All Departments
    @GetMapping("/getall")
    public ResponseEntity<?> getAllDepartments(
        @RequestParam(defaultValue = "0")int page,
        @RequestParam(defaultValue = "0")int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        return departmentService.getAllDepartment(pageable);
    }

    //Expand Employees under Departments
    @GetMapping("/expand/{id}")
    public ResponseEntity<?> getDepartment(
        @PathVariable Long id ,
        @RequestParam(required = false) String expand,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        return departmentService.getDepartment(id,expand,pageable);
    }


    
}