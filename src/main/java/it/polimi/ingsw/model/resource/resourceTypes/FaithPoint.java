package it.polimi.ingsw.model.resource.resourceTypes;

import it.polimi.ingsw.model.resource.Resource;

public class FaithPoint  implements Resource {
    @Override
    public boolean isStrorable() {
        return false;
    }

    @Override
    public void onObtain() {
        // da implementare "faith point obtained"
    }
}
