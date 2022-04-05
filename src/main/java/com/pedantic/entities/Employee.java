/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pedantic.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.ejb.Local;
import javax.json.bind.annotation.JsonbDateFormat;
import javax.json.bind.annotation.JsonbPropertyOrder;
import javax.json.bind.annotation.JsonbTransient;
import javax.json.bind.config.PropertyOrderStrategy;
import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

import com.pedantic.config.AbstractEntityListener;
import com.pedantic.config.EmployeeListener;

/**
 * @author Seeraj
 */
@Entity

//named natve query
//this creates a sql query not an sql query
@NamedNativeQuery(name = "Employee.findAllNativeNamed", query = "select * from Employee", resultClass = Employee.class)
//join is quering accross an entity relationship
//@NamedQuery(name = "", query = "select al from Employee e join e.employeeAllowances al")

//WHERE clause
//the ?1 is a positional parameter so for multiple you can say ?1, ?2, ?3 etc
//@NamedQuery(name = "", query = "select al from Employee e join e.employeeAllowances al where al.allowanceAmount > ?1")
//WHERE with named paramter
@NamedQuery(name = Employee.GET_EMPLOYEE_ALLOWANCES_GREATER, query = "select al from Employee e join e.employeeAllowances al where al.allowanceAmount > :greaterThanValue")

// between clause
@NamedQuery(name = Employee.EMPLOYEE_SALARY_BOUND, query = "select e from Employee e where e.basicSalary  between :lowerBound and :upperBound")
//phone numbers are a map so we put the key and value to specify this is how you query over maps
//otherwise it will defualt to the values
@NamedQuery(name = Employee.EMPLOYEE_PHONE_NUMBER_MAP, query = "select e, KEY(p), VALUE(p) from Employee e join e.employeePhoneNumbers p")
//when accessing a map from an element that is lazily fetched outside of its persistency context it will be detached so we use a join query to get the data
//we dont need to set a alias for allowances it will initialize the collection with an employee instance
@NamedQuery(name = Employee.GET_EMPLOYEE_ALLOWANCES, query = "select e from Employee e join fetch e.employeeAllowances")
@NamedQuery(name = Employee.FIND_BY_ID, query = "select e from Employee e where e.id = :id and e.userEmail = :email")
@NamedQuery(name = Employee.FIND_BY_NAME, query = "select e from Employee e where e.fullName = :name and e.userEmail = :email")
@NamedQuery(name = Employee.LIST_EMPLOYEES, query = "select  e from Employee e where e.userEmail = :email order by e.fullName")
@NamedQuery(name = Employee.FIND_PAST_PAYSLIP_BY_ID, query = "select p from Employee e join e.pastPayslips p where e.id = :employeeId and e.userEmail =:email and p.id =:payslipId and p.userEmail = :email")
@NamedQuery(name = Employee.GET_PAST_PAYSLIPS, query = "select p from Employee e inner join e.pastPayslips p where e.id = :employeeId and e.userEmail=:email")
//the last type of the query navigation will be what is returned 
//you can continue to navigate in a query until reacing a simple java type aka string int etc.
//@NamedQuery(name = Employee.GET_ALL_PARKING_SPACES, query = "select e.parkingSpace.parkingLotNumber from Employee e")
@NamedQuery(name = Employee.GET_ALL_PARKING_SPACES, query = "select e.parkingSpace from Employee e")
//a combined expression allows to request multiple fields in the query
//since there are 2 types what will be returned from the query will be a collection array of type object
@NamedQuery(name = Employee.EMPLOYEE_PROJECTION, query = "select e.fullName, e.basicSalary from Employee e")
//constructor instruction use a class to create the query with the fields instead of arrays
//then the database will instantiate a new object for each row in the database
@NamedQuery(name = Employee.EMPLOYEE_CONSTRUCTOR_PROJECTION, query = "select new com.pedantic.entities.EmployeeDetails(e.fullName, e.basicSalary, e.department.departmentName) from Employee e")
//@Table(name = "Employee", schema = "HR")

//to link this class with the entity listeners we use the following annotation
//this takes an array of listeners
@EntityListeners({EmployeeListener.class, AbstractEntityListener.class})
//we can customize property order with jsonb api
@JsonbPropertyOrder(PropertyOrderStrategy.REVERSE)
public class Employee extends AbstractEntity {
//public class Employee {
	
	
//	@TableGenerator(name = "Emp_Gen", table = "ID_GEN", pkColumnName = "GEN_NAME", valueColumnName = "pl_value")
//	@GeneratedValue(generator = "Emp_Gen")
//	@Id
//	private Long id;
	
