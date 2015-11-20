'use strict';

var slugify = require('slugify');

module.exports = function (tipo, nome) {
  var oldId = m.route.param('id');
  var newId = slugify(nome);
  if (!_.isEqual(oldId, newId)) {
    m.route('/editar/' + tipo + '/' + newId);
  }
};
