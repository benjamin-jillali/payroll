package com.pedantic.entities;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class ParkingSpace extends AbstractEntity{
	
	private String parkingLotNumber;
	
	@OneToOne
	@JoinColumn(name = "EMPLOYEE_ID")
	private Employee employee;

	public String getParkingLotNumber() {
		return parkingLotNumber;
	}

	public void setParkingLotNumber(String parkingLotNumber) {
		this.parkingLotNumber = parkingLotNumber;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	
}
