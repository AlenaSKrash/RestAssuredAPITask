package apiReqres.registration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UnSuccessReg{

	@JsonProperty("error")
	private String error;

	public String getError(){
		return error;
	}
}