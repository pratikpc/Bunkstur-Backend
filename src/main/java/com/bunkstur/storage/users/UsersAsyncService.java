package com.bunkstur.storage.users;

import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.smallrye.mutiny.Uni;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;

@ApplicationScoped
public class UsersAsyncService {
        @Inject
        DynamoDbAsyncClient dynamoDB;

        private static ScanRequest.Builder BuildScan() {
                return ScanRequest.builder().tableName(Table.NAME);
        }

        public Uni<List<User>> All() {
                ScanRequest scan = UsersAsyncService.BuildScan()
                                // Return all
                                .attributesToGet(Columns.USER_ID, Columns.USER_CONTACT).build();

                // Scan Items
                return Uni.createFrom().completionStage(() -> dynamoDB.scan(scan)).onItem()
                                .transform(resp -> resp.items().stream()
                                                // Convert row to User
                                                .map(row -> new User(row)).collect(Collectors.toList()));
        }

        public Uni<Boolean> Add(final @NonNull User user) {
                return Uni.createFrom().completionStage(() -> dynamoDB.putItem(itemPutBuilder -> itemPutBuilder
                                // Set Table Name
                                .tableName(Table.NAME)
                                // Set Item
                                .item(user.AsRow()))).onItem().transform(resp -> true);
        }

}
