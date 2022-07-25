package Services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaQuery;

import java.util.List;
import java.util.Optional;

/**
 * Created by vacax on 03/06/16.
 */
public class ManageDb<T>{

    protected static EntityManagerFactory emf;
    private static  ManageDb instance;
    private Class<T> entity;
    private ManageDb (){

    }
    public static ManageDb getInstance(){
        if(instance==null){
            instance=new ManageDb();
        }
        return instance;
    }

    public ManageDb(Class<T> entity) {
        emf = Persistence.createEntityManagerFactory("MiUnidadPersistencia");
        this.entity = entity;
    }

    public Optional<T> create (T entity){
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        }catch (Exception e){
            new RuntimeException(e);
        }finally {
            em.close();
        }
        return Optional.ofNullable(entity);
    }

    public List<T> findAll(){
        EntityManager em = emf.createEntityManager();
        try{
            CriteriaQuery<T> criteriaQuery = em.getCriteriaBuilder().createQuery(entity);
            criteriaQuery.select(criteriaQuery.from(entity));
            return em.createQuery(criteriaQuery).getResultList();
        }finally {
            em.close();
        }
    }
    public T modify(T entity){
        EntityManager em = emf.createEntityManager();;
        try {
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
        }finally {
            em.close();
        }
        return entity;
    }
    public T find(Object id) {
        EntityManager em = emf.createEntityManager();
        try{
            return em.find(entity, id);
        }finally {
            em.close();
        }
    }

}
