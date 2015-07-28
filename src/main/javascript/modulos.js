'use strict';

var editor = require('editor-de-servicos');

m.route.mode = 'pathname';
m.route(document.body, '/editar', {
  '/editar': editor,
  '/editar/servico/:id': editor
});
