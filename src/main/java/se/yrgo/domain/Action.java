package se.yrgo.domain;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Represents a job that we have to perform as the result of a call.
 * For example: Check with Jim that the new patch will be ready by 15 December 2019.
 */
@Entity
public class Action {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int actionId;


	private String details;


	private Calendar requiredBy;


	private String owningUser;


	private boolean complete;


	public Action(String details, Calendar requiredBy, String owningUser){
		this.details = details;
		this.requiredBy = requiredBy;
		this.owningUser = owningUser;
		this.complete = false;
	}

	public Action(String actionId, String details, Calendar requiredBy, String owningUser, boolean complete){
		this.details = details;
		this.requiredBy = requiredBy;
		this.owningUser = owningUser;
		this.complete = complete;


		this.actionId = new Integer(actionId);
	}


	public boolean isOverdue()	{
		Calendar dateNow = new java.util.GregorianCalendar();

		return dateNow.after(this.requiredBy);
	}


	public String toString()	{
		return "Action for " + this.owningUser + ": " + this.details + ", required by " + this.requiredBy.getTime();
	}


	public void completeAction()	{
		this.complete = true;
	}


	public boolean isComplete() 	{
		return this.complete;
	}

	public String getOwningUser() {
		return owningUser;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Calendar getRequiredBy() {
		return requiredBy;
	}

	public void setRequiredBy(Calendar requiredBy) {
		this.requiredBy = requiredBy;
	}

	public void setOwningUser(String owningUser) {
		this.owningUser = owningUser;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public int getActionId() {
		return actionId;
	}

	public void setActionId(int actionId) {
		this.actionId = actionId;
	}


	public Action() {}
}
