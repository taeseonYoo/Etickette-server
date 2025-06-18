package com.tae.Etickette.venue.query;

import com.tae.Etickette.venue.command.domain.VenueStatus;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.SingularAttribute;
import org.springframework.data.jpa.domain.Specification;

public class VenueDataSpecs {
    public static Specification<VenueData> statusActive(VenueStatus status) {
        return (Root<VenueData> root, CriteriaQuery<?> query, CriteriaBuilder cb)
                -> cb.equal(root.get("status"), status);
    }
}
