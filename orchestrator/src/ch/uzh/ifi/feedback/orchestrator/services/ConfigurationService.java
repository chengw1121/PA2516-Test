package ch.uzh.ifi.feedback.orchestrator.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import ch.uzh.ifi.feedback.library.transaction.DatabaseConfiguration;
import ch.uzh.ifi.feedback.library.transaction.IDatabaseConfiguration;
import ch.uzh.ifi.feedback.orchestrator.model.Configuration;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackMechanism;
import ch.uzh.ifi.feedback.orchestrator.model.GeneralConfiguration;
import ch.uzh.ifi.feedback.orchestrator.transaction.OrchestratorDatabaseConfiguration;
import javassist.NotFoundException;
import static java.util.Arrays.asList;

@Singleton
public class ConfigurationService extends OrchestratorService<Configuration>{

	private MechanismService mechanismService;
	private GeneralConfigurationService generalConfigurationService;
	
	@Inject
	public ConfigurationService(
			ConfigurationResultParser resultParser, 
			MechanismService mechanismService,
			GeneralConfigurationService generalConfigurationService,
			OrchestratorDatabaseConfiguration config,
			@Named("timestamp")Provider<Timestamp> timestampProvider) 
	{
		super(
			resultParser, 
			Configuration.class, 
			"configurations", 
			config.getDatabase(),
			timestampProvider);
		
		this.mechanismService = mechanismService;
		this.generalConfigurationService = generalConfigurationService;
	}
	
	@Override
	public Configuration GetById(int id) throws SQLException, NotFoundException {

		Configuration config = super.GetById(id);
		config.getFeedbackMechanisms().addAll(mechanismService.GetWhere(asList(id), "configurations_id = ?"));
		if(config.getGeneralConfigurationId() != null)
			config.setGeneralConfiguration(generalConfigurationService.GetById(config.getGeneralConfigurationId()));
		
		return config;
	}
	
	@Override
	public List<Configuration> GetWhere(List<Object> values, String... conditions) throws SQLException 
	{
		List<Configuration> configurations =  super.GetWhere(values, conditions);
		
		for(Configuration config : configurations)
		{
			config.getFeedbackMechanisms().addAll(mechanismService.GetWhere(asList(config.getId()), "configurations_id = ?"));
			if(config.getGeneralConfigurationId() != null)
				try {
					config.setGeneralConfiguration(generalConfigurationService.GetById(config.getGeneralConfigurationId()));
				} catch (NotFoundException e) {
					e.printStackTrace();
				}
		}
		
		return configurations;
	}

	@Override
	public void Update(Connection con, Configuration config) throws SQLException, NotFoundException, UnsupportedOperationException 
	{
		GeneralConfiguration generalConfig = config.getGeneralConfiguration();
		Integer generalConfigId = null;
		if(generalConfig != null)
		{
			if(generalConfig.getId() == null)
			{
				generalConfigId = generalConfigurationService.Insert(con, generalConfig);
				config.setGeneralConfigurationId(generalConfigId);
			}else{
				generalConfigurationService.Update(con, generalConfig);
			}
		}
		
		super.Update(con, config);
		for(FeedbackMechanism mechanism : config.getFeedbackMechanisms())
		{
			mechanism.setConfigurationsid(config.getId());
			if(mechanism.getId() == null)
			{
				mechanismService.Insert(con, mechanism);
			}else{
				mechanismService.Update(con, mechanism);
			}
		}
	}
	
	@Override
	public int Insert(Connection con, Configuration config)
			throws SQLException, NotFoundException, UnsupportedOperationException {
		
		GeneralConfiguration generalConfig = config.getGeneralConfiguration();
		if(generalConfig != null)
		{
			int generalConfigId = generalConfigurationService.Insert(con, generalConfig);
			config.setGeneralConfigurationId(generalConfigId);
		}
		
		int configId = super.Insert(con, config);
		
		for(FeedbackMechanism mechanism : config.getFeedbackMechanisms())
		{
			mechanism.setConfigurationsid(config.getId());
			mechanismService.Insert(con, mechanism);
		}
		
		return configId;
	}

}
