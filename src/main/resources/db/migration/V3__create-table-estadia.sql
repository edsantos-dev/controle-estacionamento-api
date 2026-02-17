create table estadia (
    id bigint auto_increment primary key,
    vaga_id bigint not null,
    veiculo_id bigint not null,
    data_entrada datetime not null,
    data_saida datetime null,
    valor_final decimal(10,2) not null,
    status varchar(20) not null,

    constraint fk_estadia_vaga
        foreign key (vaga_id)
        references vaga(id),

    constraint fk_estadia_veiculo
        foreign key (veiculo_id)
        references veiculo(id)
);

create index idx_estadia_veiculo_status
on estadia (veiculo_id, status);