package br.com.joaojoaquim;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.net.URI;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import br.com.joaojoaquim.bean.Event;
import br.com.joaojoaquim.bean.Person;
import br.com.joaojoaquim.repository.EventRepository;
import br.com.joaojoaquim.repository.PersonRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=Application.class)
@WebAppConfiguration   // 3
@IntegrationTest("server.port:0")   // 4
public class EventTest {
	
	private static RestTemplate template;
	
	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	private PersonRepository personRepository;
		
	@Value("${local.server.port}")
	private int port;
	
	private static HttpHeaders headers;

	private String pathTestUrl;

	private Event eventCreated;
	private Person personCreated;
	
	@BeforeClass
	public static void beforeClass(){
		headers = new HttpHeaders();
		headers.setAccept(asList(MediaType.APPLICATION_JSON));
		
		template = new TestRestTemplate();
        template.setMessageConverters(Arrays.<HttpMessageConverter<?>>asList(new MappingJackson2HttpMessageConverter()));
	}
	
	@Before
	public void before(){
		pathTestUrl = "http://localhost:" + this.port + "/events";
		
		Event event = new Event();
		event.setName("Java One 2015");
		event.setTag("javaone2015");
		eventCreated = eventRepository.save(event);
		
		Person person = new Person();
		person.setName("John");
		personCreated = personRepository.save(person);
	}
	
	@Test
	public void testWebServiceOK(){
		ResponseEntity<Map> entity = template.getForEntity(pathTestUrl, Map.class);
		assertEquals(HttpStatus.OK, entity.getStatusCode());
	}
	
	@Test
	public void shouldRegisterParticipantInEvent(){
		ResponseEntity<Map> entity = template.postForEntity(new LinkBuilder().participant().id(personCreated.getId()).checkin().id(eventCreated.getId()).build(),null, Map.class);
		assertEquals(HttpStatus.ACCEPTED, entity.getStatusCode());
	}
	
	@Test
	public void shouldNotCheckinEventParticipantChecked(){
		ResponseEntity<Map> entity = template.postForEntity(new LinkBuilder().participant().id(personCreated.getId()).checkin().id(eventCreated.getId()).build(),null, Map.class);
		assertEquals(HttpStatus.ACCEPTED, entity.getStatusCode());
		ResponseEntity<Map> entity2 = template.postForEntity(new LinkBuilder().participant().id(personCreated.getId()).checkin().id(eventCreated.getId()).build(),null, Map.class);
		assertEquals(HttpStatus.NOT_ACCEPTABLE, entity2.getStatusCode());
	}
	
	@Test
	public void shouldNotCheckoutEventPartipantNotChecked(){
		ResponseEntity<Map> entity2 = template.postForEntity(new LinkBuilder().participant().id(personCreated.getId()).checkout().id(eventCreated.getId()).build(),null, Map.class);
		assertEquals(HttpStatus.NOT_ACCEPTABLE, entity2.getStatusCode());		
	}
	
	@Test
	public void shouldCheckoutParticipantEvent(){
		ResponseEntity<Map> entity = template.postForEntity(new LinkBuilder().participant().id(personCreated.getId()).checkin().id(eventCreated.getId()).build(),null, Map.class);
		assertEquals(HttpStatus.ACCEPTED, entity.getStatusCode());
		entity = template.postForEntity(new LinkBuilder().participant().id(personCreated.getId()).checkout().id(eventCreated.getId()).build(),null, Map.class);
		assertEquals(HttpStatus.ACCEPTED, entity.getStatusCode());
	}
		
	@After
	public void after(){
		eventRepository.deleteAll();
		personRepository.deleteAll();
	}
	
	private class LinkBuilder{
		private StringBuilder link;
		
		public LinkBuilder() {
			link = new StringBuilder("http://localhost:").append(port);
		}
		
		public String build() {
			return link.toString();
		}

		public LinkBuilder participant(){
			link.append("/participants");
			return this;
		}
		
		public LinkBuilder id(Long id){
			link.append("/").append(id);
			return this;
		}
		
		public LinkBuilder checkin(){
			link.append("/checkin");
			return this;
		}
		
		public LinkBuilder checkout(){
			link.append("/checkout");
			return this;
		}
		
	}
	
}
