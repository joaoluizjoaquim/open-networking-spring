package br.com.joaojoaquim.controller;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.joaojoaquim.bean.Event;
import br.com.joaojoaquim.bean.User;
import br.com.joaojoaquim.repository.EventRepository;
import br.com.joaojoaquim.repository.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserRepository repository;
	
	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	private GraphDatabaseService graph;
	
	@RequestMapping(method=RequestMethod.POST, value="{personId}/checkin/{eventId}")
	@Transactional
	public ResponseEntity<User> checkin(@PathVariable("personId") Long personId,
			@PathVariable("eventId") Long eventId){
		try(Transaction tx = graph.beginTx()){
			User person = repository.findOne(personId);
			Event event = eventRepository.findOne(eventId);
			if(!person.getEvents().add(event)){
				return new ResponseEntity<User>(person, HttpStatus.NOT_ACCEPTABLE);
			};
			repository.save(person);
			tx.success();
			return new ResponseEntity<User>(person, HttpStatus.ACCEPTED);
		}catch(IllegalArgumentException exception){
			return new ResponseEntity<User>(HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@RequestMapping(method=RequestMethod.POST, value="{personId}/checkout/{eventId}")
	@Transactional
	public ResponseEntity<User> checkout(@PathVariable("personId") Long personId,
			@PathVariable("eventId") Long eventId){
		try(Transaction tx = graph.beginTx()){
			User person = repository.findOne(personId);
			Event event = eventRepository.findOne(eventId);
			if(!person.getEvents().remove(event)){
				return new ResponseEntity<User>(person, HttpStatus.NOT_ACCEPTABLE);
			};
			repository.save(person);
			tx.success();
			return new ResponseEntity<User>(person, HttpStatus.ACCEPTED);
		}catch(IllegalArgumentException exception){
			return new ResponseEntity<User>(HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
}
