package com.mobiledi.earnitapi.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDayTaskStatus is a Querydsl query type for DayTaskStatus
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDayTaskStatus extends EntityPathBase<DayTaskStatus> {

    private static final long serialVersionUID = 1935169242L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDayTaskStatus dayTaskStatus = new QDayTaskStatus("dayTaskStatus");

    public final DateTimePath<java.sql.Timestamp> createdDateTime = createDateTime("createdDateTime", java.sql.Timestamp.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final QRepititionSchedule repititionSchedule;

    public final StringPath status = createString("status");

    public QDayTaskStatus(String variable) {
        this(DayTaskStatus.class, forVariable(variable), INITS);
    }

    public QDayTaskStatus(Path<? extends DayTaskStatus> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDayTaskStatus(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDayTaskStatus(PathMetadata metadata, PathInits inits) {
        this(DayTaskStatus.class, metadata, inits);
    }

    public QDayTaskStatus(Class<? extends DayTaskStatus> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.repititionSchedule = inits.isInitialized("repititionSchedule") ? new QRepititionSchedule(forProperty("repititionSchedule")) : null;
    }

}

