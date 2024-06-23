package com.ward.ward_server.api.releaseInfo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReleaseInfo is a Querydsl query type for ReleaseInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReleaseInfo extends EntityPathBase<ReleaseInfo> {

    private static final long serialVersionUID = -1611555452L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReleaseInfo releaseInfo = new QReleaseInfo("releaseInfo");

    public final com.ward.ward_server.global.Object.QBaseTimeEntity _super = new com.ward.ward_server.global.Object.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final EnumPath<com.ward.ward_server.api.releaseInfo.entity.enums.CurrencyUnit> currencyUnit = createEnum("currencyUnit", com.ward.ward_server.api.releaseInfo.entity.enums.CurrencyUnit.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final EnumPath<com.ward.ward_server.api.releaseInfo.entity.enums.DeliveryMethod> deliveryMethod = createEnum("deliveryMethod", com.ward.ward_server.api.releaseInfo.entity.enums.DeliveryMethod.class);

    public final QDrawPlatform drawPlatform;

    public final DateTimePath<java.time.LocalDateTime> dueDate = createDateTime("dueDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> itemId = createNumber("itemId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final EnumPath<com.ward.ward_server.api.releaseInfo.entity.enums.NotificationMethod> notificationMethod = createEnum("notificationMethod", com.ward.ward_server.api.releaseInfo.entity.enums.NotificationMethod.class);

    public final DateTimePath<java.time.LocalDateTime> presentationDate = createDateTime("presentationDate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> releaseDate = createDateTime("releaseDate", java.time.LocalDateTime.class);

    public final EnumPath<com.ward.ward_server.api.releaseInfo.entity.enums.ReleaseMethod> releaseMethod = createEnum("releaseMethod", com.ward.ward_server.api.releaseInfo.entity.enums.ReleaseMethod.class);

    public final NumberPath<Integer> releasePrice = createNumber("releasePrice", Integer.class);

    public final StringPath siteUrl = createString("siteUrl");

    public QReleaseInfo(String variable) {
        this(ReleaseInfo.class, forVariable(variable), INITS);
    }

    public QReleaseInfo(Path<? extends ReleaseInfo> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReleaseInfo(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReleaseInfo(PathMetadata metadata, PathInits inits) {
        this(ReleaseInfo.class, metadata, inits);
    }

    public QReleaseInfo(Class<? extends ReleaseInfo> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.drawPlatform = inits.isInitialized("drawPlatform") ? new QDrawPlatform(forProperty("drawPlatform")) : null;
    }

}

