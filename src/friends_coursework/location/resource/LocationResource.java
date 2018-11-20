package friends_coursework.location.resource;

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
import friends_coursework.location.model.*;
import friends_coursework.config.*;
import io.swagger.annotations.Api;

@SuppressWarnings("serial")


@Path("/location")
@Api(value="Location", description= "Location Endpoint")
public class LocationResource
{
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public Response addALocation(	@FormParam("user_id") String user_id,
			@FormParam("latitude") double latitude,
			@FormParam("longitude") double longitude)
			
	{
		try	{
			Location location=new Location(user_id,longitude,latitude);

			DynamoDBMapper mapper=DynamoDBUtil.getDBMapper(Config.REGION,Config.LOCAL_ENDPOINT);
			mapper.save(location);
			return Response.status(201).entity("location saved").build();
		} catch (Exception e)
		{
			return Response.status(400).entity("error in saving location").build();
		}
	} //end method

	@Path("/{location_id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Location getOneLocation(@PathParam("location_id") String id)
	{
		DynamoDBMapper mapper=DynamoDBUtil.getDBMapper(Config.REGION,Config.LOCAL_ENDPOINT);
		Location location=mapper.load(Location.class,id);

		if (location==null)
			throw new WebApplicationException(404);

		return location;
	} //end method

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Location> getAllLocations()
	{
		DynamoDBMapper mapper=DynamoDBUtil.getDBMapper(Config.REGION,Config.LOCAL_ENDPOINT);
		DynamoDBScanExpression scanExpression=new DynamoDBScanExpression();	//create scan expression
		List<Location> result=mapper.scan(Location.class, scanExpression);			//retrieve all cities from DynamoDB
		return result;
	} //end method


	@Path("/{location_id}")
	@DELETE
	public Response deleteOneLocation(@PathParam("location_id") String id)
	{
		DynamoDBMapper mapper=DynamoDBUtil.getDBMapper(Config.REGION,Config.LOCAL_ENDPOINT);
		Location location=mapper.load(Location.class,id);

		if (location==null)
			throw new WebApplicationException(404);

		mapper.delete(location);
		return Response.status(200).entity("location deleted").build();
	} //end method
} //end class
