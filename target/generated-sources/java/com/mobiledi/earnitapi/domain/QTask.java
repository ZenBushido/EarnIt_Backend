package com.mobiledi.earnitapi.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTask is a Querydsl query type for Task
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QTask extends EntityPathBase<Task> {

    private static final long serialVersionUID = -56448674L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTask task = new QTask("task");

    public final NumberPath<Double> allowance = createNumber("allowance", Double.class);

    public final QChildren children;

    public final DateTimePath<java.sql.Timestamp> createDate = createDateTime("createDate", java.sql.Timestamp.class);

    public final StringPath description = createString("description");

    public final DateTimePath<java.sql.Timestamp> dueDate = createDateTime("dueDate", java.sql.Timestamp.class);

    public final QGoal goal;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final StringPath name = createString("name");

    public final NumberPath<Integer> pictureRequired = createNumber("pictureRequired", Integer.class);

    public final QRepititionSchedule repititionSchedule;

    public final BooleanPath shouldLockAppsIfTaskOverdue = createBoolean("shouldLockAppsIfTaskOverdue");

    public final StringPath status = createString("status");

    public final ListPath<TaskComment, QTaskComment> taskComments = this.<TaskComment, QTaskComment>createList("taskComments", TaskComment.class, QTaskComment.class, PathInits.DIRECT2);

    public final DateTimePath<java.sql.Timestamp> updateDate = createDateTime("updateDate", java.sql.Timestamp.class);

    public QTask(String variable) {
        this(Task.class, forVariable(variable), INITS);
    }

    public QTask(Path<? extends Task> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTask(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTask(PathMetadata metadata, PathInits inits) {
        this(Task.class, metadata, inits);
    }

    public QTask(Class<? extends Task> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.children = inits.isInitialized("children") ? new QChildren(forProperty("children"), inits.get("children")) : null;
        this.goal = inits.isInitialized("goal") ? new QGoal(forProperty("goal"), inits.get("goal")) : null;
        this.repititionSchedule = inits.isInitialized("repititionSchedule") ? new QRepititionSchedule(forProperty("repititionSchedule")) : null;
    }

}

