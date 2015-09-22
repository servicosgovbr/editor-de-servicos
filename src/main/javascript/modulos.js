'use strict';

module.exports = function (m) {
  m.route.mode = 'pathname';

  m.route(document.body, '/editar', {
    '/editar': require('home/home'),
    '/editar/erro': require('erro'),
    '/editar/servico/:id': require('servico/editor'),
    '/editar/orgao/:id': require('orgao/editor'),
    '/editar/pagina/nova' : require('pagina/nova')
  });
};
