package apiReqres.users;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserTimeResponse {

	@JsonProperty("name")
	private String name;

	@JsonProperty("job")
	private String job;

	@JsonProperty("updatedAt")
	private String updatedAt;

	public String getName(){
		return name;
	}

	public String getJob(){
		return job;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}
}