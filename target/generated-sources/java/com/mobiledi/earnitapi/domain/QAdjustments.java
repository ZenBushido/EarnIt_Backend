package com.mobiledi.earnitapi.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAdjustments is a Querydsl query type for Adjustments
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAdjustments extends EntityPathBase<Adjustments> {

    private static final long serialVersionUID = -331286867L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAdjustments adjustments = new QAdjustments("adjustments");

    public final NumberPath<Double> amount = createNumber("amount", Double.class);

    public final DateTimePath<java.sql.Timestamp> createdDateTime = createDateTime("createdDateTime", java.sql.Timestamp.class);

    public final QGoal goal;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath reason = createString("reason");

    public QAdjustments(String variable) {
        this(Adjustments.class, forVariable(variable), INITS);
    }

    public QAdjustments(Path<? extends Adjustments> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAdjustments(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAdjustments(PathMetadata metadata, PathInits inits) {
        this(Adjustments.class, metadata, inits);
    }

    public QAdjustments(Class<? extends Adjustments> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.goal = inits.isInitialized("goal") ? new QGoal(forProperty("goal"), inits.get("goal")) : null;
    }

}

