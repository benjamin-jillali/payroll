package com.pedantic.config;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class Producers {

    @Produces
    //persistence context declared in the persistence.xml with the pu persistence unit 
    @PersistenceContext(unitName = "pu")
    EntityManager entityManager;
}
