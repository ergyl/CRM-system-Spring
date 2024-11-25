package se.yrgo.dataaccess;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import se.yrgo.domain.Action;

import java.util.List;

@Repository
public class ActionDaoJpaImpl implements ActionDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(Action newAction) {
        em.persist(newAction);
    }

    @Override
    public List<Action> getIncompleteActions(String userId) {
        return em.createQuery("SELECT action FROM Action AS action" +
                        " WHERE action.owningUser =:owningUser" +
                        " AND action.complete =:isComplete", Action.class)
                .setParameter("owningUser", userId)
                .setParameter("isComplete", false)
                .getResultList();
    }

    @Override
    public void update(Action actionToUpdate) throws RecordNotFoundException {
        var existingAction = em.find(Action.class, actionToUpdate.getActionId());

        if (existingAction == null) {
            throw new RecordNotFoundException();
        }
        existingAction.setDetails(actionToUpdate.getDetails());
        existingAction.setRequiredBy(actionToUpdate.getRequiredBy());
        existingAction.setOwningUser(actionToUpdate.getOwningUser());
        existingAction.setComplete(actionToUpdate.isComplete());
    }

    @Override
    public void delete(Action oldAction) throws RecordNotFoundException {
        var actionToDelete = em.find(Action.class, oldAction.getActionId());
        if (actionToDelete == null) {
            throw new RecordNotFoundException();
        }
        em.remove(actionToDelete);
    }
}
