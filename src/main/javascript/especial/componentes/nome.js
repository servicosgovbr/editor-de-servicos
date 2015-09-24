'use strict';

var safeGet = require('utils/code-checks').safeGet;

module.exports = {
  view: function (ctrl, args) {
    var prop = safeGet(args, 'prop');

    return m('input', {
      oninput: m.withAttr('value', prop),
      value: prop()
    });
  }
};
