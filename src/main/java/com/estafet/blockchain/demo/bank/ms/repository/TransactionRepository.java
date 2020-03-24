package com.estafet.blockchain.demo.bank.ms.repository;

import com.estafet.blockchain.demo.bank.ms.model.Transaction;
import org.springframework.data.couchbase.core.query.N1qlPrimaryIndexed;
import org.springframework.data.couchbase.core.query.ViewIndexed;
import org.springframework.data.couchbase.repository.CouchbasePagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
@ViewIndexed(designDoc = "transaction")
@N1qlPrimaryIndexed
public interface TransactionRepository extends CouchbasePagingAndSortingRepository<Transaction, String> {

    Transaction findByWalletTransactionId(String walletTransactionId);
}
