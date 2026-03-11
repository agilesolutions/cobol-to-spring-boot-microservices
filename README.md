# Anthropic Claude AI reverse-engineering legacy z/OS COBOL code and migrate to Spring Boot microservices on Kubernetes

## About modernization (Before we start)
[Migrating legacy COBOL systems to modern architectures is not a language translation exercise (e.g., to Java or C Sharp). It is fundamentally a business and architectural transformation.](./docus/COBOLMIGRATION.md) to Java or C#, it requires a comprehensive approach to understand the existing COBOL codebase, identify business capabilities, and design a new architecture that can support the modern microservices paradigm. This process involves several key steps, including reverse-engineering the COBOL code, designing microservices based on identified business capabilities, implementing a Java REST API gateway for integration, and deploying the new microservices to Kubernetes for scalability and manageability. By following this approach, organizations can successfully transition from a legacy COBOL monolith to a modern microservices architecture that is more agile, scalable, and maintainable.

## About this project
In This project I demonstrate the Strangler Fig pattern for modernizing a legacy monolith z/OS COBOL system into Spring Boot microservices on Kubernetes, using a Java REST API gateway as the integration layer.
- The process starts with Anthropic Claude AI reverse-engineering the legacy COBOL code to understand its functionality and identify the business capabilities that can be extracted into microservices. The Strangler Fig pattern allows you to incrementally replace parts of the legacy system with new microservices, while keeping the old system running until the new one is fully functional.
- Next, I developed Spring Boot microservices for the identified business capabilities. Each microservice will be designed to handle a specific functionality and will communicate with the Java REST API gateway to access the legacy system's data and logic.
- The Java REST API gateway will act as the integration layer between the legacy system and the new microservices. It will expose RESTful APIs that the new microservices can call to interact with the legacy system without needing to rewrite it all at once. This allows for a gradual transition from the legacy system to the new microservices architecture.
- Finally, deployment new microservices to Kubernetes, which provides features like scaling, load balancing, and service discovery to manage the microservices in production. As you develop and deploy new microservices, you will continuously monitor their performance, optimize them as needed, and gradually replace more critical parts of the legacy system until it is fully modernized. 


***The Strangler Fig pattern allows you to incrementally replace parts of the legacy system with new microservices, while keeping the old system running until the new one is fully functional.***

## Key Components
1. **Legacy z/OS COBOL System**: This is an existing legacy monolith system running on z/OS, which contains the business logic and data that you want to modernize. 
2. **CICS Transaction Gateway (CTG)** Cobol copy books are exposed as REST APIs using CICS to allow the new microservices to interact with the legacy system without needing to rewrite it all at once.
2. **Java REST API Gateway**: This acts as the integration layer between the legacy system and the new microservices. It exposes RESTful APIs that the new microservices can call to interact with the legacy system.
3. **Spring Boot Microservices**: These are the new microservices that you will develop to replace the functionality of the legacy system. Each microservice will handle a specific business capability and will communicate with the Java REST API Gateway to access the legacy system's data and logic.
4. **Kubernetes**: This is the container orchestration platform where you will deploy your Spring Boot microservices. Kubernetes provides features like scaling, load balancing, and service discovery, which are essential for managing microservices in production.
5. **Data Migration**: As you develop new microservices, you will need to migrate data from the legacy system to the new microservices. This can be done incrementally, allowing you to keep the legacy system running while you transition to the new architecture.
6. **Monitoring and Logging**: Implement monitoring and logging for both the legacy system and the new microservices to ensure that you can track performance, identify issues, and maintain visibility into the system during the transition.
7. **Testing and Validation**: As you develop new microservices, it's crucial to test them thoroughly to ensure they meet the required functionality and performance standards. This includes unit testing, integration testing, and end-to-end testing.
8. **Deployment and Rollout**: Plan the deployment and rollout strategy for the new microservices. This may involve deploying them in stages, starting with non-critical functionality, and gradually replacing more critical parts of the legacy system as you gain confidence in the new architecture.
9. **Documentation and Training**: Ensure that you have comprehensive documentation for both the legacy system and the new microservices. Additionally, provide training for your development and operations teams to ensure they are familiar with the new architecture and technologies being used.
10. **Continuous Improvement**: As you transition to the new architecture, continuously evaluate and improve your microservices and the overall system. This includes refactoring code, optimizing performance, and adding new features as needed.
11. **Security**: Implement security best practices for both the legacy system and the new microservices. This includes securing APIs, implementing authentication and authorization, and ensuring data protection.
12. **Collaboration and Communication**: Foster collaboration and communication between the teams working on the legacy system and the new microservices. This will help ensure a smooth transition and allow for knowledge sharing between teams.
13. **Change Management**: Implement a change management process to handle the transition from the legacy system to the new microservices. This includes managing changes to the codebase, coordinating deployments, and communicating changes to stakeholders.
14. **Performance Optimization**: As you develop new microservices, continuously monitor and optimize their
15. **FluxCD**: Use FluxCD to automate the deployment of your microservices to Kubernetes. This will allow you to manage your deployments using GitOps principles, ensuring that your infrastructure and application changes are version-controlled and auditable. [Read FluxCD installation and deployment instructions here](./docus/fluxcd.md).

