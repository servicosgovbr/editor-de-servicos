'use strict';

var ultimo = '';

module.exports = {
  set: function(v) {
    ultimo = v;
  },
  get: function() {
    return ultimo;
  },
  clear: function() {
    ultimo = '';
  }
};
