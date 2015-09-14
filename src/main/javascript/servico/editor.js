'use strict';

var CabecalhoModel = require('cabecalho/modelo');
var slugify = require('slugify');
var salvarServico = require('xml/salvar');
var carregarServico = require('xml/carregar');
var validacoes = require('utils/validacoes');
var limparModelo = require('limpar-modelo');

var modificado = m.prop(false);

module.exports = {

  controller: function () {
    this.cabecalho = new CabecalhoModel();
    this.servico = carregarServico(m.route.param('id'), this.cabecalho);

    this.salvar = function () {
      if (validacoes.valida(this.servico().nome)) {
        return salvarServico(this.servico, this.cabecalho.metadados)
          .then(_.bind(this.cabecalho.limparErro, this.cabecalho))
          .then(function () {
            modificado(false);
          }).then(function () {
            var oldId = m.route.param('id');
            var newId = slugify(this.servico().nome());
            if (!_.isEqual(oldId, newId)) {
              m.route('/editar/servico/' + newId);
            }
          }.bind(this));
      } else {
        return m.deferred().reject(this.servico().nome.erro()).promise;
      }
    };

    this.publicar = function () {
      this.servico(limparModelo(this.servico()));
      var id = slugify(this.servico().nome());
      if (validacoes.valida(this.servico())) {
        return this.salvar().then(function () {
          m.request({
            method: 'PUT',
            url: '/editar/api/servico/' + id
          });
        });
      } else {
        return false;
      }
    };
  },

  view: function (ctrl) {
    var binding = {
      servico: ctrl.servico,
      novo: m.route.param('id') === 'novo'
    };

    return m('#conteudo', {
      config: function (element, isInitialized) {
        if (isInitialized) {
          return;
        }

        jQuery(element).on('change', function () {
          modificado(true);
        });

        jQuery(window).bind('beforeunload', function () {
          if (modificado()) {
            return 'Suas últimas alterações ainda não foram salvas.';
          }
        });
      }
    }, [
      m('span.cabecalho-cor'),
      m('#wrapper', [
        m.component(require('cabecalho/cabecalho'), {
          metadados: true,
          logout: true,
          salvar: _.bind(ctrl.salvar, ctrl),
          publicar: _.bind(ctrl.publicar, ctrl),
          cabecalho: ctrl.cabecalho
        }),
        m.component(require('componentes/menu-lateral'), binding),

        m('#servico', m('.scroll', [
          m.component(require('servico/dados-basicos/dados-basicos'), binding),
          m.component(require('servico/solicitantes/solicitantes'), binding),
          m.component(require('servico/etapas/etapas'), binding),
          m.component(require('servico/outras-informacoes/outras-informacoes'), binding),
        ]))
      ])
    ]);
  }
};
