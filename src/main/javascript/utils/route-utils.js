'use strict';

function paramId() {
  return m.route.param('id');
}

function ehNovo() {
  return paramId() === 'novo';
}

module.exports = {
  id: paramId,
  ehNovo: ehNovo
};
