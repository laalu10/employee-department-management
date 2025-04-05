package com.example.employeemanagement.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.service.EmployeeService;



@RestController
@RequestMapping("/employee")
public class EmployeeController
{

    @Autowired
    private EmployeeService employeeService;
    

    //Create Employee
    @PostMapping("/create")
    public ResponseEntity<?> createEmployee(@RequestBody Employee employee)
    {
        return employeeService.createEmployee(employee);
    }


    //Update Employee
    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updatEmployee(@PathVariable Long id,@RequestBody Employee updatedEmployee) 
    {
        return employeeService.UpadteEmployee(id, updatedEmployee);
    }


    //Update Employee's Department
    @PatchMapping("/{employeeId}/update-department/{departmentId}")
    public ResponseEntity<?> updatEmployeeDepartment (@PathVariable Long employeeId, @PathVariable Long departmentId)
    {
        return employeeService.updatEmployeeDepartment(employeeId,departmentId);
    }

    //Fetch All Employees
    @GetMapping("/getall")
    public ResponseEntity<?> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size )
    {
        Pageable pageable = PageRequest.of(page,size);
        return employeeService.getAllEmployees(pageable);
    }

    // List Employee Name and ID
    @GetMapping("/get")
    public ResponseEntity<?> getEmployeesNameAndId(
        @RequestParam (defaultValue = "false") boolean lookup,
        @RequestParam (defaultValue = "0")int page,
        @RequestParam (defaultValue = "20")int size)
    {
        if(lookup)
        {
            return employeeService.getEmployeesNameAndId();
        }
        else
        {
            Pageable pageable = PageRequest.of(page, size);
            return employeeService.getAllEmployees(pageable);
        }
    }



    //Employee Information
    @GetMapping("/employeeDetails/{id}")
    public ResponseEntity<?> getEmployeeDetails(@PathVariable Long id)
    {
        return employeeService.getEmployeeDetails(id);
    }



    @GetMapping("/{employeeId}/update-department/{departmentId}")
public ResponseEntity<?> testEndpoint(@PathVariable Long employeeId, @PathVariable Long departmentId) {
    return ResponseEntity.ok("Mapping is working!");
}


}