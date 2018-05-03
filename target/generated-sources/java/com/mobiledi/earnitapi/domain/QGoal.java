package com.mobiledi.earnitapi.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGoal is a Querydsl query type for Goal
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QGoal extends EntityPathBase<Goal> {

    private static final long serialVersionUID = -56823060L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGoal goal = new QGoal("goal");

    public final ListPath<Adjustments, QAdjustments> adjustments = this.<Adjustments, QAdjustments>createList("adjustments", Adjustments.class, QAdjustments.class, PathInits.DIRECT2);

    public final NumberPath<Double> amount = createNumber("amount", Double.class);

    public final QChildren children;

    public final DateTimePath<java.sql.Timestamp> createDate = createDateTime("createDate", java.sql.Timestamp.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final StringPath name = createString("name");

    public final ListPath<Task, QTask> tasks = this.<Task, QTask>createList("tasks", Task.class, QTask.class, PathInits.DIRECT2);

    public final DateTimePath<java.sql.Timestamp> updateDate = createDateTime("updateDate", java.sql.Timestamp.class);

    public QGoal(String variable) {
        this(Goal.class, forVariable(variable), INITS);
    }

    public QGoal(Path<? extends Goal> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QGoal(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QGoal(PathMetadata metadata, PathInits inits) {
        this(Goal.class, metadata, inits);
    }

    public QGoal(Class<? extends Goal> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.children = inits.isInitialized("children") ? new QChildren(forProperty("children"), inits.get("children")) : null;
    }

}

