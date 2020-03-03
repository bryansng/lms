package ie.ucd.lms.service;

/**
 * ActionConclusion
 *
 * Made to ease passing of success or failure messages resulted from an Service Action, i.e. create(), update(), etc.
 */
public class ActionConclusion {
	public Boolean isSuccess;
	public String message;

	public ActionConclusion(Boolean isSuccess) {
		setIsSuccess(isSuccess);
		assignDefaultMessage(isSuccess);
	}

	public ActionConclusion(Boolean isSuccess, String message) {
		setIsSuccess(isSuccess);
		setMessage(message);
	}

	private void assignDefaultMessage(Boolean isSuccess) {
		String message;
		if (isSuccess) {
			message = "Action was executed successfully.";
			setMessage(message);
			return;
		}
		message = "Unable to execute action. Please try again.";
		setMessage(message);
	}

	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}