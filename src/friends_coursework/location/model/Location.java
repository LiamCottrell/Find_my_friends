package friends_coursework.location.model;

import java.util.Date;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

import friends_coursework.config.*;

@DynamoDBTable(tableName=Config.DYNAMODB_LOCATIONS_TABLE_NAME)
public class Location
{
	private  String location_id;
	private  String user_id;
	private  double longitude,latitude;
	private Date updated;

	public Location()
	{
	} //end method

	public Location(String user_id,double longitude,double latitude)
	{
		this.setUserID(user_id);
		this.setLongitude(longitude);
		this.setLatitude(latitude);
	} //end method

	@DynamoDBHashKey(attributeName="location_id")
	@DynamoDBAutoGeneratedKey
	public String getId() {
		return location_id;
	} //end method

	public void setId(String id) {
		this.location_id = id;
	} //end method

	@DynamoDBAttribute(attributeName="user_id")
	public String getUserID() {
		return user_id;
	} //end method

	public void setUserID(String user) {
		this.user_id = user;
	} //end method

	@DynamoDBAttribute(attributeName="longitude")
	public double getLongitude() {
		return longitude;
	} //end method

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	} //end method

	@DynamoDBAttribute(attributeName="latitude")
	public double getLatitude() {
		return latitude;
	} //end method

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	} //end method
	
	@DynamoDBAttribute(attributeName="last_login")
	@DynamoDBAutoGeneratedTimestamp(strategy=DynamoDBAutoGenerateStrategy.ALWAYS)
	public Date getLastLoginDate() {
		return updated;
	}
	
	public void setLastLoginDate(Date location_update) {
		this.updated = location_update;
	}
} //end class