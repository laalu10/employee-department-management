package com.example.employeemanagement.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.employeemanagement.model.Department;
import com.example.employeemanagement.model.Employee;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee,Long>
{
    boolean existsByDepartment(Department department);
    
    Page <Employee> findAll(Pageable pageable);

    Page <Employee> findByDepartment(Department department,Pageable pageable);
}