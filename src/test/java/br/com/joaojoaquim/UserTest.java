package br.com.joaojoaquim;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import br.com.joaojoaquim.bean.Event;
import br.com.joaojoaquim.bean.User;
import br.com.joaojoaquim.repository.EventRepository;
import br.com.joaojoaquim.repository.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class UserTest {
	
	private static RestTemplate template;
	
	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	private UserRepository personRepository;
		
	@Value("${local.server.port}")
	private int port;

	private Event eventCreated;
	private User personCreated;
	
	@BeforeClass
	public static void beforeClass(){
		template = new TestRestTemplate();
        template.setMessageConverters(Arrays.<HttpMessageConverter<?>>asList(new MappingJackson2HttpMessageConverter()));
	}
	
	@Before
	public void before(){
		Event event = new Event();
		event.setName("Java One 2015");
		event.setTag("javaone2015");
		eventCreated = eventRepository.save(event);
		
		User person = new User("John");
		personCreated = personRepository.save(person);
	}
	
	@After
	public void after(){
		eventRepository.deleteAll();
		personRepository.deleteAll();
	}
	
	@Test
	public void shouldCreateNewUser(){
		String link = new LinkBuilder(port).user().build();
		User user = new User("Test user");
		assertEquals(HttpStatus.CREATED, template.postForEntity(link, user, Map.class).getStatusCode());
	}
	
	@Test
	public void shouldRegisterParticipantInEvent(){
		String link = new LinkBuilder(port).user().id(personCreated.getId()).checkin().id(eventCreated.getId()).build();
		
		assertEquals(HttpStatus.ACCEPTED, template.postForEntity(link,null, Map.class).getStatusCode());
	}
	
	@Test
	public void shouldNotCheckinEventParticipantChecked(){
		String link = new LinkBuilder(port).user().id(personCreated.getId()).checkin().id(eventCreated.getId()).build();
		
		assertEquals(HttpStatus.ACCEPTED, template.postForEntity(link,null, Map.class).getStatusCode());
		
		assertEquals(HttpStatus.NOT_ACCEPTABLE, template.postForEntity(link,null, Map.class).getStatusCode());
	}
	
	@Test
	public void shouldNotCheckoutEventPartipantNotChecked(){
		String link = new LinkBuilder(port).user().id(personCreated.getId()).checkout().id(eventCreated.getId()).build();
		assertEquals(HttpStatus.NOT_ACCEPTABLE, template.postForEntity(link,null, Map.class).getStatusCode());		
	}
	
	@Test
	public void shouldCheckoutParticipantEvent(){
		String linkCheckin = new LinkBuilder(port).user().id(personCreated.getId()).checkin().id(eventCreated.getId()).build();
		assertEquals(HttpStatus.ACCEPTED, template.postForEntity(linkCheckin,null, Map.class).getStatusCode());
		
		String linkCheckout = new LinkBuilder(port).user().id(personCreated.getId()).checkout().id(eventCreated.getId()).build();
		assertEquals(HttpStatus.ACCEPTED, template.postForEntity(linkCheckout,null, Map.class).getStatusCode());
	}
	
}
