package ch.uzh.ifi.feedback.repository.mail;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.apache.catalina.Session;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.mail.MailClient;
import ch.uzh.ifi.feedback.library.mail.Attachment;
import ch.uzh.ifi.feedback.library.mail.MailConfiguration;
import ch.uzh.ifi.feedback.repository.model.CategoryFeedback;
import ch.uzh.ifi.feedback.repository.model.Feedback;
import ch.uzh.ifi.feedback.repository.model.RatingFeedback;
import ch.uzh.ifi.feedback.repository.model.ScreenshotFeedback;
import ch.uzh.ifi.feedback.repository.model.TextFeedback;

@Singleton
public class MailService 
{
	private static final Log LOGGER = LogFactory.getLog(MailService.class);
	private CloseableHttpClient httpClient;
	private MailClient mailClient;
	private Gson gson;
	
	@Inject
	public MailService(MailClient mailClient)
	{
		this.httpClient = GetHttpClient();
		this.mailClient = mailClient;
		this.gson = new GsonBuilder()
				.setPrettyPrinting()
				.setDateFormat("yyyy-MM-dd hh:mm:ss.S")
				.create();
	}
	
	public void NotifyOfFeedback(int applicationId, Feedback feedback, String to)
	{
		if(!mailClient.isMailFeedbackEnabled()) {
			LOGGER.info("NotifyOfFeedback");
			return;
		}
		
		try 
		{
			if(to == null) {
				String configTo = getMailAddressForApplication(applicationId);
				if(configTo == null) {
					return;
				}
				to = configTo;
			}
			
			String textFeedbacksContent = "";
			for(TextFeedback textFeedback : feedback.getTextFeedbacks()) {
				textFeedbacksContent += "<br />Text mechanism (ID " + textFeedback.getMechanismId() + "): " + textFeedback.getText() + "<br />";
			}
			
			String ratingFeedbacksContent = "";
			if(feedback.getRatingFeedbacks() != null) {
				ratingFeedbacksContent += "<br />";
				for(RatingFeedback ratingFeedback : feedback.getRatingFeedbacks()) {
					ratingFeedbacksContent += "<br />Rating mechanism (ID " + ratingFeedback.getMechanismId() + "): " + ratingFeedback.getTitle() + ": " + ratingFeedback.getRating() + "<br />";
				}
			}
			
			String categoryFeedbacksContent = "";
			if(feedback.getCategoryFeedbacks() != null) {
				categoryFeedbacksContent += "<br />";
				for(CategoryFeedback categoryFeedback : feedback.getCategoryFeedbacks()) {
					categoryFeedbacksContent += "<br />Category mechanism: " + categoryFeedback.getText() + " " + categoryFeedback.getParameterId() + "<br />";
				}
			}
			
			List<Attachment> attachments = new ArrayList<Attachment>();
			if(feedback.getScreenshotFeedbacks() != null) {
				for(ScreenshotFeedback screenshotFeedback : feedback.getScreenshotFeedbacks()) {
					attachments.add(new Attachment(screenshotFeedback.getPath(), screenshotFeedback.getName() + "." + screenshotFeedback.getFileExtension()));
				}
			}

			String contextInformation = String.format("<br />Resolution: %s<br />User Agent: %s<br />", feedback.getContextInformation().getResolution(), feedback.getContextInformation().getUserAgent());
			
			String divider = "---------------------------------------";
			String message = String.format("Hello,<br />A new feedback was sent for application %d: <br /><br /> %s %s %s %s %s %s<br /><br />Best regards,<br />Feedback Gathering",
					applicationId, divider, textFeedbacksContent, ratingFeedbacksContent, categoryFeedbacksContent, contextInformation, divider);
					
			mailClient.sendEmail(to, "New Feedback for Application " + applicationId, message, attachments);
			LOGGER.info("E-Mail sent to " + to + " for application ID " + applicationId);
		}catch(MessagingException ex)
		{
			LOGGER.error("Error sending mail to the configured mail address: \n" + ex.getMessage());
		}catch (IOException e) {
			LOGGER.error("Error retrieving configured mail address from the orchestrator. Communication to orchestrator failed: \n" + e.getMessage());
		}
	}
	
	private String getMailAddressForApplication(int applicationId) throws ClientProtocolException, IOException
	{
		HttpUriRequest request = new HttpGet(
			 mailClient.getOrchestratorUrl()+ "/en/applications/" + applicationId + "/general_configuration");
	
		HttpResponse httpResponse = httpClient.execute(request);
		
		if(httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
		{
			LOGGER.warn("Could not retrieve feedback mail address from the orchestrator. Response status: \n" + httpResponse.getStatusLine().toString());
			return null;
		}
			
		InputStream is = httpResponse.getEntity().getContent();
		String encoding = "UTF-8";
		String jsonResponse = IOUtils.toString(is, encoding);
		Map<String, Object> response = gson.fromJson(jsonResponse, Map.class);
		if(response.containsKey("parameters"))
		{
			List<Map<String, Object>> params = (List<Map<String, Object>>)response.get("parameters");
			for(Map<String, Object> param : params)
			{
				if(param.get("key").equals(mailClient.getFeedbackMailKeyName()))
				{
					return (String)param.get("value");
				}
			}
		}
		
		return null;
	}
	
	@SuppressWarnings("deprecation")
	private CloseableHttpClient GetHttpClient()
	{
	    SSLContextBuilder builder = new SSLContextBuilder();
	    try {
			builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
		} catch (NoSuchAlgorithmException | KeyStoreException e) {
			e.printStackTrace();
		}
	    SSLConnectionSocketFactory sslsf;
		try {
			sslsf = new SSLConnectionSocketFactory(builder.build(), SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		    CloseableHttpClient httpclient = HttpClients.custom()
		    		.setSSLSocketFactory(sslsf)
		    		.setRedirectStrategy(new DefaultRedirectStrategy(){
		    	        public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context)  {
			    	            boolean isRedirect=false;
			    	            try {
			    	                isRedirect = super.isRedirected(request, response, context);
			    	            } catch (ProtocolException e) {
			    	                e.printStackTrace();
			    	            }
			    	            if (!isRedirect) {
			    	                int responseCode = response.getStatusLine().getStatusCode();
			    	                if (responseCode == 302) {
			    	                    return true;
			    	                }
			    	            }
			    	            return isRedirect;
		    	        }
		    		})
		    		.build();
		    
		    
		    return httpclient;
		} catch (KeyManagementException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
