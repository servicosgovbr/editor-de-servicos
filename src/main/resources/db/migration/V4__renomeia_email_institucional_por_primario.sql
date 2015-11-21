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
  siorg varchar(50) not null,
  siape varchar(50) unique,
  email_primario varchar(256) not null,
  email_secundario varchar(256),
  constraint fk_usuarios_papeis foreign key(papel_id) references papeis(id)
);

INSERT INTO public.papeis (nome) VALUES ('ROLE_ADMIN');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg, email_primario) VALUES ('mauricio.formiga@planejamento.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934', 'mauricio.formiga@planejamento.gov.br');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg, email_primario) VALUES ('formiga.mauricio@gmail.com', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934', 'formiga.mauricio@gmail.com');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg, email_primario) VALUES ('almeidafab@gmail.com', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934', 'almeidafab@gmail.com');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg, email_primario) VALUES ('fabricio.fontenele@planejamento.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934', 'fabricio.fontenele@planejamento.gov.br');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg, email_primario) VALUES ('carlos.vieira@planejamento.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934', 'carlos.vieira@planejamento.gov.br');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg, email_primario) VALUES ('silvia.belarmino@planejamento.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934', 'silvia.belarmino@planejamento.gov.br');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg, email_primario) VALUES ('andrea.ricciardi@planejamento.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934', 'andrea.ricciardi@planejamento.gov.br');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg, email_primario) VALUES ('everson.aguiar@planejamento.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934', 'everson.aguiar@planejamento.gov.br');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg, email_primario) VALUES ('joelson.vellozo@planejamento.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934', 'joelson.vellozo@planejamento.gov.br');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg, email_primario) VALUES ('izabel.garcia@planejamento.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934', 'izabel.garcia@planejamento.gov.br');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg, email_primario) VALUES ('carlos-eduardo.melo@planejamento.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934', 'carlos-eduardo.melo@planejamento.gov.br');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg, email_primario) VALUES ('cvillela@thoughtworks.com', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934', 'cvillela@thoughtworks.com');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg, email_primario) VALUES ('bleite@thoughtworks.com', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934', 'bleite@thoughtworks.com');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg, email_primario) VALUES ('oliviaj@thoughtworks.com', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934', 'oliviaj@thoughtworks.com');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg, email_primario) VALUES ('srosa@thoughtworks.com', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934', 'srosa@thoughtworks.com');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg, email_primario) VALUES ('jkirchne@thoughtworks.com', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934', 'jkirchne@thoughtworks.com');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg, email_primario) VALUES ('pleal@thoughtworks.com', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934', 'pleal@thoughtworks.com');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg, email_primario) VALUES ('gfreita@thoughtworks.com', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934', 'gfreita@thoughtworks.com');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg, email_primario) VALUES ('gramos@thoughtworks.com', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934', 'gramos@thoughtworks.com');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg, email_primario) VALUES ('nitai.silva@cultura.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934', 'nitai.silva@cultura.gov.br');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg, email_primario) VALUES ('esau.mendes@planejamento.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934', 'esau.mendes@planejamento.gov.br');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg, email_primario) VALUES ('josselmo.bezerra@planejamento.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934', 'josselmo.bezerra@planejamento.gov.br');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg, email_primario) VALUES ('thiago.avila@seplag.al.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934', 'thiago.avila@seplag.al.gov.br');
INSERT INTO public.usuarios (cpf, senha, papel_id, servidor, habilitado, siorg, email_primario) VALUES ('toni.esteves@gmail.com', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true, '1934', 'toni.esteves@gmail.com');

