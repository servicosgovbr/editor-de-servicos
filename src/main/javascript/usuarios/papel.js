'use strict';

module.exports = function (m, papeis, element) {
  var papel = {};
  papel.id = m.prop('1');

  function convertToSelect2Data(originalArray) {
    var newArray = [];

    for (var i = 0; i < originalArray.length; i++) {
      newArray.push({
        id: originalArray[i].id,
        text: convertToTitleCase(originalArray[i].nome)
      });
    }

    return newArray;
  }

  function convertToTitleCase(value) {
    return value.replace('_', ' ').replace(/\w\S*/g, 
      function (text) {
        return text.charAt(0).toUpperCase() + text.substr(1).toLowerCase();
      }
    );
  }

  papel.view = function () {
    return m('label', [
        m.component(require('componentes/select2'), {
        prop: papel.id,
        tte: {},
        data: convertToSelect2Data(papeis)
      }),
        m('input[type=hidden]#papelId', {
        name: 'papelId',
        value: papel.id()
      })
    ]);
  };

  m.mount(document.getElementById(element), {
    view: papel.view
  });
};
