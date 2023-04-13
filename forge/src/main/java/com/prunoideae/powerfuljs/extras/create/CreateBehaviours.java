package com.prunoideae.powerfuljs.extras.create;

import com.prunoideae.powerfuljs.extras.create.behavior.BeltProcessingBehaviourBuilder;
import com.prunoideae.powerfuljs.extras.create.behavior.CreateBehaviourBuilder;

public interface CreateBehaviours {
    static CreateBehaviourBuilder.Simple custom() {
        return new CreateBehaviourBuilder.Simple();
    }

    static BeltProcessingBehaviourBuilder beltProcessing() {
        return new BeltProcessingBehaviourBuilder();
    }
}
