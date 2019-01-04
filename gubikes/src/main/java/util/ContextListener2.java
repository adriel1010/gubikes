package util;
 
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextListener2 implements ServletContextListener {
 
	
	@Inject
	private  EntityManagerProducer ent;
	
	@Inject
	private EntityManager manager;
	
    @Override //quando desliga o tomcat
    public void contextDestroyed(ServletContextEvent event) {
     	ent.closeEntityManager(manager);
    	  
        
    }

    @Override //quando liga o tomcat
    public void contextInitialized(ServletContextEvent event) {
      
    //	ent.createEntityManager();
    	ent.createEntityManager();
        
    }

}
