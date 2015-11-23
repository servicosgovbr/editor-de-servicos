'use strict';

var safeGet = require('utils/code-checks').safeGet;

module.exports = {
  view: function (ctrl, args) {
    var prop = safeGet(args, 'prop');
    return m('div.input-container', {
        class: prop.erro()
      },
      m('input[type=text]', {
        onchange: m.withAttr('value', prop),
        value: prop()
      })
    );
  }
};
