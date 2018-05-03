package com.mobiledi.earnitapi.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChildren is a Querydsl query type for Children
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QChildren extends EntityPathBase<Children> {

    private static final long serialVersionUID = -1445511848L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChildren children = new QChildren("children");

    public final QAccount account;

    public final StringPath avatar = createString("avatar");

    public final DateTimePath<java.sql.Timestamp> createDate = createDateTime("createDate", java.sql.Timestamp.class);

    public final StringPath email = createString("email");

    public final StringPath fcmToken = createString("fcmToken");

    public final StringPath firstName = createString("firstName");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final StringPath lastName = createString("lastName");

    public final StringPath message = createString("message");

    public final StringPath password = createString("password");

    public final StringPath phone = createString("phone");

    public final ListPath<Task, QTask> tasks = this.<Task, QTask>createList("tasks", Task.class, QTask.class, PathInits.DIRECT2);

    public final DateTimePath<java.sql.Timestamp> updateDate = createDateTime("updateDate", java.sql.Timestamp.class);

    public QChildren(String variable) {
        this(Children.class, forVariable(variable), INITS);
    }

    public QChildren(Path<? extends Children> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChildren(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChildren(PathMetadata metadata, PathInits inits) {
        this(Children.class, metadata, inits);
    }

    public QChildren(Class<? extends Children> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.account = inits.isInitialized("account") ? new QAccount(forProperty("account")) : null;
    }

}

