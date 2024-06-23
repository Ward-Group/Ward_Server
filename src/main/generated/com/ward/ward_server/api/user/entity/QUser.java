package com.ward.ward_server.api.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -50418732L;

    public static final QUser user = new QUser("user");

    public final BooleanPath appPushNotification = createBoolean("appPushNotification");

    public final StringPath email = createString("email");

    public final BooleanPath emailNotification = createBoolean("emailNotification");

    public final ListPath<com.ward.ward_server.api.entry.entity.EntryRecord, com.ward.ward_server.api.entry.entity.QEntryRecord> entryRecords = this.<com.ward.ward_server.api.entry.entity.EntryRecord, com.ward.ward_server.api.entry.entity.QEntryRecord>createList("entryRecords", com.ward.ward_server.api.entry.entity.EntryRecord.class, com.ward.ward_server.api.entry.entity.QEntryRecord.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final EnumPath<com.ward.ward_server.api.user.entity.enumtype.Role> role = createEnum("role", com.ward.ward_server.api.user.entity.enumtype.Role.class);

    public final BooleanPath snsNotification = createBoolean("snsNotification");

    public final ListPath<com.ward.ward_server.api.wishItem.WishItem, com.ward.ward_server.api.wishItem.QWishItem> wishItems = this.<com.ward.ward_server.api.wishItem.WishItem, com.ward.ward_server.api.wishItem.QWishItem>createList("wishItems", com.ward.ward_server.api.wishItem.WishItem.class, com.ward.ward_server.api.wishItem.QWishItem.class, PathInits.DIRECT2);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

