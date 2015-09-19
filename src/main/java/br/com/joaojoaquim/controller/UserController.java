package br.com.joaojoaquim.controller;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	private GraphDatabaseService graphDatabase;
	
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<User> create(User user){
		try(Transaction tx = graphDatabase.beginTx()){
			repository.save(user);
			return new ResponseEntity<User>(user, HttpStatus.CREATED);
		}catch(RuntimeException exception){
			return new ResponseEntity<User>(HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@RequestMapping(method=RequestMethod.POST, value="{userId}/checkin/{eventId}")
	public ResponseEntity<User> checkin(@PathVariable("userId") Long userId,
			@PathVariable("eventId") Long eventId){
		try(Transaction tx = graphDatabase.beginTx()){
			User user = repository.findOne(userId);
			Event event = eventRepository.findOne(eventId);
			user.checkin(event);
			repository.save(user);
			tx.success();
			return new ResponseEntity<User>(user, HttpStatus.ACCEPTED);
		}catch(RuntimeException exception){
			return new ResponseEntity<User>(HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@RequestMapping(method=RequestMethod.POST, value="{userId}/checkout/{eventId}")
	public ResponseEntity<User> checkout(@PathVariable("userId") Long userId,
			@PathVariable("eventId") Long eventId){
		try(Transaction tx = graphDatabase.beginTx()){
			User user = repository.findOne(userId);
			Event event = eventRepository.findOne(eventId);
			user.checkout(event);
			repository.save(user);
			tx.success();
			return new ResponseEntity<User>(user, HttpStatus.ACCEPTED);
		}catch(RuntimeException exception){
			return new ResponseEntity<User>(HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
}
