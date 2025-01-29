#!/usr/bin/env bash

# Execute this script to deploy the needed Azure OpenAI models to execute the integration tests.
# For this, you need Azure CLI installed: https://learn.microsoft.com/cli/azure/install-azure-cli

echo "Setting up environment variables..."
echo "----------------------------------"
PROJECT="langchain4j-$RANDOM-$RANDOM-$RANDOM"
AZURE_RESOURCE_GROUP="rg-$PROJECT"
AZURE_LOCATION="swedencentral"
AZURE_AI_SERVICE="ai-$PROJECT"
SEARCH_SERVICE="search-$PROJECT"
TAG="$PROJECT"

echo "Creating the resource group..."
echo "------------------------------"
az group create \
  --name "$AZURE_RESOURCE_GROUP" \
  --location "$AZURE_LOCATION" \
  --tags system="$TAG"

SCRIPTPATH="$( cd -- "$(dirname "$0")" >/dev/null 2>&1 ; pwd -P )"
echo "AZURE_RESOURCE_GROUP=$AZURE_RESOURCE_GROUP" > $SCRIPTPATH/../../../.env
echo "AZURE_LOCATION=$AZURE_LOCATION" >> $SCRIPTPATH/../../../.env
echo "AZURE_AI_SERVICE=$AZURE_AI_SERVICE" >> $SCRIPTPATH/../../../.env

# If you want to know the available SKUs, run the following Azure CLI command:
# az cognitiveservices account list-skus --location "$AZURE_LOCATION"  -o table

echo "Creating the Cognitive Service..."
echo "---------------------------------"
COGNITIVE_SERVICE_ID=$(az cognitiveservices account create \
  --name "$AZURE_AI_SERVICE" \
  --resource-group "$AZURE_RESOURCE_GROUP" \
  --location "$AZURE_LOCATION" \
  --custom-domain "$AZURE_AI_SERVICE" \
  --tags system="$TAG" \
  --kind "OpenAI" \
  --sku "S0" \
   | jq -r ".id")

echo "Storing Azure AI Foundry endpoint and key in an environment variable..."
echo "--------------------------------------------------------"
AZURE_AI_ENDPOINT=$(
  az cognitiveservices account show \
    --name "$AZURE_AI_SERVICE" \
    --resource-group "$AZURE_RESOURCE_GROUP" \
    | jq -r .properties.endpoint
  )

AZURE_AI_KEY=$(
  az cognitiveservices account keys list \
    --name "$AZURE_AI_SERVICE" \
    --resource-group "$AZURE_RESOURCE_GROUP" \
    | jq -r .key1
  )

# If you want to know the available models, run the following Azure CLI command:
# az cognitiveservices account list-models --resource-group "$AZURE_RESOURCE_GROUP" --name "$AZURE_AI_SERVICE" -o table  

echo "Deploying a deepseek-r1 model..."
echo "----------------------"
az cognitiveservices account deployment create \
  --name "$AZURE_AI_SERVICE" \
  --resource-group "$AZURE_RESOURCE_GROUP" \
  --deployment-name "deepseek-r1" \
  --model-name "deepseek-r1" \
  --sku-capacity 1 \
  --sku-name "Standard"

echo "Creating the AI Search Service..."
echo "---------------------------------"
az search service create \
  --name "$SEARCH_SERVICE" \
  --resource-group "$AZURE_RESOURCE_GROUP" \
  --location "$AZURE_LOCATION" \
  --sku "basic" \
  --tags system="$TAG"

echo "Storing Azure AI Search endpoint and key in environment variables..."
echo "--------------------------------------------------------"
AZURE_SEARCH_ENDPOINT="https://$SEARCH_SERVICE.search.windows.net"
AZURE_SEARCH_KEY=$(az search admin-key show --service-name "$SEARCH_SERVICE" --resource-group "$AZURE_RESOURCE_GROUP" | jq -r .primaryKey)

echo "AZURE_AI_ENDPOINT=$AZURE_AI_ENDPOINT" >> $SCRIPTPATH/../../../.env
echo "AZURE_AI_KEY=$AZURE_AI_KEY" >> $SCRIPTPATH/../../../.env
echo "AZURE_SEARCH_ENDPOINT=$AZURE_SEARCH_ENDPOINT" >> $SCRIPTPATH/../../../.env
echo "AZURE_SEARCH_KEY=$AZURE_SEARCH_KEY" >> $SCRIPTPATH/../../../.env

echo "#########################################################################################################"
echo "The environment variables you need to set are also stored in the .env file at the root of the project."
echo "#########################################################################################################"
echo "Done!"
