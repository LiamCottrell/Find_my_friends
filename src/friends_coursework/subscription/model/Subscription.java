package friends_coursework.subscription.model;

import java.util.Date;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

import friends_coursework.config.*;

@DynamoDBTable(tableName=Config.DYNAMODB_SUBSCRIPIONS_TABLE_NAME)
public class Subscription
{
	private String subscription_id;
	private Integer status;
	private String from_id;
	private String to_id;
	private Date request_timestamp;


	public Subscription()
	{
	} //end method

	public Subscription(Integer status, String from_id, String to_id)
	{
		this.setStatus(status);
		this.setFrom(from_id);
		this.setTo(to_id);
	} //end method

	@DynamoDBHashKey(attributeName="subscription_id")
	@DynamoDBAutoGeneratedKey
	public String getId() {
		return subscription_id;
	} //end method

	public void setId(String subscription_id) {
		this.subscription_id = subscription_id;
	} //end method
	
	@DynamoDBAttribute(attributeName="status")
	public Integer getStatus() {
		return status;
	} //end method

	public void setStatus(Integer status) {
		this.status = status;
	} //end method

	@DynamoDBAttribute(attributeName="from_id")
	public String getFrom() {
		return from_id;
	} //end method

	public void setFrom(String from_id) {
		this.from_id = from_id;
	} //end method
	
	@DynamoDBAttribute(attributeName="to_id")
	public String getTo() {
		return to_id;
	} //end method

	public void setTo(String to_id) {
		this.to_id = to_id;
	} //end method
	
	@DynamoDBAttribute(attributeName="request_timestamp")
	@DynamoDBAutoGeneratedTimestamp(strategy=DynamoDBAutoGenerateStrategy.ALWAYS)
	public Date getSubscriptionDate() {
		return request_timestamp;
	}
	
	public void setSubscriptionDate(Date request_timestamp) {
		this.request_timestamp = request_timestamp;
	}

} //end class