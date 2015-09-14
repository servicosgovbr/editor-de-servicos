'use strict';

module.exports = function (m) {
  m.route.mode = 'pathname';

  m.route(document.body, '/editar', {
    '/editar': require('bem-vindo'),
    '/editar/erro': require('erro'),
    '/editar/servico/:id': require('servico/editor'),
    '/editar/orgao/:id': require('editor-de-paginas/editor'),
    '/editar/pagina/:id': require('editor-de-paginas/editor')
  });
};
