package com.pedantic.service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;

import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.pedantic.entities.*;
//stateless beans do not keep states so one method can not depend on the other method since
// the container will likely discard the info after using
//every method is isolated
//you can also give it a role access for security
//@RolesAllowed
@Stateless
//transactions are handled by default if you want to manually handle the transaction
//can use the below annotation
@TransactionManagement(TransactionManagementType.BEAN)
public class QueryService {
	
	@Inject
	private SecurityUtil securityUtil;
	
	
	@Inject
	EntityManager entityManager;
	
	@PostConstruct
	private void init() {
		
	}
	
	@PreDestroy
	private void destroy() {
		
	}	
	
	public List<Department> getAllDepartments(){
		//will return a typed query
		//will alwayts get a typedQuery can also do below to get the range of methods example 
	    //TypedQuery<Department> namedQuery = entityManager.createNamedQuery(Department.GET_DEPARTMENT_LIST, Department.class);
		return entityManager.createNamedQuery(Department.GET_DEPARTMENT_LIST, Department.class).getResultList();
	}
	
	public List<String> getAllDepartmentNames(){
		return entityManager.createNamedQuery(Department.GET_DEPARTMENT_NAMES, String.class).getResultList();
	}
	
	public List<ParkingSpace> getAllAllocatedParkingSpaces(){
		return entityManager.createNamedQuery(Employee.GET_ALL_PARKING_SPACES, ParkingSpace.class).getResultList();
	}
	//since the query gets different types it returns a Collection of array objects
	public Collection<Object[]> getEmployeeProjection(){
		return entityManager.createNamedQuery(Employee.EMPLOYEE_PROJECTION, Object[].class).getResultList();
	}
	public List<EmployeeDetails> getEmployeeDetails(){
		return entityManager.createNamedQuery(Employee.EMPLOYEE_CONSTRUCTOR_PROJECTION, EmployeeDetails.class).getResultList();
	}
	public Collection<Allowance> getEmployeeAllowances(BigDecimal greaterThenValue){
		return entityManager.createNamedQuery(Employee.GET_EMPLOYEE_ALLOWANCES, Allowance.class)
							.setParameter("greaterThanValue", greaterThenValue).getResultList();
	}
	//calls a query with 2 paramaters to set
	public Collection<Employee> filterEmployeeBySalary(BigDecimal lowerBound, BigDecimal upperBound){
		return entityManager.createNamedQuery(Employee.EMPLOYEE_SALARY_BOUND, Employee.class)
						.setParameter("upperBound", upperBound).setParameter("lowerBound", lowerBound).getResultList();
	}
	//"filter" and pattern will be escaped
	//this will search with a pattern
	public Collection<Employee> filterEmployees(String pattern){
		return entityManager.createQuery("select e from Employee e where e.fullName LIKE :filter", Employee.class)
				.setParameter("filter", pattern).getResultList();//__vid matches david, any 2 characters + 
																 //vid otherwise jo% % means any number of chars after jo example joseph joel
	}
	
	//subqueries putting 2 queries where result from sub query resolves condition for main query
	//we return get single result since we only want 1 result
	//getsingleresult throws an error if theres more then one case or non
	public Employee getEmployeeWithHighestSalary() {
		return entityManager.createQuery("select e from Employee e where e.basicSalary = (select max(emp.basicSalary) from Employee emp", Employee.class)
		.getSingleResult();
	}
//in clause will looke for results that exist in the list to the right can also use NOT IN instead of IN for opposite result
	public Collection<Employee> filterEmployeesByStates(){
		return entityManager.createQuery("select e from Employee e where e.address.state in ('NY', 'CA')", Employee.class).getResultList();
	}
	//"is not empty" check if subordinates is not empty aka they are in charge of ppl and therefore a manager
	public Collection<Employee> getManagers(){
		return entityManager.createQuery("select e from Employee e where e.subordinates is not empty", Employee.class).getResultList();
	}
	//get collection of elements which is a MEMBER OF an entity for instance the employees that are members of a project
	//its always good to use named parameters throughout the project for security to protect against sql attacks
	public Collection<Employee> getEmployeesByProject(Project project){
		return entityManager.createQuery("select e from Employee e where :project member of e.projects order by e.department.departmentName desc", Employee.class)
				.setParameter("project", project).getResultList();
	}
//AND is used to join 2 conditions	
//select all employees and check thier basic salary that is less then subordinates
//ALL will check what is the left to the result set on the right so will check basic salary to sub basic salary runtime will issue a join
//can use ANY instead of ALL all checks all the results any checks at least if one passes some is an alias for any
	public Collection<Employee> getAllLowerPaidManagers(){
		return entityManager.createQuery("select e from Employee e where e.subordinates is not empty and e.basicSalary < all(select s.basicSalary from e.subordinates s)", Employee.class).getResultList();
	}
//ORDER BY default = ascending
//bonus declared and given an alias as bonus then order by bonus
	public Collection<Employee> getEmployeesByBonus(){
		return entityManager.createQuery("select e from Employee e.basicSalary * 0.15 as bonus from Employee e order by bonus", Employee.class).getResultList();
	}
//aggregate queries SUM 
//sum will return the sum of the salaries in the department
//the sum uses the e from the 2nd query that traverses employees in the department to get the sum of salaries grouped by thier departments 
//then returns a collection of sums of salaries of employees in each department
	public Collection<Object[]> getTotalEmployeeSalariesByDept(){
		TypedQuery<Object[]> query = entityManager.createQuery("select d.departmentName, sum(e.basicSalary) from Department d join d.employees e group by d.departmentName", Object[].class);
		return query.getResultList();
	}
	
