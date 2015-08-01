'use strict';

m.route.mode = 'pathname';

m.route(document.body, '/editar', {
  '/editar': require('bem-vindo'),
  '/editar/servico/:id': require('editor-de-servicos')
});
