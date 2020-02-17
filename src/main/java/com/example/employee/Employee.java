package com.example.employee;

import java.io.Serializable;

public class Employee implements Serializable{

	private String employeeID ;
	
	private String employeeName ;
	
	private String title ;
	
	private String businessUnit ;
	
	private String place ;
	
	private String supervisorID ;
	
	private String competencies ;
	
	private double salary ;

		
	public Employee() {
		
	}

	public Employee(String employeeID, String employeeName, String title, String businessUnit, String place,
			String supervisorID, String competencies, double salary) {
		this.employeeID = employeeID;
		this.employeeName = employeeName;
		this.title = title;
		this.businessUnit = businessUnit;
		this.place = place;
		this.supervisorID = supervisorID;
		this.competencies = competencies;
		this.salary = salary;
	}

	public String getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(String employeeID) {
		this.employeeID = employeeID;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getSupervisorID() {
		return supervisorID;
	}

	public void setSupervisorID(String supervisorID) {
		this.supervisorID = supervisorID;
	}

	public String getCompetencies() {
		return competencies;
	}

	public void setCompetencies(String competencies) {
		this.competencies = competencies;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public void salaryIncrement(double d){
		salary = salary + salary*d/100;
				
	}
	@Override
	public String toString() {
		return "EmployeeDTO [employeeID=" + employeeID + ", employeeName=" + employeeName + ", title=" + title
				+ ", businessUnit=" + businessUnit + ", place=" + place + ", supervisorID=" + supervisorID
				+ ", competencies=" + competencies + ", salary=" + salary + "]";
	}

		
}
