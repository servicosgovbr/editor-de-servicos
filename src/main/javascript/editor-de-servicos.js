'use strict';

var modelos = require('modelos');
var importarXml = require('xml/importar-v3');
var exportarXml = require('xml/exportar');
var salvarXml = require('xml/salvar');
var slugify = require('slugify');
var limparModelo = require('limpar-modelo');
var carregarServico = require('xml/carregar-servico');
var validador = require('validador');
var mensagemErro = require('mensagens-erro');

module.exports = function (servico) {
  return {
    controller: function () {
      this.cabecalho = new modelos.Cabecalho();
      this.servico = servico || carregarServico(m.route.param('id'), this.cabecalho);
      this.validador = new m.validator(validador);

      this.salvar = function () {
        servico = limparModelo(this.servico());
        
        this.validador.validate(servico);
        if (this.validador.hasErrors()) {
          var erro = mensagemErro(this.validador.hasError('nome'));
          this.validador.clearErrors();

          return m.deferred().reject(erro).promise;
        }

        var onAjaxError = require('utils/erro-ajax');
        return salvarXml(slugify(servico.nome()), exportarXml(servico), this.cabecalho.metadados)
          .then(importarXml, onAjaxError)
          .then(this.servico, onAjaxError)
          .then(_.bind(this.cabecalho.limparErro, this.cabecalho), onAjaxError);
      };
    },

    view: function (ctrl) {
      var binding = {
        servico: ctrl.servico
      };

      return m('#conteudo', [
        m('span.cabecalho-cor'),
        m('#wrapper', [
          m.component(require('componentes/cabecalho'), {
            salvar: _.bind(ctrl.salvar, ctrl),
            cabecalho: ctrl.cabecalho
          }),
          m.component(require('componentes/menu-lateral'), binding),

          m('#servico', m('.scroll', [
            m.component(require('componentes/dados-basicos'), binding),
            m.component(require('componentes/solicitantes'), binding),
            m.component(require('componentes/etapas'), binding),
            m.component(require('componentes/outras-informacoes'), binding),
          ]))
        ])
    ]);
    }
  };
};
