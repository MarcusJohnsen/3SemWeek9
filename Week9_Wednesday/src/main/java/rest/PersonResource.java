package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.PersonDTO;
import dto.PersonsDTO;
import exceptions.PersonNotFoundException;
import utils.EMF_Creator;
import facades.PersonFacade;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("person")
public class PersonResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(
                "pu",
                "jdbc:mysql://localhost:3307/Person",
                "dev",
                "ax2",
                EMF_Creator.Strategy.CREATE);
    
    private static final PersonFacade FACADE =  PersonFacade.getPersonFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
            
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"Hello World\"}";
    }
    
    @Path("count")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getPersonCount() {
        long count = FACADE.getPersonCount();
        return "{\"count\":" + count + "}";
    }
    
    @Path("/all")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllPersons() {
        PersonsDTO result = FACADE.getAllPersons();
        return GSON.toJson(result);
    }
    
    @Path("/id/{id}")
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public String getPersonByID(@PathParam ("id") int id) throws PersonNotFoundException {
        try {
            PersonDTO result = FACADE.getPerson(id);
            return GSON.toJson(result);
        } catch (PersonNotFoundException ex) {
            return GSON.toJson(null);
        }
    }
    
    @Path("/add")
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public String addPerson(String person) {
        PersonDTO p = GSON.fromJson(person, PersonDTO.class);
        PersonDTO result = FACADE.addPerson(p.getFirstName(), p.getLastName(), p.getPhone());
        return GSON.toJson(result);
    }
    
    @Path("/edit")
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public String editPerson(String person) throws PersonNotFoundException {
        try {
        PersonDTO p = GSON.fromJson(person, PersonDTO.class);
        PersonDTO result = FACADE.editPerson(p);
        return GSON.toJson(result);
        } catch (PersonNotFoundException ex) {
            return GSON.toJson(null);
        }
    }
    
    @Path("/delete/{id}")
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public String deletePersonByID(@PathParam ("id") int id) {
        try {
            PersonDTO result = FACADE.deletePerson(id);
            return GSON.toJson(result+"is now deleted");
        } catch (PersonNotFoundException ex) {
            return GSON.toJson(null);
        }
    }
}