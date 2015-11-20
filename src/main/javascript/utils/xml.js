'use strict';

var cdata = function (doc, selector) {
  _.each(doc.querySelectorAll(selector), function (el) {
    var content = _(el.childNodes).map(function (cn) {
      return el.removeChild(cn).nodeValue;
    }).join('');

    el.appendChild(doc.createCDATASection(content));
  });
};

var createDocument = function (namespace) {
  return document.implementation.createDocument(namespace, '');
};

module.exports = {
  createDocument: createDocument,
  cdata: cdata
};
