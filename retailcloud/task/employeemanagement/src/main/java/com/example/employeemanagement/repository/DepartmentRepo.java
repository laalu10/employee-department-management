package com.example.employeemanagement.repository;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.employeemanagement.model.Department;

@Repository
public interface DepartmentRepo extends JpaRepository<Department,Long> 
{
    Page<Department> findAll(Pageable pageable);
}