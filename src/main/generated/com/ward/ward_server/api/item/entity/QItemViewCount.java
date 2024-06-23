package com.ward.ward_server.api.item.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QItemViewCount is a Querydsl query type for ItemViewCount
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QItemViewCount extends EntityPathBase<ItemViewCount> {

    private static final long serialVersionUID = 1904605030L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QItemViewCount itemViewCount = new QItemViewCount("itemViewCount");

    public final DateTimePath<java.time.LocalDateTime> calculatedAt = createDateTime("calculatedAt", java.time.LocalDateTime.class);

    public final EnumPath<com.ward.ward_server.api.item.entity.enumtype.Category> category = createEnum("category", com.ward.ward_server.api.item.entity.enumtype.Category.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QItem item;

    public final NumberPath<Long> viewCount = createNumber("viewCount", Long.class);

    public QItemViewCount(String variable) {
        this(ItemViewCount.class, forVariable(variable), INITS);
    }

    public QItemViewCount(Path<? extends ItemViewCount> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QItemViewCount(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QItemViewCount(PathMetadata metadata, PathInits inits) {
        this(ItemViewCount.class, metadata, inits);
    }

    public QItemViewCount(Class<? extends ItemViewCount> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.item = inits.isInitialized("item") ? new QItem(forProperty("item"), inits.get("item")) : null;
    }

}

