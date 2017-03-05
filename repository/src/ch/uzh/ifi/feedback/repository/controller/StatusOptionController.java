package ch.uzh.ifi.feedback.repository.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;

import ch.uzh.ifi.feedback.library.rest.RestController;
import ch.uzh.ifi.feedback.library.rest.annotations.Authenticate;
import ch.uzh.ifi.feedback.library.rest.annotations.Controller;
import ch.uzh.ifi.feedback.library.rest.annotations.DELETE;
import ch.uzh.ifi.feedback.library.rest.annotations.GET;
import ch.uzh.ifi.feedback.library.rest.annotations.POST;
import ch.uzh.ifi.feedback.library.rest.annotations.PUT;
import ch.uzh.ifi.feedback.library.rest.annotations.Path;
import ch.uzh.ifi.feedback.library.rest.annotations.PathParam;
import ch.uzh.ifi.feedback.library.rest.authorization.UserAuthenticationService;
import ch.uzh.ifi.feedback.repository.model.StatusOption;
import ch.uzh.ifi.feedback.repository.service.StatusOptionService;
import ch.uzh.ifi.feedback.repository.validation.StatusOptionValidator;

@Controller(StatusOption.class)
@RequestScoped
public class StatusOptionController extends RestController<StatusOption>{

	@Inject
	public StatusOptionController(
			StatusOptionService dbService, 
			StatusOptionValidator validator,
			HttpServletRequest request, 
			HttpServletResponse response) 
	{
		super(dbService, validator, request, response);
	}

	@GET
	@Path("/{lang}/status_options")
	public List<StatusOption> GetAll() throws Exception
	{
		return super.GetAll();
	}
	
	@POST
	@Path("/{lang}/status_options")
	@Authenticate(service = UserAuthenticationService.class)
	public StatusOption Insert(StatusOption option) throws Exception
	{
		return super.Insert(option);
	}
	
	@PUT
	@Path("/{lang}/status_options")
	@Authenticate(service = UserAuthenticationService.class)
	public StatusOption Update(StatusOption option) throws Exception
	{
		return super.Update(option);
	}
	
	@DELETE
	@Path("/{lang}/status_options/{id}")
	@Authenticate(service = UserAuthenticationService.class)
	public void Delete(@PathParam("id") Integer id) throws Exception
	{
		super.Delete(id);
	}
}
