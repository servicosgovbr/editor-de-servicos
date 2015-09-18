'use strict';

module.exports = {
  safeGet: function (obj, propName) {
    var value = _.property(propName)(obj);
    if (!value) {
      throw new Error(propName + ' não foi informado: ' + value);
    }
    return value;
  }
};
