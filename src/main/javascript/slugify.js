'use strict';

module.exports = function (str) {
  return _.deburr(str)
    .trim()
    .toLowerCase()
    .replace(/\s+/g, '-')
    .replace(/\//g, '-')
    .replace(/\./g, '-')
    .replace(/_/g, '-')
    .replace(/[^\w\-]+/g, '')
    .replace(/\-\-+/g, '-')
    .replace(/^-+/, '')
    .replace(/-+$/, '');
};
