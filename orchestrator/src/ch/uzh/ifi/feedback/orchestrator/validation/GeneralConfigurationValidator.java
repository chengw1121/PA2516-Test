package ch.uzh.ifi.feedback.orchestrator.validation;


import com.google.inject.Inject;
import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.validation.ValidationResult;
import ch.uzh.ifi.feedback.library.rest.validation.ValidationSerializer;
import ch.uzh.ifi.feedback.library.rest.validation.ValidatorBase;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackParameter;
import ch.uzh.ifi.feedback.orchestrator.model.GeneralConfiguration;
import ch.uzh.ifi.feedback.orchestrator.services.GeneralConfigurationService;

@Singleton
public class GeneralConfigurationValidator extends ValidatorBase<GeneralConfiguration>{

	private ParameterValidator parameterValidator;
	
	@Inject
	public GeneralConfigurationValidator(ParameterValidator parameterValidator, GeneralConfigurationService service, ValidationSerializer serializer) {
		super(GeneralConfiguration.class, service, serializer);
		this.parameterValidator = parameterValidator;
	}
	
	@Override
	public ValidationResult Validate(GeneralConfiguration object) throws Exception {
		ValidationResult result = super.Validate(object);
		
		for(FeedbackParameter param : object.getParameters())
		{
			ValidationResult childResult = parameterValidator.Validate(param);
			if(childResult.hasErrors())
			{
				result.setHasErrors(true);
				result.GetValidationErrors().addAll(childResult.GetValidationErrors());
			}
		}
		
		return result;
	}

}
