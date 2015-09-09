'use strict';

var slugify = require('slugify');

module.exports = function (id) {
  id = slugify(id);

  return jQuery.ajax({
    type: 'GET',
    url: '/editar/api/existe-id-servico/' + id,
    async: false
  }).responseText === 'true';
};
