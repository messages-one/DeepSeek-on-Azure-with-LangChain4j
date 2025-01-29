# DeepSeek on Azure with LangChain4j demo

_Author: [Julien Dubois](https://www.julien-dubois.com)_

## Goal

This is a Spring Boot project that demonstrates how to use DeepSeek on Azure with LangChain4j.

It contains the following demos:

- How to generate a text using deepseek-r1.
- How to use a chat conversation with memory of the context.

Those demos either run locally (using Ollama and Qdrant) or in the cloud (using Azure AI Foundry and Azure AI Search).

## Configuration

There are 2 Spring Boot profiles, one to run everything locally, and one to run everything on Azure.

### _Option 1_ : Running in the cloud with Azure

This configuration uses:

- __Chat Model__: Azure AI Foundry with deepseek-r1
- __Embedding model__: in-memory Java with AllMiniLmL6V2EmbeddingModel
- __Embedding store__: Azure AI Search

It is enabled by using the `azure` Spring Boot profile.
One way to do this is to set `spring.profiles.active=azure` in the `src/main/resources/application.properties` file.

To provision the Azure resources, you need to run the `src/main/script/deploy-to-azure.sh` script. It will create the following resources:

- An Azure AI Foundry instance, with the necessary models for this demo.
- An Azure AI Search instance.

At the end of this script, the following environment variables will be displayed (and stored in the `.env` file), and you will need them to run the application:
- `AZURE_AI_ENDPOINT`: your Azure AI Foundry URL endpoint.
- `AZURE_AI_KEY`: your Azure AI Foundry API key.
- `AZURE_SEARCH_ENDPOINT`: your Azure AI Search URL endpoint.
- `AZURE_SEARCH_KEY`: your Azure AI Search API key.

### _Option 2_ : Fully local

This configuration uses:

- __Chat Model__: Ollama with deepseek-r1
- __Embedding model__: in-memory Java with AllMiniLmL6V2EmbeddingModel
- __Embedding store__: Qdrant

It is enabled by using the `local` Spring Boot profile.
One way to do this is to set `spring.profiles.active=local` in the `src/main/resources/application.properties` file.

### Run deepseek-r1 locally using Ollama

Install [Ollama](https://ollama.com/) to be able to run LLMs locally on your machine.

Start Ollama by typing:

```bash
ollama serve
```

Install deepseek-r1 from Ollama, available at https://ollama.com/library/deepseek-r1 by typing:

```bash
ollama pull deepseek-r1
```

### Run Qdrant locally

This is needed for the RAG pattern demo.

Install [Docker](https://www.docker.com) on your machine.

Run Qdrant with Docker Compose using the `src/main/docker/docker-compose.yml` file:

```bash
docker compose -f src/main/docker/docker-compose.yml up
```

Once Qdrant is running, its Web UI will be available at [http://localhost:6333/dashboard](http://localhost:6333/dashboard).

## Running the demos

Once the resources (Azure or local) are configured, you can run the demos using the following command:

```shell
./mvnw spring-boot:run
```

Then you can access the base URL, where you find the Web UI: [http://localhost:8080/](http://localhost:8080/).

The demos are available in the top menu.
