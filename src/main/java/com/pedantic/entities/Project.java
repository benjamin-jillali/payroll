package com.pedantic.entities;

import java.time.LocalDate;
import java.util.Collection;

import javax.persistence.*;

@Entity
public class Project extends AbstractEntity{
	private String projectName;
	private LocalDate projectStartDate;
	private LocalDate projectEndDate;
	
	@ManyToMany	
	//adds names to the join table table name PROJ_EMPLOYEES fk for Project entity PROJECT_ID
	//fk for Employees entity EMP_ID so table holds keys to each element and the table for the entities
	//stores the key for the jointable key
	@JoinTable(name = "PROJ_EMPLOYEES", joinColumns = @JoinColumn(name = "PROJECT_ID"),
				inverseJoinColumns = @JoinColumn(name = "EMP_ID"))	
	private Collection<Employee> employees;

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public LocalDate getProjectStartDate() {
		return projectStartDate;
	}

	public void setProjectStartDate(LocalDate projectStartDate) {
		this.projectStartDate = projectStartDate;
	}

	public LocalDate getProjectEndDate() {
		return projectEndDate;
	}

	public void setProjectEndDate(LocalDate projectEndDate) {
		this.projectEndDate = projectEndDate;
	}

	public Collection<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(Collection<Employee> employees) {
		this.employees = employees;
	}
	

}
