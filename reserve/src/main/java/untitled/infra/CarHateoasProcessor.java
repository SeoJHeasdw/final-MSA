package untitled.infra;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import untitled.domain.*;

@Component
public class CarHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<Car>> {

    @Override
    public EntityModel<Car> process(EntityModel<Car> model) {
        return model;
    }
}
