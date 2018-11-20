package friends_coursework.config;

public class Config
{
	//
	// table names in DynamoDB
	//
	public static final String DYNAMODB_TABLE_NAME="cm4108-lab06-city"; //remove later
	public static final String DYNAMODB_USERS_TABLE_NAME="dev-cm4108-friends-users";
	public static final String DYNAMODB_LOCATIONS_TABLE_NAME="dev-cm4108-friends-locations";
	public static final String DYNAMODB_SUBSCRIPIONS_TABLE_NAME="dev-cm4108-friends-subscriptions";
	//
	// AWS Region. Refer to API to see what regions are available.
	// *** To use a local server, set this to "local". ***
	//
	public static final String REGION="eu-west-2";
	public static final String LOCAL_ENDPOINT="apigateway.eu-west-2.amazonaws.com";
} //end class
