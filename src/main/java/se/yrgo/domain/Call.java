package se.yrgo.domain;

import java.util.Date;
import java.util.Objects;

import javax.persistence.*;

/**
 * Represents a single call made by a customer.
 * For example: Jim Called from HB at 10.30 at 15 November 2019
 */
@Entity
@Table(name = "CUSTOMER_CALL")
public class Call {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "TIME_AND_DATE")
    private Date timeAndDate;

    private String notes;

    public Call(String notes) {
        // this defaults to a timestamp of "now"
        this(notes, new Date());
    }

    public Call(String notes, Date timestamp) {
        // this defaults to a timestamp of "now"
        this.timeAndDate = timestamp;
        this.notes = notes;
    }


    public Date getTimeAndDate() {
        return timeAndDate;
    }

    public void setTimeAndDate(Date timeAndDate) {
        this.timeAndDate = timeAndDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return this.timeAndDate + " : " + this.notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Call call = (Call) o;
        return Objects.equals(timeAndDate, call.timeAndDate)
                && Objects.equals(notes, call.notes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeAndDate, notes);
    }

    public Call() {
    }
}
