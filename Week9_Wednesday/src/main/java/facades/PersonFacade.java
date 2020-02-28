package facades;

import dto.PersonDTO;
import dto.PersonsDTO;
import entities.Person;
import exceptions.PersonNotFoundException;
import java.util.Date;
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
    
    public long getPersonCount() {
        EntityManager em = emf.createEntityManager();
        try {
            int personCount = (int) em.createQuery("SELECT COUNT(p) FROM Person p").getSingleResult();
            return personCount;
        } finally {
            em.close();
        }
    }
    
    @Override
    public PersonDTO getPerson(int id) throws PersonNotFoundException {
       em = emf.createEntityManager();
       try {
           TypedQuery<Person> tq = em.createQuery("SELECT p from Person p WHERE p.id=:id", Person.class);
           tq.setParameter("id", id);
           Person person = tq.getSingleResult();
           PersonDTO result = new PersonDTO(person);
           return result;
       } catch (Exception e) {
            throw new PersonNotFoundException("Couldn't find the person in question. Try another ID");
        } finally {
           em.close();
       }
    }
    
    @Override
    public PersonsDTO getAllPersons() {
        em = emf.createEntityManager();
        try {
            TypedQuery<Person> tq = em.createQuery("SELECT p FROM Person p", Person.class);
            List<Person> personList = tq.getResultList();
            PersonsDTO result = new PersonsDTO(personList);
            return result;
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
    public PersonDTO editPerson(PersonDTO p) throws PersonNotFoundException {
        em = emf.createEntityManager();
        try {
            Person person = em.find(Person.class, p);
            em.getTransaction().begin();
            person.setLastEdited(new Date());
            person.setFirstName(p.getFirstName());
            person.setLastName(p.getLastName());
            person.setFirstName(p.getPhone());
            em.getTransaction().commit();
            p = new PersonDTO(person);
            return p;
        } catch (Exception e) {
            throw new PersonNotFoundException("Deletion wasn't possible, person doesn't exist");
        } finally {
            em.close();
        }
    }
    
    @Override
    public PersonDTO deletePerson(int id) throws PersonNotFoundException {
        em = emf.createEntityManager();
        try {
            Person person = em.find(Person.class, id);
            em.getTransaction().begin();
            em.remove(person);
            em.getTransaction().commit();
            PersonDTO result = new PersonDTO(person);
            return result;
        } catch (Exception e) {
            throw new PersonNotFoundException("Edit wasn't possible, check if the right person was chosen");
        } finally {
            em.close();
        } 
    }
}