package friends_coursework.subscription.resource;

//general Java
import java.util.*;
//JAX-RS

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.glassfish.jersey.media.multipart.FormDataParam;

import com.amazonaws.regions.Regions;
//AWS SDK
import com.amazonaws.services.dynamodbv2.datamodeling.*;

import friends_coursework.aws.util.*;
import friends_coursework.subscription.model.*;
import friends_coursework.config.*;
import io.swagger.annotations.Api;
import io.swagger.jaxrs.PATCH;

@SuppressWarnings("serial")


@Path("/subscription")
@Api(value="Subscription", description= "Subscription Endpoint")
public class SubscriptionResource
{
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public Response addASubscription(	@FormParam("status") Integer status,
			@FormParam("from_id") String from_id,
			@FormParam("to_id") String to_id)
	{
		try	{
			Subscription subscription = new Subscription(status ,from_id ,to_id);

			DynamoDBMapper mapper = DynamoDBUtil.getDBMapper(Config.REGION,Config.LOCAL_ENDPOINT);
			mapper.save(subscription);
			return Response.status(201).entity("subscription saved").build();
		} catch (Exception e)
		{
			e.printStackTrace();
			return Response.status(400).entity("error in saving subscription").build();
		}
	} //end method

	@Path("/{subscription_id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Subscription getOneSubscription(@PathParam("subscription_id") String id)
	{
		DynamoDBMapper mapper = DynamoDBUtil.getDBMapper(Config.REGION,Config.LOCAL_ENDPOINT);
		Subscription subscription = mapper.load(Subscription.class,id);

		if (subscription==null)
			throw new WebApplicationException(404);

		return subscription;
	} //end method

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Subscription> getAllSubscriptions()
	{
		DynamoDBMapper mapper=DynamoDBUtil.getDBMapper(Config.REGION,Config.LOCAL_ENDPOINT);
		DynamoDBScanExpression scanExpression=new DynamoDBScanExpression();	//create scan expression
		List<Subscription> result = mapper.scan(Subscription.class, scanExpression);			//retrieve all cities from DynamoDB
		return result;
	} //end method


	@Path("/{subscription_id}")
	@DELETE
	@Produces(MediaType.TEXT_PLAIN)
	public Response deleteOneSubscription(@PathParam("subscription_id") String id)
	{
		DynamoDBMapper mapper=DynamoDBUtil.getDBMapper(Config.REGION,Config.LOCAL_ENDPOINT);
		Subscription subscription = mapper.load(Subscription.class, id);

		if (subscription==null)
			throw new WebApplicationException(404);

		mapper.delete(subscription);
		
		return Response.status(200).entity("subscription deleted").build();
	} //end method

	@PATCH
	@Path("/updateStatus/{subscription_id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response updateSubscription(@PathParam("subscription_id") String id,
			@FormParam("status") Integer status) {

		DynamoDBMapper mapper = DynamoDBUtil.getDBMapper(Config.REGION,Config.LOCAL_ENDPOINT);
		Subscription subscription = mapper.load(Subscription.class, id);

		if (subscription == null) {
			throw new WebApplicationException("Can't find Subscription", 404);
		}
		try {
			subscription.setStatus(status);
			mapper.save(subscription);
			return Response.status(201).entity("subscription updated").build();
		} catch (Exception e)
		{
			e.printStackTrace();
			return Response.status(400).entity("error in saving subscription").build();
		}
	}
} //end class
