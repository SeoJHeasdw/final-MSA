package airobotrental.infra;

import airobotrental.domain.*;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(
    collectionResourceRel = "searchOrders",
    path = "searchOrders"
)
public interface SearchOrderRepository
    extends PagingAndSortingRepository<SearchOrder, Long> {}
