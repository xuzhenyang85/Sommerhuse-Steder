package deploy;

// import entity.Place;
import entity.Place;
import entity.Role;
import entity.User;
import facades.UserFacade;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import security.Secret;

@WebListener
public class DeploymentConfiguration implements ServletContextListener {

  public static String PU_NAME = "PU-Local";

  @Override
  @SuppressWarnings("empty-statement")
  public void contextInitialized(ServletContextEvent sce) {
    System.out.println("######################################################################################");
    System.out.println("############################ In ContextIntialized ####################################");
    System.out.println("######################################################################################");

    //Handling init-params from the properties file (secrets that should not be pushed to GIT)
    InputStream input = null;
    Properties prop = new Properties();
    try {
      input = getClass().getClassLoader().getResourceAsStream("/config.properties");;
      if (input == null) {
        System.out.println("Could not load init-properties");
        return;
      }
      prop.load(input);
      Secret.SHARED_SECRET = prop.getProperty("tokenSecret").getBytes();
      input.close();

    } catch (IOException ex) {
      Logger.getLogger(DeploymentConfiguration.class.getName()).log(Level.SEVERE, null, ex);
    }
    ServletContext context = sce.getServletContext();
 
    boolean makeTestUser = context.getInitParameter("makeTestUser").toLowerCase().equals("true");
    if (makeTestUser) {
      EntityManager em = Persistence.createEntityManagerFactory("pu_development").createEntityManager();
      try {
        System.out.println("Creating TEST Users");
        if (em.find(User.class, "user") == null) {
          // Populate with default users/roles
          em.getTransaction().begin();
          Role userRole = new Role("User");
          Role adminRole = new Role("Admin");
          User user = new User("user", "test");
          user.addRole(userRole);
          User admin = new User("admin", "test");
          admin.addRole(adminRole);
          User both = new User("user_admin", "test");
          both.addRole(userRole);
          both.addRole(adminRole);
          //Populate with dummy places
          Place p = new Place();
          p.setDescription("Et sted i provinsen");
          p.setImageURL("Somewhere/nice/billede.jpg");
         p.setLatitude("55.783282");
         p.setLongtitude("12.502484");
         p.setRating("1");
         p.setStreet("Egesvinget 11");
         p.setZip("2730");
         
          Place p2 = new Place();
          p2.setDescription("Et sted i lyngby");
          p2.setImageURL("etsted/ilyngby/billede2.jpg");
         p2.setLatitude("55.783282");
         p2.setLongtitude("12.502484");
         p2.setRating("2");
         p2.setStreet("Caroline Amalie Vej 35");
         p2.setZip("2800");
          
         
         
         //Persist part
          em.persist(userRole);
          em.persist(adminRole);
          em.persist(user);
          em.persist(admin);
          em.persist(both);
          em.persist(p);
          em.persist(p2);
          em.getTransaction().commit();
          System.out.println("Created TEST Users");
        }
      } catch (Exception ex) {
        Logger.getLogger(UserFacade.class.getName()).log(Level.SEVERE, null, ex);
        em.getTransaction().rollback();
      } finally {
        em.close();
      }
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {

  }
}
