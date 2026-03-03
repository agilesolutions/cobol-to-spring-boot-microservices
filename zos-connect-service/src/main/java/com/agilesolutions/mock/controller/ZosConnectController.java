package com.agilesolutions.mock.controller;

import com.agilesolutions.mock.model.AccountResponse;
import com.agilesolutions.mock.model.CustomerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.CustomAutowireConfigurer;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Tag(
        name = "CRUD REST APIs to CREATE, READ, UPDATE, DELETE accounts",
        description = "CRUD REST APIs for managing accounts"
)
@RestController
@RequestMapping("/legacy")
public class ZosConnectController {

    @Operation(
            summary = "Fetch all accounts",
            description = "REST API to fetch all accounts from the database"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    }
    )
    @GetMapping("/accounts/{id}")
    public Mono<AccountResponse> getAccount(@PathVariable String id) {
        // Simulated COBOL copybook translation to JSON
        AccountResponse account = new AccountResponse(id, "Checking", 1234.56);

        return Mono.just(account);

    }
    @Operation(
            summary = "Fetch all customers",
            description = "REST API to fetch all customers from the database"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    }
    )
    @GetMapping("/customers/{id}")
    public Mono<CustomerResponse> getCustomer(@PathVariable String id) {
        // Simulated COBOL copybook translation to JSON
        CustomerResponse customer = new CustomerResponse(id, "Online", "Robert");


        return Mono.just(customer);

    }

}