	//Average uses avg() to get the average salary of employees that arnt managers
 	public Collection<Object[]> getAverageEmployeeSalaryByDept(){
		return entityManager.createQuery("select d.departmentName, avg(e.basicSalary) from Department d join d.employees e where e.subordinates is empty group by d.departmentName", Object[].class).getResultList();
	}
 	
 	//Count counts number of employees in a department
 	public Collection<Object[]> countEmployeesByDept(){
 		return entityManager.createQuery("select d.departmentName, count(e) from Department d join d.employees e group by d.departmentName", Object[].class).getResultList();
 	}
 	//MAX and MIN
 	//get the max salary of employee in each department
 	public Collection<Object[]> getEmployeesLowestByDept(){
 		return entityManager.createQuery("select d.departmentName, min(e.basicSalary) from Department d join d.employees e group by d.departmentName", Object[].class).getResultList();
 	}
 	//MAX and MIN
 	//get the min salary of employee in each department
 	public Collection<Object[]> getEmployeesHighestByDept(){
 		return entityManager.createQuery("select d.departmentName, max(e.basicSalary) from Department d join d.employees e group by d.departmentName", Object[].class).getResultList();
 	}
 	//HAVING
	//Average uses avg() to get the average salary of employees that arnt managers
 	//only check the employees whos average salary is over minimum threshold of salaries using the Having avg() > :placeholder
 	public Collection<Object[]> getAverageEmployeeSalaryByDept(BigDecimal minimumThreshold){
		return entityManager.createQuery("select d.departmentName, avg(e.basicSalary) from Department d join d.employees e where e.subordinates is empty group by d.departmentName having avg(e.basicSalary) > :minThreshold", Object[].class)
				.setParameter("minThreshold", minimumThreshold).getResultList();
	}
 	
 	//Criteria API is typesafe
 	public Collection<Employee> criteriaQuery(){
 		//JPQL equivalent
 		//select e from Employee e where e.fullName = 'Average Joe'
 		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
 		CriteriaQuery<Employee> c = cb.createQuery(Employee.class);
 		Root<Employee> emp = c.from(Employee.class);
 		CriteriaQuery<Employee> query = c.select(emp).where(cb.equal(emp.get("fullName"), "Average Joe"));
 		return entityManager.createQuery(query).getResultList();
 		
 	}
 	//uses a sql instead of jpql query
 	public Collection<Employee> findAllEmployeesNamedNative(){
 		return entityManager.createNamedQuery("employee.findAllNativeNamed", Employee.class).getResultList();
 	}
 	
 	//sql dynamic queries
 	@SuppressWarnings("unchecked")
 	public Collection<Department> getDepartmentNativeQueries(){
 		return entityManager.createNativeQuery("select * from Department", Department.class).getResultList();
 	}
	
	public Department findDepartmentById(Long id) {
		return entityManager.find(Department.class, id);		
	}
	
	public Employee findEmployeeById(Long id) {
		return entityManager.find(Employee.class, id);
	}
	
	//@TransactionAttribute
	public List<Employee> getEmployees(){
		return entityManager.createNamedQuery(Employee.LIST_EMPLOYEES, Employee.class).getResultList();
	}
	
	public List<Department> getDepartment(){
		return null;
	}
	
	public Collection<Employee> bla(){
		//select e from Employee e where e.fullName = 'Average Joe'
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Employee> c = cb.createQuery(Employee.class);
		Root<Employee> emp = c.from(Employee.class);
		CriteriaQuery<Employee> query = c.select(emp)
		.where(cb.equal(emp.get("fullname"), "Average Joe"));
		return entityManager.createQuery(query).getResultList();
		
	}
	
    @SuppressWarnings("unchecked")
    public Collection<Department> getDepartmentsNativeQuery() {
        return entityManager.createNativeQuery("select * from Department", Department.class).getResultList();
    }
	
//	public ApplicationUser findUserByCredentials(String email, String plainTextPassword) {
//		return entityManager.createNamedQuery(ApplicationUser.FIND_USER_BY_CREDENTIALS, ApplicationUser.class)
//				.setParameter("encryptedPassword", securityUtil.encryptText(plainTextPassword))
//				.setParameter("email", email).getResultList().get(0);
//	}
    //recieves plaintext password and email then fetches user by email from the database
    //then grab first one returned
    public boolean authenticateUser(String email, String plainTextPassword) {
    	ApplicationUser user = entityManager.createNamedQuery(ApplicationUser.FIND_USER_BY_CREDENTIALS, ApplicationUser.class)
    			.setParameter("email", email.toLowerCase()).getResultList().get(0);
    	if(user != null) {
    		//password match takes hashed password salt and plaintext to see if thier valid
    		//to compare the two we use the salt 
    		return securityUtil.passwordMatch(user.getPassword(), user.getSalt(), plainTextPassword);
    	}
    	return false;
    }

}
