'use strict';

module.exports = function (xml) {

  return m.request({

    method: 'POST',
    url: '/editar/v3/servico',
    data: xml,

    config: function (xhr) {
      xhr.setRequestHeader('Accepts', 'application/xml');
      xhr.setRequestHeader('Content-Type', 'application/xml');
    },

    serialize: function (svc) {
      return new XMLSerializer().serializeToString(svc);
    },

    deserialize: function (str) {
      return new DOMParser().parseFromString(str, 'application/xml');
    },

  });

};
