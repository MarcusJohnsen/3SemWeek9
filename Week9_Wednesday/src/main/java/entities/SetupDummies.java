package entities;

import facades.PersonFacade;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import utils.EMF_Creator;

public class SetupDummies {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(
            "pu",
            "jdbc:mysql://localhost:3307/Person",
            "dev",
            "ax2",
            EMF_Creator.Strategy.DROP_AND_CREATE);

    private static final PersonFacade FACADE = PersonFacade.getPersonFacade(EMF);

    public static void main(String[] args) {
        EntityManager em = EMF.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        addPersons();
    }

    private static void addPersons() {
        FACADE.addPerson("Marcus", "Johnsen", "85712952"); FACADE.addPerson("Andreas", "Petersen", "12561852");
    }
}