package ch.uzh.ifi.feedback.repository.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.service.ServiceBase;
import ch.uzh.ifi.feedback.library.transaction.DatabaseConfiguration;
import ch.uzh.ifi.feedback.library.transaction.IDatabaseConfiguration;
import ch.uzh.ifi.feedback.repository.model.CategoryFeedback;

@Singleton
public class CategoryFeedbackService extends ServiceBase<CategoryFeedback>{

	@Inject
	public CategoryFeedbackService(
			CategoryResultParser resultParser,
			String tableName, 
			String dbName,
			IDatabaseConfiguration config) {
		super(resultParser, CategoryFeedback.class, "category_feedbacks", config.getDatabase());
	}
}
