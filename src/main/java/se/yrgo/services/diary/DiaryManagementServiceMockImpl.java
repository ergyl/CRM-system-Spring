package se.yrgo.services.diary;

import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.yrgo.domain.Action;

@Transactional
@Service
public class DiaryManagementServiceMockImpl implements DiaryManagementService {

    private Set<Action> allActions = new HashSet<>();

    @Override
    public void recordAction(Action action) {
        if (action != null) {
            allActions.add(action);
        }
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
