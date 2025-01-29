package io.github.azure.configuration.chatmodel.azure;

import dev.langchain4j.model.azure.AzureOpenAiChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("azure")
public class AzureChatModelConfiguration {

    @Value("${AZURE_AI_ENDPOINT}")
    private String azureAiEndpoint;

    @Value("${AZURE_AI_KEY}")
    private String azureAiKey;

    @Bean
    ChatLanguageModel azureOpenAIChatLanguageModel() {
        return AzureOpenAiChatModel.builder()
                .endpoint(azureAiEndpoint)
                .apiKey(azureAiKey)
                .deploymentName("deepseek-r1")
                .logRequestsAndResponses(true)
                .strictJsonSchema(true)
                .build();
    }
}
