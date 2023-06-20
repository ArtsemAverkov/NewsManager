package ru.clevertec.NewsManager.common;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.springframework.stereotype.Component;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;

/**
 * This class initializes and configures WireMock for simulating HTTP-based APIs.
 */
@Component
public class WireMockInitializer {

    private static WireMockServer wireMockServer;

    /**
     Sets up and starts the WireMock server for Config Cloud.
     */
    public static void setup() {
        WireMockConfiguration wireMockConfig = WireMockConfiguration.options()
                .port(8086);

        wireMockServer = new WireMockServer(wireMockConfig);
        wireMockServer.start();

        wireMockServer.stubFor(get(urlEqualTo("/news-api/prod"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("news-api.json")));
    }

    /**
     * Tears down WireMock by stopping the server.
     */
    public static void teardown() {
        wireMockServer.stop();
    }

}
