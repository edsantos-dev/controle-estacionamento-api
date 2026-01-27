create table veiculo (
    id bigint not null auto_increment,
    placa varchar(10) not null,
    tipo varchar(20) not null.

    constraint pk_veiculo primary key (id),
    constraint uk_veiculo_placa unique (placa)

)