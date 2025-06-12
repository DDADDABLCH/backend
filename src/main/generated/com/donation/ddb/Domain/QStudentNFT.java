package com.donation.ddb.Domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStudentNFT is a Querydsl query type for StudentNFT
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStudentNFT extends EntityPathBase<StudentNFT> {

    private static final long serialVersionUID = 1422799504L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStudentNFT studentNFT = new QStudentNFT("studentNFT");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final QStudentUser studentUser;

    public QStudentNFT(String variable) {
        this(StudentNFT.class, forVariable(variable), INITS);
    }

    public QStudentNFT(Path<? extends StudentNFT> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStudentNFT(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStudentNFT(PathMetadata metadata, PathInits inits) {
        this(StudentNFT.class, metadata, inits);
    }

    public QStudentNFT(Class<? extends StudentNFT> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.studentUser = inits.isInitialized("studentUser") ? new QStudentUser(forProperty("studentUser")) : null;
    }

}

