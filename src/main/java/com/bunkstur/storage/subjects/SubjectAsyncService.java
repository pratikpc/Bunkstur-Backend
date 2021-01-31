package com.bunkstur.storage.subjects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.smallrye.mutiny.Uni;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;

@ApplicationScoped
public class SubjectAsyncService {
        @Inject
        DynamoDbAsyncClient dynamoDB;

        private static ScanRequest.Builder BuildScan() {
                return ScanRequest.builder().tableName(Table.NAME);
        }

        public Uni<List<String>> GetAllSubjects() {
                ScanRequest scan = SubjectAsyncService.BuildScan()
                                // Get only the top 30 results
                                .limit(30)
                                // Return only subject name
                                .attributesToGet(Columns.SUBJECT_NAME).build();

                // Scan Items
                return Uni.createFrom().completionStage(() -> dynamoDB.scan(scan)).onItem()
                                .transform(resp -> resp.items().stream()
                                                // Convert row to Subject Name
                                                .map(row -> row.get(Columns.SUBJECT_NAME).s())
                                                .collect(Collectors.toList()));
        }

        public Uni<Boolean> Add(final @NonNull String subject) {
                Map<String, AttributeValue> item = new HashMap<>();
                // Create item to insert
                item.put(
                                // Column Name
                                Columns.SUBJECT_NAME,
                                // Subject Name
                                AttributeValue.builder().s(subject).build());
                System.out.println(subject + " " + item + " " + Columns.SUBJECT_NAME);
                return Uni.createFrom()
                                .completionStage(() -> dynamoDB.putItem(
                                                itemPutBuilder -> itemPutBuilder
                                                // Set Table Name
                                                .tableName(Table.NAME)
                                                // Set Item
                                                .item(item)))
                                .onItem().transform(resp -> true);
        }

}