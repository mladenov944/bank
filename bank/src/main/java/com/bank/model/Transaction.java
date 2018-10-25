package com.bank.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "transaction")
//@NamedQuery(name = "transaction.findTransactionByAccId", query = "SELECT t FROM transaction t WHERE t.id = :tId")
public class Transaction extends BaseEntity {

	@ManyToOne
	@JoinColumn
	private Account srcAccount;

	@Column
	@Enumerated(EnumType.STRING)
	private PaymentType type;

	@ManyToOne
	@JoinColumn
	private Account receiverAccount;

	@Column
	private BigDecimal amount;

	@Column
	private LocalDateTime transactionDate;

	public Account getSrcAccount() {
		return srcAccount;
	}

	public void setSrcAccount(Account srcAccount) {
		this.srcAccount = srcAccount;
	}

	public PaymentType getType() {
		return type;
	}

	public void setType(PaymentType type) {
		this.type = type;
	}

	public Account getReceiverAccount() {
		return receiverAccount;
	}

	public void setReceiverAccount(Account receiverAccount) {
		this.receiverAccount = receiverAccount;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public LocalDateTime getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate() {
		this.transactionDate = LocalDateTime.now();
	}

}
