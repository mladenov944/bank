package com.bank.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
@Entity
@Table(name = "account")
//@NamedQuery(name = "acc.findAccByAccId", query = "SELECT a FROM account a WHERE a.id = :aId")
public class Account extends BaseEntity {

	@Column
	private BigDecimal balance;

	@ManyToOne
	@JoinColumn(nullable = false, name = "user_id")
	private User user;

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	@JsonIgnore
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
