package se.yrgo.services.customers;

import java.util.List;

import se.yrgo.domain.Call;
import se.yrgo.domain.Customer;


public interface CustomerManagementService {

	public void newCustomer(Customer newCustomer);


	public void updateCustomer(Customer changedCustomer);


	public void deleteCustomer(Customer oldCustomer);


	public Customer findCustomerById(String customerId) throws CustomerNotFoundException;

	public List<Customer> findCustomersByName (String name);

	public List<Customer> getAllCustomers();


	public Customer getFullCustomerDetail(String customerId) throws CustomerNotFoundException;

	
	public void recordCall(String customerId, Call callDetails) throws CustomerNotFoundException;
}
