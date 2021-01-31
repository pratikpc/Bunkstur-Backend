package com.bunkstur.storage.attendance;

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
public class AttendanceAsyncService {
        @Inject
        DynamoDbAsyncClient dynamoDB;

        private static ScanRequest.Builder BuildScan() {
                return ScanRequest.builder().tableName(Table.NAME)
                                // Return all
                                .attributesToGet(Columns.USER_ID, Columns.ATT_TYPE, Columns.SUBJECT, Columns.DATE,
                                                Columns.TIME_START, Columns.TIME_END, Columns.UUID);
        }

        public Multi<Attendance> All() {
                ScanRequest scan = AttendanceAsyncService.BuildScan().build();
                // Scan Items
                return Uni.createFrom().completionStage(() -> dynamoDB.scan(scan)).onItem()
                                .transformToMulti(resp -> Multi.createFrom().items(resp.items().stream()))
                                // Convert row to Subject Name
                                .map(row -> new Attendance(row));
        }

        private Multi<Map<String, AttributeValue>> SingleUserRecords(final String userId) {
                final var query = dynamoDB.query(queryRequestBuilder -> queryRequestBuilder.tableName(Table.NAME)
                                .keyConditionExpression("#user = :user")
                                .expressionAttributeNames(Map.of("#user", Columns.USER_ID)).expressionAttributeValues(
                                                Map.of(":user", AttributeValue.builder().s(userId).build())));

                // Scan Items
                return Uni.createFrom().completionStage(() -> query).onItem()
                                .transformToMulti(rows -> Multi.createFrom().items(rows.items().stream()));
        }

        public Multi<Attendance> SingleUser(String userId) {
                // Scan Items
                return SingleUserRecords(userId).map(row -> new Attendance(row));
        }

        public Uni<Boolean> RemoveSingleRecord(String userId, String uuid) {
                final var deleter = dynamoDB
                                .deleteItem(deleteItemRequestBuilder -> deleteItemRequestBuilder.tableName(Table.NAME)
                                                .key(Map.of(Columns.USER_ID, AttributeValue.builder().s(userId).build(),
                                                                Columns.UUID,
                                                                AttributeValue.builder().s(uuid).build())));

                // Scan Items
                return Uni.createFrom().completionStage(() -> deleter).map(resp -> true);
        }

        public Uni<Boolean> RemoveUser(final String userId) {
                return Uni.combine().all().unis(
                                // Scan Items
                                SingleUserRecords(userId)
                                                // Operate on all Multi Row
                                                // Convert to UUID
                                                .map(row -> row.get(Columns.UUID).s())
                                                // Run next operation tp remove all rows
                                                .map(uuid -> RemoveSingleRecord(userId, uuid)).collectItems().asList())
                                // Final Result of Combined Uni will be true
                                .discardItems().map(unused -> true);
        }

        public Uni<Boolean> Add(final @NonNull Attendance attendance) {
                return Uni.createFrom().completionStage(() -> dynamoDB.putItem(itemPutBuilder -> itemPutBuilder
                                // Set Table Name
                                .tableName(Table.NAME)
                                // Set Item
                                .item(attendance.AsRow()))).map(resp -> true);
        }

}
