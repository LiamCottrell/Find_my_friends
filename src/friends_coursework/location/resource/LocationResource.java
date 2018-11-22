package friends_coursework.location.resource;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//AWS SDK
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.google.gson.Gson;

import friends_coursework.api.error.resource.APIError;
import friends_coursework.aws.util.DynamoDBUtil;
import friends_coursework.config.Config;
import friends_coursework.location.model.Location;
import io.swagger.annotations.Api;

@SuppressWarnings("serial")


@Path("/location")
@Api(value="Location", description= "Location Endpoint")
public class LocationResource
{
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response addALocation(	@FormParam("user_id") String user_id,
			@FormParam("latitude") double latitude,
			@FormParam("longitude") double longitude)
	{
		APIError error_handler = new APIError();
		Boolean set_responce = false;
		Response my_responce = null;
		DynamoDBMapper mapper = null;

		Location location = new Location(user_id,longitude,latitude);

		try {
			if(location.getUserID()==null) {
				Gson gson = new Gson();

				error_handler.setError_msg("Required data has not been provided.");
				error_handler.setError_body(gson.toJson(location));
				my_responce = Response.status(400).entity(error_handler).build();
				set_responce = true;
			}
		} catch (Exception e) {
			error_handler.setError_msg("API encountered error checking values for null.");
			String sStackTrace = e.toString(); // stack trace as a string
			error_handler.setError_body(sStackTrace);
			my_responce = Response.status(501).entity(error_handler).build();
			set_responce = true;
		}

		if(set_responce == false) {
			try { //try connect to db
				mapper = DynamoDBUtil.getDBMapper(Config.REGION,Config.LOCAL_ENDPOINT);

			} catch (Exception e) {
				error_handler.setError_msg("Database table `" + Config.DYNAMODB_LOCATIONS_TABLE_NAME + "` is not reachable, make sure connection is present.");
				String sStackTrace = e.toString(); // stack trace as a string
				error_handler.setError_body(sStackTrace);
				my_responce = Response.status(503).entity(error_handler).build();
				set_responce = true;

			}
		}

		if(set_responce == false) {
			try	{
				mapper.save(location);
				my_responce = Response.status(201).entity(location).build();
				set_responce = true;

			} catch (Exception e)
			{
				error_handler.setError_msg("API doesn't know how to handle request.");
				String sStackTrace = e.toString(); // stack trace as a string
				error_handler.setError_body(sStackTrace);
				my_responce = Response.status(501).entity(error_handler).build();
				set_responce = true;
			}
		}

		return my_responce;

	} //end method

	@Path("/{location_id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOneLocation(@PathParam("location_id") String id)
	{
		APIError error_handler = new APIError();
		Boolean set_responce = false;
		Response my_responce = null;
		DynamoDBMapper mapper = null;
		Location location = null;

		try { //try connect to db
			mapper = DynamoDBUtil.getDBMapper(Config.REGION,Config.LOCAL_ENDPOINT);
			location = mapper.load(Location.class, id);			//retrieve all cities from DynamoDB

		} catch (Exception e) {
			error_handler.setError_msg("Database table `" + Config.DYNAMODB_LOCATIONS_TABLE_NAME + "` is not reachable, make sure connection is present.");
			String sStackTrace = e.toString(); // stack trace as a string
			error_handler.setError_body(sStackTrace);
			my_responce = Response.status(503).entity(error_handler).build();
			set_responce = true;

		}
		if(set_responce==false) { //if connection is successful
			try {
				if (location==null) { //no locations matching id
					error_handler.setError_msg("No location with location id `" + id + "`");
					my_responce =  Response.status(404).entity(error_handler).build();
					set_responce = true;

				} else { //if result is found matching id
					my_responce = Response.status(200).entity(location).build();
					set_responce = true;
				}
			} catch (Exception e) {
				error_handler.setError_msg("API doesn't know how to handle request");
				String sStackTrace = e.toString(); // stack trace as a string
				error_handler.setError_body(sStackTrace);
				my_responce = Response.status(501).entity(error_handler).build();
				set_responce = true;
			}
		}
		return my_responce;
	}



	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllLocations()
	{
		APIError error_handler = new APIError();
		Boolean set_responce = false;
		List<Location> result = null;
		Response my_responce = null;
		DynamoDBMapper mapper = null;
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();	//create scan expression

		try {
			mapper = DynamoDBUtil.getDBMapper(Config.REGION,Config.LOCAL_ENDPOINT);
			result = mapper.scan(Location.class, scanExpression);			//retrieve all cities from DynamoDB

		} catch (Exception e) {
			error_handler.setError_msg("Database table `" + Config.DYNAMODB_LOCATIONS_TABLE_NAME + "` is not reachable, make sure connection is present.");

			String sStackTrace = e.toString(); // stack trace as a string
			error_handler.setError_body(sStackTrace);
			my_responce = Response.status(503).entity(error_handler).build();
			set_responce = true;
		}

		if(set_responce==false) {
			try {
				if(result.isEmpty()==true) {
					error_handler.setError_msg("Database table `" + Config.DYNAMODB_LOCATIONS_TABLE_NAME + "` has no values.");
					my_responce = Response.status(404).entity(error_handler).build();
					set_responce = true;

				} else {
					my_responce = Response.status(200).entity(result).build();
					set_responce = true;
				}
			} catch (Exception e) {
				error_handler.setError_msg("API has encountered an error");
				String sStackTrace = e.toString(); // stack trace as a string
				error_handler.setError_body(sStackTrace);
				my_responce = Response.status(503).entity(error_handler).build();
				set_responce = true;
			}
		}

		return my_responce;

	} //end method


	@Path("/{location_id}")
	@DELETE
	public Response deleteOneLocation(@PathParam("location_id") String id)
	{

		APIError error_handler = new APIError();
		Boolean set_responce = false;
		Location location = null;
		Response my_responce = null;
		DynamoDBMapper mapper = null;

		try {
			mapper = DynamoDBUtil.getDBMapper(Config.REGION,Config.LOCAL_ENDPOINT);
			location = mapper.load(Location.class, id);			//retrieve all cities from DynamoDB

		} catch (Exception e) {
			error_handler.setError_msg("Database table `" + Config.DYNAMODB_LOCATIONS_TABLE_NAME + "` is not reachable, make sure connection is present.");

			String sStackTrace = e.toString(); // stack trace as a string
			error_handler.setError_body(sStackTrace);
			my_responce = Response.status(503).entity(error_handler).build();
			set_responce = true;
		}
		
		if(set_responce==false) { //if connection is successful
			try {
				if (location==null) { //no locations matching id
					error_handler.setError_msg("No location with location id `" + id + "`");
					my_responce =  Response.status(404).entity(error_handler).build();
					set_responce = true;

				} else { //if result is found matching id
					mapper.delete(location);
					my_responce = Response.status(200).entity("Location id `" + id + "` deleted.").build();
					set_responce = true;
				}
			} catch (Exception e) {
				error_handler.setError_msg("API doesn't know how to handle request");
				String sStackTrace = e.toString(); // stack trace as a string
				error_handler.setError_body(sStackTrace);
				my_responce = Response.status(501).entity(error_handler).build();
				set_responce = true;
			}
		}
		
		return my_responce;

		
	} //end method
} //end class
