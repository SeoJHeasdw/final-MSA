package airobotrental.domain;

import airobotrental.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//<<< PoEAA / Repository
@RepositoryRestResource(collectionResourceRel="airobots", path="airobots")
public interface AirobotRepository extends PagingAndSortingRepository<Airobot, >{
}