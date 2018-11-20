package friends_coursework.api.resource;

//JAX-RS
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.*;

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
		schemes = {SwaggerDefinition.Scheme.HTTP, SwaggerDefinition.Scheme.HTTPS}
		)
public interface ApiResource
{
} //end interface
