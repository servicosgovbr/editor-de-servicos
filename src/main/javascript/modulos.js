'use strict';

module.exports = function (m) {
  m.route.mode = 'pathname';

  m.route(document.body, '/editar', {
    '/editar': require('home/home'),
    '/editar/erro': require('erro'),
    '/editar/servico/:id': require('servico/editor'),
    '/editar/orgao/:id': require('pagina/orgao/editor'),
    '/editar/importar-xml/:any': require('importar-xml/editor'),
    '/editar/pagina-tematica/:id': require('pagina/tematica/editor'),
    '/editar/pagina/nova': require('pagina/nova'),
    '/editar/visualizar/servico/:id': require('servico/visualizar')
  });
};
