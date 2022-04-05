package com.pedantic.service;

import java.util.Map;

import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import org.apache.shiro.SecurityUtils;
import com.pedantic.entities.ApplicationUser;
import com.pedantic.entities.Department;
import com.pedantic.entities.Employee;
import com.pedantic.entities.ParkingSpace;


@DataSourceDefinition(
        name = "java:app/Payroll/MyDS",
        className = "org.apache.derby.jdbc.ClientDriver",
        url = "jdbc:derby://localhost:1527/payroll",
        user = "appuser",
        password = "password")
@Stateless
public class PersistenceService {
	
	@Inject
	QueryService queryService;

    @Inject
    EntityManager entityManager;
    
    @Inject
    SecurityUtil securityUtil;
    
    //default transaction type is "required"    
    public Department saveDepartment(Department department) {
    	//persist is for entities that dont already exist
    	entityManager.persist(department);
    	return department; 
    }
    
    public void removeParkingSpace(Long employeeId) {
    	//employee and parking space have a 1to1 relationship
    	//in order to remove a parking space first get the employee
    	//get the parking space from employee
    	//important to make employe parking space null otherwise will recieve an error
    	//then use .remove on parkingSpace
    	Employee employee = queryService.findEmployeeById(employeeId);
    	ParkingSpace parkingSpace = employee.getParkingSpace();  
    	
    	employee.setParkingSpace(null);
    	entityManager.remove(parkingSpace);
    }
    //since we cascade persist on employee so parking space will also be persisted with cascade.persist
    public void saveEmployee(Employee employee, ParkingSpace parkingSpace) {
    	employee.setParkingSpace(parkingSpace);
    	entityManager.persist(employee);
    }
    public void saveEmployee(Employee employee) {
    	if(employee.getId() == null) {
    		entityManager.persist(employee);
    	}else {
    		entityManager.merge(employee);
    	}
    }
    //Never persist a plaintext password of user in a database or stored anywhere in application
    public void saveUser(ApplicationUser applicationUser) {
    	//security util will hash the password pass the plaintext password then hash
    	//hash method returns hash password and a salt
    	//we need the salt to be able to compare the stored hashedpassword and client password
    	Map<String, String> credMap = securityUtil.hashPassword(applicationUser.getPassword());
    	//set the hashed password and the salt then we persist user to db 
    	applicationUser.setPassword(credMap.get("hashedPassword"));
    	applicationUser.setSalt(credMap.get("salt"));
    	if(applicationUser.getId() == null) {
    		entityManager.persist(applicationUser);
    	}else {
    		entityManager.merge(applicationUser);
    	}
    }
    //this method will allow merging of an undetached department entity
    public void updateDepartment(Department department) {
    	entityManager.merge(department);
    }
}











