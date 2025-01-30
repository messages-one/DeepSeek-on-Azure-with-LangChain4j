# DeepSeek-R1 on Azure with LangChain4j demo

_Author: [Julien Dubois](https://www.julien-dubois.com)_

## Introduction

DeepSeek-R1 has been announced on [GitHub Models](https://github.blog/changelog/2025-01-29-deepseek-r1-is-now-available-in-github-models-public-preview/)
as well as on [Azure AI Foundry](https://azure.microsoft.com/en-us/blog/deepseek-r1-is-now-available-on-azure-ai-foundry-and-github/), and
the goal of this sample AI application is to demonstrate how to use it with LangChain4j and Java.

_We concentrate here on GitHub Models as they are easier to use (you just need a GitHub token, no Azure subscription required), then Azure AI Foundry uses the same model and infrastructure_

## About this demo project

This is a Spring Boot project that demonstrates how to use DeepSeek-R1 on Azure with LangChain4j.

It contains the following demos:

- How to generate text
- A reasoning test
- How to use a chat conversation with memory of the context

Please note that despite being called a `Chat Model`, DeepSeek-R1 is better at reasoning than at doing a real conversation, so the most 
interesting demo is the reasoning test.

Those demos either run locally (using Ollama) or in the Azure cloud (using GitHub Models).

## Configuration

There are 2 Spring Boot profiles, one to run locally, and one to run using GitHub Models.

### _Option 1_ : Running DeepSeek-R1 locally

_This configuration uses Ollama with DeepSeek-R1:14b, a light model that can run on a local machine_

We're using the `14b` version of DeepSeek-R1 for this demo: it normally provides a good balance between performance and quality. If you have 
less ressources, you can try with the `7b` version, which is lighter, but the reasoning test will usually fail. With GitHub Models (see Option 2 below),
you'll have the full `671b` model which will be able to do much more complex reasoning. 

It is enabled by using the `local` Spring Boot profile.
One way to do this is to set `spring.profiles.active=local` in the `src/main/resources/application.properties` file.

### Run deepseek-r1 locally using Ollama

Install [Ollama](https://ollama.com/) to be able to run LLMs locally on your machine.

Start Ollama by typing:

```bash
ollama serve
```

Install `deepseek-r1:14b` from Ollama, available at https://ollama.com/library/deepseek-r1 by typing:

```bash
ollama pull deepseek-r1:14b
```

### _Option 2_ : Running in the cloud with GitHub Models

_This configuration uses GitHub Models with DeepSeek-R1:671b, the most advanced model which needs advanced GPU and memory resources_

It is enabled by using the `github` Spring Boot profile.
One way to do this is to set `spring.profiles.active=github` in the `src/main/resources/application.properties` file.

Go to https://github.com/marketplace?type=models and follow the documentation to create a GitHub token, 
that you'll use to access the models.

You will then need to configure an environment variable named `GITHUB_TOKEN` with the token value:

```bash
export GITHUB_TOKEN=<your_token>
``` 

## Running the demos

Once the resources (local or GitHub Models) are configured, you can run the demos using the following command:

```shell
./mvnw spring-boot:run
```

Then you can access the base URL, where you find the Web UI: [http://localhost:8080/](http://localhost:8080/).

The demos are available in the top menu.
