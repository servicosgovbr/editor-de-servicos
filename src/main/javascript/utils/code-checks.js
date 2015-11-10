'use strict';

module.exports = {
  safeGet: function (obj, propName) {
    var value = _.get(obj, propName);
    if (!value) {
      throw new Error(propName + ' não foi informado: ' + value);
    }
    return value;
  }
};
