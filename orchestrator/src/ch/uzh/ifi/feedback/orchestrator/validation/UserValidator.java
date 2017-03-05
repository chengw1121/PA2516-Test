package ch.uzh.ifi.feedback.orchestrator.validation;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ch.uzh.ifi.feedback.library.rest.validation.ValidationSerializer;
import ch.uzh.ifi.feedback.library.rest.validation.ValidatorBase;
import ch.uzh.ifi.feedback.orchestrator.model.User;
import ch.uzh.ifi.feedback.orchestrator.services.UserService;

@Singleton
public class UserValidator extends ValidatorBase<User> {

	@Inject
	public UserValidator(UserService service, ValidationSerializer serializer) {
		super(User.class, service, serializer);
	}
}
