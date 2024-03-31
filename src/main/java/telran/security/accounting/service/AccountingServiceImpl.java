package telran.security.accounting.service;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.security.accounting.dto.AccountDto;
import telran.security.accounting.exceptions.AccountNotFoundException;
import telran.security.accounting.exceptions.AccountStateException;
import telran.security.accounting.model.Account;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountingServiceImpl implements AccountingService {
	final MongoTemplate mongoTemplate;
	final PasswordEncoder passwordEncoder;

	@Override
	public AccountDto addAccount(AccountDto accountDto) {
		String email = accountDto.email();
		Account account = null;
		AccountDto encodedAccount = getEncoded(accountDto);
		try {
			account = mongoTemplate.insert(Account.of(encodedAccount));
		} catch (DuplicateKeyException e) {
			throw new AccountStateException(email);
		}
		log.debug("account {} has been saved", email);
		return account.build();
	}

	private AccountDto getEncoded(AccountDto accountDto) {

		return new AccountDto(accountDto.email(), passwordEncoder.encode(accountDto.password()), accountDto.roles());
	}

	@Override
	public AccountDto removeAccount(String email) {
		Account account = mongoTemplate.findAndRemove(new Query(Criteria.where("email").is(email)), Account.class);
		if (account == null) {
			throw new AccountNotFoundException(email);
		}
		log.debug("account {} has been removed", email);
		return account.build();
	}

	@Override
	public void updatePassword(String email, String newPassword)
			throws AccountNotFoundException, IllegalArgumentException {
		String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
		if (!currentUsername.equals(email)) {
			throw new IllegalArgumentException("Only current authenticated user may update its password");
		}
		Account account = mongoTemplate.findById(new Query(Criteria.where("email").is(email)), Account.class);
		if (account == null) {
			throw new AccountNotFoundException(email);
		}
		AccountDto encodedAccount = getEncoded(new AccountDto(account.getEmail(), newPassword, account.getRoles()));
		account = mongoTemplate.insert(Account.of(encodedAccount));
	}

}
