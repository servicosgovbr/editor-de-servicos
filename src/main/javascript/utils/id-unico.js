'use strict';

var slugify = require('slugify');

module.exports = function (unsafeId) {
  return jQuery.ajax({
    type: 'GET',
    url: '/editar/api/existe-id-servico/' + slugify(unsafeId),
    async: false
  }).responseText !== 'true';
};
