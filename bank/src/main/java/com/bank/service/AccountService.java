package com.bank.service;

import java.math.BigDecimal;

import javax.security.auth.login.AccountException;

import org.omg.CORBA.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.model.Account;
import com.bank.model.PaymentType;
import com.bank.model.Transaction;
import com.bank.model.User;
import com.bank.repository.AccountRepository;

@Service("accountService")
@Transactional
public class AccountService extends BaseService {

	@Autowired
	UserService userService;

	@Autowired
	AccountRepository accountDao;

	public Account findAccById(long id) throws AccountException {
		Account acc = getEm().find(Account.class, id);
		if (acc == null) {
			throw new AccountException("Account: " + id + " was not found!");
		}
		return acc;
	}

	@SuppressWarnings("serial")
	public void makeTransaction(String principal, Long senderId, Long receiverId, BigDecimal amount)
			throws AccountException, UserException {

		Account one = findAccById(senderId);
		Account two = findAccById(receiverId);

		if (principal.equals(one.getUser().getUsername())) {

			if (one.getBalance().compareTo(amount) < 0) {
				throw new AccountException("Nedostatuchna nalichnost !");
			}

			firstTransaction(amount, one, two);
			secondTransaction(amount, one, two);

			one.setBalance(one.getBalance().subtract(amount));
			two.setBalance(two.getBalance().add(amount));
		} else {
			throw new UserException("Nevalidna smetka") {
			};
		}
	}

	private void secondTransaction(BigDecimal amount, Account one, Account two) {
		Transaction t2 = new Transaction();
		t2.setSrcAccount(one);
		t2.setReceiverAccount(two);
		t2.setAmount(amount);
		t2.setTransactionDate();
		t2.setType(PaymentType.DEBIT);
		getEm().persist(t2);
	}

	private void firstTransaction(BigDecimal amount, Account one, Account two) {
		Transaction t = new Transaction();
		t.setSrcAccount(one);
		t.setReceiverAccount(two);
		t.setAmount(amount);
		t.setTransactionDate();
		t.setType(PaymentType.CREDIT);
		getEm().persist(t);
	}

	public Account createAccount(String username) {
		User u = (User) userService.loadUserByUsername(username);
		Account temp = new Account();
		int startMoney = (int) (Math.random() * 500) + 1;
		temp.setBalance(BigDecimal.valueOf(startMoney));
		temp.setUser(u);
		getEm().persist(temp);
		return temp;

	}

	public Page<Account> findAll(Pageable pageable) {
		return accountDao.findAll(pageable);
	}

}
