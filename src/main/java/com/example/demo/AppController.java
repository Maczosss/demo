package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class AppController {

    private CustomerRepository customerRepository;
    private AccountTypesRepository accountTypesRepository;
    private TransactionRepository transactionRepository;

    @Autowired
    public AppController(CustomerRepository customerRepository, AccountTypesRepository accountTypesRepository, TransactionRepository transactionRepository) {
        this.customerRepository = customerRepository;
        this.accountTypesRepository = accountTypesRepository;
        this.transactionRepository = transactionRepository;
    }

    @RequestMapping(value = "transactionsTable/AccountTypes={id[]}/ClientIds={clientIds[]}", method = RequestMethod.GET)
    List<String> getTransactions(@PathVariable("id[]") Set<String> accountTypes,
                                 @PathVariable("clientIds[]") Set<String> clientIds) {
        Response response = new Response();
        List<Transactions> transactionsList = new LinkedList<>();
        List<Transactions> sortedTransactions = new LinkedList<>();
        boolean allClients = false;
        boolean allAccountTypes = false;
        clientIds.stream().distinct().close();

        if ((accountTypes.contains("ALL") || accountTypes.size() == 0)
                && (clientIds.contains("ALL") || clientIds.size() == 0)) {
            allAccountTypes = true;
            allClients = true;
        }
        else if (accountTypes.contains("ALL") || accountTypes.size() == 0) {
            allAccountTypes = true;
        }
        else if (clientIds.contains("ALL") || clientIds.size() == 0) {
            allClients = true;
        }

        if (allAccountTypes && allClients) {
            transactionsList = transactionRepository.findAll();
            createResponse(response, transactionsList, sortedTransactions);

        }
        if (allAccountTypes && !allClients) {
            for (String id : clientIds) {
                transactionsList.addAll(transactionRepository.findAllByCustomerId(id));
            }
            createResponse(response, transactionsList, sortedTransactions);
        }
        if (!allAccountTypes && allClients) {
            for (String type : accountTypes) {
                transactionsList.addAll(transactionRepository.findAllByAccountType(type));
            }
            createResponse(response, transactionsList, sortedTransactions);
        }
        if (!allAccountTypes && !allClients) {
            for (String id : clientIds) {
                transactionsList.addAll(transactionRepository.findAllByCustomerId(id));
            }
            transactionsList.removeIf(transaction -> !accountTypes.contains(transaction.getAccountType()));

            createResponse(response, transactionsList, sortedTransactions);
        }
        return response.getResponse().size()==0?response.returnEmptyResponse():response.getResponse();
    }

    public void createResponse(Response response, List<Transactions> transactionsList, List<Transactions> sortedTransactions) {
        sortedTransactions = transactionsList.stream().
                sorted(Comparator.comparingDouble(Transactions::getTransactionAmount)).collect(Collectors.toList());
        for (Transactions t : sortedTransactions) {
            AccountTypes at = accountTypesRepository.getAccountTypesByAccountType(t.getAccountType());
            Customer c = customerRepository.findCustomerById(t.getCustomerId());
            if(at!=null && c!=null) {
                response.addToResponse(t.getTransactionDate(), t.getTransactionId(), t.getTransactionAmount(),
                        at.getName(), c.getName(), c.getLastname());
            }
        }
    }
}

