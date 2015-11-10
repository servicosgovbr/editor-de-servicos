'use strict';

var CabecalhoModel = require('cabecalho/cabecalho-model');
var slugify = require('slugify');
var salvarServico = require('xml/salvar');
var publicarServico = require('xml/publicar');
var descartarServico = require('xml/descartar');
var servicoEmEdicao = require('servico/servico-em-edicao');
var promise = require('utils/promise');

var modificado = m.prop(false);

function redirecionarNovoServico(nome) {
  var oldId = m.route.param('id');
  var newId = slugify(nome);
  if (!_.isEqual(oldId, newId)) {
    m.route('/editar/servico/' + newId);
  }
}

function endComputation() {
  m.endComputation();
}

module.exports = {
  controller: function () {
    this.cabecalho = new CabecalhoModel();
    this.servico = servicoEmEdicao.recuperar(this.cabecalho);

    this._servicoSalvo = _.bind(function (servico) {
      this.servico(servico);
      redirecionarNovoServico(this.servico().nome());
      this.cabecalho.limparErro();
      modificado(false);

      return servico;
    }, this);

    this.salvar = function () {
      m.startComputation();

      return promise.onSuccOrError(
        salvarServico(this.servico(), this.cabecalho.metadados)
        .then(this._servicoSalvo),
        endComputation);
    };

    this.publicar = function () {
      m.startComputation();

      return promise.onSuccOrError(
        this.salvar()
          .then(_.bind(function(s) {
            return publicarServico(s, this.cabecalho.metadados);
          }, this)),
        endComputation);
    };

    this.descartar = function () {
      return descartarServico(this.servico(), this.cabecalho.metadados)
        .then(this._servicoSalvo);
    };

    this.visualizar = function () {
      var id = slugify(this.servico().nome());
      servicoEmEdicao.manter(this.servico);
      m.route('/editar/visualizar/servico/' + id);
      return true;
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
          visualizar: _.bind(ctrl.visualizar, ctrl),
          descartar: _.bind(ctrl.descartar, ctrl),
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
