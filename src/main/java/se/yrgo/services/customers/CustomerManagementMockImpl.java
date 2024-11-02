package se.yrgo.services.customers;

import java.util.HashMap;
import java.util.List;

import se.yrgo.domain.Call;
import se.yrgo.domain.Customer;

/**
 * Mock implementation of the CustomerManagementService
 */
public class CustomerManagementMockImpl implements CustomerManagementService {
    private final HashMap<String, Customer> CUSTOMER_MAP;

    public CustomerManagementMockImpl() {
        CUSTOMER_MAP = new HashMap<>();
        CUSTOMER_MAP.put("OB74", new Customer("OB74", "Fargo Ltd", "some notes"));
        CUSTOMER_MAP.put("NV10", new Customer("NV10", "North Ltd", "some other notes"));
        CUSTOMER_MAP.put("RM210", new Customer("RM210", "River Ltd", "some more notes"));
    }

    @Override
    public void newCustomer(Customer newCustomer) throws IllegalArgumentException {
        // Do not add the new customer if they already exist
        if (CUSTOMER_MAP.containsKey(newCustomer.getCustomerId())) {
            throw new IllegalArgumentException("Customer with that ID already exists");
        }
        CUSTOMER_MAP.put(newCustomer.getCustomerId(), newCustomer);
    }

    @Override
    public void updateCustomer(Customer changedCustomer) {
        if (!CUSTOMER_MAP.containsKey(changedCustomer.getCustomerId())) {
            // Would like to throw "CustomerNotFoundException" here, but I don't want to change code in interface
            // Fail silently
            return;
        }
        CUSTOMER_MAP.replace(changedCustomer.getCustomerId(), changedCustomer);
    }

    @Override
    public void deleteCustomer(Customer oldCustomer) {
        if (!CUSTOMER_MAP.containsKey(oldCustomer.getCustomerId())) {
            // Would like to throw "CustomerNotFoundException" here, but I don't want to change code in interface
            // Fail silently
            return;
        }
        CUSTOMER_MAP.remove(oldCustomer.getCustomerId());
    }

    @Override
    public Customer findCustomerById(String customerId) throws CustomerNotFoundException {
        var customer = CUSTOMER_MAP.get(customerId);
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
        return CUSTOMER_MAP.values().stream()
                .filter(x -> x.getCompanyName().equalsIgnoreCase(name))
                .toList();
    }

    @Override
    public List<Customer> getAllCustomers() {
        return CUSTOMER_MAP.values().stream()
                .toList();
    }

    @Override
    public Customer getFullCustomerDetail(String customerId) throws CustomerNotFoundException {
        var customer = CUSTOMER_MAP.get(customerId);
        if (customer == null) {
            throw new CustomerNotFoundException();
        }
        // Return full customer details, including calls
        return customer;
    }

    @Override
    public void recordCall(String customerId, Call callDetails) throws CustomerNotFoundException {
        var customer = CUSTOMER_MAP.get(customerId);
        if (customer == null) {
            throw new CustomerNotFoundException();
        } else if (callDetails == null) {
            throw new IllegalArgumentException("Call cannot be null");
        }
        customer.addCall(callDetails);
    }
}
