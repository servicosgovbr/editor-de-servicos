'use strict';

var slugify = require('slugify');

module.exports = function (unsafeId) {
  var id = slugify(unsafeId);

  return jQuery.ajax({
    method: 'GET',
    url: '/editar/api/id-unico/' + id,
    async: false
  }).responseText === 'true';
};
