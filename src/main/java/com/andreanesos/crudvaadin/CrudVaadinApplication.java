package com.andreanesos.crudvaadin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CrudVaadinApplication {

    
    Logger log = LoggerFactory.getLogger(CrudVaadinApplication.class);
    
	public static void main(String[] args) {
		SpringApplication.run(CrudVaadinApplication.class, args);
	}
	
	@Bean
	CommandLineRunner loadData(CustomerRepository customerRepository) {
	
	    return args -> {
	        
	        //save customers in database
	        customerRepository.save(new Customer("Giampiero", "Galeazzi"));
	        customerRepository.save(new Customer("Marco", "Tufettti"));
	        customerRepository.save(new Customer("Antonio", "Scamorza"));
	        customerRepository.save(new Customer("Franco", "Alpini"));
	        customerRepository.save(new Customer("Luca", "Fernelli"));
	        
	     
            
	        //fetch  all customer in log
	        log.info("Customers found by findAll():");
	        customerRepository.findAll()
	            .stream()
	            .forEach( customer -> 
	                        log.info(customer.toString()));
	        
	     // fetch an individual customer by ID
            log.info("Customer found with findById(1L):");
            log.info(customerRepository.findById(1L).get()
                            .toString());
	        
	        //fetch customer findbylastname in log
	        log.info("Customer found by findbyLasrtName(Alpini):");
	        customerRepository.findByLastNameStartsWithIgnoreCase("Alpini")
	            .stream()
	            .forEach( alpini -> 
	                        log.info(alpini.toString()));
	        
	    };
	}

}
