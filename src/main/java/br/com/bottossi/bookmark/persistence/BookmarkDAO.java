package br.com.bottossi.bookmark.persistence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
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
import br.gov.frameworkdemoiselle.pagination.PaginationContext;
import br.gov.frameworkdemoiselle.stereotype.PersistenceController;
import br.gov.frameworkdemoiselle.template.JPACrud;

@PersistenceController
public class BookmarkDAO extends JPACrud<Bookmark, Long> {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	@SuppressWarnings("unused")
	private Logger logger;

	@Inject	
	private EntityManager entityManager;
	
	@Inject
	private PaginationContext pgcontext;
	
	public List<Bookmark> findByPagination(String sortField, SortOrder sortOrder, Map<String, String> filters){		
		//criacao do criteria
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Bookmark> cQuery = builder.createQuery(Bookmark.class);
		Root<Bookmark> from = cQuery.from(Bookmark.class);
		CriteriaQuery<Bookmark> select = cQuery.select(from);
		
		//filtro
		List<Predicate> predicates = new ArrayList<Predicate>();
		for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
			String filterProperty = it.next();
			String filterValue = filters.get(filterProperty);
			predicates.add(builder.like((Expression) from.get(filterProperty), filterValue + "%"));
		}
		select.where(predicates.toArray(new Predicate[]{}));

		//ordenacao
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
		
		//obter o total de registros
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		countQuery.select(builder.count(countQuery.from(Bookmark.class)));
		entityManager.createQuery(countQuery);
		
		
		//setando o contexto de paginação
		Pagination p = pgcontext.getPagination(Bookmark.class);
		p.setTotalResults((int) (entityManager.createQuery(countQuery).getSingleResult() + 0));

		//paginacao da consulta		
		TypedQuery<Bookmark> listQuery = entityManager.createQuery(select);
		listQuery.setFirstResult(p.getFirstResult());
		listQuery.setMaxResults(p.getPageSize());

		return listQuery.getResultList();
	}
	
}
