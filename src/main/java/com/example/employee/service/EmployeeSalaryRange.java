package com.example.employee.service;

import java.sql.Timestamp;

public class EmployeeSalaryRange {
	
	String title;
	double minSalary;
	double maxSalary;
	Timestamp timestamp;
	
	public EmployeeSalaryRange(){
		
	}
	
	public EmployeeSalaryRange(String title, double minSalary, double maxSalary, Timestamp timestamp) {
		super();
		this.title = title;
		this.minSalary = minSalary;
		this.maxSalary = maxSalary;
		this.timestamp = timestamp;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public double getMinSalary() {
		return minSalary;
	}
	public void setMinSalary(double minSalary) {
		this.minSalary = minSalary;
	}
	public double getMaxSalary() {
		return maxSalary;
	}
	public void setMaxSalary(double maxSalary) {
		this.maxSalary = maxSalary;
	}
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	
	

}
