'use strict';

var slugify = require('slugify');

module.exports = function (unsafeId) {
  return jQuery.ajax({
    method: 'GET',
    url: '/editar/api/id-unico/' + slugify(unsafeId),
    async: false
  }).responseText === 'true';
};
