package academy.learnprogramming.resource;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;
import javax.json.JsonArray;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.pedantic.Resource.JaxRsClient;

@RunWith(Arquillian.class)
public class JaxRsClientTest {
	@Inject
	private JaxRsClient jaxRsClient;
	
	@Deployment
	public static JavaArchive createDeployment() {
		return ShrinkWrap.create(JavaArchive.class).addClass(JaxRsClient.class)
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}
	//the pwnd api needs registration for a private key have not set it up so will return an error
//	@Test
//	public void getBreaches() {
//		JsonArray breaches = jaxRsClient.getBreaches("bla@bla.com");
//		assertNotNull(breaches);
//		assertTrue(breaches.size() > 1);
//	}
	

}
