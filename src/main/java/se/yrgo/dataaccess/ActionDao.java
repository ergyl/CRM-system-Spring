package se.yrgo.dataaccess;

import java.util.List;

import se.yrgo.domain.Action;

public interface ActionDao {
	public void create(Action newAction);
	public List<Action> getIncompleteActions(String userId);
	public void update(Action actionToUpdate) throws RecordNotFoundException;
	public void delete(Action oldAction) throws RecordNotFoundException;
}
