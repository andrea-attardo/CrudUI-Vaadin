package com.andreanesos.crudvaadin;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long>{
   
   //non ho bisogno di srivere implemntazione lo fa spring at runtime
   //parsa i methodi anche dal nome 
   List<Customer> findByLastNameStartsWithIgnoreCase(String lastName);
}
