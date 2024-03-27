package telran.accountingmanagement.exceptions;

@SuppressWarnings("serial")
public class EmailIlegalStateException extends IllegalStateException {
	public EmailIlegalStateException(String email, String collection) {
		super(String.format("account with email %s already exists in the collection %s", email, collection));
	}
}
