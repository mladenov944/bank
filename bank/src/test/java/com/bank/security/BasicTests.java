package com.bank.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.security.auth.login.AccountException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.bank.exceptions.UserException;
import com.bank.model.Account;
import com.bank.model.User;
import com.bank.service.AccountService;
import com.bank.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BasicTests {

	@Autowired
	WebApplicationContext wac;

	@Autowired
	UserService userService;

	@Autowired
	AccountService accService;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	// --------------------------- CONTROLLER TESTS ---------------------------

	@Test
	@WithMockUser(username = "testing", password = "1234", roles = { "USER" })
	public void testListAll() throws Exception {
		this.mockMvc.perform(post("/account-managment/findAll").param("page", "1").param("size", "4")
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))).andExpect(status().isOk());
	}

	@Test
	public void testRegisterUser() throws Exception {
		this.mockMvc
				.perform(post("/user-managment/register")
						.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser(username = "testing", password = "1234", roles = { "USER" })
	public void testDebit() throws Exception {
		this.mockMvc
				.perform(post("/user-managment/debit").param("senderAccount", "1").param("receiverAccount", "1")
						.param("amount", "5").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(status().isOk());
	}

	@Test
	public void testRegisterAccount() throws Exception {
		this.mockMvc
				.perform(post("/account-managment/createAccount")
						.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(status().isMethodNotAllowed());
	}

	@Test
	public void testHello() throws Exception {
		this.mockMvc
				.perform(
						get("/user-managment/hello").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(status().isOk());
	}

	// ------------------------------ SERVICES TESTS ------------------------------

	// shouldn't register because of existing username...
	@Test(expected = PersistenceException.class)
	public void registerExistingUserTest() {
		User test = new User();
		test.setUsername("testing");
		test.setPassword("1234");
		userService.register(test);
	}

	// registering a user
	@Test
	public void registerUserTest() {
		User test = new User();
		test.setUsername("tempp71");
		test.setPassword("1234");
		userService.register(test);
		User temp = (User) userService.loadUserByUsername(test.getUsername());
		assertNotNull(temp);
		assertTrue(temp.getUsername().equals(test.getUsername()));
	}

	// finding an existing user in database
	@Test
	public void findUser() {
		String username = "testing";
		User temp = (User) userService.loadUserByUsername(username);
		assertTrue(temp.getUsername().equals(username));
	}

	// transfer money from one account to another
	@Test
	public void makeTransaction() throws UserException, AccountException, org.omg.CORBA.UserException {
		Account first = accService.findAccById(1L);
		BigDecimal first_money = first.getBalance();

		accService.makeTransaction("testing", 1L, 2L, BigDecimal.valueOf(1));
		Account second = accService.findAccById(1L);
		BigDecimal second_money = second.getBalance();
		assertNotEquals(first_money, second_money);
	}

	// making transaction with money exceeding the balance
	@Test(expected = AccountException.class)
	public void makeTransactionWithothEnoughMoney() throws AccountException, org.omg.CORBA.UserException {
		accService.makeTransaction("testing", 1L, 2L, BigDecimal.valueOf(1000));
	}

//	 creating an Account
	@Test
	public void makeAccountTest() throws AccountException {
		Account temp = accService.createAccount("testing");
		Long tempId = temp.getId();
		Account tempNew = accService.findAccById(tempId);
		assertEquals(tempNew.getId(), tempId);
	}

	// finding username by ID
	@Test(expected = NoResultException.class)
	public void loadByUserNameException() {
		User temp = (User) userService.loadUserByUsername("asddddd");
		temp.getId();
	}

}