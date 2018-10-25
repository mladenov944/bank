package com.bank.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.model.Account;
import com.bank.service.AccountService;
import com.bank.service.UserService;

@RestController
@RequestMapping("/account-managment")
public class AccountController {

	@Autowired
	AccountService accService;

	@Autowired
	UserService userService;

	@RequestMapping(value = "/createAccount", method = RequestMethod.GET)
	public ResponseEntity<String> createAccount(Principal principal) {
		accService.createAccount(principal.getName());
		return new ResponseEntity<String>("Uspeshna registraciq ! ", HttpStatus.OK);
	}

	@RequestMapping(value = "/findAll", method = RequestMethod.POST)
	public ResponseEntity<?> findAll(@RequestParam int page, @RequestParam int size) {
		return new ResponseEntity<Page<Account>>(accService.findAll(PageRequest.of(page, size)), HttpStatus.OK);
	}

}
