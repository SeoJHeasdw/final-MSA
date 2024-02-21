package airobotrental.domain;

import airobotrental.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//<<< PoEAA / Repository
@RepositoryRestResource(collectionResourceRel = "installs", path = "installs")
public interface InstallRepository
    extends PagingAndSortingRepository<Install, Long> {}
