'use strict';

module.exports = function (m) {
  m.route.mode = 'pathname';

  m.route(document.body, '/editar', {
    '/editar': require('home/home'),
    '/editar/erro': require('erro'),
    '/editar/servico/:id': require('servico/editor'),
    '/editar/orgao/:id': require('orgao/editor'),
    '/editar/area-de-interesse/:id': require('area-de-interesse/editor'),
    '/editar/importar-xml/:any': require('importar-xml/editor'),
    '/editar/pagina-especial/:id': require('pagina-especial/editor'),
    '/editar/pagina/nova': require('pagina/nova'),
    '/editar/visualizar/servico/:id': require('servico/visualizar')
  });
};

