package telran.accountingmanagement.exceptions;

@SuppressWarnings("serial")
public class AccountNotFoundException extends NotFoundException {
	public AccountNotFoundException(String email, String collection) {
		super(String.format("account with email %s not exists in the collection %s", email, collection));
	}
}