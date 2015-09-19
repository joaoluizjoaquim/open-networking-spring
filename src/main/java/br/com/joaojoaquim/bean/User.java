package br.com.joaojoaquim.bean;

import java.util.Set;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
public class User {
	
	@GraphId
	private Long id;
	
	private String name;
	
	private Set<Event> events;
	
	public User() {
	}
	
	public User(String name){
		this.name = name;
	}
	
	public void checkin(Event event){
		if(!events.add(event)){
			throw new RuntimeException("User already registered.");
		}
	}
	
	public void checkout(Event event){
		if(!events.remove(event)){
			throw new RuntimeException("User not registered in the event "+event.getName());
		}
	}

	public Long getId(){
		return this.id;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		User other = (User) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equalsIgnoreCase(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [name=" + name + "]";
	}
	
}