## COBOL legacy code reverse-engineering with Anthropic Claude AI and what I did to migrate to Spring Boot microservices (CONTINUOUSLY UPDATED)
I took advantage of Anthropic Claude AI's capabilities to analyze the COBOL codebase and extract valuable insights about the business logic and data structures. 
To Migrate the legacy COBOL system to Spring Boot microservices, I first reverse-engineered the COBOL code to understand its functionality and identify the business capabilities that can be extracted into microservices.
Then I designed the new microservices based on the identified business capabilities and implemented a Java REST API gateway to allow the new microservices to interact with the legacy system without needing to rewrite it all at once. Finally, I deployed the new microservices to Kubernetes, allowing for scalability and manageability in production.
Then I brought in Spring Cloud capabilities to implement method resiliency and retry mechanisms on new Spring Boot microservices, ensuring that they can gracefully handle failures and maintain a good user experience even when there are issues with external services.
**This process involved several steps:**
1. Enable Spring Cloud gateway in the Java REST API Gateway to route requests to the appropriate microservices or legacy services based on the API version specified in the request.
2. Implement API versioning in the Java REST API Gateway to allow for backward compatibility while gradually migrating functionality from the legacy system to the new microservices.
3. Upgrade to Java 25, Spring Boot 4 and Spring Framework 7 to take advantage of the latest features and improvements in the Java ecosystem, including enhanced support for microservices architecture and improved performance, like virtual threads and structured concurrency, which can help to improve the scalability and responsiveness of your microservices.
4. Use Spring Cloud's support for resiliency patterns such as Circuit Breaker, Retry, and Bulkhead to improve the resilience of the new microservices when calling external services, such as the legacy system or other microservices.
4. Test the new microservices and the API version-based routing using tools like Bruno or curl to ensure that the requests are being routed to the correct services based on the API version specified in the request and that the new microservices are functioning correctly and can handle failures gracefully.
5. Continuously monitor and optimize the performance of the new microservices, and gradually replace more critical parts of the legacy system until it is fully modernized.
6. Enable Spring Cloud Config Server to manage the configuration of the new microservices in a centralized and consistent manner. This allows for easier management of configuration changes and ensures that all microservices are using the correct configuration settings.
7. Implement Spring Cloud Sleuth for distributed tracing of requests across the new microservices and the legacy system. This will help you to identify performance bottlenecks and troubleshoot issues in the new architecture
8. Use Spring Cloud Gateway's support for rate limiting and request throttling to protect the new microservices from being overwhelmed by too many requests, especially during the transition period when both the legacy system and the new microservices are running simultaneously.
9. Implement Spring Cloud Circuit Breaker to detect when an external service is failing and to prevent further calls to that service until it has recovered. This helps to avoid cascading failures and allows your microservices to continue functioning even when an external service is down.
10. Use Spring Cloud Retry to automatically retry failed calls to external services a specified number of times with a delay between retries. This can help to handle transient failures and improve the chances of a successful call.
11. Implement Spring Cloud Bulkhead to isolate different parts of your microservices to prevent failures in one part from affecting the entire system. This can help to improve the overall resilience of your microservices by containing failures and allowing other parts of the system to continue functioning.
12. Use Spring Cloud Fallback to provide a fallback response when an external service call fails. This can help to maintain a good user experience even when there are issues with external services.
13. Implement Spring Cloud Timeouts to set timeouts for calls to external services. This can help to prevent your microservices from waiting indefinitely for a response from an external service that may be experiencing issues.
14. Implement Spring Cloud Config dynamic configuration updates to allow for changes to the configuration of the new microservices without needing to redeploy them. This can help to improve the agility of your microservices and allow for faster response to changing business needs.
15. Implement feature flags with Spring Cloud Config to allow for the gradual rollout of new features in the new microservices. This can help to mitigate risks associated with deploying new features and allow for easier rollback if issues arise.
16. Implement Spring Cloud Bus and RabbitMQ messaging capabilities to enable feature flags and configuration changes to be propagated across all instances of the new microservices in real-time. This ensures that all microservices are using the latest configuration and feature flag settings without needing to restart or redeploy them.
17. Implement Spring Cloud Config Monitor triggered by GitHub Webhooks to automatically refresh the configuration of the new microservices when changes are made to the configuration repository. This can help to ensure that your microservices are always using the latest configuration settings and can respond quickly to changes in the environment or business needs.
18. Implement Cross cutting concerns such as Tracing and Logging using Spring Cloud Gateway and Spring Cloud Sleuth to ensure that you have visibility into the interactions between the new microservices and the legacy system, as well as the performance and behavior of the new microservices in production.
19. Implement Resiliency patterns such as Circuit Breaker, Retry, and Bulkhead using Spring Cloud Circuit Breaker and Spring Cloud Retry to improve the resilience of the new microservices when calling external services, such as the legacy system or other microservices. This will help to ensure that your microservices can gracefully handle failures and maintain a good user experience even when there are issues with external services.
20. Implement security best practices for both the legacy system and the new microservices using Spring Security
21. Implement OAuth 2.0 and OpenID Connect in the Java REST API Gateway and the new microservices to secure access to the APIs and ensure that only authorized users can interact with the system. This will help to protect sensitive data and ensure that your system is secure from unauthorized access.

