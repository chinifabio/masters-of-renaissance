package it.polimi.ingsw.model.resource.resourceTypes;

import it.polimi.ingsw.model.resource.Resource;

public class Empty  implements Resource {
    @Override
    public boolean isStrorable() {
        return false;
    }

    @Override
    public void onObtain() {
        // need to throw exception
    }
}
