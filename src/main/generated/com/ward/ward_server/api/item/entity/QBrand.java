package com.ward.ward_server.api.item.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBrand is a Querydsl query type for Brand
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBrand extends EntityPathBase<Brand> {

    private static final long serialVersionUID = 1376850518L;

    public static final QBrand brand = new QBrand("brand");

    public final StringPath englishName = createString("englishName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath koreanName = createString("koreanName");

    public final StringPath logoImage = createString("logoImage");

    public final NumberPath<Long> viewCount = createNumber("viewCount", Long.class);

    public QBrand(String variable) {
        super(Brand.class, forVariable(variable));
    }

    public QBrand(Path<? extends Brand> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBrand(PathMetadata metadata) {
        super(Brand.class, metadata);
    }

}

