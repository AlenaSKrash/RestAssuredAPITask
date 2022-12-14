package apiReqres.colors;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ColorsData{

	@JsonProperty("color")
	private String color;

	@JsonProperty("year")
	private int year;

	@JsonProperty("name")
	private String name;

	@JsonProperty("id")
	private int id;

	@JsonProperty("pantone_value")
	private String pantoneValue;

	public String getColor(){
		return color;
	}

	public int getYear(){
		return year;
	}

	public String getName(){
		return name;
	}

	public int getId(){
		return id;
	}

	public String getPantoneValue(){
		return pantoneValue;
	}
}