# Bunkstur Backend code

Written in Quarkus

## Developing offline
---
### Use [AWS DynamoDB Local](https://hub.docker.com/r/amazon/dynamodb-local)
```shell script
docker run --publish 8000:8000 amazon/dynamodb-local -jar DynamoDBLocal.jar -inMemory -sharedDb
```

## Create Tables
---
### Subjects

1. Name (Partition/Sort Key)

#### AWS Command

```shell script
aws dynamodb create-table \
    --table-name subjects \
    --attribute-definitions AttributeName=name,AttributeType=S \
    --key-schema AttributeName=name,KeyType=HASH \
    --billing-mode=PAY_PER_REQUEST
```
