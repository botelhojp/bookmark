package br.com.bottossi.bookmark.interceptor;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.interceptor.Interceptor;

import org.slf4j.Logger;

import br.gov.frameworkdemoiselle.annotation.Name;
import br.gov.frameworkdemoiselle.internal.implementation.TransactionInfo;
import br.gov.frameworkdemoiselle.internal.interceptor.TransactionalInterceptor;
import br.gov.frameworkdemoiselle.transaction.Transaction;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.ResourceBundle;

/**
 * FIXME Solução provisória para execução em JBossAS 7
 * 
 * @author Rodolfo Bottossi
 */
@Interceptor
@Transactional
public class BookmarkTransactionalInterceptor extends TransactionalInterceptor {
	
	private static final long serialVersionUID = 1L;

	@Inject
	public BookmarkTransactionalInterceptor(Instance<Transaction> transaction,
			Instance<TransactionInfo> transactionInfo, Logger logger,
			@Name("demoiselle-core-bundle") ResourceBundle bundle) {
		super(transaction, transactionInfo, logger, bundle);		
	}
}
