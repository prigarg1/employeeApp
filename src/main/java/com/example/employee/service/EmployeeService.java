package com.example.employee.service;

import java.util.List;

import com.example.employee.Employee;

public interface EmployeeService {

	void init();
	
	List<Employee> updateEmployeesByPlace(String place, double percentage);
		
	List<Employee> getEmployeesBySupervisor(String supervisorId);
	
	double getTotalSalary(String group, String groupValue);
	
	List<Employee> getEmployeesByPlace(String place);
	
	EmployeeSalaryRange getEmployeeSalaryRangeByTitle(String title);

}
