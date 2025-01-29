package io.github.azure.configuration.embeddingmodel.minilm;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MiniLmEmbeddingModelConfiguration {

    @Bean
    EmbeddingModel miniLmEmbeddingModel() {
        return new AllMiniLmL6V2EmbeddingModel();
    }
}
