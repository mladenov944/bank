package com.bank.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.bank.model.Account;

public interface AccountRepository extends PagingAndSortingRepository<Account, Long> {

}
