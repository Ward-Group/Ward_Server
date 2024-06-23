package com.ward.ward_server.api.wishItem;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWishItem is a Querydsl query type for WishItem
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWishItem extends EntityPathBase<WishItem> {

    private static final long serialVersionUID = 1963438413L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWishItem wishItem = new QWishItem("wishItem");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.ward.ward_server.api.item.entity.QItem item;

    public final com.ward.ward_server.api.user.entity.QUser user;

    public final DateTimePath<java.time.LocalDateTime> wishDate = createDateTime("wishDate", java.time.LocalDateTime.class);

    public QWishItem(String variable) {
        this(WishItem.class, forVariable(variable), INITS);
    }

    public QWishItem(Path<? extends WishItem> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWishItem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWishItem(PathMetadata metadata, PathInits inits) {
        this(WishItem.class, metadata, inits);
    }

    public QWishItem(Class<? extends WishItem> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.item = inits.isInitialized("item") ? new com.ward.ward_server.api.item.entity.QItem(forProperty("item"), inits.get("item")) : null;
        this.user = inits.isInitialized("user") ? new com.ward.ward_server.api.user.entity.QUser(forProperty("user")) : null;
    }

}

