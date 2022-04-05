package com.pedantic.entities;

import java.math.BigDecimal;

//query constructor expression
public class EmployeeDetails {
	private String fullName;
	private BigDecimal basicSalary;
	private String departmentName;
	public EmployeeDetails(String fullName, BigDecimal basicSalary, String departmentName) {
		super();
		this.fullName = fullName;
		this.basicSalary = basicSalary;
		this.departmentName = departmentName;
	}
	public EmployeeDetails() {
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public BigDecimal getBasicSalary() {
		return basicSalary;
	}
	public void setBasicSalary(BigDecimal basicSalary) {
		this.basicSalary = basicSalary;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	
	
	
}
