DROP table authorities;
DROP table users;

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
  siorg varchar(50) not null,
  siape varchar(50) unique,
  email_institucional varchar(256),
  email_secundario varchar(256),
  constraint fk_usuarios_papeis foreign key(papel_id) references papeis(id)
);

INSERT INTO public.papeis (nome) VALUES ('ROLE_ADMIN');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg) VALUES ('mauricio.formiga@planejamento.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg) VALUES ('formiga.mauricio@gmail.com', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg) VALUES ('almeidafab@gmail.com', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg) VALUES ('fabricio.fontenele@planejamento.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg) VALUES ('carlos.vieira@planejamento.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg) VALUES ('silvia.belarmino@planejamento.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg) VALUES ('andrea.ricciardi@planejamento.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg) VALUES ('everson.aguiar@planejamento.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg) VALUES ('joelson.vellozo@planejamento.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg) VALUES ('izabel.garcia@planejamento.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg) VALUES ('carlos-eduardo.melo@planejamento.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg) VALUES ('cvillela@thoughtworks.com', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg) VALUES ('bleite@thoughtworks.com', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg) VALUES ('oliviaj@thoughtworks.com', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg) VALUES ('srosa@thoughtworks.com', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg) VALUES ('jkirchne@thoughtworks.com', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg) VALUES ('pleal@thoughtworks.com', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg) VALUES ('gfreita@thoughtworks.com', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg) VALUES ('gramos@thoughtworks.com', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg) VALUES ('nitai.silva@cultura.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg) VALUES ('esau.mendes@planejamento.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg) VALUES ('josselmo.bezerra@planejamento.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg) VALUES ('thiago.avila@seplag.al.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg) VALUES ('toni.esteves@gmail.com', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934');
