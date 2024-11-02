 package se.yrgo.services.diary;

import java.util.List;

import se.yrgo.domain.Action;

public interface DiaryManagementService {
	/**
	 * Records an action in the diary
	 */
	public void recordAction(Action action);

	/**
	 * Gets all actions for a particular user that have not yet been complete
	 */
	public List<Action> getAllIncompleteActions(String requiredUser);
}
