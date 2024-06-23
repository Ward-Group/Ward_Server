package com.ward.ward_server.api.wishBrand;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWishBrand is a Querydsl query type for WishBrand
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWishBrand extends EntityPathBase<WishBrand> {

    private static final long serialVersionUID = -1413897295L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWishBrand wishBrand = new QWishBrand("wishBrand");

    public final com.ward.ward_server.api.item.entity.QBrand brand;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.ward.ward_server.api.user.entity.QUser user;

    public final DateTimePath<java.time.LocalDateTime> wishDate = createDateTime("wishDate", java.time.LocalDateTime.class);

    public QWishBrand(String variable) {
        this(WishBrand.class, forVariable(variable), INITS);
    }

    public QWishBrand(Path<? extends WishBrand> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWishBrand(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWishBrand(PathMetadata metadata, PathInits inits) {
        this(WishBrand.class, metadata, inits);
    }

    public QWishBrand(Class<? extends WishBrand> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.brand = inits.isInitialized("brand") ? new com.ward.ward_server.api.item.entity.QBrand(forProperty("brand")) : null;
        this.user = inits.isInitialized("user") ? new com.ward.ward_server.api.user.entity.QUser(forProperty("user")) : null;
    }

}

