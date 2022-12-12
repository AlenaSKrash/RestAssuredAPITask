package api;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Response{

	@JsonProperty("per_page")
	private int perPage;

	@JsonProperty("total")
	private int total;

	@JsonProperty("data")
	private List<DataItem> data;

	@JsonProperty("page")
	private int page;

	@JsonProperty("total_pages")
	private int totalPages;

	@JsonProperty("support")
	private Support support;

	public int getPerPage(){
		return perPage;
	}

	public int getTotal(){
		return total;
	}

	public List<DataItem> getData(){
		return data;
	}

	public int getPage(){
		return page;
	}

	public int getTotalPages(){
		return totalPages;
	}

	public Support getSupport(){
		return support;
	}
}