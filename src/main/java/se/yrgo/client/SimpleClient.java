package se.yrgo.client;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import se.yrgo.domain.Action;
import se.yrgo.domain.Call;
import se.yrgo.domain.Customer;
import se.yrgo.services.calls.CallHandlingService;
import se.yrgo.services.customers.CustomerManagementService;
import se.yrgo.services.customers.CustomerNotFoundException;
import se.yrgo.services.diary.DiaryManagementService;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class SimpleClient {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext container = new ClassPathXmlApplicationContext("application.xml");

        CustomerManagementService cms = container.getBean(CustomerManagementService.class);
        DiaryManagementService dms = container.getBean(DiaryManagementService.class);
        CallHandlingService chs = container.getBean(CallHandlingService.class);

        // Some code to see if our services seem to be working as expected...
        Call newCall = new Call("North Ltd wants a discount for a new ARI440k system");
        Action action1 = new Action("Give North Ltd 10 % discount for ARI440K system",
                new GregorianCalendar(2024, 11, 04), "Carrie Diaz");
        Action action2 = new Action("Follow up if customer placed an order",
                new GregorianCalendar(2024, 12, 05), "Carrie Diaz");

        var actions = new ArrayList<>(List.of(action1, action2));

        try {
            chs.recordCall("NV10", newCall, actions);
        } catch (CustomerNotFoundException ex) {
            System.err.println("The customer does not exist");
        }


        Customer customer;

        try {
            final var NONE = "---";
            customer = cms.getFullCustomerDetail("NV10");
            System.out.println("Customer details for " + customer.getCompanyName() + ":");
            System.out.println("id: " + customer.getCustomerId());
            System.out.println("company name: " + customer.getCompanyName());
            System.out.println("email: " + (customer.getEmail() == null ? NONE : customer.getEmail()));
            System.out.println("phone: " + (customer.getTelephone() == null ? NONE : customer.getTelephone()));
            System.out.println("notes: " + (customer.getNotes() == null ? NONE : customer.getNotes()));
            System.out.println("registered calls: " + (customer.getCalls() == null ? NONE : customer.getCalls()));
        } catch (CustomerNotFoundException e) {
            System.err.println("The customer does not exist");
        }
        var incompleteActions = dms.getAllIncompleteActions("Carrie Diaz");

        System.out.println(System.lineSeparator() + "All registered actions:");

        incompleteActions.forEach(System.out::println);

        container.close();
    }
}
