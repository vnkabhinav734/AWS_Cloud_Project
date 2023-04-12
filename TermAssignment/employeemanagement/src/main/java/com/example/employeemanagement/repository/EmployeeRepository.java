package com.example.employeemanagement.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.example.employeemanagement.entity.*;

@Repository
public class EmployeeRepository {
	
    @Autowired
    private DynamoDBMapper dynamoDBMapper;
    
    @Autowired
    private AmazonSNSClient snsClient; 
    
    
	@Value("${aws.publish.topic.arn}")
	private String TOPIC_ARN;
	
	@Value("${aws.cloud.queue}")
	private String queueName;
	
	@Autowired
	private QueueMessagingTemplate queueMessagingTemplate;
	    
    public Employee save(Employee employee) {
        dynamoDBMapper.save(employee);
        addSubscription(employee.getEmail());
        return employee;
    }
    
    public List<Employee> getAllEmployees() {
    	DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();

    	List<Employee> results = dynamoDBMapper.scan(Employee.class, scanExpression);
    	return results;
}
    
    public String delete(String employeeId) {
        Employee emp = dynamoDBMapper.load(Employee.class, employeeId);
        dynamoDBMapper.delete(emp);
        return "Employee Deleted!";
    }
    
    public Employee update(String employeeId, Employee employee) {
        Employee checkEmployee = dynamoDBMapper.load(Employee.class, employeeId);
        if (checkEmployee != null) {
        	checkEmployee.setFirstName(employee.getFirstName());
        	checkEmployee.setLastName(employee.getLastName());
        	checkEmployee.setEmail(employee.getEmail());
        	checkEmployee.getDepartment().setDepartmentCode(employee.getDepartment().getDepartmentCode());
        	checkEmployee.getDepartment().setDepartmentName(employee.getDepartment().getDepartmentName());
            dynamoDBMapper.save(checkEmployee);
            return employee;
        } else {
            throw new ResourceNotFoundException("Employee not found with id: " + employeeId);
        }
    }
    
    public String publishMessageTopic(String message) {
    	PublishRequest publishrequest=new PublishRequest(TOPIC_ARN,message,"Notification");
    	snsClient.publish(publishrequest);
    	sendMessageToQueue(message);
    	return message;
    }
    
    public void addSubscription(String email) {
    	SubscribeRequest request=new SubscribeRequest(TOPIC_ARN,"email",email);
    	snsClient.subscribe(request);
    }
    
    public void sendMessageToQueue(String message) {
    	queueMessagingTemplate.send(queueName,MessageBuilder.withPayload(message).build());
    }

}
