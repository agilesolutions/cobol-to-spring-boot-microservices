# legacy z/OS COBOL to Spring Boot microservices on Kubernetes
This project is demonstrating Strangler Fig pattern for modernizing a legacy monolith z/OS COBOL system into Spring Boot microservices on Kubernetes, using a Java REST API gateway as the integration layer.

The Strangler Fig pattern allows you to incrementally replace parts of the legacy system with new microservices, while keeping the old system running until the new one is fully functional.

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

<img title="Strangler Fig Migration Roadmap" alt="Alt text" src="/docus/strangler-fig.png"> width="600"/>

## Project structure
```
springboot-gateway-demo/
│
├── gateway/
│   └── src/main/java/com/agilesolutions/gateway/
│       ├── GatewayApplication.java
│       └── config/GatewayConfig.java
│
├── account-service/
│   └── src/main/java/com/agilesolutions/account/
│       └── AccountServiceApplication.java
│
├── customer-service/
│   └── src/main/java/com/agilesolutions/customer/
│       └── CustomerServiceApplication.java
│
├── build.gradle
└── settings.gradle

```
