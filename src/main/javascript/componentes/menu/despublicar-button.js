/*global loggedUser*/
'use strict';

var safeGet = require('utils/code-checks').safeGet;
var promise = require('utils/promise');
var confirmacao = require('componentes/menu/despublicar-confirmacao');
var permissoes = require('utils/permissoes');

function botaoQueEspera(opts) {
  return m('button.botao-primario#' + opts.id, {
    onclick: opts.onclick,
    disabled: (opts.disabled ? 'disabled' : '')
  }, opts.espera ? [m('i.fa.fa-spin.fa-spinner'), m.trust('&nbsp;Despublicando...')] : [m('i.fa.fa-' + opts.icon), 'Despublicar']);
}

function urlInNewContext(tipo, contexto) {
  var parser = document.createElement('a');
  parser.href = window.location.href;
  parser.pathname = '/' + tipo + '/' + contexto;
  parser.search = '';
  parser.hash = '';
  return parser.href;
}

module.exports = {
  controller: function (args) {
    this.despublicar = safeGet(args, 'despublicar');
    this.despublicando = m.prop(false);
    this.orgaoId = args.orgaoId;

    this.onClick = function () {
      this.despublicando(true);
      m.redraw();
      return promise.onSuccOrError(this.despublicar(), _.bind(function () {
        this.despublicando(false);
        m.redraw();
      }, this));
    };
  },

  view: function (ctrl, args) {
    var tipo = _.get(args, 'tipo');
    var publicado = _.get(args, 'metadados.publicado.revisao');

    return m('#secao-despublicar', [
      m('hr'),
      m('label', [
        m('', [
          'Status: ',
          publicado ? m('span.publicado', 'publicado') : m('span.npublicado', 'despublicado')
        ]),
        publicado ? m('a', {
          href: urlInNewContext(tipo, m.route.param('id')),
          target: '_blank'
        }, 'Acesse a versão no Portal') : ''
     ]),
      permissoes.podeDespublicarPagina(loggedUser.siorg, ctrl.orgaoId) ? botaoQueEspera({
        id: 'despublicar',
        onclick: confirmacao(_.bind(ctrl.onClick, ctrl)),
        icon: '',
        disabled: !publicado || ctrl.despublicando(),
        espera: ctrl.despublicando()
      }) : m(''),
      m('hr')
    ]);
  }
};
