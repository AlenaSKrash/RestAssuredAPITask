package apiReqres.registration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Register{

	@JsonProperty("password")
	private String password;

	@JsonProperty("email")
	private String email;

	public Register(String password, String email) {
		this.password = password;
		this.email = email;
	}

	public String getPassword(){
		return password;
	}

	public String getEmail(){
		return email;
	}
}