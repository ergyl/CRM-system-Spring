package se.yrgo.services.calls;

import java.util.Collection;

import se.yrgo.domain.Action;
import se.yrgo.domain.Call;
import se.yrgo.services.customers.CustomerNotFoundException;

public interface CallHandlingService {
    /**
     * Records a call with the customer management service, and also records
     * any actions in the diary service
     */
    public void recordCall(String customerId, Call newCall, Collection<Action> actions) throws CustomerNotFoundException;
}
