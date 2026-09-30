package com.example.user_management.service;
import com.example.user_management.entity.Customer;
import com.example.user_management.repository.CustomerRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    // CREATE (Hash password with BCrypt before saving!)
    public Customer createCustomer(Customer customer) {
        String hashedPassword = BCrypt.hashpw(customer.getPassword(), BCrypt.gensalt(10));
        customer.setPassword(hashedPassword);
        return repository.save(customer);
    }

    // READ ALL
    public List<Customer> getAllCustomers() {
        return repository.findAll();
    }

    // READ ONE
    public Customer getCustomerById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    // UPDATE
    public Customer updateCustomer(Long id, Customer updatedData) {
        Customer existing = getCustomerById(id);
        existing.setFirstName(updatedData.getFirstName());
        existing.setLastName(updatedData.getLastName());
        existing.setEmail(updatedData.getEmail());
        existing.setActive(updatedData.getActive());

        // If password is changed, hash it again
        if (updatedData.getPassword() != null && !updatedData.getPassword().isEmpty()) {
            existing.setPassword(BCrypt.hashpw(updatedData.getPassword(), BCrypt.gensalt(10)));
        }
        return repository.save(existing);
    }

    // DELETE
    public void deleteCustomer(Long id) {
        repository.deleteById(id);
    }
}