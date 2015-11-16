'use strict';

var EditorBase = require('componentes/editor-base');
var CabecalhoModel = require('cabecalho/cabecalho-model');
var input = require('componentes/input');
var servicoEmEdicao = require('servico/servico-em-edicao');
var promise = require('utils/promise');

var importarXml = require('xml/importar-xml');
var isBlank = _.flow(_.trim, _.isEmpty);

function botaoQueEspera(opts) {
  return m('button.botao-primario#' + opts.id, {
    onclick: opts.onclick,
    disabled: (opts.disabled ? 'disabled' : '')
  }, opts.espera ? [m('i.fa.fa-spin.fa-spinner'), 'Importando...'] : [m('i.fa.fa-' + opts.icon), 'Importar']);
}

module.exports = {
  controller: function () {
    this.cabecalho = new CabecalhoModel();
    this.url = m.prop('');
    this.importando = m.prop(false);

    this.ok = function () {
      this.importando(true);
      m.redraw();

      promise.onSuccOrError(
        importarXml(this.url())
        .then(servicoEmEdicao.manter)
        .then(function () {
          m.route('/editar/servico/novo');
        }),
        _.bind(function () {
          this.importando(false);
        }, this));
    };
  },

  view: function (ctrl) {
    return m.component(EditorBase, {
      config: _.noop,
      cabecalhoConfig: {
        metadados: false,
        logout: true,
        cabecalho: ctrl.cabecalho
      },
      componentes: [
        m('fieldset#importar-xml', {
          method: 'POST',
          action: '/editar/importar-xml'
        }, [
          m('h3', 'Informe a URL de um XML de Serviço:'),
          m.component(input, {
            prop: ctrl.url
          }),
          botaoQueEspera({
            id: 'importar-xml',
            onclick: _.bind(ctrl.ok, ctrl),
            disabled: ctrl.importando() || isBlank(ctrl.url()),
            espera: ctrl.importando()
          })
        ])
      ]
    });
  }
};
