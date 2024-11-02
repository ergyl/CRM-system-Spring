package se.yrgo.client;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import se.yrgo.domain.Customer;
import se.yrgo.services.customers.CustomerManagementService;

public class SimpleClient {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext container = new ClassPathXmlApplicationContext("application.xml");
        CustomerManagementService customers = container.getBean(CustomerManagementService.class);

        var allCustomers = customers.getAllCustomers();

        for (Customer customer : allCustomers) {
            System.out.println(customer);
        }
    }
}
