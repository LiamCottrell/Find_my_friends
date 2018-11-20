package friends_coursework.user.resource;

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
import friends_coursework.config.*;
import friends_coursework.user.model.*;
import io.swagger.annotations.Api;

@SuppressWarnings("serial")


@Path("/user")
@Api(value="User", description= "Users Endpoint")
public class UserResource
{
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public Response addAUser(	@FormParam("user_name") String user_name)
	{
		try	{
			User user=new User(user_name);

			DynamoDBMapper mapper=DynamoDBUtil.getDBMapper(Config.REGION,Config.LOCAL_ENDPOINT);
			mapper.save(user);
			return Response.status(201).entity("user saved").build();
		} catch (Exception e)
		{
			return Response.status(400).entity("error in saving user").build();
		}
	} //end method

	@Path("/{user_id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public User getOneUser(@PathParam("user_id") String id)
	{
		DynamoDBMapper mapper=DynamoDBUtil.getDBMapper(Config.REGION,Config.LOCAL_ENDPOINT);
		User user=mapper.load(User.class,id);

		if (user==null)
			throw new WebApplicationException(404);

		return user;
	} //end method

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<User> getAllUsers()
	{
		DynamoDBMapper mapper=DynamoDBUtil.getDBMapper(Config.REGION,Config.LOCAL_ENDPOINT);
		DynamoDBScanExpression scanExpression=new DynamoDBScanExpression();	//create scan expression
		List<User> result=mapper.scan(User.class, scanExpression);			//retrieve all cities from DynamoDB
		return result;
	} //end method


	@Path("/{user_id}")
	@DELETE
	public Response deleteOneUser(@PathParam("user_id") String id)
	{
		DynamoDBMapper mapper=DynamoDBUtil.getDBMapper(Config.REGION,Config.LOCAL_ENDPOINT);
		User user=mapper.load(User.class,id);

		if (user==null)
			throw new WebApplicationException(404);

		mapper.delete(user);
		return Response.status(200).entity("User deleted").build();
	} //end method
} //end class
