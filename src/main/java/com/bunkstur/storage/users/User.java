package com.bunkstur.storage.users;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotBlank;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.smallrye.common.constraint.NotNull;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@RegisterForReflection
public class User {
    private final @NotBlank String userId;
    private final @NotBlank String contact;

    public User(final String userId, final String contact) {
        this.userId = userId;
        this.contact = contact;
    }

    public User(final @NotNull Map<String, AttributeValue> user) {
        this(user.get(Columns.USER_ID).s(), user.get(Columns.USER_CONTACT).s());
    }

    public Map<String, AttributeValue> AsRow() {
        Map<String, AttributeValue> row = new HashMap<>();
        row.put(Columns.USER_ID, AttributeValue.builder().s(userId).build());
        row.put(Columns.USER_CONTACT, AttributeValue.builder().s(contact).build());
        return row;
    }

    /**
     * @return the contact
     */
    public String getContact() {
        return contact;
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }
}
