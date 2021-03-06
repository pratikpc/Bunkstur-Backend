package com.bunkstur.storage.subjects;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.smallrye.mutiny.Multi;
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

        public Multi<String> GetSubjects() {
                ScanRequest scan = SubjectAsyncService.BuildScan()
                                // Return only subject name
                                .attributesToGet(Columns.SUBJECT_NAME).build();

                // Scan Items
                return Uni.createFrom().completionStage(() -> dynamoDB.scan(scan)).onItem()
                                .transformToMulti(resp -> Multi.createFrom().items(resp.items().stream()))
                                // Convert row to Subject Name
                                .map(row -> row.get(Columns.SUBJECT_NAME).s());
        }

        public Uni<Boolean> Add(final @NonNull String subject) {
                return Uni.createFrom().completionStage(() -> dynamoDB.putItem(itemPutBuilder -> itemPutBuilder
                                // Set Table Name
                                .tableName(Table.NAME)
                                // Set Item
                                .item( // Create item to insert
                                                Map.of( // Column Name
                                                                Columns.SUBJECT_NAME,
                                                                // Subject Name
                                                                AttributeValue.builder().s(subject).build()))))
                                .map(resp -> true);
        }

}
