package cn.duke.common.dao.impl;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import cn.duke.common.dao.HibernateCallbackByPageImpl;
import cn.duke.common.dao.ICommonDAO;
import cn.duke.common.dao.PageBean;

public class CommonDAO extends HibernateDaoSupport implements ICommonDAO {

	private boolean cacheQueries = false;

	@Override
	public void bulkUpdate(String hql, Object... value) {
		getHibernateTemplate().bulkUpdate(hql, value);
	}

	@Override
	public void clear() {
		getHibernateTemplate().clear();
	}

	@Override
	public <T> void delete(T obj) {
		getHibernateTemplate().delete(obj);
	}

	@Override
	public void deleteAll(String hql, Object... value) {
		getHibernateTemplate().deleteAll(find(hql, value));
	}

	@Override
	public void evict(Object obj) {
		getHibernateTemplate().evict(obj);
	}

	@Override
	public <T> void evictList(List<T> list) {
		for (Object object : list) {
			evict(object);
		}
	}

	@Override
	public void executeHQL(String hql, Object[] params) {
		Query query = getSession().createQuery(hql);
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				query.setParameter(i, params[i]);
			}
		}
		query.executeUpdate();
	}

	@Override
	public void executeSQL(String sql, Object[] params) {
		Query query = getSession().createSQLQuery(sql);
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				query.setParameter(i, params[i]);
			}
		}
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> findBySQL(String sql) {
		Query query = getSession().createSQLQuery(sql);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> findBySQL(String sql, Object[] params) {
		Query query = getSession().createSQLQuery(sql);
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				query.setParameter(i, params[i]);
			}
		}
		return query.list();
	}

	/**
	 * 
	 * @2013-03-26
	 */
	@Override
	public boolean excuteBatchHql(final String hql, final Map<String, Object> praValuesMap) {
		getHibernateTemplate().executeWithNativeSession(new HibernateCallback<Object>() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException {
				Query queryObject = session.createQuery(hql);
				prepareQuery(queryObject);
				if (!praValuesMap.isEmpty()) {
					Iterator<Entry<String, Object>> it = praValuesMap.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry<String, Object> me = (Map.Entry<String, Object>) it.next();
						if (me.getValue() instanceof List) {
							queryObject.setParameterList(me.getKey(), (List<?>) me.getValue());
						} else {
							queryObject.setParameter(me.getKey(), me.getValue());
						}
					}
				}
				queryObject.executeUpdate();
				return null;
			}
		});
		return true;
	}

	protected Query prepareQuery(Query query) {
		if (isCacheQueries()) {
			query.setCacheable(true);
		}
		return query;
	}

	public boolean isCacheQueries() {
		return this.cacheQueries;
	}

	@Override
	public void findBySQL(String sql, PageBean<?> pageBean) {
		getHibernateTemplate().executeFind(new HibernateCallbackByPageImpl(sql, pageBean, true));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<?> find(String hql) {
		return getHibernateTemplate().find(hql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<?> find(String hql, Object... value) {
		return getHibernateTemplate().find(hql, value);
	}

	@Override
	public void find(String hql, PageBean<?> pageObj, Object... values) {
		getHibernateTemplate().executeFind(new HibernateCallbackByPageImpl(hql, pageObj, values));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<?> find(String hql, String[] paramNames, Object[] values) {
		return getHibernateTemplate().findByNamedParam(hql, paramNames, values);
	}

	@Override
	public void find(String hql, PageBean<?> pageObj, String[] paramNames, Object[] values) {
		getHibernateTemplate().executeFind(new HibernateCallbackByPageImpl(hql, pageObj, values, paramNames));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<?> findTop(String hql, int topNum, Object... value) {
		return getHibernateTemplate().executeFind(new HibernateCallbackByPageImpl(hql, topNum, value));
	}

	@Override
	public void flush() {
		getHibernateTemplate().flush();
	}

	@Override
	public <T> T get(Class<T> entityClass, Serializable id) {
		return getHibernateTemplate().get(entityClass, id);
	}

	@Override
	public HibernateTemplate getHibertemplate() {
		return getHibernateTemplate();
	}

	@Override
	public Session getSessions() {
		return getSession();
	}

	@Override
	public void initialize(Object obj) {
		getHibernateTemplate().initialize(obj);
	}

	@Override
	public <T> T load(Class<T> entityClass, Serializable id) {
		return getHibernateTemplate().load(entityClass, id);
	}

	@Override
	public <T> void refresh(T obj) {
		getHibernateTemplate().refresh(obj);
	}

	@Override
	public <T> void save(T obj) {
		getHibernateTemplate().save(obj);
	}

	@Override
	public <T> void saveOrUpdate(T obj) {
		getHibernateTemplate().saveOrUpdate(obj);
	}

	@Override
	public <T> void update(T obj) {
		getHibernateTemplate().update(obj);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List findByHqlWhitRowIndex(final String hql, final int rowIndex, final int pageSize, final Object... values) {

		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(hql);
				query.setCacheable(true);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						query.setParameter(i, values[i]);
					}
				}
				return query.setFirstResult(rowIndex).setMaxResults(pageSize).list();
			}
		});
	}

	@Override
	public void execute(HibernateCallback<?> action) {
		getHibernateTemplate().execute(action);

	}
}
