create table respuestas(

    id bigserial not null,
    mensaje varchar(255) not null,
    topico_id bigint not null,
    fecha_creacion timestamp not null,
    autor_id bigint not null,
    solucion boolean,

    primary key(id),
    constraint fk_respuestas_topico_id foreign key(topico_id) references topicos(id),
    constraint fk_respuestas_autor_id foreign key(autor_id) references usuarios(id)
);