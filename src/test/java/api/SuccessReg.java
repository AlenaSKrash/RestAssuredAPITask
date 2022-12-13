package api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SuccessReg {

	@JsonProperty("id")
	private int id;

	@JsonProperty("token")
	private String token;


	public int getId(){
		return id;
	}

	public String getToken(){
		return token;
	}
}