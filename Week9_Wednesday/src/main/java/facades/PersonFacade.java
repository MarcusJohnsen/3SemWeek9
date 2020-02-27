package facades;

import dto.PersonDTO;
import dto.PersonsDTO;
import entities.Person;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

public class PersonFacade implements IPersonFacade {

    private static PersonFacade instance;
    private static EntityManagerFactory emf;
    private static EntityManager em;
    
    private PersonFacade() {}
    
    public static PersonFacade getPersonFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonFacade();
        }
        return instance;
    }
    
    @Override
    public PersonDTO getPerson(int id) {
       em = emf.createEntityManager();
       try {
           TypedQuery<Person> tq = em.createQuery("SELECT p from Person p WHERE p.id=:id", Person.class);
           tq.setParameter("id", id);
           Person person = tq.getSingleResult();
           PersonDTO result = new PersonDTO(person);
           return result;
       } finally {
           em.close();
       }
    }
    
    //Kan ikke få det her til at virke pga "PersonsDTO"-klassen, som jeg ikke ved hvordan jeg skal bruge
    @Override
    public PersonsDTO getAllPersons() {
        em = emf.createEntityManager();
        try {
            TypedQuery<PersonsDTO> tq = em.createQuery("SELECT p FROM Person p", PersonsDTO.class);
            return tq.getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public PersonDTO addPerson(String fName, String lName, String phone){
        em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Person person = new Person (fName, lName, phone);
            em.persist(person);
            em.getTransaction().commit();
            PersonDTO result = new PersonDTO(person);
            return result;
        } finally {
            em.close();
        }
    }
    
    @Override
    public PersonDTO editPerson(PersonDTO p) {
        em = emf.createEntityManager();
        /*try {
            Person person = em.find(Person.class, p);
            em.getTransaction().begin();
            person.setFirstName(firstName);
        }*/
        return null;
    }
    
    @Override
    public PersonDTO deletePerson(int id) {
        em = emf.createEntityManager();
        try {
            Person person = em.find(Person.class, id);
            em.getTransaction().begin();
            em.remove(person);
            em.getTransaction().commit();
            PersonDTO result = new PersonDTO(person);
            return result;
        } finally {
            em.close();
        } 
    }
}