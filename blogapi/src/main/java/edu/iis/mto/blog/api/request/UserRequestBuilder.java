package edu.iis.mto.blog.api.request;

public class UserRequestBuilder {
	private String firstName;
    private String lastName;
    private String email;
    
	public UserRequestBuilder() {
		super();
	}

	public UserRequestBuilder setFirstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	public UserRequestBuilder setLastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	public UserRequestBuilder setEmail(String email) {
		this.email = email;
		return this;
	}
	
	public UserRequest create() {
		return new UserRequest(firstName, lastName, email);
	}
    
}
