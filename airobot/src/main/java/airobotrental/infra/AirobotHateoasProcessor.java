package airobotrental.infra;

import airobotrental.domain.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class AirobotHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<Airobot>> {

    @Override
    public EntityModel<Airobot> process(EntityModel<Airobot> model) {
        return model;
    }
}
