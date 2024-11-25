package se.yrgo.services.customers;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.yrgo.dataaccess.CustomerDao;
import se.yrgo.dataaccess.RecordNotFoundException;
import se.yrgo.domain.Call;
import se.yrgo.domain.Customer;

import java.util.List;

@Transactional
@Service
public class CustomerServiceProductionImpl implements CustomerManagementService {
    private CustomerDao dao;

    public CustomerServiceProductionImpl(CustomerDao dao) {
        this.dao = dao;
    }

    @Override
    public void newCustomer(Customer newCustomer) {
        dao.create(newCustomer);
    }

    @Override
    public void updateCustomer(Customer changedCustomer) throws CustomerNotFoundException {
        try {
            dao.update(changedCustomer);
        } catch (RecordNotFoundException ex) {
            throw new CustomerNotFoundException();
        }
    }

    @Override
    public void deleteCustomer(Customer oldCustomer) throws CustomerNotFoundException {
        try {
            dao.delete(oldCustomer);
        } catch (RecordNotFoundException ex) {
            throw new CustomerNotFoundException();
        }
    }

    @Override
    public Customer findCustomerById(String customerId) throws CustomerNotFoundException {
        try {
            return dao.getById(customerId);
        } catch (RecordNotFoundException ex) {
            throw new CustomerNotFoundException();
        }
    }

    @Override
    public List<Customer> findCustomersByName(String name) throws CustomerNotFoundException {
        try {
            return dao.getByName(name);
        } catch (RecordNotFoundException ex) {
            throw new CustomerNotFoundException();
        }
    }

    @Override
    public List<Customer> getAllCustomers() {
        return dao.getAllCustomers();
    }

    @Override
    public Customer getFullCustomerDetail(String customerId) throws CustomerNotFoundException {
        try {
            return dao.getFullCustomerDetail(customerId);
        } catch (RecordNotFoundException ex) {
            throw new CustomerNotFoundException();
        }
    }

    @Override
    public void recordCall(String customerId, Call callDetails) throws CustomerNotFoundException {
        try {
            dao.addCall(callDetails, customerId);
        } catch (RecordNotFoundException ex) {
            throw new CustomerNotFoundException();
        }
    }
}
