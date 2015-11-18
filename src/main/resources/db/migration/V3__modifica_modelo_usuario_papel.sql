DROP table users;
DROP table authorities;

create table roles (
  id SERIAL primary key,
  name varchar(50) not null UNIQUE
);

create table users(
  cpf varchar(50) not null primary key,
  password varchar(100) not null,
  role_id INTEGER not null,
  servidor boolean not null,
  enabled boolean not null

);

INSERT INTO public.roles (name) VALUES ('ROLE_ADMIN');
INSERT INTO public.users (cpf, password, role_id, servidor, enabled) VALUES ('mauricio.formiga@planejamento.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true);
INSERT INTO public.users (cpf, password, role_id, servidor, enabled) VALUES ('formiga.mauricio@gmail.com', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true);
INSERT INTO public.users (cpf, password, role_id, servidor, enabled) VALUES ('almeidafab@gmail.com', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true);
INSERT INTO public.users (cpf, password, role_id, servidor, enabled) VALUES ('fabricio.fontenele@planejamento.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true);
INSERT INTO public.users (cpf, password, role_id, servidor, enabled) VALUES ('carlos.vieira@planejamento.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true);
INSERT INTO public.users (cpf, password, role_id, servidor, enabled) VALUES ('silvia.belarmino@planejamento.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true);
INSERT INTO public.users (cpf, password, role_id, servidor, enabled) VALUES ('andrea.ricciardi@planejamento.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true);
INSERT INTO public.users (cpf, password, role_id, servidor, enabled) VALUES ('everson.aguiar@planejamento.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true);
INSERT INTO public.users (cpf, password, role_id, servidor, enabled) VALUES ('joelson.vellozo@planejamento.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true);
INSERT INTO public.users (cpf, password, role_id, servidor, enabled) VALUES ('izabel.garcia@planejamento.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true);
INSERT INTO public.users (cpf, password, role_id, servidor, enabled) VALUES ('carlos-eduardo.melo@planejamento.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true);
INSERT INTO public.users (cpf, password, role_id, servidor, enabled) VALUES ('cvillela@thoughtworks.com', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true);
INSERT INTO public.users (cpf, password, role_id, servidor, enabled) VALUES ('bleite@thoughtworks.com', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true);
INSERT INTO public.users (cpf, password, role_id, servidor, enabled) VALUES ('oliviaj@thoughtworks.com', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true);
INSERT INTO public.users (cpf, password, role_id, servidor, enabled) VALUES ('srosa@thoughtworks.com', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true);
INSERT INTO public.users (cpf, password, role_id, servidor, enabled) VALUES ('jkirchne@thoughtworks.com', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true);
INSERT INTO public.users (cpf, password, role_id, servidor, enabled) VALUES ('pleal@thoughtworks.com', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true);
INSERT INTO public.users (cpf, password, role_id, servidor, enabled) VALUES ('gfreita@thoughtworks.com', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true);
INSERT INTO public.users (cpf, password, role_id, servidor, enabled) VALUES ('gramos@thoughtworks.com', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true);
INSERT INTO public.users (cpf, password, role_id, servidor, enabled) VALUES ('nitai.silva@cultura.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true);
INSERT INTO public.users (cpf, password, role_id, servidor, enabled) VALUES ('esau.mendes@planejamento.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true);
INSERT INTO public.users (cpf, password, role_id, servidor, enabled) VALUES ('josselmo.bezerra@planejamento.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true);
INSERT INTO public.users (cpf, password, role_id, servidor, enabled) VALUES ('thiago.avila@seplag.al.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true);
INSERT INTO public.users (cpf, password, role_id, servidor, enabled) VALUES ('toni.esteves@gmail.com', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', 1, true, true);