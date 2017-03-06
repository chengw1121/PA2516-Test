package ch.uzh.ifi.feedback.orchestrator.validation;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.validation.ValidationError;
import ch.uzh.ifi.feedback.library.rest.validation.ValidationResult;
import ch.uzh.ifi.feedback.library.rest.validation.ValidationSerializer;
import ch.uzh.ifi.feedback.library.rest.validation.ValidatorBase;
import ch.uzh.ifi.feedback.orchestrator.model.Application;
import ch.uzh.ifi.feedback.orchestrator.model.Configuration;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackMechanism;
import ch.uzh.ifi.feedback.orchestrator.model.GeneralConfiguration;
import ch.uzh.ifi.feedback.orchestrator.services.ApplicationService;

@Singleton
public class ApplicationValidator extends ValidatorBase<Application>{

	private GeneralConfigurationValidator generalConfigurationValidator;
	private ConfigurationValidator configurationValidator;
	
	@Inject
	public ApplicationValidator(
			GeneralConfigurationValidator generalConfigurationValidator, 
			ConfigurationValidator configurationValidator,
			ApplicationService applicationService,
			ValidationSerializer serializer) {
		super(Application.class, applicationService, serializer);
		this.generalConfigurationValidator = generalConfigurationValidator;
		this.configurationValidator = configurationValidator;
	}

	@Override
	public ValidationResult Validate(Application object) throws Exception 
	{
		ValidationResult result = super.Validate(object);

		for(Configuration config : object.getConfigurations())
		{
			ValidationResult childResult = configurationValidator.Validate(config);
			if(childResult.hasErrors())
			{
				result.setHasErrors(true);
				result.GetValidationErrors().addAll(childResult.GetValidationErrors());
			}
		}
		
		GeneralConfiguration config = object.getGeneralConfiguration();
		if(config != null)
		{
			ValidationResult childResult = generalConfigurationValidator.Validate(config);
			if(childResult.hasErrors())
			{
				result.setHasErrors(true);
				result.GetValidationErrors().addAll(childResult.GetValidationErrors());
			}
		}
		
		return result;
	}
}
