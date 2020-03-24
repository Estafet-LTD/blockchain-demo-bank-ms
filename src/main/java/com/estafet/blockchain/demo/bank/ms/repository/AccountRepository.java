package com.estafet.blockchain.demo.bank.ms.repository;

import com.estafet.blockchain.demo.bank.ms.model.Account;
import org.springframework.data.couchbase.core.query.N1qlPrimaryIndexed;
import org.springframework.data.couchbase.core.query.ViewIndexed;
import org.springframework.data.couchbase.repository.CouchbasePagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@ViewIndexed(designDoc = "account")
@N1qlPrimaryIndexed
public interface AccountRepository extends CouchbasePagingAndSortingRepository<Account, String> {

    List<Account> findAll();

    Account findByWalletAddress(String walletAddress);
}
