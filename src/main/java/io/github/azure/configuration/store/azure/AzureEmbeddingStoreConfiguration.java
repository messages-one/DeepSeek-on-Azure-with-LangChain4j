package io.github.azure.configuration.store.azure;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.azure.search.AzureAiSearchEmbeddingStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("azure")
public class AzureEmbeddingStoreConfiguration {

    @Value("${AZURE_AI_ENDPOINT}")
    private String azureAiEndpoint;

    @Value("${AZURE_AI_KEY}")
    private String azureAiKey;

    @Bean
    EmbeddingStore<TextSegment> embeddingStore() {
        return AzureAiSearchEmbeddingStore.builder()
                .endpoint(azureAiEndpoint)
                .apiKey(azureAiKey)
                .dimensions(768)
                .build();
    }
}
