package com.bank.test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.bank.service.AccountService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountTest {

	@Autowired
	AccountService accService;

}
