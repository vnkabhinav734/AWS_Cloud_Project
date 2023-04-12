package com.example.employeemanagement.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Configuration
public class DynamoDbConfiguration  {
	
	@Value("${dynamo.db.region}")
	private String region;
	
	@Value("${dynamo.service.endpoint}")
	private String serviceEndpoint;
	
	@Value("${aws.access.key}")
	private String accessKey;
	
	@Value("${aws.secret.key}")
	private String secretKey;
	
	@Value("${aws.session.key}")
	private String sessionKey;
	

	public static Map<String,String> secretMap=new HashMap<String,String>();
	
    
    @Bean
    public DynamoDBMapper buildAmazonDynamoDB() throws JsonMappingException, JsonProcessingException {


    	AWSCredentials awsCredentials= new BasicSessionCredentials(accessKey,secretKey,sessionKey);

    	AmazonDynamoDB client =AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(serviceEndpoint,region))
        		.withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).build();
    	
    	return new DynamoDBMapper(client);
    }
    
    @Bean
    public AmazonSNSClient getSnsClient() {
    	AWSCredentials awsCredentials= new BasicSessionCredentials(accessKey,secretKey,sessionKey);
    	return (AmazonSNSClient) AmazonSNSClientBuilder.standard().withRegion(region)
    			.withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).build();
    }
    
    @Bean
    public QueueMessagingTemplate queueMessagingTemplate() {
    	return new QueueMessagingTemplate(amazonSQSAsync());
    }
    
    @Bean
    public AmazonSQSAsync amazonSQSAsync() {
    	AWSCredentials awsCredentials= new BasicSessionCredentials(accessKey,secretKey,sessionKey);
    	return AmazonSQSAsyncClientBuilder.standard().withRegion(region)
    			.withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).build();
    }
}
