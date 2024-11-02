package se.yrgo.services.customers;

import java.util.HashMap;
import java.util.List;

import se.yrgo.domain.Call;
import se.yrgo.domain.Customer;

/**
 * Mock implementation of the CustomerManagementService
 */
public class CustomerManagementMockImpl implements CustomerManagementService {
    private final HashMap<String, Customer> customerMap;

    public CustomerManagementMockImpl() {
        customerMap = new HashMap<>();
        customerMap.put("OB74", new Customer("OB74", "Fargo Ltd", "some notes"));
        customerMap.put("NV10", new Customer("NV10", "North Ltd", "some other notes"));
        customerMap.put("RM210", new Customer("RM210", "River Ltd", "some more notes"));
    }

    @Override
    public void newCustomer(Customer newCustomer) throws IllegalArgumentException {
        // Do not add the new customer if they already exist
        if (customerMap.containsKey(newCustomer.getCustomerId())) {
            throw new IllegalArgumentException("Customer with that ID already exists");
        }
        customerMap.put(newCustomer.getCustomerId(), newCustomer);
    }

    @Override
    public void updateCustomer(Customer changedCustomer) {
        if (!customerMap.containsKey(changedCustomer.getCustomerId())) {
            // Would like to throw "CustomerNotFoundException" here, but I don't want to change code in interface
            // Fail silently
            return;
        }
        customerMap.replace(changedCustomer.getCustomerId(), changedCustomer);
    }

    @Override
    public void deleteCustomer(Customer oldCustomer) {
        if (!customerMap.containsKey(oldCustomer.getCustomerId())) {
            // Would like to throw "CustomerNotFoundException" here, but I don't want to change code in interface
            // Fail silently
            return;
        }
        customerMap.remove(oldCustomer.getCustomerId());
    }

    @Override
    public Customer findCustomerById(String customerId) throws CustomerNotFoundException {
        var customer = customerMap.get(customerId);
        if (customer == null) {
            throw new CustomerNotFoundException();
        }
        // Return customer details excluding calls (which can hold a heavy data load)
        return new Customer(customer.getCustomerId(), customer.getCompanyName(),
                customer.getEmail(), customer.getTelephone(), customer.getNotes());
    }

    @Override
    public List<Customer> findCustomersByName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Company name cannot be null or empty");
        }
        return customerMap.values().stream()
                .filter(x -> x.getCompanyName().equalsIgnoreCase(name))
                .toList();
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerMap.values().stream()
                .toList();
    }

    @Override
    public Customer getFullCustomerDetail(String customerId) throws CustomerNotFoundException {
        var customer = customerMap.get(customerId);
        if (customer == null) {
            throw new CustomerNotFoundException();
        }
        // Return full customer details, including calls
        return customer;
    }

    @Override
    public void recordCall(String customerId, Call callDetails) throws CustomerNotFoundException {
        var customer = customerMap.get(customerId);
        if (customer == null) {
            throw new CustomerNotFoundException();
        } else if (callDetails == null) {
            throw new IllegalArgumentException("Call cannot be null");
        }
        customer.addCall(callDetails);
    }
}
