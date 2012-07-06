package br.com.bottossi.bookmark.persistence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.primefaces.model.SortOrder;
import org.slf4j.Logger;

import br.com.bottossi.bookmark.domain.Bookmark;
import br.gov.frameworkdemoiselle.pagination.Pagination;
import br.gov.frameworkdemoiselle.stereotype.PersistenceController;
import br.gov.frameworkdemoiselle.template.JPACrud;

@PersistenceController
public class BookmarkDAO extends JPACrud<Bookmark, Long> {

	private static final long serialVersionUID = 1L;

	@Inject
	@SuppressWarnings("unused")
	private Logger logger;

	public List<Bookmark> findByPagination(String sortField, SortOrder sortOrder, Map<String, String> filters){
		StringBuffer select = new StringBuffer("SELECT this FROM Bookmark this ");
		
		if (!filters.isEmpty()) {
			select.append(" WHERE ");
		}
		
		for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
			String filterProperty = it.next();
			String filterValue = filters.get(filterProperty);
			select.append("this.").append(filterProperty).append(" LIKE '").append(filterValue).append("%'");
			select.append(" AND ");
		}
		if (!filters.isEmpty()) {
			select = new StringBuffer(select.substring(0, select.length() - 5 ));
		}
		
		if (sortField != null) {
			switch (sortOrder) {
				case ASCENDING:
					select.append(" ORDER BY ").append("this.").append(sortField).append(" DESC");
					break;
				case DESCENDING:
					select.append(" ORDER BY ").append("this.").append(sortField).append(" ASC");
					break;
			}
		}		
		return super.findJPQL(select.toString());
	}

	public List<Bookmark> findByPagination2(String sortField, SortOrder sortOrder, Map<String, String> filters){
			// criacao do critério de busca
			CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<Bookmark> cQuery = builder.createQuery(Bookmark.class);
			Root<Bookmark> from = cQuery.from(Bookmark.class);
			CriteriaQuery<Bookmark> select = cQuery.select(from);

			// filtro vindo do datatable
			List<Predicate> predicates = new ArrayList<Predicate>();
			for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
				String filterProperty = it.next();
				String filterValue = filters.get(filterProperty);
				Expression expression = (Expression) from.get(filterProperty);
				predicates.add(builder.like(expression,  filterValue + "%"));
			}
			select.where(predicates.toArray(new Predicate[] {}));

			// ordenacao vindo do datatable
			if (sortField != null) {
				switch (sortOrder) {
					case ASCENDING:
						select.orderBy(builder.asc(from.get(sortField)));
						break;
					case DESCENDING:
						select.orderBy(builder.desc(from.get(sortField)));
						break;
				}
			}
			//retorna a lista paginada
			return _findCriteria(select);
		}
	
	/**
	 * Perform a paged query by JPQL
	 * @param jpql
	 * @return
	 */
	private List<Bookmark> _findJPQL(String jpql) {
		TypedQuery<Bookmark> listQuery = getEntityManager().createQuery(jpql, getBeanClass());
		Pagination pagination = getPagination();
		if (pagination != null) {
			CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
			countQuery.select(builder.count(countQuery.from(getBeanClass())));
			getEntityManager().createQuery(jpql, getBeanClass());
			pagination.setTotalResults((int) (getEntityManager().createQuery(countQuery).getSingleResult() + 0));
			listQuery.setFirstResult(pagination.getFirstResult());
			listQuery.setMaxResults(pagination.getPageSize());
		}
		return listQuery.getResultList();
	}
	
	/**
	 * Perform a paged query by CriteriaQuery
	 * @param jpql
	 * @return
	 */
	private List<Bookmark> _findCriteria(CriteriaQuery<Bookmark> select) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		countQuery.select(builder.count(countQuery.from(getBeanClass())));
		getEntityManager().createQuery(countQuery);	

		TypedQuery<Bookmark> listQuery = getEntityManager().createQuery(select);
		
		Pagination pagination = getPagination();
		if (pagination != null) {
			pagination.setTotalResults((int) (getEntityManager().createQuery(countQuery).getSingleResult() + 0));
			listQuery.setFirstResult(pagination.getFirstResult());
			listQuery.setMaxResults(pagination.getPageSize());
		}		
		return listQuery.getResultList();
	}
	


	}
