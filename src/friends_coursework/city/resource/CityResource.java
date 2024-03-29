package friends_coursework.city.resource;

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
import friends_coursework.city.model.*;
import friends_coursework.config.*;
import io.swagger.annotations.Api;

@SuppressWarnings("serial")


@Path("/city")
@Api(value="City", description= "City Endpoint")
public class CityResource
{
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public Response addACity(	@FormParam("name") String name,
			@FormParam("longitude") double longitude,
			@FormParam("latitude") double latitude)
	{
		try	{
			City city=new City(name,longitude,latitude);

			DynamoDBMapper mapper=DynamoDBUtil.getDBMapper(Config.REGION,Config.LOCAL_ENDPOINT);
			mapper.save(city);
			return Response.status(201).entity("city saved").build();
		} catch (Exception e)
		{
			return Response.status(400).entity("error in saving city").build();
		}
	} //end method

	@Path("/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public City getOneCity(@PathParam("id") String id)
	{
		DynamoDBMapper mapper=DynamoDBUtil.getDBMapper(Config.REGION,Config.LOCAL_ENDPOINT);
		City city=mapper.load(City.class,id);

		if (city==null)
			throw new WebApplicationException(404);

		return city;
	} //end method

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<City> getAllCities()
	{
		DynamoDBMapper mapper=DynamoDBUtil.getDBMapper(Config.REGION,Config.LOCAL_ENDPOINT);
		DynamoDBScanExpression scanExpression=new DynamoDBScanExpression();	//create scan expression
		List<City> result=mapper.scan(City.class, scanExpression);			//retrieve all cities from DynamoDB
		return result;
	} //end method


	@Path("/{id}")
	@DELETE
	public Response deleteOneCity(@PathParam("id") String id)
	{
		DynamoDBMapper mapper=DynamoDBUtil.getDBMapper(Config.REGION,Config.LOCAL_ENDPOINT);
		City city=mapper.load(City.class,id);

		if (city==null)
			throw new WebApplicationException(404);

		mapper.delete(city);
		return Response.status(200).entity("deleted").build();
	} //end method
} //end class
