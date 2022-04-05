package com.pedantic.entities;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.pedantic.config.AbstractEntityListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*
 * format for select query
 * SELECT <select_expression>
 * FROM <from_clause>
 * "optional"
 * [WHERE <conditional_expression>]
 * [ORDER BY <order_by_clause>]
 */
@Entity
//jpql has namedquery and dynamicquery
//namedquery are types of queries that are defined on entity classes or xml files and are compiled with entities they are static
//dynamic queries are defined dynamically at runtime
//named queries can be optimized by the provider
//aliasing in jpql is mandatory
//a query cant return a collection and you cant navigate to collections in a query
@NamedQuery(name = Department.GET_DEPARTMENT_LIST, query = "select d from Department d")
@NamedQuery(name = Department.GET_DEPARTMENT_NAMES, query = "select d.departmentName from Department d")


@NamedQuery(name = Department.FIND_BY_ID, query = "select d from Department d where d.id = :id and d.userEmail = :email")
@NamedQuery(name = Department.FIND_BY_NAME, query = "select d from Department d where d.departmentName = :name and d.userEmail = :email")
@NamedQuery(name = Department.LIST_DEPARTMENTS, query = "select d from Department d where  d.userEmail = :email")
@Access(AccessType.FIELD)
//kink entity listener
@EntityListeners({AbstractEntityListener.class})
public class Department extends AbstractEntity {

    public static final String FIND_BY_ID = "Department.findById";
    public static final String FIND_BY_NAME = "Department.findByName";
    public static final String LIST_DEPARTMENTS = "Department.listDepartments";
    public static final String GET_DEPARTMENT_LIST = "Department.getAllDepartments";
    public static final String GET_DEPARTMENT_NAMES = "Department.getDepartmentNames";


    @NotEmpty(message = "Department name must be set")
   //@Transient
    //transient so getdepartmentName is used as property access type
    @Pattern(regexp = "", message = "Department name must be in the form dept abbreviation, number and branch. ex FIN0011MAIN")
    private String departmentName; //department name format example FIN0011MAIN

//    @OneToMany(mappedBy = "department")
//    //ordering by ascending (asc) is the default
//    //without properties in OrderBy defualt is ordered by primary key
//    
//    @OrderBy("fullName ASC, dateOfBirth desc")
//    
//    //orderColumn if you dont have fields that have a good way to order elements
//    //orders elements based off thier position on the list
//    //it creates a row within the table to keep position list
//    @OrderColumn(name = "EMPLOYEE POSITION")
//    private List<Employee> employees = new ArrayList<>();
    @OneToMany
    //using the MapKey with name "id" uses the id field valuefrom the type in this case employee
    //as the id for the map table for instance emplyee id 4 will have id of 4 in the map table
    /*
	 *   an element gets detached outside of its persistence context when it is sent to another layer
	 *   for instance front end as a result data that is lazily loaded becomes undefined
	 *   certain fields like Maps are lazily loaded by default so when they are sent to different layer they will be outside of
	 *   thier persistence context then method to getemployees will become undefined
	 *   
	 *   the opposide of detachment is merging
	 *   check the  PersistenceService class with the method updateDepartment to see example of merging
    */
    @MapKey(name = "id")
    private Map<Long, Employee> employees = new HashMap<>();
    //when the value is a simple type its always element collection otherwise 
    //when value is an entity it uses relationshop mapping like one to many
    @ElementCollection
    @CollectionTable(name = "EMPLOYEE_RANKS")
    //this column will be represented by employee id since keying by the entity
    @MapKeyJoinColumn(name = "EMP_ID")
    @Column(name = "RANK")
    private Map<Employee, Integer> employeeRanks = new HashMap<>();
    

    //@Transient keeps field from being mapped to the database.
    @Transient
    private String departmentCode;


    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }
    //allows property access type means it uses getters and setters
   // @Access(AccessType.PROPERTY)
    //requires the name to not be null
    @NotEmpty(message = "Department name must be set")
    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
//overide hascode and equals method to check if each department has a unique name
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getDepartmentName().toUpperCase() == null) ? 0 : getDepartmentCode().toUpperCase().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Department other = (Department) obj;
		if (departmentName == null) {
			if (other.departmentName != null)
				return false;
		} else if (!getDepartmentName().toUpperCase().equals(other.getDepartmentName().toUpperCase()))
			return false;
		return true;
	}

//    public List<Employee> getEmployees() {
//        return employees;
//    }

//    public void setEmployees(List<Employee> employees) {
//        this.employees = employees;
//    }
    
    
}
