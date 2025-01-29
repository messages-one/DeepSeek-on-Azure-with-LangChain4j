# DeepSeek on Azure with LangChain4j demo

_Author: [Julien Dubois](https://www.julien-dubois.com)_

## Goal

This is a Spring Boot project that demonstrates how to use DeepSeek on Azure with LangChain4j.

It contains the following demos:

- How to generate text
- A reasoning test
- How to use a chat conversation with memory of the context.

Those demos either run locally (using Ollama) or in the Azure cloud (using GitHub Models).

## Configuration

There are 2 Spring Boot profiles, one to run locally, and one to run using GitHub Models.

### _Option 1_ : Running DeepSeek-R1 locally

_This configuration uses Ollama with deepseek-r1_

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
ollama pull deepseek-r1:14b
```

### _Option 2_ : Running in the cloud with GitHub Models

_This configuration uses GitHub Models with deepseek-r1_

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
