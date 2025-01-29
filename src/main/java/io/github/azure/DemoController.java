package io.github.azure;

import dev.langchain4j.chain.ConversationalChain;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.UrlDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.document.transformer.jsoup.HtmlToTextDocumentTransformer;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DemoController {

    private final ChatLanguageModel chatLanguageModel;

    private final EmbeddingModel embeddingModel;

    private final EmbeddingStore<TextSegment> embeddingStore;


    public DemoController(ChatLanguageModel chatLanguageModel, EmbeddingModel embeddingModel, EmbeddingStore<TextSegment> embeddingStore) {
        this.chatLanguageModel = chatLanguageModel;
        this.embeddingModel = embeddingModel;
        this.embeddingStore = embeddingStore;
    }

    @GetMapping("/")
    public String demo() {
        return "demo";
    }

    @GetMapping("/1")
    String getAnswer(Model model) {
        String question = "Who painted the Mona Lisa?";
        String answer = chatLanguageModel.generate(UserMessage.from(question)).content().text();
        return getView(model, "1: simple question", question, answer);
    }

    @GetMapping("/2")
    String reasoning(Model model) {
        String question = "Maria's father has 4 daughters: Spring, Autumn, Winter. What is the name of the fourth daughter?";
        String answer = chatLanguageModel.generate(UserMessage.from(question)).content().text();
        return getView(model, "2: Reasoning question", question, answer);
    }

    @GetMapping("/3")
    String getAnswerWithSystemMessage(Model model) {
        SystemMessage systemMessage = SystemMessage.from("I answer questions in French, in 100 words or less.");

        String question = "Give an explanation on how the Mona Lisa was painted.";
        String answer = chatLanguageModel.generate(systemMessage, UserMessage.from(question)).content().text();
        return getView(model, "3: advanced question", question, answer);
    }

    @GetMapping("/4")
    String getAnswerWithLocation(Model model) {
        String question = "Where can you see this painting?";
        String answer = chatLanguageModel.generate(UserMessage.from(question)).content().text();
        return getView(model, "4: A question without memory", question, answer);
    }

    @GetMapping("/5")
    String getAnswerUsingConversationChain(Model model) {
        String context = "Who painted the Mona Lisa?";
        String question = "Where can you see this painting?";

        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(20);
        ConversationalChain chain = ConversationalChain.builder()
                .chatLanguageModel(chatLanguageModel)
                .chatMemory(chatMemory)
                .build();

        chain.execute(context);
        String answer = chain.execute(question);
        return getView(model, "5: A question with memory", question, answer);
    }

    @GetMapping("/6")
    String ingestNews(Model model) {
        Document document = UrlDocumentLoader.load("https://www.microsoft.com/investor/reports/ar23/index.html", new TextDocumentParser());

        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentTransformer(new HtmlToTextDocumentTransformer(".annual-report"))
                .documentSplitter(DocumentSplitters.recursive(300, 30))
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();

        ingestor.ingest(document);

        return getView(model, "6: Advanced data ingestion", "Ingesting news into the vector database", "OK");
    }

    @GetMapping("/7")
    String rag(Model model) {
        String question = "How many people are employed by Microsoft in the US?";

        RagAssistant ragAssistant = AiServices.builder(RagAssistant.class)
                .chatLanguageModel(chatLanguageModel)
                .contentRetriever(new EmbeddingStoreContentRetriever(embeddingStore, embeddingModel, 3))
                .build();

        String answer = ragAssistant.augmentedChat(question);

        return getView(model, "7: Retrieval-Augmented Generation (RAG)", question, answer);
    }

    private static String getView(Model model, String demoName, String question, String answer) {
        model.addAttribute("demo", demoName);
        model.addAttribute("question", question);
        model.addAttribute("answer", answer);
        return "demo";
    }
}

interface RagAssistant {

    String augmentedChat(String userMessage);
}
