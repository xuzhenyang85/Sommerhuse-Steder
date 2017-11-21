package rest;

import com.google.gson.Gson;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import security.IUser;
import security.IUserFacade;
import security.UserFacadeFactory;

@Path("demoadmin")
@RolesAllowed("Admin")
public class Admin {
  
  IUserFacade facadeInterface = UserFacadeFactory.getInstance();  
    
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public String getSomething(){
    String now = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(new Date());
    return "{\"message\" : \"Hello Admin from server (call accesible by only authenticated ADMINS)\",\n"+"\"serverTime\": \""+now +"\"}"; 
  }
 
  @GET
  @Path("/userlist")
  @Produces(MediaType.APPLICATION_JSON)  
  public String getAllUsers(){
      try {
            String jsonString = "{\"users\":[\"";
            List<IUser> users = facadeInterface.getAllUsers();
            for (int i = 0; i < users.size(); i++) {
                jsonString += users.get(i).getUserName();
                if (i == users.size() - 1) {
                    jsonString += "\"]}";
                } else {
                    jsonString += "\",\"";
                }
            }
            System.out.println(jsonString);
            return jsonString;
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return null;
  }
  
}