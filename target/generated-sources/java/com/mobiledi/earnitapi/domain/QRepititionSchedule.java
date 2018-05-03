package com.mobiledi.earnitapi.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRepititionSchedule is a Querydsl query type for RepititionSchedule
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QRepititionSchedule extends EntityPathBase<RepititionSchedule> {

    private static final long serialVersionUID = -1476841659L;

    public static final QRepititionSchedule repititionSchedule = new QRepititionSchedule("repititionSchedule");

    public final ListPath<DayTaskStatus, QDayTaskStatus> dayTaskStatuses = this.<DayTaskStatus, QDayTaskStatus>createList("dayTaskStatuses", DayTaskStatus.class, QDayTaskStatus.class, PathInits.DIRECT2);

    public final StringPath endTime = createString("endTime");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath repeat = createString("repeat");

    public final SetPath<String, StringPath> specificDays = this.<String, StringPath>createSet("specificDays", String.class, StringPath.class, PathInits.DIRECT2);

    public final StringPath startTime = createString("startTime");

    public final ListPath<Task, QTask> tasks = this.<Task, QTask>createList("tasks", Task.class, QTask.class, PathInits.DIRECT2);

    public QRepititionSchedule(String variable) {
        super(RepititionSchedule.class, forVariable(variable));
    }

    public QRepititionSchedule(Path<? extends RepititionSchedule> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRepititionSchedule(PathMetadata metadata) {
        super(RepititionSchedule.class, metadata);
    }

}

