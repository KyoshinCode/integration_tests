package edu.iis.mto.blog.api.request;

public class UserRequest {

    private String firstName;

    private String lastName;

    private String email;

    public UserRequest() {}

    public UserRequest(String firstName, String lastName, String email) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserRequest setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserRequest setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserRequest setEmail(String email) {
        this.email = email;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UserRequest) {
            UserRequest other = (UserRequest) obj;

            return eq(this.email, other.email);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }

    private boolean eq(Object val1, Object val2) {
        return val1 != null ? val1.equals(val2) : val2 == null ? true : false;
    }
}
