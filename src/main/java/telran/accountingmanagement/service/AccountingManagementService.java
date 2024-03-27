package telran.accountingmanagement.service;

import telran.accountingmanagement.dto.AccountDto;

public interface AccountingManagementService {
	AccountDto addAccount(AccountDto account);
	AccountDto removeAccount(String email);
}
