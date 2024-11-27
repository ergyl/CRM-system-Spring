package se.yrgo.dataaccess;

import javax.persistence.*;

import org.springframework.stereotype.Repository;
import se.yrgo.domain.Call;
import se.yrgo.domain.Customer;

import java.util.List;

@Repository
public class CustomerDaoJpaImpl implements CustomerDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(Customer customer) {
        em.persist(customer);
    }

    @Override
    public Customer getById(String customerId) throws RecordNotFoundException {
        final var q = "SELECT customer FROM Customer AS customer"
                + " WHERE customer.customerId=:customerId";
        var result = em.createQuery(q, Customer.class)
                .setParameter("customerId", customerId)
                .getSingleResult();
        if (result == null) {
            throw new RecordNotFoundException();
        }
        return result;
    }

    @Override
    public List<Customer> getByName(String name) throws RecordNotFoundException {
        var result = em.createQuery("SELECT customer FROM Customer AS customer"
                        + " WHERE customer.companyName=:companyName", Customer.class)
                .setParameter("companyName", name)
                .getResultList();

        if (result == null) {
            throw new RecordNotFoundException();
        }
        return result;
    }

    @Override
    public void update(Customer customerToUpdate) throws RecordNotFoundException {
        var existingCustomer = em.find(Customer.class, customerToUpdate.getCustomerId());

        if (existingCustomer == null) {
            throw new RecordNotFoundException();
        }

        existingCustomer.setCompanyName(customerToUpdate.getCompanyName());
        existingCustomer.setEmail(customerToUpdate.getEmail());
        existingCustomer.setTelephone(customerToUpdate.getTelephone());
        existingCustomer.setNotes(customerToUpdate.getNotes());
    }

    @Override
    public void delete(Customer oldCustomer) throws RecordNotFoundException {
        var customerToDelete = em.find(Customer.class, oldCustomer.getCustomerId());
        if (customerToDelete == null) {
            throw new RecordNotFoundException();
        }
        em.remove(customerToDelete);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return em.createQuery("SELECT customer FROM Customer AS customer", Customer.class)
                .getResultList();
    }

    @Override
    public Customer getFullCustomerDetail(String customerId) throws RecordNotFoundException {
        var query = """
                SELECT customer FROM Customer AS customer
                LEFT JOIN FETCH customer.calls
                WHERE customer.customerId =:customerId
                """;
        var customer = em.createQuery(query, Customer.class)
                .setParameter("customerId", customerId)
                .getSingleResult();
        if (customer == null) {
            throw new RecordNotFoundException();
        }
        return customer;
    }

    @Override
    public void addCall(Call newCall, String customerId) throws RecordNotFoundException {
        var existingCustomer = em.find(Customer.class, customerId);
        if (existingCustomer == null) {
            throw new RecordNotFoundException();
        }
        existingCustomer.addCall(newCall);
    }
}
