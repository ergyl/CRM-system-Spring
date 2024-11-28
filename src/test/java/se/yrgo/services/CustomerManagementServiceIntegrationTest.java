package se.yrgo.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import se.yrgo.domain.Call;
import se.yrgo.domain.Customer;
import se.yrgo.services.customers.CustomerManagementService;
import se.yrgo.services.customers.CustomerNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
class CustomerManagementServiceIntegrationTest {
    private ClassPathXmlApplicationContext context;
    private CustomerManagementService cms;
    private static final String customerId = "CS010101";

    @BeforeEach
    void setup() {
        context = new ClassPathXmlApplicationContext("application-test.xml");
        cms = context.getBean(CustomerManagementService.class);
    }

    @AfterEach
    void destroy() {
        if (context != null) {
            context.close();
        }
    }

    // Register a new customer
    @Test
    void testNewCustomer() {
        Customer newCustomer = new Customer(customerId, "TestCompany",
                "hello@testcompany.com", "49-000000-01", "First customer");
        cms.newCustomer(newCustomer);

        Customer foundCustomer = null;
        try {
            foundCustomer = cms.findCustomerById(customerId);
            assertEquals(newCustomer, foundCustomer);
            assertEquals(newCustomer.getEmail(), foundCustomer.getEmail());
            assertEquals(newCustomer.getTelephone(), foundCustomer.getTelephone());
            assertEquals(newCustomer.getNotes(), foundCustomer.getNotes());
        } catch (CustomerNotFoundException ex) {
            fail("The created customer was not found!");
        }
    }

    // Test adding a call to a customer
    @Test
    void testRecordCall() {
        Customer newCustomer = new Customer(customerId, "TestCompany",
                "hello@testcompany.com", "49-000000-01", "First customer");
        cms.newCustomer(newCustomer);

        Call newCall = new Call("Complaint about bla bla.");
        Call foundCall = null;
        try {
            cms.recordCall(customerId, newCall);
            Customer customer = cms.getFullCustomerDetail(customerId);
            foundCall = customer.getCalls().get(0);
            assertEquals(newCall, foundCall);
        } catch (CustomerNotFoundException e) {
            fail("Could not add call to the customer.");
        }
    }

    // Find an existing customer
    @Test
    void testGetFullCustomerDetail() {
        Customer newCustomer = new Customer(customerId, "TestCompany",
                "A note about the company");
        cms.newCustomer(newCustomer);

        Call newCall = new Call("Complaint about bla bla.");

        Customer foundCustomer = null;
        try {
            cms.recordCall(customerId, newCall);
        } catch (CustomerNotFoundException e) {
            fail("Could not add call to the customer.");
        }

        try {
            foundCustomer = cms.getFullCustomerDetail(customerId);
            assertNull(foundCustomer.getEmail());
            assertNull(foundCustomer.getTelephone());
            assertNotNull(foundCustomer.getCalls());
            assertEquals(newCustomer, foundCustomer);
        } catch (CustomerNotFoundException e) {
            fail("Could not retrieve full details about the customer.");
        }
    }
}
