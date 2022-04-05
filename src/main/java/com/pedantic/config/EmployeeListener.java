package com.pedantic.config;

import java.time.LocalDate;
import java.time.Period;

import javax.persistence.PrePersist;

import com.pedantic.entities.Employee;

public class EmployeeListener {
	
	@PrePersist
	public void calculateEmployeeAge(Employee employee) {
		employee.setAge(Period.between(employee.getDateOfBirth(), LocalDate.now()).getYears());
		
	}

}
