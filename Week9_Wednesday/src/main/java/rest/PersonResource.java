package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.PersonDTO;
import dto.PersonsDTO;
import utils.EMF_Creator;
import facades.PersonFacade;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
    
    @Path("/allPersons")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllPersons() {
        PersonsDTO result = FACADE.getAllPersons();
        return GSON.toJson(result);
    }
    
    @Path("/id/{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getPersonByID(@PathParam ("id") int id) {
        try {
            PersonDTO result = FACADE.getPerson(id);
            return GSON.toJson(result);
        } catch (NoResultException ex) {
            return GSON.toJson(null);
        }
    }
    
    //*** EGEN NOTE:       URL-eksempel på dette er: http://localhost:8080/jpareststarter/api/person/addPerson?fName=a&lName=b&phone=1234 for at indsætte værdierne!
    @Path("/addPerson")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String addNewPerson(@QueryParam("fName") String fName, @QueryParam("lName") String lName, @QueryParam("phone") String phone) {
        PersonDTO result = FACADE.addPerson(fName, lName, phone);
        return GSON.toJson(result);
    }
    
    /*@Path("/edit")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String editPerson() {
        PersonDTO result = FACADE.editPerson(p);
    }*/
    
    @Path("/delete/{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String deletePersonByID(@PathParam ("id") int id) {
        try {
            PersonDTO result = FACADE.deletePerson(id);
            return GSON.toJson(result+" \nis now deleted");
        } catch (NoResultException ex) {
            return GSON.toJson(null);
        }
    }
}