package com.example.employeemanagement.service;



import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.employeemanagement.model.Department;
import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.repository.DepartmentRepo;
import com.example.employeemanagement.repository.EmployeeRepo;

@Service
public class DepartmentService
{
    @Autowired
    private DepartmentRepo departmentRepo;
    @Autowired
    private EmployeeRepo employeeRepo;

    public ResponseEntity<?> createDepartment(Department department)
    {
        if(department.getDepartmentHead() != null && department.getDepartmentHead().getId() != null)
        {
            Optional<Employee> dpHead = employeeRepo.findById(department.getDepartmentHead().getId());
           
            if(dpHead.isEmpty())
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NO Employee Found For Set Departmenthead By This Id :"+department.getDepartmentHead().getId());

            department.setDepartmentHead(dpHead.get());
        }
        else
        {
            department.setDepartmentHead(null);
        }
        Department savedDepartment = departmentRepo.save(department);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDepartment);
    }



    public ResponseEntity<String> removeDepartment(Long id)
    {
        Optional<Department> optionalDepartment = departmentRepo.findById(id);

        if(optionalDepartment.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Department Not Found With This Id : "+id);

        Department department = optionalDepartment.get();

        if(employeeRepo.existsByDepartment(department))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot delete department: Employees are assigned in the department");

        departmentRepo.delete(department);
        return ResponseEntity.status(HttpStatus.OK).body(department.getName()+" Successfully Deleted !");

    }




    public ResponseEntity<?> updateDepartment(Long id,Department updatedDepartment)
    {
        Optional<Department> OptionalDepartment = departmentRepo.findById(id);
        
        if(OptionalDepartment.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Department Not Find With Id : "+id);

        Department department = OptionalDepartment.get();

        if(updatedDepartment.getName() != null)
            department.setName(updatedDepartment.getName());

        if(updatedDepartment.getCreationDate() != null)
            department.setCreationDate(updatedDepartment.getCreationDate());

        Department savedDepartment = departmentRepo.save(department);
        return ResponseEntity.status(HttpStatus.OK).body(savedDepartment);
    }


    public ResponseEntity<?> getAllDepartment(Pageable pageable)
    {
        Page <Department> departmentPage = departmentRepo.findAll(pageable);

        if(departmentPage.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No employees found.");

        Map<String,Object> response = new HashMap<>();
        response.put("Departments", departmentPage.getContent());
        response.put("currentPage", departmentPage.getNumber());
        response.put("totalPages",departmentPage.getTotalPages());
        response.put("totalDepartments", departmentPage.getTotalElements());

        return ResponseEntity.ok(response);
    }


    public ResponseEntity<?> getDepartment(Long id,String Expand,Pageable pageable)
    {
       Optional <Department> optionalDepartment = departmentRepo.findById(id);

       if(optionalDepartment.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Department Not Found With This Id : "+id);

        Department department = optionalDepartment.get();
        Map<String,Object> response = new HashMap<>();
        response.put("id", department.getId());
        response.put("name",department.getName());
        response.put("creationDate",department.getCreationDate());
        response.put("departmentHead",department.getDepartmentHead());

        if("employee".equalsIgnoreCase(Expand))
        {
            Page <Employee> employeePage = employeeRepo.findByDepartment(department,pageable);
            
            if(employeePage.isEmpty())
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Employee Found By The Department : "+department.getName());

            response.put("employees", employeePage.getContent());
            response.put("currentPage", employeePage.getNumber());
            response.put("totalPage", employeePage.getTotalPages());
            response.put("totalEmployees", employeePage.getTotalElements());

           
        }
        return ResponseEntity.ok(response);
    }



}