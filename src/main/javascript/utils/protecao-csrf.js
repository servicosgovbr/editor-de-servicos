'use strict';
var atributosCsrf = require('utils/atributos-csrf');
module.exports = function (element, isInitialized) {
  if (isInitialized) {
    return;
  }

  var csrf = document.createElement('input');
  csrf.setAttribute('type', 'hidden');
  csrf.setAttribute('name', atributosCsrf.name);
  csrf.setAttribute('value', atributosCsrf.token);

  element.appendChild(csrf);
};
