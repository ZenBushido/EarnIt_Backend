package com.mobiledi.earnitapi.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTaskComment is a Querydsl query type for TaskComment
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QTaskComment extends EntityPathBase<TaskComment> {

    private static final long serialVersionUID = 1347041057L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTaskComment taskComment = new QTaskComment("taskComment");

    public final StringPath comment = createString("comment");

    public final DateTimePath<java.sql.Timestamp> createDate = createDateTime("createDate", java.sql.Timestamp.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath pictureUrl = createString("pictureUrl");

    public final NumberPath<Integer> readStatus = createNumber("readStatus", Integer.class);

    public final QTask task;

    public final DateTimePath<java.sql.Timestamp> updateDate = createDateTime("updateDate", java.sql.Timestamp.class);

    public QTaskComment(String variable) {
        this(TaskComment.class, forVariable(variable), INITS);
    }

    public QTaskComment(Path<? extends TaskComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTaskComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTaskComment(PathMetadata metadata, PathInits inits) {
        this(TaskComment.class, metadata, inits);
    }

    public QTaskComment(Class<? extends TaskComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.task = inits.isInitialized("task") ? new QTask(forProperty("task"), inits.get("task")) : null;
    }

}

