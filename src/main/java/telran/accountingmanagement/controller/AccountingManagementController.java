package telran.accountingmanagement.controller;

import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.accountingmanagement.dto.AccountDto;
import telran.accountingmanagement.service.AccountingManagementService;

@RestController
@RequestMapping("accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountingManagementController {
	final AccountingManagementService accountingManagementService;

	@PostMapping
	AccountDto addAccount(@RequestBody @Valid AccountDto accountDto) {
		log.debug("addAccount received account dada :{}", accountDto);
		return accountingManagementService.addAccount(accountDto);
	}

	@DeleteMapping("/{email}")
	AccountDto removeAccount(@PathVariable(name = "email") @NotNull String email) {
		return accountingManagementService.removeAccount(email);
	}
}
