package cn.duke.common.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

public interface ICommonDAO {
	public <T> void save(T obj);

	public <T> void update(T obj);

	public <T> void delete(T obj);

	public <T> List<T> find(String hql);

	public <T> List<T> find(String hql, Object... value);

	public <T> T get(Class<T> entityClass, Serializable id);

	public <T> T load(Class<T> entityClass, Serializable id);

	public void find(String hql, PageBean<?> pageObj, Object... values);

	public <T> List<T> find(String hql, String[] paramNames, Object[] values);

	public void find(String hql, PageBean<?> pageObj, String[] paramNames, Object[] values);

	public <T> void refresh(T obj);

	public void deleteAll(String hql, Object... value);

	public void bulkUpdate(String hql, Object... value);

	public void initialize(Object obj);

	public void evict(Object obj);

	public <T> void evictList(List<T> list);

	public void flush();

	public void clear();

	public void findBySQL(String sql, PageBean<?> pageBean);

	public List<Object[]> findBySQL(String sql, Object[] params);

	public List<Object[]> findBySQL(String sql);

	public void executeHQL(String hql, Object[] params);

	public void executeSQL(String sql, Object[] params);

	public <T> List<T> findTop(String hql, int topNum, Object... value);

	public Session getSessions();

	public HibernateTemplate getHibertemplate();

	public <T> void saveOrUpdate(T obj);

	public boolean excuteBatchHql(String hql, Map<String, Object> praValuesMap);

	public List<?> findByHqlWhitRowIndex(final String hql, final int rowIndex, final int pageSize, final Object... values);

	public void execute(HibernateCallback<?> callback);
}
