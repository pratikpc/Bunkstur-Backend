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
---
### Users
1. UserID (Partition/Sort Key)
2. Contact information (Attribute) (Email ID or Phone Number. From Firebase JWT Token)

#### AWS Command

```shell script
aws dynamodb create-table \
    --table-name users \
    --attribute-definitions AttributeName=userid,AttributeType=S \
    --key-schema AttributeName=userid,KeyType=HASH \
    --billing-mode=PAY_PER_REQUEST
```
---
### Attendance
1. UserID (Partition Key)
2. UUID (Sort key)
3. Subject
4. Attendance Type (A/P/N)
    - A: Absent
    - P: Present
    - N: No Lecture
5. Date
6. Time Start
7. Time End

#### AWS Command

```shell script
aws dynamodb create-table \
    --table-name attendance \
    --attribute-definitions AttributeName=userid,AttributeType=S AttributeName=uuid,AttributeType=S \
    --key-schema AttributeName=userid,KeyType=HASH AttributeName=uuid,KeyType=RANGE \
    --billing-mode=PAY_PER_REQUEST
```
