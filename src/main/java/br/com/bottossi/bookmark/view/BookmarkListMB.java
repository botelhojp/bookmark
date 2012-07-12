package br.com.bottossi.bookmark.view;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.bottossi.bookmark.business.BookmarkBC;
import br.com.bottossi.bookmark.domain.Bookmark;
import br.gov.frameworkdemoiselle.annotation.NextView;
import br.gov.frameworkdemoiselle.annotation.PreviousView;
import br.gov.frameworkdemoiselle.pagination.Pagination;
import br.gov.frameworkdemoiselle.pagination.PaginationContext;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractListPageBean;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@ViewController
@NextView("/bookmark_edit.xhtml")
@PreviousView("/bookmark_list.xhtml")
public class BookmarkListMB extends AbstractListPageBean<Bookmark, Long> {

	private static final long serialVersionUID = 1L;

	@Inject
	private BookmarkBC bc;

	@Inject
	private PaginationContext pgcontext;

	private LazyDataModel<Bookmark> dataModel;

	private Bookmark selectedBean;


	@PostConstruct
	public void init() {		

		dataModel = new CrudDataModel<Bookmark>() {

			private static final long serialVersionUID = 1L;

			@Override
			public List<Bookmark> load(String sortField, SortOrder sortOrder, Map<String, String> filters) {
					//return bc.findByCriteriaQuery(sortField, sortOrder, filters);	
					return bc.findByJPQL(sortField, sortOrder, filters);
				}
			};
		
	}


	@Override
	protected List<Bookmark> handleResultList() {
		return this.bc.findAll();
	}

	@Transactional
	public String deleteSelection() {
		boolean delete;
		for (Iterator<Long> iter = getSelection().keySet().iterator(); iter.hasNext();) {
			Long id = iter.next();
			delete = getSelection().get(id);

			if (delete) {
				bc.delete(id);
				iter.remove();
			}
		}
		return getPreviousView();
	}


	public LazyDataModel<Bookmark> getDataModel() {
		return dataModel;
	}

	public void setDataModel(LazyDataModel<Bookmark> dataModel) {
		this.dataModel = dataModel;
	}



	public Bookmark getSelectedBean() {
		return selectedBean;
	}


	public void setSelectedBean(Bookmark selectedBean) {
		this.selectedBean = selectedBean;
	}

}
