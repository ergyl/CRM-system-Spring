package se.yrgo.services.diary;

import java.util.*;

import se.yrgo.domain.Action;

public class DiaryManagementServiceMockImpl implements DiaryManagementService {

    private Set<Action> allActions = new HashSet<>();

    @Override
    public void recordAction(Action action) {


    }

    public List<Action> getAllIncompleteActions(String requiredUser) {
        var actions = new ArrayList<Action>();
        var currentDate = new GregorianCalendar();

        allActions.forEach(x -> {
            if (x.getOwningUser().equals(requiredUser)
                    && !x.isComplete()
                    && currentDate.before(x.getRequiredBy())) {
                actions.add(x);
            }
        });
        return actions;
    }

}
