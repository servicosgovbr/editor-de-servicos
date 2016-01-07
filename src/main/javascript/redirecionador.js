'use strict';

var slugify = require('slugify');

module.exports = function (tipo, nome, redirect) {
  var oldId = m.route.param('id');
  var newId = slugify(nome) || oldId;
  if (!_.isEqual(oldId, newId)) {
    m.route('/editar/' + tipo + '/' + newId);
  }
  if (redirect && redirect()) {
    m.route(window.location.pathname);
  }
};