I took the sourcs from the following repositories:
- [COBOL codebase](https://github.com/hpatel-appliedai/aws-mainframe-modernization-carddemo/tree/main/app/cbl)

COBOL codebase contains the following modules:
- **[Account Management](account-service/README.md)**: This module handles account-related functionalities, such as creating accounts, updating account information, and managing account balances.
- **[Credit Card Management](creditcard-service/README.md)**: This module manages credit card-related functionalities, including issuing new cards, updating card information, and handling card transactions.


## Architecture Diagram
```
Clients (Web/Mobile) 
       |
       v
[Java REST API Gateway / BFF]
       |
   -------------------------
   |                       |
[Legacy COBOL Services]  [Spring Boot Microservices]
       |
[z/OS Mainframe via CICS, MQ, or DB]

```
## Strangler Fig Pattern Workflow
1. **Identify Functionality**: Start by identifying specific functionalities in the legacy system that can be replaced with microservices. Prioritize based on business value and complexity.
2. **Develop Microservices**: Develop new microservices for the identified functionalities. Each microservice should be designed to handle a specific business capability and should be loosely coupled with the legacy system.
3. **Expose APIs**: Use the Java REST API Gateway to expose APIs that allow the new microservices to interact with the legacy system. This may involve creating new APIs or adapting existing ones to fit the new architecture.
4. **Incremental Migration**: Gradually migrate functionality from the legacy system to the new microservices. This can be done in stages, allowing you to keep the legacy system running while you transition to the new architecture.
5. **Testing and Validation**: As you develop new microservices, test them thoroughly to ensure they meet the required functionality and performance standards. This includes unit testing, integration testing, and end-to-end testing.
6. **Deployment**: Deploy the new microservices to Kubernetes and monitor their performance. Use Kubernetes features like scaling and load balancing to manage the microservices in production.
7. **Iterate and Improve**: Continuously evaluate and improve your microservices and the overall system. This includes refactoring code, optimizing performance, and adding new features as needed. As you gain confidence in the new architecture, you can gradually replace more critical parts of the legacy system until it is fully modernized.
8. **Decommission Legacy System**: Once all functionalities have been migrated to the new microservices and the legacy system is no longer needed, you can decommission the legacy system. This should be done carefully to ensure that all data and functionality have been successfully transitioned to the new architecture.
9. **Post-Migration Support**: After the migration is complete, provide ongoing support and maintenance for the new microservices. This includes monitoring performance, addressing any issues that arise, and continuously improving the system based on user feedback and changing business needs.
10. **Documentation and Training**: Ensure that comprehensive documentation is available for the new microservices and the overall architecture. Provide training for development and operations teams to ensure they are familiar with the new technologies and processes being used.
11. **Feedback Loop**: Establish a feedback loop with stakeholders to gather insights and feedback on the new microservices. This will help you identify areas for improvement and ensure that the new architecture continues to meet business needs effectively.

<img title="Strangler Fig Migration Roadmap" alt="Alt text" src="/docus/strangler-fig.png"/>


## Testing
To test the microservices, use Bruno or curl to send HTTP requests to the Java REST API Gateway and verify that the responses are correct.

## Spring Framework 7 API Versioning to Gate Routing services
To implement API versioning in the Java REST API Gateway, you can use Spring Framework 7's support for API versioning. This allows you to route requests to different versions of your microservices based on the API version specified in the request.
This is how traffic between new world and old world can be routed to the appropriate microservices or legacy services based on the API version specified in the request. This allows you to maintain backward compatibility while gradually migrating functionality from the legacy system to the new microservices.
- Baseline version 1+ is routing to legacy services
- Version 2+ is routing to new microservices

## Bruno test API version based routing
To test API version-based routing using Bruno, you can send HTTP requests to the Java REST API Gateway with different API version headers and verify that the requests are routed to the correct services. For example:
- Send a request with `API-Version: 1` header to route to legacy services.
- Send a request with `API-Version: 2` header to route to new microservices.
- Verify the responses to ensure that the correct services are being called based on the API version specified in the request.
- You can also use curl to test API version-based routing by including the appropriate headers in your HTTP requests. For example:
```
curl -H "API-Version: 1" http://localhost:8080/api/accounts/00001001001
curl -H "API-Version: 2" http://localhost:8080/api/accounts/00001001001
```
- Verify the responses to ensure that the correct services are being called based on the API version specified in the request.

**[BRUNO Collection ](bruno/cobol-to-microservices)**

## Spring Framework 7 Built-in Resilience Features
To implement method resiliency and retry mechanisms on new Spring Boot microservices, I applied Spring Framework 7's support for resiliency patterns such as Circuit Breaker, Retry, and Bulkhead. These patterns help to improve the resilience of your microservices by allowing them to gracefully handle failures and retries when calling external services, such as the legacy system or other microservices.
- **Circuit Breaker**: This pattern allows you to detect when an external service is failing and to prevent further calls to that service until it has recovered. This helps to avoid cascading failures and allows your microservices to continue functioning even when an external service is down.
- **Retry**: This pattern allows you to automatically retry failed calls to external services a specified number of times with a delay between retries. This can help to handle transient failures and improve the chances of a successful call.
- **Bulkhead**: This pattern allows you to isolate different parts of your microservices to prevent failures in one part from affecting the entire system. This can help to improve the overall resilience of your microservices by containing failures and allowing other parts of the system to continue functioning.
- **Fallback**: This pattern allows you to provide a fallback response when an external service call fails. This can help to maintain a good user experience even when there are issues with external services.
- **Timeouts**: This pattern allows you to set timeouts for calls to external services. This can help to prevent your microservices from waiting indefinitely for a response from an external service that may be experiencing issues.

## Spring Oauth 2.0 and OpenID Connect Edge Spring Cloud Gateway Resource Server
To secure the Java REST API Gateway and the new microservices, I implemented Spring Security with OAuth 2.0 and OpenID Connect. This allows you to authenticate and authorize requests to the API Gateway and ensure that only authorized users can access the microservices.
- **OAuth 2.0**: This is an authorization framework that allows you to grant access to resources on behalf of a user. You can use OAuth 2.0 to secure your API Gateway and microservices by requiring clients to obtain an access token before making requests.
- **OpenID Connect**: This is an authentication layer built on top of OAuth 2.0 that allows you to verify the identity of users and obtain their profile information. You can use OpenID Connect to authenticate users and secure access to your API Gateway and microservices.
- **Spring Security**: This is a powerful and customizable authentication and access control framework for Java applications. You can use Spring Security to implement OAuth 2.0 and OpenID Connect in your API Gateway and microservices, providing robust security features such as token validation, user authentication, and authorization.
- **Edge Spring Cloud Gateway Resource Server**: This is a component of Spring Cloud Gateway that allows you to secure your API Gateway by acting as a resource server. It can validate incoming access tokens and ensure that only authorized requests are allowed to access the microservices behind the gateway.
- By implementing these security features, you can ensure that your API Gateway and microservices are protected from unauthorized access and that only authenticated and authorized users can interact with your system. This is crucial for maintaining the security and integrity of your application, especially when dealing with sensitive data or critical business functionalities.
```
Client
  |
  |  Bearer JWT
  v
+------------------------+
| Spring Cloud Gateway   |
| OAuth2 Resource Server |
| JWT Validation         |
+-----------+------------+
            |
            | Forward authenticated request
            v
+------------------------+
| Downstream Service     |
| Spring Boot API        |
| Method RBAC            |
+------------------------+
```