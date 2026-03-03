package com.agilesolutions.gateway.security;

import com.agilesolutions.gateway.controller.GatewayController;
import com.agilesolutions.gateway.rest.AccountHttpClient;
import com.agilesolutions.gateway.rest.ClientHttpClient;
import com.agilesolutions.gateway.service.StockService;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.micrometer.observation.ObservationRegistry;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Duration;
import java.util.Date;
import java.util.List;

@TestPropertySource(properties = { "spring.config.location=classpath:application.yaml" })
public class OAuth2MockTokenTest {

    private static WireMockServer wireMockServer;

    private static RSAKey rsaKey;

    private WebTestClient webTestClient;

    private ClientHttpClient clientHttpClient;
    private AccountHttpClient accountHttpClient;
    private StockService stockService;
    private ObservationRegistry registry;


    /**
     * This setup mocks one API  /.well-known/openid-configuration, a JSON file that an OpenID Connect provider serves over HTTPS at a well-known URL,
     * providing all the configuration details a client needs to integrate with it.
     * Allowing client applications to automatically discover auth endpoints, token endpoints, supported features, public keys, scopes, claims, etc.
     * @throws Exception
     */
    @BeforeEach
    void setup() throws Exception {
        clientHttpClient = org.mockito.Mockito.mock(ClientHttpClient.class);
        accountHttpClient = org.mockito.Mockito.mock(AccountHttpClient.class);
        stockService = org.mockito.Mockito.mock(StockService.class);
        registry = org.mockito.Mockito.mock(ObservationRegistry.class);

        webTestClient = WebTestClient.bindToController(new GatewayController(clientHttpClient, accountHttpClient, stockService, registry)).build();
        webTestClient
                .mutate()
                .responseTimeout(Duration.ofSeconds(30))
                .build();


        // Start WireMock on port 8089
        wireMockServer = new WireMockServer(8089);
        wireMockServer.start();

        // Generate RSA test keypair
        rsaKey = new RSAKeyGenerator(2048).keyID("test-key-id").generate();

        // Serve JWKS with public key
        JWKSet jwkSet = new JWKSet(rsaKey.toPublicJWK());
        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/.well-known/jwks.json"))
                .willReturn(WireMock.okJson(jwkSet.toString())));
        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/realms/my-realm/.well-known/openid-configuration"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
            {
                "issuer": "http://localhost:8089/realms/my-realm",
                "authorization_endpoint": "http://localhost:8089/realms/my-realm/oauth/authorize",
                "token_endpoint": "http://localhost:8089/realms/my-realm/oauth/token",
                "userinfo_endpoint": "http://localhost:8089/realms/my-realm/userinfo",
                "jwks_uri": "http://localhost:8089/realms/my-realm/.well-known/jwks.json",
                "response_types_supported": [
                    "code",
                    "token",
                    "id_token",
                    "code token",
                    "code id_token",
                    "token id_token",
                    "code token id_token",
                    "none"
                ],
                "subject_types_supported": [
                    "public"
                ],
                "id_token_signing_alg_values_supported": [
                    "RS256"
                ],
                "scopes_supported": [
                    "openid",
                    "email",
                    "profile"
                ]
            }
        """)));

    }

    @AfterEach
    void teardown() {
        wireMockServer.stop();
    }


    @Test
    void givenAdminRole_whenAccessingEndPoint_PermissionAllowed() throws Exception {
        String jwt = generateJwt(List.of("admin"));

        webTestClient.get().uri("/api/accounts/health")
                .header("Authorization", "Bearer " + jwt)
                .exchange()
                .expectStatus().isOk();

    }

    @Test
    void givenUserRole_whenAccessingEndPoint_PermissionAllowed() throws Exception {
        String jwt = generateJwt(List.of("user"));

        webTestClient.get().uri("/api/accounts/health")
                .header("Authorization", "Bearer " + jwt)
                .exchange()
                .expectStatus().isOk();

    }

    private String generateJwt(List<String> roles) throws Exception {
        JWSSigner signer = new RSASSASigner(rsaKey.toPrivateKey());

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject("test-user")
                .issuer("http://localhost:8082")
                .audience("my-api")
                .claim("roles", roles)
                .expirationTime(new Date(System.currentTimeMillis() + 3600_000))
                .issueTime(new Date())
                .build();

        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(rsaKey.getKeyID()).build(),
                claims
        );

        signedJWT.sign(signer);
        return signedJWT.serialize();
    }

    
}
