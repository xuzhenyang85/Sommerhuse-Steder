package facades;

import entity.Place;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import static org.eclipse.persistence.jpa.rs.util.JPARSLogger.exception;
import security.IUser;


// @author Lasse
 
public class PlaceFacade {
    
    EntityManagerFactory emf;

    public PlaceFacade(EntityManagerFactory emf)
    {
        this.emf = emf;
    }

    public PlaceFacade()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }
    
    public void createPlace(Place place){
     EntityManager em = getEntityManager();
     em.getTransaction().begin();
     
     em.persist(place);
     em.getTransaction().commit();
     em.close();
    }
    
    public List<Place> getPlaces(){
       EntityManager em = getEntityManager();
       List<Place> placeList = new ArrayList();
        try {
            Query query = em.createQuery("SELECT e FROM SEED_PLACE e");
            placeList = query.getResultList();
            return placeList;
        }catch(Exception e){
            System.out.println("Desvaerre gik der noget galt");
        }
        finally {
            em.close();
        }
        return null;
}
    
    public static void main(String[] args)
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu_development");
        Place p = new Place();
        p.setDescription("Et sted hvor man ryger fed");
        p.setImageURL("etsted/andetsted/billede.jpg");
        p.setLatitude("8.0124155");
        p.setLongtitude("3.5263644");
        p.setRating("1");
        p.setStreet("PrinsesseGade");
        p.setZip("33333");
        new PlaceFacade(emf).createPlace(p);
    }
}