	public static final String GET_EMPLOYEE_ALLOWANCES_GREATER = "Employee.getAllowancesGreaterThen";
	public static final String GET_EMPLOYEE_ALLOWANCES = "Employee.getAllowances";
	public static final String EMPLOYEE_SALARY_BOUND = "Employee.SalaryBound";
	public static final String FIND_BY_ID = "Employee.findById";
	public static final String FIND_BY_NAME = "Employee.findByName";
	public static final String LIST_EMPLOYEES = "Employee.listEmployees";
	public static final String FIND_PAST_PAYSLIP_BY_ID = "Employee.findPastPayslipById";
	public static final String GET_PAST_PAYSLIPS = "Employee.getPastPayslips";
	public static final String GET_ALL_PARKING_SPACES = "Employee.getAllParkingSpaces";
	public static final String EMPLOYEE_PROJECTION = "Employee.nameAndSalaryProjection";
	public static final String EMPLOYEE_CONSTRUCTOR_PROJECTION = "Employee.projection";
	public static final String EMPLOYEE_PHONE_NUMBER_MAP = "Employee.phoneNumberMap";

	@NotEmpty(message = "Name cannot be empty")
	@Size(max = 40, message = "Full name must be less then 40 characters")
	@Basic
	private String fullName;
	@NotEmpty(message = "Social security number must be set")
	private String socialSecurityNumber;
//@Past means it must be a date or time in the past
	@NotNull(message = "Date of birth must be set.")
	@Past(message = "Date of birth must be in the past")
	//jsonb is from jsonb api to customize the format how we want the representation
	@JsonbDateFormat(value = "yyyy-MM-dd")
	private LocalDate dateOfBirth; // yyyy-MM-dd
//NotNull and NotEmpty NotNull is older method
	@DecimalMin(value = "500", message = "basic salary must equal to or exceed 500")
	@NotNull(message = "Basic salary must be set")
	private BigDecimal basicSalary;

	@NotNull(message = "Hired date must be set")
	@JsonbDateFormat(value = "yyyy-MM-dd")
	//past or present means date must be in past or present
	@PastOrPresent(message = "Hired date must be in the past or present")
	private LocalDate hiredDate;

	@ManyToOne
	private Employee reportsTo;

	@OneToMany
	@JsonbTransient
	//set does not allow duplicate entries
	private Set<Employee> subordinates = new HashSet<>();

//sets enum type to string in database
	@Enumerated(EnumType.STRING)
	private EmploymentType employmentType;

	@Embedded
	private Address address;
	
	//collection is most generic interface type
	@ElementCollection
	@CollectionTable(name = "QUALIFICATIONS", joinColumns = @JoinColumn(name = "EMP_ID"))
	//the jsonb annotation @jsonbTransient will stop the collections from beig serialized to the json format
	@JsonbTransient
	private Collection<Qualifications> qualifications = new ArrayList<Qualifications>();
	
	@ElementCollection
	@Column(name = "NICKY")
	@JsonbTransient
	private Collection<String> nickNames = new ArrayList<String>();
//person can work upto an age of 60 so set maximum value of 60 for validation there is also the opposite @DecimalMin
	@DecimalMax(value = "60", message = "age cannot exceed 60")
	private int age;
	//the persist and remove will be cascaded down to Allowance entity data in the database
	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	@JsonbTransient
	private Set<Allowance> employeeAllowances = new HashSet<>();
	//since its OneToOne when employee is instance is created payslip will also be loaded
	//with the default eagerly 
	//by default single relationship instances are loaded eagerly
	
	@OneToOne
	@JoinColumn(name= "CURRENT_PAYSLIP_ID")
	private Payslip currentPayslip;
	
	@ElementCollection
	@CollectionTable(name = "EMP_PHONE_NUMBERS")
	//different phones like mobile, home, work numbers also tells the key to use in the map table
	@MapKeyColumn(name = "PHONE_TYPE")
	//stores the actually phone number values
	@Column(name = "PHONE_NUMBER")
	//overides the default enum to integer to make it a string
	@MapKeyEnumerated(EnumType.STRING)
	@JsonbTransient
	//the key(PhoneType) is mapped to enum	
	private Map<PhoneType, String> employeePhoneNumbers = new HashMap<>();
	
