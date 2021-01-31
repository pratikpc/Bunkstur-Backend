package com.bunkstur.storage.users;

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
public class UsersAsyncService {
        @Inject
        DynamoDbAsyncClient dynamoDB;

        private static ScanRequest.Builder BuildScan() {
                return ScanRequest.builder().tableName(Table.NAME);
        }

        public Uni<Boolean> RemoveUser(final String userId) {
                final var deleter = dynamoDB.deleteItem(
                                deleteItemRequestBuilder -> deleteItemRequestBuilder.tableName(Table.NAME).key(
                                                Map.of(Columns.USER_ID, AttributeValue.builder().s(userId).build())));

                // Scan Items
                return Uni.createFrom().completionStage(() -> deleter).map(resp -> true);
        }

        Multi<User> All() {
                ScanRequest scan = UsersAsyncService.BuildScan()
                                // Return all
                                .attributesToGet(Columns.USER_ID, Columns.USER_CONTACT).build();

                // Scan Items
                return Uni.createFrom().completionStage(() -> dynamoDB.scan(scan)).onItem()
                                .transformToMulti(resp -> Multi.createFrom().items(resp.items().stream()))
                                // Convert row to User
                                .map(row -> new User(row));
        }

        public Uni<Boolean> Add(final @NonNull User user) {
                return Uni.createFrom().completionStage(() -> dynamoDB.putItem(itemPutBuilder -> itemPutBuilder
                                // Set Table Name
                                .tableName(Table.NAME)
                                // Set Item
                                .item(user.AsRow()))).map(resp -> true);
        }

}
