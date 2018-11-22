package friends_coursework.api.resource;

//JAX-RS
import javax.ws.rs.Path;

import io.swagger.annotations.Api;
import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;

@SuppressWarnings("serial")

@Api(value = "/", tags = "Find my Friends", description = "API to aquire data from a database")
@Path("/")
@SwaggerDefinition(
		info = @Info(
				description = "Find my Friends location saving and tracking API Service",
				version = "V1.0",
				title = "Find my Friends Application",
				contact = @Contact(
						name = "Liam Cottrell",
						email = "l.a.cottrell@rgu.ac.uk",
						url = "https://github.com/LiamCottrell"

						)
				),
		consumes = {"application/x-www-form-urlencoded"},
		produces = {"application/json"},
		schemes = {SwaggerDefinition.Scheme.HTTP}
		)
public interface ApiResource
{
} //end interface
