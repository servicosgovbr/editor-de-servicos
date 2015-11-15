'use strict';

module.exports = function (element, isInitialized) {
  if (isInitialized) {
    return;
  }

  var token = jQuery('meta[name=\'_csrf_token\']').attr('content');
  var name = jQuery('meta[name=\'_csrf_name\']').attr('content');

  var csrf = document.createElement('input');
  csrf.setAttribute('type', 'hidden');
  csrf.setAttribute('name', name);
  csrf.setAttribute('value', token);

  element.appendChild(csrf);
};
