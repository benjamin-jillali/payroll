package academy.learnprogramming.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import com.hazelcast.core.LifecycleListener;
import com.pedantic.Resource.EmployeeResource;
import com.pedantic.config.JAXRSConfiguration;
import com.pedantic.entities.Employee;
import com.pedantic.service.PersistenceService;
import com.pedantic.service.QueryService;

@RunWith(Arquillian.class)
public class PersistenceServiceTest {
	
	@EJB
	private PersistenceService persistenceService;
	@Inject
	private QueryService queryService;
	private Client client;
	private WebTarget webTarget;
	
	@ArquillianResource
	private URL base;
	
	@PersistenceContext
	EntityManager entityManager;
	//only requirement for arqui test we need to make web archive annotated with @Deoployment 
	//will use as test package to server we add different components of addPackage and addClass to the archive
	//will be deployed to declare server
	@Deployment
	public static WebArchive createDeployment() {
		return ShrinkWrap.create(WebArchive.class, "payroll.war")
				.addPackage(PersistenceService.class.getPackage())
				.addPackage(Employee.class.getPackage())
				.addPackage(JAXRSConfiguration.class.getPackage())
				.addPackage(EmployeeResource.class.getPackage())
				.addAsResource("persistence.xml", "META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}
	
//  @Before
//  public void init() {
//      persistenceService = new PersistenceService();
//      persistenceService.entityManager = entityManager;
//  }
//
//  @After
//  public void clean() {
//      entityManager.close();
//
//  }
	
	@Before
	public void init() throws MalformedURLException{
		client = ClientBuilder.newBuilder().connectTimeout(7, TimeUnit.SECONDS)
				.readTimeout(3, TimeUnit.SECONDS).build();
		webTarget = client.target(URI.create(new URL(base, "api/v1/employees/employees").toExternalForm()));
	}
	@After
	public void cleanUp() {
		client.close();
	}
//test method	
//first testing to see if the integrations and artifacts are working well
//query rest and client target
	@org.junit.Test
	public void greet() {
		//first create employee
		Employee employee = new Employee();
		employee.setFullName("Bob barker");
        employee.setSocialSecurityNumber("123495ufhdjkd");
        employee.setBasicSalary(new BigDecimal("350000"));
        employee.setHiredDate(LocalDate.now());
        employee.setDateOfBirth(LocalDate.of(1987, 10, 23));
        
        //Test persistence service call save employee
        persistenceService.saveEmployee(employee);
        
        //Test query service
        //get employee from database thene asert if id is not null aka succesfully persisted
        //check if employee list not null and check if size of list is correct
        final List<Employee> employees = queryService.getEmployees();
        assertNotNull(employee.getId());
        assertNotNull(employees);
        assertEquals(1,  employees.size());
        
        //Test REST service
        //making rest request to employee resource to see if returned employees
        //were expecting jsonarray with jsonp api
        JsonArray jsonArray = webTarget.request(MediaType.APPLICATION_JSON).get(JsonArray.class);
        assertNotNull(jsonArray);
        assertEquals(1, jsonArray.size());
	}

}





