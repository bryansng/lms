package ie.ucd.lms.service.admin;

/**
 * ActionConclusion
 *
 * Made to ease passing of success or failure messages resulted from an Service Action, i.e. create(), update(), etc.
 */
public class ActionConclusion {
	public Boolean isSuccess;
	public String message;
	public String successMessage;
	public String failMessage;

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
			setSuccessMessage(message);
			return;
		}
		message = "Unable to execute action. Please try again.";
		setMessage(message);
		setFailMessage(message);
	}

	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
		this.message = successMessage;
	}

	public void setFailMessage(String failMessage) {
		this.failMessage = failMessage;
		this.message = failMessage;
	}
}