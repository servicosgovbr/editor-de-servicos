'use strict';

var CabecalhoModel = require('cabecalho/cabecalho-model');
var carregarServico = require('xml/carregar');

module.exports = {

  controller: function (args) {
    this.cabecalho = new CabecalhoModel();
    this.servico = carregarServico(m.route.param('id'), this.cabecalho);
  },

  view: function (ctrl, args) {
    var servico = ctrl.servico();
    return m('#conteudo', [m('span.cabecalho-cor'),
                m('#wrapper', [
                m.component(require('cabecalho/cabecalho'), {
          metadados: false,
          logout: true,
          cabecalho: ctrl.cabecalho
        }),
                m('#servico', m('.scroll', [m('a', servico.nome())]))
            ])]);
  }
};
