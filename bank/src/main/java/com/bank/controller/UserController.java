package com.bank.controller;

import java.math.BigDecimal;

import javax.security.auth.login.AccountException;

import org.omg.CORBA.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.bank.model.User;
import com.bank.service.AccountService;
import com.bank.service.UserService;

@RestController
@RequestMapping("/user-managment")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private AccountService accountService;

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<?> createUser(@RequestBody User user) {
		try {
			return new ResponseEntity<User>(userService.register(user), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<String>("Neuspeshno registrirane " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public ResponseEntity<?> hello() {
		return new ResponseEntity<>("Hello, user", HttpStatus.OK);
	}

	@RequestMapping(value = "/debit", method = RequestMethod.POST, params = { "senderAccount", "receiverAccount",
			"amount" })

	public ResponseEntity<String> makeTransaction(Long senderAccount, Long receiverAccount, double amount)
			throws Exception {

		try {

			if (SecurityContextHolder.getContext().getAuthentication().getName() != null) {
				try {
					accountService.makeTransaction(SecurityContextHolder.getContext().getAuthentication().getName(),
							senderAccount, receiverAccount, BigDecimal.valueOf(amount));
				} catch (AccountException | UserException ex) {
					ex.printStackTrace();
					return new ResponseEntity<String>("Neuspeshna transakciq! ", HttpStatus.BAD_REQUEST);
				}
				return new ResponseEntity<String>("Uspeshna transakciq ! ", HttpStatus.OK);

			}
			return new ResponseEntity<String>("Neuspeshna transakciq! ", HttpStatus.BAD_REQUEST);

		} catch (Exception e) {
			throw new Exception("asda " + e.getMessage());
		}
	}

	@RequestMapping(value = "/createNewAccount", method = RequestMethod.GET)
	public ModelAndView createAccount(ModelMap model) {
		model.addAttribute("attribute", "redirectWithRedirectPrefix");
		return new ModelAndView("redirect:/account-managment/createAccount", model);
	}
}
