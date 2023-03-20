package com.items.parsing.service.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.items.parsing.client.model.GameDto;
import com.items.parsing.client.model.GamesResponse;
import com.items.parsing.service.messaging.kafka.KafkaNewItemsSender;
import com.items.parsing.service.parsers.ItemCategory;
import com.items.parsing.service.parsers.games.GamesParser;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;

@WireMockTest(httpPort = 1111)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(GamesParserIntegrationTest.TestConfig.class)
class GamesParserIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GamesParser gamesParser;

    @MockBean
    private KafkaNewItemsSender kafkaNewItemsSender;

    @Value("${parsing.games.rawg.api-key}")
    private String apiKey;

    @Test
    void shouldSuccessfullyParseGameAndSendToKafka() throws Exception {
        String jsonResponse = getFileContent("mock/response/games-response-all-in-one-page.json");
        stubFor(get(urlPathEqualTo("/api/games"))
                .withQueryParam("page", equalTo("1"))
                .withQueryParam("page_size", equalTo("100"))
                .withQueryParam("metacritic", equalTo("10,100"))
                .withQueryParam("ordering", equalTo("-released"))
                .withQueryParam("dates", equalTo("2023-02-10,2023-03-10"))
                .withQueryParam("key", equalTo(apiKey))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(jsonResponse)
                        .withHeader("Content-type", "application/json")
                )
        );

        // when
        gamesParser.parse();

        // then
        verify(getRequestedFor(urlPathEqualTo("/api/games"))
                .withQueryParam("page", equalTo("1"))
                .withQueryParam("page_size", equalTo("100"))
                .withQueryParam("metacritic", equalTo("10,100"))
                .withQueryParam("ordering", equalTo("-released"))
                .withQueryParam("dates", equalTo("2023-02-10,2023-03-10"))
                .withQueryParam("key", equalTo(apiKey)));

        List<GameDto> games = objectMapper.readValue(jsonResponse, GamesResponse.class).getResults();

        ArgumentCaptor<GameDto> captor = ArgumentCaptor.forClass(GameDto.class);
        Mockito.verify(kafkaNewItemsSender, Mockito.timeout(5000).times(games.size())).send(captor.capture(), Mockito.anyString(), Mockito.eq(ItemCategory.GAME));
        Assertions.assertThat(captor.getAllValues()).isEqualTo(games);
    }

    @SneakyThrows
    private static String getFileContent(String filePath) {
        URL resource = GamesParserIntegrationTest.class.getClassLoader().getResource(filePath);
        Objects.requireNonNull(resource);
        return Files.readString(Paths.get(resource.toURI()));
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        @Primary
        public Clock fixedClock() {
            return Clock.fixed(LocalDateTime.of(2023, Month.MARCH, 10, 1, 1).toInstant(ZoneOffset.UTC), ZoneId.of("UTC"));
        }
    }
}
