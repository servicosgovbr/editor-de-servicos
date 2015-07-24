'use strict';

module.exports.create = function (config) {
  var capitalize = function (str) {
    return str && str.charAt(0).toUpperCase() + str.slice(1);
  };

  return {
    controller: function (args) {
      this[config.chave] = args[config.chave];

      this['todos' + capitalize(config.chave)] = m.request({
        method: 'GET',
        url: config.url
      });

      this.adicionar = function (e) {
        var obj = e.target.value;
        var objs = this[config.chave]();

        objs = _.without(objs, obj);
        if (e.target.checked) {
          objs.push(obj);
        }

        this[config.chave](objs);
      };
    },

    view: function (ctrl) {
      return m('fieldset#' + config.id, [
        m('h3', config.titulo),
        m('', ctrl['todos' + capitalize(config.chave)]().map(function (obj) {
          return m('label', [
            m('input[type=checkbox]', {
              value: obj,
              checked: _.contains(ctrl[config.chave](), obj),
              onchange: ctrl.adicionar.bind(ctrl)
            }),
            obj
          ]);
        }))
      ]);
    }
  };
};
