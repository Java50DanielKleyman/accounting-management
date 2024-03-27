package telran.accountingmanagement.service;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.accountingmanagement.dto.AccountDto;
import telran.accountingmanagement.exceptions.AccountNotFoundException;
import telran.accountingmanagement.exceptions.EmailIlegalStateException;
import telran.accountingmanagement.model.Account;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountingManagementServiceImpl implements AccountingManagementService {
	final MongoTemplate mongoTemplate;
	String collectionName = "accounts";

	@Override
	public AccountDto addAccount(AccountDto accountDto) {
		Account account = new Account(accountDto.email(), accountDto.password(), accountDto.roles());
		try {
			mongoTemplate.insert(account);
		} catch (DuplicateKeyException e) {
			log.error("account with email {} already exists in collection {}", accountDto.email(), collectionName);
			throw new EmailIlegalStateException(accountDto.email(), collectionName);
		}
		log.debug("account with email {} has been added", accountDto.email());
		return new AccountDto(accountDto.email(), "********", accountDto.roles());
	}

	@Override
	public AccountDto removeAccount(String email) {
		Query query = new Query();
		query.addCriteria(Criteria.where("email").is(email));
		Account account = mongoTemplate.findOne(query, Account.class);
		if (account == null) {
			throw new AccountNotFoundException(email, "Account");
		}
		mongoTemplate.remove(query, Account.class);
		log.debug("account with email {} has been removed", email);
		return new AccountDto(account.getEmail(), "********", account.getRoles());
	}

}
