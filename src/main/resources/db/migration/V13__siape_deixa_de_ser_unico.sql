DROP table tokens;
DROP table usuarios;
DROP table papeis;

create table papeis (
  id SERIAL primary key,
  nome varchar(50) not null UNIQUE
);

create table usuarios(
  id SERIAL primary key,
  cpf varchar(50) not null,
  senha varchar(100),
  papel_id INTEGER not null,
  servidor boolean not null,
  habilitado boolean not null,
  siorg varchar(256) not null,
  nome varchar(100) not null,
  siape varchar(50),
  email_primario varchar(256) not null,
  email_secundario varchar(256),
  constraint fk_usuarios_papeis foreign key(papel_id) references papeis(id)
);

create table tokens(
  id SERIAL PRIMARY KEY,
  usuario_id INTEGER not null,
  token varchar(100) not null,
  data_criacao timestamp not null,
  tentativas_sobrando smallint not null,
  constraint fk_tokens_usuarios foreign key(usuario_id) references usuarios(id)
);
INSERT INTO public.papeis (nome) VALUES ('ADMIN');
INSERT INTO public.papeis (nome) VALUES ('PONTO_FOCAL');
INSERT INTO public.papeis (nome) VALUES ('PUBLICADOR');
INSERT INTO public.papeis (nome) VALUES ('EDITOR');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg, email_primario, nome) VALUES ('12312312312', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934', 'jeank@thoughtworks.com', 'Editor de Serviços');
