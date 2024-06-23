package com.ward.ward_server.api.entry.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEntryRecord is a Querydsl query type for EntryRecord
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEntryRecord extends EntityPathBase<EntryRecord> {

    private static final long serialVersionUID = 518327829L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEntryRecord entryRecord = new QEntryRecord("entryRecord");

    public final DateTimePath<java.time.LocalDateTime> entryDate = createDateTime("entryDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.ward.ward_server.api.releaseInfo.entity.QReleaseInfo releaseInfo;

    public final com.ward.ward_server.api.user.entity.QUser user;

    public QEntryRecord(String variable) {
        this(EntryRecord.class, forVariable(variable), INITS);
    }

    public QEntryRecord(Path<? extends EntryRecord> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEntryRecord(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEntryRecord(PathMetadata metadata, PathInits inits) {
        this(EntryRecord.class, metadata, inits);
    }

    public QEntryRecord(Class<? extends EntryRecord> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.releaseInfo = inits.isInitialized("releaseInfo") ? new com.ward.ward_server.api.releaseInfo.entity.QReleaseInfo(forProperty("releaseInfo"), inits.get("releaseInfo")) : null;
        this.user = inits.isInitialized("user") ? new com.ward.ward_server.api.user.entity.QUser(forProperty("user")) : null;
    }

}

