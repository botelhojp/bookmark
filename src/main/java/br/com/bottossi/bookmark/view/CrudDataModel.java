package br.com.bottossi.bookmark.view;

import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.gov.frameworkdemoiselle.pagination.Pagination;
import br.gov.frameworkdemoiselle.pagination.PaginationContext;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.Reflections;

public abstract class CrudDataModel<T> extends LazyDataModel<T> {

	private static final long serialVersionUID = 1L;

	private PaginationContext pgcontext;
	
	private Class<T> beanClass;
	
	private Pagination pagination;
	
	public CrudDataModel(){
		pgcontext = Beans.getReference(PaginationContext.class);
		pagination = pgcontext.getPagination(getBeanClass(), true);
	}
	
	@Override
	public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
		pagination.setFirstResult(first);
		pagination.setPageSize(pageSize);
		return load(sortField, sortOrder, filters);
	}
	
    public abstract List<T> load(String sortField, SortOrder sortOrder,	Map<String, String> filters);
    

	public Object getRowKey(T item) {  
        return item;
    } 
	
	protected Class<T> getBeanClass() {
		if (this.beanClass == null) {
			this.beanClass = Reflections.getGenericTypeArgument(this.getClass(), 0);
		}
		return this.beanClass;
	}
	
	@Override
	public int getRowCount() {
		return pagination.getTotalResults();
	}

}
