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
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class SimpleClient {
    public static void main(String[] args) {
        // try-with-resources to close container in all scenarios
        try (ClassPathXmlApplicationContext container = new ClassPathXmlApplicationContext("application.xml")) {

            CustomerManagementService cms = container.getBean(CustomerManagementService.class);
            DiaryManagementService diaryManagementService = container.getBean(DiaryManagementService.class);
            CallHandlingService callService = container.getBean(CallHandlingService.class);

            // Some code to see if our CMS seems to be working as expected...

            cms.newCustomer(new Customer("CS090933", "Acme",
                    "hello@helloacme.com", "49-249249-20", "VIP customer"));

            var newCall = new Call("Barry asked for 10 % discount on EPTEP system");
            var action1 = new Action("Contact sales department to get discount offer for customer",
                    new GregorianCalendar(2025, Calendar.NOVEMBER, 15), "hargy");
            var action2 = new Action("Call back Barry with discount offer",
                    new GregorianCalendar(2025, Calendar.NOVEMBER, 18), "hargy");

            var actions = new ArrayList<>(List.of(action1, action2));
            Customer customerLight = null;
            Customer customerDetails = null;

            try {
                callService.recordCall("CS090933", newCall, actions);
                customerLight = cms.findCustomerById("CS090933");
                customerDetails = cms.getFullCustomerDetail("CS090933");
            } catch (CustomerNotFoundException ex) {
                System.err.println("That customer does not exist");
            }

            if (customerLight != null) {
                System.out.println("Light customer info:");
                System.out.println(customerLight);
                System.out.println("email: " + customerLight.getEmail());
                System.out.println("phone: " + customerLight.getTelephone());
                System.out.println("notes: " + customerLight.getNotes());
                System.out.println("calls: " + customerLight.getCalls());
                System.out.println("--------------------------");
            }

            if (customerDetails != null) {
                System.out.println("Full customer info:");
                System.out.println(customerDetails);
                System.out.println("email: " + customerDetails.getEmail());
                System.out.println("phone: " + customerDetails.getTelephone());
                System.out.println("notes: " + customerDetails.getNotes());
                System.out.println("calls: " + customerDetails.getCalls());
                System.out.println("--------------------------");
            }


            System.out.println("Here are the outstanding actions:");
            var incompleteActions = diaryManagementService.getAllIncompleteActions("hargy");
            incompleteActions.forEach(System.out::println);
        }
    }
}