	//using mapped by employee is owned by ParkingSpace
	//we can use persistence to change attributes for the entities that share a relation
	//for instance with parking space and employee here whenever we persist employee we persist his parking space
	@OneToOne(mappedBy = "employee", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private ParkingSpace parkingSpace;

	//collections are loaded by default lazily
	@OneToMany
	@JsonbTransient
	private Collection<Payslip> pastPayslips = new ArrayList<>();

	@ManyToOne
	//changes the name of the foreign key column
	@JoinColumn(name = "DEPT_ID")
	private Department department;
	
	public Collection<Project> getProjects() {
		return projects;
	}



	public void setProjects(Collection<Project> projects) {
		this.projects = projects;
	}

	//many to many association with project where the owner is Project
	//the runtime uses a joint table to manage the association
	@JsonbTransient
	@ManyToMany(mappedBy = "employees")
	private Collection<Project> projects = new ArrayList<Project>();

	// large binary object
	// can also use @Basic
	// @Basic(fetch = FetchType.LAZY) //will only fetch when making an explicit
	// request
	@Lob
	private byte[] picture;
//lifecycle just before entity is persisted must return void and not take any parameters
//here we are initializing the persons age
	//one entity can have all of the callbacks on one method that meets the requirements but cant use 2 of the same callbacks
//	@PrePersist
//	//other callbacks
//	//will be invoked after persist cant depent on post persist to assume transaction is successful since persist may be rolled back
//	@PostPersist
//	//will be invoked just before entity is updateddepartmentNa
//	@PreUpdate
//	//after entity is updated
//	@PostUpdate
//	//theres no pre load.. after data has been loaded and persisted from the database
//	@PostLoad
	//theres also 
//	@PreRemove
//	@PostRemove
//	
//	private void init() {
//		this.age = Period.between(dateOfBirth, LocalDate.now()).getYears();
//	}
	
	

	public void setAge(int age) {
		this.age = age;
	}



	public String getSocialSecurityNumber() {
		return socialSecurityNumber;
	}



	public void setSocialSecurityNumber(String socialSecurityNumber) {
		this.socialSecurityNumber = socialSecurityNumber;
	}



	public Collection<Qualifications> getQualifications() {
		return qualifications;
	}



	public void setQualifications(Collection<Qualifications> qualifications) {
		this.qualifications = qualifications;
	}



	public Collection<String> getNickNames() {
		return nickNames;
	}



	public void setNickNames(Collection<String> nickNames) {
		this.nickNames = nickNames;
	}



	public Map<PhoneType, String> getEmployeePhoneNumbers() {
		return employeePhoneNumbers;
	}



	public void setEmployeePhoneNumbers(Map<PhoneType, String> employeePhoneNumbers) {
		this.employeePhoneNumbers = employeePhoneNumbers;
	}



	public ParkingSpace getParkingSpace() {
		return parkingSpace;
	}



	public void setParkingSpace(ParkingSpace parkingSpace) {
		this.parkingSpace = parkingSpace;
	}



	public Employee getReportsTo() {
		return reportsTo;
	}

	public void setReportsTo(Employee reportsTo) {
		this.reportsTo = reportsTo;
	}

	public Set<Employee> getSubordinates() {
		return subordinates;
	}

	public void setSubordinates(Set<Employee> subordinates) {
		this.subordinates = subordinates;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public int getAge() {
		return age;
	}

	public EmploymentType getEmploymentType() {
		return employmentType;
	}

	public void setEmploymentType(EmploymentType employmentType) {
		this.employmentType = employmentType;
	}

	public byte[] getPicture() {
		return picture;
	}

	public void setPicture(byte[] picture) {
		this.picture = picture;
	}

	public Payslip getCurrentPayslip() {
		return currentPayslip;
	}

	public void setCurrentPayslip(Payslip currentPayslip) {
		this.currentPayslip = currentPayslip;
	}

	public Collection<Payslip> getPastPayslips() {
		return pastPayslips;
	}

	public void setPastPayslips(Collection<Payslip> pastPayslips) {
		this.pastPayslips = pastPayslips;
	}

	public LocalDate getHiredDate() {
		return hiredDate;
	}

	public void setHiredDate(LocalDate hiredDate) {
		this.hiredDate = hiredDate;
	}

	public Set<Allowance> getEmployeeAllowances() {
		return employeeAllowances;
	}

	public void setEmployeeAllowances(Set<Allowance> employeeAllowances) {
		this.employeeAllowances = employeeAllowances;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public BigDecimal getBasicSalary() {
		return basicSalary;
	}

	public void setBasicSalary(BigDecimal basicSalary) {
		this.basicSalary = basicSalary;
	}



	@Override
	public int hashCode() {
		return Objects.hash(getSocialSecurityNumber().toUpperCase());
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		return Objects.equals(getSocialSecurityNumber().toUpperCase(), other.getSocialSecurityNumber().toUpperCase());
	}
	
	
	
}
