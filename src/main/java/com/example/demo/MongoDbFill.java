package com.example.demo;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class MongoDbFill {

    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionrepository;
    private final AccountTypesRepository accountTypesRepository;

    private List<String> filesPaths;

    public MongoDbFill(CustomerRepository customerRepository, TransactionRepository transactionrepository, AccountTypesRepository accountTypesRepository, String[] args) {
        this.customerRepository = customerRepository;
        this.transactionrepository = transactionrepository;
        this.accountTypesRepository = accountTypesRepository;
        this.filesPaths= Arrays.asList(args);
    }

    public void createDatabase() throws FileNotFoundException {
        customerRepository.deleteAll();
        transactionrepository.deleteAll();
        accountTypesRepository.deleteAll();

        CSVParser parser = new CSVParser();
        List<Customer> listOfCustomersToAdd = new LinkedList<>();
        List<AccountTypes> listOfAccountTypesToAdd = new LinkedList<>();
        List<Transactions> listOfTransactionsToAdd = new LinkedList<>();

        List<List<String>> customerResult = parser.getFromCSV(filesPaths.stream().filter(c->c.contains("customers")).collect(Collectors.joining()));
        List<List<String>> transactionsResult = parser.getFromCSV(filesPaths.stream().filter(c->c.contains("transaction")).collect(Collectors.joining()));
        List<List<String>> accountTypesResult = parser.getFromCSV(filesPaths.stream().filter(c->c.contains("account")).collect(Collectors.joining()));
        customerResult.remove(0);
        transactionsResult.remove(0);
        accountTypesResult.remove(0);

        for (List<String> list : customerResult) {
            int count = 0;
            try {
                Customer c = new Customer(list.get(count), list.get(count + 1), list.get(count + 2), list.get(count + 3));
                listOfCustomersToAdd.add(c);
            } catch (IndexOutOfBoundsException e) {
                System.out.println(e);
            }
        }

        for (List<String> list : transactionsResult) {
            int count = 0;
            try {
                double transactionValue = 0D;
                String value = list.get(1).replace(',', '.');
                if (!value.equals("")) {
                    transactionValue = Double.parseDouble(value);
                }
                Transactions t = new Transactions(list.get(count), transactionValue, list.get(count + 2), list.get(count + 3), list.get(count + 4));
                listOfTransactionsToAdd.add(t);
            } catch (IndexOutOfBoundsException e) {
                System.out.println(e);
            }
        }

        for (List<String> list : accountTypesResult) {
            int count = 0;
            try {
                AccountTypes a = new AccountTypes(list.get(count), list.get(count + 1));
                listOfAccountTypesToAdd.add(a);
            } catch (IndexOutOfBoundsException e) {
                System.out.println(e);
            }
        }
        customerRepository.saveAll(listOfCustomersToAdd);

        transactionrepository.saveAll(listOfTransactionsToAdd);

        accountTypesRepository.saveAll(listOfAccountTypesToAdd);

    }

}
