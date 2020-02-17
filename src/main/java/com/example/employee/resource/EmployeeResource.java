package com.example.employee.resource;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.employee.Employee;
import com.example.employee.service.EmployeeSalaryRange;
import com.example.employee.service.EmployeeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value="Employee Application",  description="Operations pertaining to employee")
@RestController
@RequestMapping(path = "/")
public class EmployeeResource {

	private final static Logger log = LoggerFactory.getLogger(EmployeeResource.class);

    @Autowired
    private EmployeeService employeeService;

    @ApiOperation(value = "Update employee salary by percentage and return all employees", response = List.class)
    @PutMapping("/employee/place/{place}/salary/{percentage}")
    public List<Employee> updateEmployeesByPlace(@PathVariable String place,  @PathVariable double percentage) {
        log.info("call employeeService to update salary by place ", place);
        return employeeService.updateEmployeesByPlace(place,  percentage);
    }

    @ApiOperation(value = "View nested list of supervisees of a given supervisor", response = List.class)
    @GetMapping("/employee/supervisor/{supervisorId}")
    public List<Employee> getEmployeesBySupervisor(@PathVariable String supervisorId) {
        log.info("call employeeService to get all supervisee for ", supervisorId);
        return employeeService.getEmployeesBySupervisor(supervisorId);
    }
    
    @ApiOperation(value = "View Total salary for a : BU/Supervisor/Place", response = Double.class)
    @GetMapping("/employee/totalSalary/{group}/{groupValue}")
    public double getTotalSalary(@PathVariable String group, @PathVariable String  groupValue) {
        log.info("call employeeService to total salary for ", group);
        return employeeService.getTotalSalary(group, groupValue);
    }
    
    @ApiOperation(value = "View all employees for a given place", response = List.class)
    @GetMapping("/employee/place/{place}")
    public  List<Employee> getEmployeesByPlace(@PathVariable String place) {
        log.info("call employeeService to get all employees for "+ place);
        return employeeService.getEmployeesByPlace(place);
    }
    
    @ApiOperation(value = "View range of salaries for a given title", response = EmployeeSalaryRange.class)
    @GetMapping("/employee/salaryRangeByTitle/{title}")
    public EmployeeSalaryRange getEmployeeSalaryRangeByTitle(@PathVariable String title) {
        log.info("call employeeService to get salary range by title ", title);
        return employeeService.getEmployeeSalaryRangeByTitle(title);
    }    
}
