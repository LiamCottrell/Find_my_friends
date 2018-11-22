package friends_coursework.user.resource;

//general Java
//JAX-RS
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//AWS SDK
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import friends_coursework.api.error.resource.APIError;
import friends_coursework.aws.util.DynamoDBUtil;
import friends_coursework.config.Config;
import friends_coursework.user.model.User;
import io.swagger.annotations.Api;

@SuppressWarnings("serial")


@Path("/user")
@Api(value="User", description= "Users Endpoint")
public class UserResource
{
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response addAUser(	@FormParam("user_name") String user_name)
	{
		try	{
			User user = new User(user_name);

			DynamoDBMapper mapper=DynamoDBUtil.getDBMapper(Config.REGION,Config.LOCAL_ENDPOINT);
			mapper.save(user);
			return Response.status(201).entity(user).build();
		} catch (Exception e)
		{
			return Response.status(400).entity("error in saving user").build();
		}
	} //end method

	@Path("/{user_id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOneUser(@PathParam("user_id") String id)
	{
		DynamoDBMapper mapper = DynamoDBUtil.getDBMapper(Config.REGION,Config.LOCAL_ENDPOINT);
		User user = mapper.load(User.class,id);

		APIError error_message = new APIError();

		if (user==null) {
			error_message.setError_msg("No user with user id `" + id + "`");
			return Response.status(404).entity(error_message).build();
		} else {
			System.out.println(user);
			return Response.status(200).entity(user).build();
		}
	} //end method

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllUsers()
	{
		DynamoDBMapper mapper=DynamoDBUtil.getDBMapper(Config.REGION,Config.LOCAL_ENDPOINT);
		DynamoDBScanExpression scanExpression=new DynamoDBScanExpression();		//create scan expression

		APIError error_message = new APIError();

		List<User> result = mapper.scan(User.class, scanExpression);			//retrieve all cities from DynamoDB
		if (result==null) {
			error_message.setError_msg("No users saved");
			return Response.status(404).entity(error_message).build();
		}
		return Response.status(200).entity(result).build();
	} //end method


	@Path("/{user_id}")
	@DELETE
	public Response deleteOneUser(@PathParam("user_id") String id)
	{
		DynamoDBMapper mapper=DynamoDBUtil.getDBMapper(Config.REGION,Config.LOCAL_ENDPOINT);
		User user=mapper.load(User.class,id);

		APIError error_message = new APIError();

		if (user==null) {
			error_message.setError_msg("User with user_id `" + id + "` not found.");
			return Response.status(404).entity(error_message).build();
		} else {
			mapper.delete(user);
			return Response.status(200).entity("User deleted").build();
		}
	} //end method

	@GET
	@Path("/name/{user_name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response loginUser(@PathParam("user_name") AttributeValue user_name){

		DynamoDBMapper mapper = DynamoDBUtil.getDBMapper(Config.REGION,Config.LOCAL_ENDPOINT);


		try {
			DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
					.withFilterExpression("user_name = :username")
					.addExpressionAttributeValuesEntry(":username", user_name)
					.withLimit(1);	//create scan expression


			List<User> result = mapper.scan(User.class, scanExpression);			//retrieve all cities from DynamoDB

			if (result == null) {
				throw new WebApplicationException("Can't find User", 404);
			}

			return Response.status(200).entity(result).build();

		} catch (Exception e)
		{
			e.printStackTrace();
			return Response.status(400).entity("error in obtaining user").build();
		}
	}
} //end class
