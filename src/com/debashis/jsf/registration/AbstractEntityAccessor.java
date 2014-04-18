package com.debashis.jsf.registration;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnit;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

public abstract class AbstractEntityAccessor {
	
	
	@PersistenceUnit
	private EntityManagerFactory emf;
	@Resource
	private UserTransaction utx;
	
	//execute action with result
	protected final  <T> T doInTransaction(PersistenceAction<T> action) throws Exception {
		
		EntityManager em = emf.createEntityManager();
		try {
			utx.begin();
			T result = action.execute(em);
			utx.commit();
			return result;
		} catch (Exception e) {
			
			try{
				utx.rollback();
			}
			
			catch(Exception ex){
				Logger.getLogger(AbstractEntityAccessor.class.getName()).log(Level.SEVERE,null,ex);
			}
			
			throw new Exception(e);
		} 
		
		finally{
			em.close();
		}
		
	}
	
	//execute action without result
	protected final void doInTransaction(PersistenceActionWithoutResult action) throws Exception {
		
		EntityManager em = emf.createEntityManager();
		try {
			utx.begin();
			action.execute(em);
			utx.commit();
		} catch (Exception e) {
			try {
				utx.rollback();
				
			} catch (Exception ex) {
				Logger.getLogger(AbstractEntityAccessor.class.getName()).log(Level.SEVERE, null, ex);
			}
			
			throw new Exception(e);
		}
		
		finally{
			em.close();
		}
				
	}
}

interface PersistenceAction<T> {
	T execute(EntityManager em);
	}

interface PersistenceActionWithoutResult {
	void execute(EntityManager em);
	}
