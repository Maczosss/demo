package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.io.FileNotFoundException;
@AutoConfigureBefore
@AutoConfigurationPackage
@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionrepository;
    private final AccountTypesRepository accountTypesRepository;

    public DemoApplication(CustomerRepository customerRepository, TransactionRepository transactionrepository, AccountTypesRepository accountTypesRepository) {
        this.customerRepository = customerRepository;
        this.transactionrepository = transactionrepository;
        this.accountTypesRepository = accountTypesRepository;
    }

//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//        return application.sources(DemoApplication.class);
//    }

    //extends SpringBootServletInitializer

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args){
        MongoDbFill mongo = new MongoDbFill(customerRepository, transactionrepository, accountTypesRepository, args);
        try {
            mongo.createDatabase();
        } catch (FileNotFoundException e) {
            System.out.println("Error could not find files needed for creating database");
        }
    }
}
