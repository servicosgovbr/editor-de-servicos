'use strict';

var capitalize = function (str) {
  return str && str.charAt(0).toUpperCase() + str.slice(1);
};

module.exports.create = function (config) {

  return {
    controller: function (args) {
      this.servico = args.servico;
      this['todos' + capitalize(config.chave)] = m.prop(config.itens);

      this.adicionar = function (e) {
        var obj = e.target.value;
        var objs = this.servico()[config.chave]();

        objs = _.without(objs, obj);
        if (e.target.checked) {
          objs.push(obj);
        }

        this.servico()[config.chave](objs);
      };
    },

    view: function (ctrl) {
      return m('fieldset#' + config.id, [
        m('h3.input-container', {
          class: ctrl.servico()[config.chave].erro()
        }, [
          config.titulo,
          m.component(require('tooltips')[config.chave])
        ]),

        m('', ctrl['todos' + capitalize(config.chave)]().map(function (obj) {
          return m('label', [
            m('input[type=checkbox]', {
              value: obj,
              checked: _.contains(ctrl.servico()[config.chave](), obj),
              onchange: ctrl.adicionar.bind(ctrl)
            }),
            obj
          ]);
        }))
      ]);
    }
  };
};
