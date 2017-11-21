/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import entity.Place;
import facades.UserFacade;
import java.util.Random;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import facades.PlaceFacade;
import javax.persistence.EntityManagerFactory;
import net.minidev.json.JSONObject;
import security.IUserFacade;

/**
 * REST Web Service
 *
 * @author plaul1
 */
@Path("demoall")
public class All {

  Random r = new Random();    
    
  @Context
  private UriInfo context;
  
  IUserFacade uf;
  Gson gson = new Gson();
  
  /**
   * Creates a new instance of A
   */
  public All() {
      
       uf = new UserFacade(Persistence.createEntityManagerFactory("pu_development"));       
  }
  
  
  /**
   * Retrieves representation of an instance of rest.All
   * @return an instance of java.lang.String
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public String getText() {
    return " {\"message\" : \"result for all\"}";
  }
  
  @GET
  @Path("randomNum")
  @Produces(MediaType.APPLICATION_JSON)
  public String randomNumber(){
      return "{\"message\":\""+r.nextInt(100000) + "\"}";
  }
  
  
  @POST
  @Path("regUser")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public String registerUser(String jsonString){
      JsonObject json = new JsonParser().parse(jsonString).getAsJsonObject();
      String username = json.get("username").getAsString();
      String password = json.get("password").getAsString();
      String regRes = uf.regUser(username, password);
      System.out.println(regRes);
      JSONObject jsonRet = new JSONObject();
      jsonRet.put("message", regRes);
      return jsonRet.toString();     
  }
  
  @POST
  @Path("createPlace")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public void createPlace(String json){
      entity.Place p  = new Place();
      p.setDescription("Lets Test man");
      EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu_development");
      PlaceFacade pFacade = new PlaceFacade(emf);
      pFacade.createPlace(p);
  }
  
  @GET
  @Path("getPlaces")
  @Produces(MediaType.APPLICATION_JSON)
  public String getPlaces(){
     EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu_development");
      PlaceFacade pFacade = new PlaceFacade(emf);
      return gson.toJson(pFacade.getPlaces());
   
  }
  
}
