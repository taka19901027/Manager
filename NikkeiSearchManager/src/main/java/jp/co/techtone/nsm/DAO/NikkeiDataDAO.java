package jp.co.techtone.nsm.DAO;

import java.io.Serializable;
import java.util.List;

import jp.co.techtone.nsm.entity.NikkeiDateEntity;

public interface NikkeiDataDAO<T> extends Serializable {

	public List<T> getAll();

	public T findByDate(String date);

	public List<NikkeiDateEntity> findAverage(String startDate, String endDate);

	public void add(T data);

	public void update(T data);

	public void delete(T data);

	public void delete(String id);

}
