package se.yrgo.services.customers;

import java.util.List;

import se.yrgo.domain.Call;
import se.yrgo.domain.Customer;

/**
 * Service that only deals with the customers.
 */
public interface CustomerManagementService {

    public void newCustomer(Customer newCustomer);


    public void updateCustomer(Customer changedCustomer) throws CustomerNotFoundException;


    public void deleteCustomer(Customer oldCustomer) throws CustomerNotFoundException;


    public Customer findCustomerById(String customerId) throws CustomerNotFoundException;

    public List<Customer> findCustomersByName(String name) throws CustomerNotFoundException;

    public List<Customer> getAllCustomers();


    public Customer getFullCustomerDetail(String customerId) throws CustomerNotFoundException;


    public void recordCall(String customerId, Call callDetails) throws CustomerNotFoundException;
}
