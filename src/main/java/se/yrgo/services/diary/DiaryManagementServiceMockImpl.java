package se.yrgo.services.diary;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import se.yrgo.domain.Action;

public class DiaryManagementServiceMockImpl implements DiaryManagementService {
	
	private Set<Action>allActions= new HashSet<Action>();

	@Override
	public void recordAction(Action action) {
		// TODO Auto-generated method stub

	}

	//Hint: 
	//Create a list<Action>
	//In the for each loop going through the list use this condition: "if(action.getOwningUser().equals(requiredUser) && !action.isComplete())" to add a new action to the list. 
	public List<Action> getAllIncompleteActions(String requiredUser) {
		// TODO Auto-generated method stub
		return null;
	}

}
