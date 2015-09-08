'use strict';

module.exports = function (m) {
  m.route.mode = 'pathname';

  m.route(document.body, '/editar', {
    '/editar': require('bem-vindo'),
    '/editar/servico/migracao': require('migracao'),
    '/editar/erro': require('erro'),
    '/editar/servico/:id': require('editor-de-servicos')
  });
};
