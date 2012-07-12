package br.com.bottossi.bookmark.business;

import java.util.List;
import java.util.Map;

import org.primefaces.model.SortOrder;

import br.gov.frameworkdemoiselle.annotation.Startup;
import br.gov.frameworkdemoiselle.stereotype.BusinessController;
import br.gov.frameworkdemoiselle.template.DelegateCrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;

import br.com.bottossi.bookmark.domain.Bookmark;
import br.com.bottossi.bookmark.persistence.BookmarkDAO;

@BusinessController
public class BookmarkBC extends DelegateCrud<Bookmark, Long, BookmarkDAO> {
	
	private static final long serialVersionUID = 1L;
	
	@Startup
	@Transactional
	public void load() {
		if (findAll().isEmpty()) {
			insert(new Bookmark("Demoiselle Portal", "http://www.frameworkdemoiselle.gov.br"));
			insert(new Bookmark("Demoiselle SourceForge", "http://sf.net/projects/demoiselle"));
			insert(new Bookmark("Twitter", "http://twitter.frameworkdemoiselle.gov.br"));
			insert(new Bookmark("Blog", "http://blog.frameworkdemoiselle.gov.br"));
			insert(new Bookmark("Wiki", "http://wiki.frameworkdemoiselle.gov.br"));
			insert(new Bookmark("Bug Tracking", "http://tracker.frameworkdemoiselle.gov.br"));
			insert(new Bookmark("Forum", "http://forum.frameworkdemoiselle.gov.br"));
			insert(new Bookmark("SVN", "http://svn.frameworkdemoiselle.gov.br"));
			insert(new Bookmark("Maven", "http://repository.frameworkdemoiselle.gov.br"));
			insert(new Bookmark("Downloads", "http://download.frameworkdemoiselle.gov.br"));
			insert(new Bookmark("Meu", "http://download.frameworkdemoiselle.gov.br"));
			insert(new Bookmark("Casa1", "http://casa1.frameworkdemoiselle.gov.br"));
			insert(new Bookmark("Casa2", "http://casa2.frameworkdemoiselle.gov.br"));
			insert(new Bookmark("Casa3", "http://casa3.frameworkdemoiselle.gov.br"));
			insert(new Bookmark("Casa4", "http://casa4.frameworkdemoiselle.gov.br"));
			insert(new Bookmark("Casa5", "http://casa5.frameworkdemoiselle.gov.br"));
			insert(new Bookmark("Casa6", "http://casa6.frameworkdemoiselle.gov.br"));
			insert(new Bookmark("Casa7", "http://casa7.frameworkdemoiselle.gov.br"));
			insert(new Bookmark("Casa8", "http://casa8.frameworkdemoiselle.gov.br"));
			insert(new Bookmark("Casa9", "http://casa9.frameworkdemoiselle.gov.br"));
			insert(new Bookmark("Casa10", "http://casa10.frameworkdemoiselle.gov.br"));
		}
	}

	public List<Bookmark> findByJPQL(String sortField, SortOrder sortOrder,	Map<String, String> filters) {
		return getDelegate().findByJPQL(sortField, sortOrder, filters);
	}
	
	public List<Bookmark> findByCriteriaQuery(String sortField, SortOrder sortOrder,	Map<String, String> filters) {
		return getDelegate().findByCriteriaQuery(sortField, sortOrder, filters);
	}

	
}
