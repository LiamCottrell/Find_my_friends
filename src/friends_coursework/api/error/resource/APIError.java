package friends_coursework.api.error.resource;

public class APIError {
	
	String error;
	String body;

	public APIError() {

	}

	public String getError_msg() {
		return error;
	}

	public void setError_msg(String error_msg) {
		this.error = error_msg;
	}
	
	public String getError_body() {
		return body;
	}

	public void setError_body(String error_body) {
		this.body = error_body;
	}
}
