package jp.co.techtone.nsm.DAO;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import jp.co.techtone.nsm.entity.NikkeiDateEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Service;

@Service
public class NikkeiDateDAOImpl extends AbstractNikkeiDataDAO {

	@Autowired
	private LocalContainerEntityManagerFactoryBean factory;

	@PersistenceContext
	private EntityManager manager;

	public NikkeiDateDAOImpl(){
		init();
	}

	public List<NikkeiDateEntity> getAll() {
		Query query = manager.createNamedQuery("NikkeiDateEntity.getAll");
		return query.getResultList();
	}

	public List<NikkeiDateEntity> findByDate(String date) {
		Query query = manager.createNamedQuery("NikkeiDateEntity.findByDate")
				.setParameter("date", date);
		return query.getResultList();
	}

	public NikkeiDateEntity findByDateSingle(String date) {
		Query query = manager.createNamedQuery("NikkeiDateEntity.findByDate")
				.setParameter("date", date);
		return (NikkeiDateEntity)query.getSingleResult();
	}

	public List<NikkeiDateEntity> findAverage(String startDate, String endDate){

		//引数から平均の値を算出するクエリを生成
		Query query = manager.createNamedQuery("NikkeiDateEntity.findAverage");
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List resultList = query.getResultList();

		return resultList;
	}

	/**
	 * 受け取ったエンティティをDBに追加するメソッド
	 * 追加が成功すればtrue、日付が重複する場合はfalseを返す
	 * @param entity
	 * @return
	 */
	public boolean addEntity(NikkeiDateEntity entity) {
		boolean addResult = true;

		try{

		EntityManager manager = factory.getNativeEntityManagerFactory().createEntityManager();
		EntityTransaction transaction = manager.getTransaction();
		transaction.begin();
		manager.persist(entity);
		manager.flush();
		transaction.commit();

		}catch(PersistenceException e){
			addResult = false;
			return addResult;
		}

		return addResult;
	}

	public void removeEntity(String date){
		EntityManager manager = factory.getNativeEntityManagerFactory().createEntityManager();
		EntityTransaction transaction = manager.getTransaction();
		transaction.begin();
		manager.remove(manager.getReference(NikkeiDateEntity.class, date));
		manager.flush();
		transaction.commit();
	}

	public void removeEntity(Object entity) {
		EntityManager manager = factory.getNativeEntityManagerFactory().createEntityManager();
		EntityTransaction transaction = manager.getTransaction();
		transaction.begin();
		manager.remove(entity);
		manager.flush();
		transaction.commit();
	}

	public void add(Object entity) {
		EntityManager manager = factory.getNativeEntityManagerFactory().createEntityManager();
		EntityTransaction transaction = manager.getTransaction();
		transaction.begin();
		manager.persist(entity);
		manager.flush();
		transaction.commit();
	}

	public void update(Object entity) {
		EntityManager manager = factory.getNativeEntityManagerFactory().createEntityManager();
		EntityTransaction transaction = manager.getTransaction();
		transaction.begin();
		manager.merge(entity);
		manager.flush();
		transaction.commit();
	}

	public void delete(Object entity) {
		EntityManager manager = factory.getNativeEntityManagerFactory().createEntityManager();
		EntityTransaction transaction = manager.getTransaction();
		transaction.begin();
		manager.remove(entity);
		manager.flush();
		transaction.commit();
	}

	public void delete(String id) {
	}

}
