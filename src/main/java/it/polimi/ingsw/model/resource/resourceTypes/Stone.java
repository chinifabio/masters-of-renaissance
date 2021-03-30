package it.polimi.ingsw.model.resource.resourceTypes;

import it.polimi.ingsw.model.resource.Resource;

public class Stone  implements Resource {
    @Override
    public boolean isStrorable() {
        return true;
    }

    @Override
    public void onObtain() {

    }
}
