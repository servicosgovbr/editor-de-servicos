DROP table usuarios;
DROP table papeis;

create table papeis (
  id SERIAL primary key,
  nome varchar(50) not null UNIQUE
);

create table usuarios(
  cpf varchar(50) not null primary key,
  senha varchar(100) not null,
  papel_id INTEGER not null,
  servidor boolean not null,
  habilitado boolean not null,
  siorg varchar(256) not null,
  nome varchar(100) not null,
  siape varchar(50) unique,
  email_primario varchar(256) not null,
  email_secundario varchar(256),
  constraint fk_usuarios_papeis foreign key(papel_id) references papeis(id)
);

create table tokens(
  cpf varchar(50) not null primary key,
  token varchar(100) not null,
  data_criacao timestamp not null
);

INSERT INTO public.papeis (nome) VALUES ('ROLE_ADMIN');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg, email_primario, nome) VALUES ('12312312312', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934', 'email@email', 'Editor de Serviços');
