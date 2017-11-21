package facades;

import entity.Role;
import security.IUserFacade;
import entity.User;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.Response;
import security.IUser;
import security.PasswordStorage;

public class UserFacade implements IUserFacade
{

    EntityManagerFactory emf;

    public UserFacade(EntityManagerFactory emf)
    {
        this.emf = emf;
    }

    public UserFacade()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    @Override
    public IUser getUserByUserId(String id)
    {
        EntityManager em = getEntityManager();
        try
        {
            return em.find(User.class, id);
        } finally
        {
            em.close();
        }
    }

    /*
  Return the Roles if users could be authenticated, otherwise null
     */
    @Override
    public List<String> authenticateUser(String userName, String password)
    {
        try
        {
            System.out.println("User Before:" + userName + ", " + password);
            IUser user = getUserByUserId(userName);
            System.out.println("User After:" + user.getUserName() + ", " + user.getPasswordHash());
            return user != null && PasswordStorage.verifyPassword(password, user.getPasswordHash()) ? user.getRolesAsStrings() : null;
        } catch (PasswordStorage.CannotPerformOperationException | PasswordStorage.InvalidHashException ex)
        {
            throw new NotAuthorizedException("Invalid username or password", Response.Status.FORBIDDEN);
        }
    }

    @Override
    public String regUser(String username, String password)
    {
        System.out.println("Registering New User in Database");

        try
        {
            EntityManager em = getEntityManager();
            
            try
            {
                em.getTransaction().begin();
                User newuser = new User(username, password);
                Role userRole = new Role("User");
                newuser.addRole(userRole);
                em.persist(newuser);
                em.getTransaction().commit();
            } finally
            {
                em.close();
            }
            return "Succesfully Registered User";
        } catch (PasswordStorage.CannotPerformOperationException ex)
        {
            Logger.getLogger(UserFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Failed to Register User, this is probably Emils fault, not Lasse";

    }

    @Override
    public String testMethod(String test)
    {
        return "Test virker: "+test;
                
    }

    @Override
    public List<IUser> getAllUsers() {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createQuery("SELECT e FROM SEED_USER e");
            return (List<IUser>) query.getResultList();
        } finally {
            em.close();
        }
    }

}
