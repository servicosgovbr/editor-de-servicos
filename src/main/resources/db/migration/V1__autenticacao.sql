create table users(
	username varchar(50) not null primary key,
	password varchar(100) not null,
	enabled boolean not null
);

create table authorities (
	username varchar(50) not null,
	authority varchar(50) not null,
	constraint fk_authorities_users foreign key(username) references users(username)
);
create unique index ix_auth_username on authorities (username,authority);

INSERT INTO public.users (username, password, enabled) VALUES ('mauricio.formiga@planejamento.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', true);
INSERT INTO public.users (username, password, enabled) VALUES ('formiga.mauricio@gmail.com', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', true);
INSERT INTO public.users (username, password, enabled) VALUES ('almeidafab@gmail.com', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', true);
INSERT INTO public.users (username, password, enabled) VALUES ('fabricio.fontenele@planejamento.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', true);
INSERT INTO public.users (username, password, enabled) VALUES ('carlos.vieira@planejamento.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', true);
INSERT INTO public.users (username, password, enabled) VALUES ('silvia.belarmino@planejamento.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', true);
INSERT INTO public.users (username, password, enabled) VALUES ('andrea.ricciardi@planejamento.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', true);
INSERT INTO public.users (username, password, enabled) VALUES ('everson.aguiar@planejamento.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', true);
INSERT INTO public.users (username, password, enabled) VALUES ('joelson.vellozo@planejamento.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', true);
INSERT INTO public.users (username, password, enabled) VALUES ('izabel.garcia@planejamento.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', true);
INSERT INTO public.users (username, password, enabled) VALUES ('carlos-eduardo.melo@planejamento.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', true);
INSERT INTO public.users (username, password, enabled) VALUES ('cvillela@thoughtworks.com', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', true);
INSERT INTO public.users (username, password, enabled) VALUES ('bleite@thoughtworks.com', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', true);
INSERT INTO public.users (username, password, enabled) VALUES ('oliviaj@thoughtworks.com', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', true);
INSERT INTO public.users (username, password, enabled) VALUES ('srosa@thoughtworks.com', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', true);
INSERT INTO public.users (username, password, enabled) VALUES ('jkirchne@thoughtworks.com', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', true);
INSERT INTO public.users (username, password, enabled) VALUES ('pleal@thoughtworks.com', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', true);
INSERT INTO public.users (username, password, enabled) VALUES ('gfreita@thoughtworks.com', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', true);
INSERT INTO public.users (username, password, enabled) VALUES ('gramos@thoughtworks.com', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', true);
INSERT INTO public.users (username, password, enabled) VALUES ('nitai.silva@cultura.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', true);
INSERT INTO public.users (username, password, enabled) VALUES ('esau.mendes@planejamento.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', true);
INSERT INTO public.users (username, password, enabled) VALUES ('thiago.avila@seplag.al.gov.br', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', true);
INSERT INTO public.users (username, password, enabled) VALUES ('toni.esteves@gmail.com', '$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm', true);

INSERT INTO public.authorities (username, authority) VALUES ('mauricio.formiga@planejamento.gov.br', 'ROLE_ADMIN');
INSERT INTO public.authorities (username, authority) VALUES ('formiga.mauricio@gmail.com', 'ROLE_ADMIN');
INSERT INTO public.authorities (username, authority) VALUES ('almeidafab@gmail.com', 'ROLE_ADMIN');
INSERT INTO public.authorities (username, authority) VALUES ('fabricio.fontenele@planejamento.gov.br', 'ROLE_ADMIN');
INSERT INTO public.authorities (username, authority) VALUES ('carlos.vieira@planejamento.gov.br', 'ROLE_ADMIN');
INSERT INTO public.authorities (username, authority) VALUES ('silvia.belarmino@planejamento.gov.br', 'ROLE_ADMIN');
INSERT INTO public.authorities (username, authority) VALUES ('andrea.ricciardi@planejamento.gov.br', 'ROLE_ADMIN');
INSERT INTO public.authorities (username, authority) VALUES ('everson.aguiar@planejamento.gov.br', 'ROLE_ADMIN');
INSERT INTO public.authorities (username, authority) VALUES ('joelson.vellozo@planejamento.gov.br', 'ROLE_ADMIN');
INSERT INTO public.authorities (username, authority) VALUES ('izabel.garcia@planejamento.gov.br', 'ROLE_ADMIN');
INSERT INTO public.authorities (username, authority) VALUES ('carlos-eduardo.melo@planejamento.gov.br', 'ROLE_ADMIN');
INSERT INTO public.authorities (username, authority) VALUES ('cvillela@thoughtworks.com', 'ROLE_ADMIN');
INSERT INTO public.authorities (username, authority) VALUES ('bleite@thoughtworks.com', 'ROLE_ADMIN');
INSERT INTO public.authorities (username, authority) VALUES ('oliviaj@thoughtworks.com', 'ROLE_ADMIN');
INSERT INTO public.authorities (username, authority) VALUES ('srosa@thoughtworks.com', 'ROLE_ADMIN');
INSERT INTO public.authorities (username, authority) VALUES ('jkirchne@thoughtworks.com', 'ROLE_ADMIN');
INSERT INTO public.authorities (username, authority) VALUES ('pleal@thoughtworks.com', 'ROLE_ADMIN');
INSERT INTO public.authorities (username, authority) VALUES ('gfreita@thoughtworks.com', 'ROLE_ADMIN');
INSERT INTO public.authorities (username, authority) VALUES ('gramos@thoughtworks.com', 'ROLE_ADMIN');
INSERT INTO public.authorities (username, authority) VALUES ('nitai.silva@cultura.gov.br', 'ROLE_ADMIN');
INSERT INTO public.authorities (username, authority) VALUES ('esau.mendes@planejamento.gov.br', 'ROLE_ADMIN');
INSERT INTO public.authorities (username, authority) VALUES ('thiago.avila@seplag.al.gov.br', 'ROLE_ADMIN');
INSERT INTO public.authorities (username, authority) VALUES ('toni.esteves@gmail.com', 'ROLE_ADMIN');

