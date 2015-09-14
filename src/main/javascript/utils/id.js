'use strict';

var counters = {};

var gerador = function (base) {
  if (!counters[base]) {
    counters[base] = 0;
  }
  return base + '-' + counters[base]++;
};

gerador.reset = function () {
  counters = {};
};

module.exports = gerador;