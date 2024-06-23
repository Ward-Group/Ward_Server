package com.ward.ward_server.api.releaseInfo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDrawPlatform is a Querydsl query type for DrawPlatform
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDrawPlatform extends EntityPathBase<DrawPlatform> {

    private static final long serialVersionUID = 320308104L;

    public static final QDrawPlatform drawPlatform = new QDrawPlatform("drawPlatform");

    public final StringPath englishName = createString("englishName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath koreanName = createString("koreanName");

    public final StringPath logoImage = createString("logoImage");

    public QDrawPlatform(String variable) {
        super(DrawPlatform.class, forVariable(variable));
    }

    public QDrawPlatform(Path<? extends DrawPlatform> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDrawPlatform(PathMetadata metadata) {
        super(DrawPlatform.class, metadata);
    }

}

