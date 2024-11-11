package se.yrgo.dataaccess;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import se.yrgo.domain.Call;
import se.yrgo.domain.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CustomerDaoJdbcTemplateImpl implements CustomerDao {
    private JdbcTemplate jdbcTemplate;

    private static final String CREATE_CUSTOMER_TABLE_SQL = """
                CREATE TABLE CUSTOMER (
                    CUSTOMER_ID VARCHAR(10),
                    COMPANY_NAME VARCHAR(100),
                    EMAIL VARCHAR(200),
                    PHONE VARCHAR(50),
                    NOTES VARCHAR(255),
                    PRIMARY KEY (CUSTOMER_ID)
                )
            """;

    private static final String CREATE_CALL_TABLE_SQL = """
                CREATE TABLE CUSTOMER_CALL (
                    CALL_ID INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1),
                    CUSTOMER_ID VARCHAR(10),
                    TIME_AND_DATE TIMESTAMP,
                    NOTES VARCHAR(255),
                    PRIMARY KEY (CALL_ID),
                    FOREIGN KEY (CUSTOMER_ID) REFERENCES CUSTOMER(CUSTOMER_ID)
                )
            """;

    private static final String INSERT_SQL = """
            INSERT INTO CUSTOMER(CUSTOMER_ID, COMPANY_NAME, EMAIL, PHONE, NOTES) 
            VALUES (?,?,?,?,?)
            """;
    private static final String UPDATE_SQL = """
            UPDATE CUSTOMER SET COMPANY_NAME=?, EMAIL=?, PHONE=?, NOTES=? WHERE CUSTOMER_ID=?
            """;
    private static final String DELETE_SQL = """
            DELETE FROM CUSTOMER WHERE CUSTOMER_ID=?
            """;
    private static final String GET_INCOMPLETE_SQL = """
            SELECT CUSTOMER_ID, COMPANY_NAME, EMAIL, PHONE, NOTES FROM CUSTOMER WHERE CUSTOMER_ID=?
            """;
    private static final String GET_BY_NAME_SQL = """
            SELECT CUSTOMER_ID, COMPANY_NAME, EMAIL, PHONE FROM CUSTOMER WHERE COMPANY_NAME=?
            """;
    private static final String GET_ALL_SQL = """
            SELECT CUSTOMER_ID, COMPANY_NAME, EMAIL, PHONE FROM CUSTOMER
            """;
    private static final String GET_COMPLETE_SQL =
            """
                    SELECT
                        c.CUSTOMER_ID,
                        c.COMPANY_NAME,
                        c.EMAIL,
                        c.PHONE,
                        c.NOTES,
                        ca.TIME_AND_DATE AS CALL_TIME,
                        ca.NOTES AS CALL_NOTES
                    FROM CUSTOMER c
                    LEFT JOIN CUSTOMER_CALL ca ON c.CUSTOMER_ID = ca.CUSTOMER_ID
                    WHERE c.CUSTOMER_ID = ?
                    """;

    private static final String ADD_CALL_SQL = """
            INSERT INTO CUSTOMER_CALL(CUSTOMER_ID, TIME_AND_DATE, NOTES)
            VALUES (?,?,?)
            """;

    public CustomerDaoJdbcTemplateImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private void createTables() {
        try {
            jdbcTemplate.update(CREATE_CUSTOMER_TABLE_SQL);
            jdbcTemplate.update(CREATE_CALL_TABLE_SQL);
        } catch (org.springframework.jdbc.BadSqlGrammarException ex) {
            System.err.println("The CUSTOMER table already exists.");
        }
    }

    @Override
    public void create(Customer customer) {
        jdbcTemplate.update(INSERT_SQL, customer.getCustomerId(),
                customer.getCompanyName(), customer.getEmail(),
                customer.getTelephone(), customer.getNotes());
    }

    @Override
    public Customer getById(String customerId) throws RecordNotFoundException {
        return jdbcTemplate.queryForObject(GET_INCOMPLETE_SQL, new CustomerLightRowMapper(), customerId);
    }

    @Override
    public List<Customer> getByName(String name) {
        return jdbcTemplate.query(GET_BY_NAME_SQL, new CustomerCompleteRowMapper(), name, false);
    }

    @Override
    public void update(Customer customerToUpdate) throws RecordNotFoundException {
        jdbcTemplate.update(UPDATE_SQL, customerToUpdate.getCustomerId(),
                customerToUpdate.getCompanyName(), customerToUpdate.getTelephone(),
                customerToUpdate.getEmail(), customerToUpdate.getNotes());
    }

    @Override
    public void delete(Customer oldCustomer) throws RecordNotFoundException {
        jdbcTemplate.update(DELETE_SQL, oldCustomer.getCustomerId());
    }

    @Override
    public List<Customer> getAllCustomers() {
        return jdbcTemplate.query(GET_ALL_SQL, new CustomerCompleteRowMapper(), false);
    }

    @Override
    public Customer getFullCustomerDetail(String customerId) throws RecordNotFoundException {
        List<Customer> customers = jdbcTemplate.query(GET_COMPLETE_SQL, new CustomerCompleteRowMapper(), customerId);

        if (customers.isEmpty()) {
            throw new RecordNotFoundException();
        }

        return customers.get(0);  // Since we expect only one customer, return the first (and only) result
    }

    @Override
    public void addCall(Call newCall, String customerId) throws RecordNotFoundException {
        jdbcTemplate.update(ADD_CALL_SQL, customerId, newCall.getTimeAndDate(), newCall.getNotes());
    }

    private class CustomerLightRowMapper implements RowMapper<Customer> {
        @Override
        public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
            String customerId = rs.getString(1);
            String companyName = rs.getString(2);
            String email = rs.getString(3);
            String phone = rs.getString(4);
            String notes = rs.getString(5);

            return new Customer("" + customerId, companyName,
                    email, phone, notes);
        }
    }

    private class CustomerCompleteRowMapper implements RowMapper<Customer> {
        @Override
        public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
            String customerId = rs.getString(1);
            String companyName = rs.getString(2);
            String email = rs.getString(3);
            String phone = rs.getString(4);
            String customerNotes = rs.getString(5);

            Customer customer = new Customer("" + customerId, companyName,
                    email, phone, customerNotes);

            List<Call> calls = new ArrayList<>();

            do {
                Date callTime = rs.getTimestamp("CALL_TIME");
                String callNotes = rs.getString("CALL_NOTES");

                if (callTime != null) {
                    Call call = new Call(callNotes, callTime);
                    calls.add(call);
                }

            } while (rs.next() && rs.getString("CUSTOMER_ID").equals(customerId));

            customer.setCalls(calls);

            return customer;
        }
    }

}
