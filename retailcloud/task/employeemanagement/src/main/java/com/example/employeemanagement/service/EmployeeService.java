package com.example.employeemanagement.service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
public class EmployeeService
{
    @Autowired
   private EmployeeRepo employeeRepo;
   @Autowired
   private DepartmentRepo departmentRepo;


   public ResponseEntity<?> createEmployee(Employee employee)
   {
        if(employee.getDepartment() != null && employee.getDepartment().getId() != null)
        {
            Optional<Department> optlDpt = departmentRepo.findById(employee.getDepartment().getId());
            
            if(optlDpt.isEmpty())
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Department Found By this Department ID : "+employee.getDepartment().getId());

            employee.setDepartment(optlDpt.get());
        }
        else
        {
            employee.setDepartment(null);
        }

        if(employee.getReportingManager() != null && employee.getReportingManager().getId() != null)
        {
            Optional<Employee> optlEmp = employeeRepo.findById(employee.getReportingManager().getId());

            if(optlEmp.isEmpty())
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Employee Found By this ReportingManager ID : "+employee.getReportingManager().getId());
            
            employee.setReportingManager(optlEmp.get());
        }
        else
        {
            employee.setReportingManager(null);
        }
        Employee createdEmployee = employeeRepo.save(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
   }


   public ResponseEntity<?> UpadteEmployee(Long id,Employee updatedEmployee)
   {
        Optional<Employee> optionalEmployee = employeeRepo.findById(id);

        if(optionalEmployee.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee Not Found With ID: " + id);

        Employee employee = optionalEmployee.get();

        if(updatedEmployee.getName() != null)
            employee.setName(updatedEmployee.getName());

        if(updatedEmployee.getDateOfBirth() != null)
            employee.setDateOfBirth(updatedEmployee.getDateOfBirth());

        if(updatedEmployee.getSalary() != null)
            employee.setSalary(updatedEmployee.getSalary());

        if(updatedEmployee.getAddress() != null)
            employee.setAddress(updatedEmployee.getAddress());

        if(updatedEmployee.getJoiningDate() != null)
            employee.setJoiningDate(updatedEmployee.getJoiningDate());

        if(updatedEmployee.getRole() != null)
            employee.setRole(updatedEmployee.getRole());

        if(updatedEmployee.getYearlyBonusPercentage() > 0)
            employee.setYearlyBonusPercentage(updatedEmployee.getYearlyBonusPercentage());


        if(updatedEmployee.getReportingManager() != null && updatedEmployee.getReportingManager().getId() != null)
        {
            Optional<Employee> employeeoptl = employeeRepo.findById(updatedEmployee.getReportingManager().getId());
           
            if(employeeoptl.isEmpty())
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee Not Found With ID: " +updatedEmployee.getReportingManager().getId() +" For Changing Reporting Manager");

            employee.setReportingManager(employeeoptl.get());
        }
            
       
        Employee savedEmployee = employeeRepo.save(employee);

        return ResponseEntity.ok(savedEmployee);
    }





    public ResponseEntity<?> updatEmployeeDepartment(Long employeeId,Long newDepartmentId)
    {
        Optional<Employee> optionalEmployee = employeeRepo.findById(employeeId);

        if(optionalEmployee.isEmpty())
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee Not Found With This Id : "+employeeId);


        Optional<Department> optionalDepartment = departmentRepo.findById(newDepartmentId);

        if(optionalDepartment.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Department Not Found With This Id : "+newDepartmentId);


        Employee employee = optionalEmployee.get();
        Department newDepartment = optionalDepartment.get();

        employee.setDepartment(newDepartment);
        employeeRepo.save(employee);

        return ResponseEntity.status(HttpStatus.OK)
                .body("Employee " + employee.getName() + " moved to department " + newDepartment.getName() + " successfully.");
    }




    public ResponseEntity<?> getAllEmployees(Pageable pageable)
    {
        Page<Employee> employeePage= employeeRepo.findAll(pageable);

        if(employeePage.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No Employees Found");

        Map<String,Object> response = new HashMap<>();
        response.put("employees ",employeePage.getContent());
        response.put("currentpage ",employeePage.getNumber());
        response.put("totalpages", employeePage.getTotalPages());
        response.put("totalemployees", employeePage.getTotalElements());

        return ResponseEntity.ok(response);
    }




    public ResponseEntity<?> getEmployeesNameAndId()
    {
        List<Employee> employees = employeeRepo.findAll();

        if(employees.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        List<Map<String,Object>> employeeList =employees.stream()
            .map(emp ->{
                Map<String,Object> empData = new HashMap<>();
                empData.put("id",emp.getId());
                empData.put("name", emp.getName());
                return empData;
            })
            .collect(Collectors.toList());

        return ResponseEntity.ok(employeeList);

    }





    public ResponseEntity<?> getEmployeeDetails(Long id)
    {
        Optional <Employee> optionalEmployee = employeeRepo.findById(id);

        if(optionalEmployee.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee Not Found With This Id : "+id);

        Employee employee = optionalEmployee.get();

        Map<String,Object> employeeDetails = new HashMap<>();
        employeeDetails.put("id", id);
        employeeDetails.put("name", employee.getName());
        employeeDetails.put("dateOfBirth", employee.getDateOfBirth());
        employeeDetails.put("address", employee.getAddress());
        employeeDetails.put("role" ,employee.getRole());
        employeeDetails.put("salary", employee.getSalary());
        employeeDetails.put("joiningDate", employee.getJoiningDate());
        employeeDetails.put("yearlybonus",employee.getYearlyBonusPercentage());

        if(employee.getDepartment() != null)
        {
            employeeDetails.put("department",employee.getDepartment());
        }

        if(employee.getReportingManager() != null)
        {
            Map<String,Object> reportingManager = new HashMap<>();

            reportingManager.put("id", employee.getReportingManager().getId());
            reportingManager.put("name", employee.getReportingManager().getName());


            employeeDetails.put("reportingManager",reportingManager);
        }


        return ResponseEntity.ok(employeeDetails);


    }


}