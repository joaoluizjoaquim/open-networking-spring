package br.com.joaojoaquim;

class LinkBuilder{
	private StringBuilder link;
	
	public LinkBuilder(int port) {
		link = new StringBuilder("http://localhost:").append(port);
	}
	
	public String build() {
		return link.toString();
	}

	public LinkBuilder user(){
		link.append("/users");
		return this;
	}
	
	public LinkBuilder id(Object id){
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
