'use strict';

var CabecalhoModel = require('cabecalho/cabecalho-model');
var slugify = require('slugify');
var salvarServico = require('xml/salvar').salvarServico;
var publicarServico = require('xml/publicar').publicarServico;
var descartarServico = require('xml/descartar').descartarServico;
var despublicarServico = require('api').despublicar;
var servicoEmEdicao = require('servico/servico-em-edicao');
var redirecionarNovoServico = require('redirecionador');
var routeUtils = require('utils/route-utils');
var permissoes = require('utils/permissoes');
var modificado = m.prop(false);

module.exports = {
  controller: function () {
    this.cabecalho = new CabecalhoModel();
    this.salvandoServico = m.prop(false);
    this.caiuSessao = m.prop(false);
    this.servico = servicoEmEdicao.recuperar(this.cabecalho);
    this.redirect = m.prop(false);

    this._servicoSalvo = _.bind(function (servico) {
      this.servico(servico);
      redirecionarNovoServico('servico', this.servico().nome(), this.redirect);
      this.cabecalho.limparErro();
      modificado(false);
      return servico;
    }, this);

    this.salvar = function () {
      return salvarServico(this.servico(), this.cabecalho.metadados)
        .then(this._servicoSalvo);
    };

    this.publicar = function () {
      return this.salvar()
        .then(_.bind(function (s) {
          return publicarServico(s, this.cabecalho.metadados);
        }, this));
    };

    this.descartar = function () {
      this.redirect(true);
      return descartarServico(this.servico(), this.cabecalho.metadados)
        .then(this._servicoSalvo);
    };

    this.despublicar = function () {
      return despublicarServico('servico', routeUtils.id(), this.cabecalho.metadados);
    };

    this.visualizar = function () {
      var id = slugify(this.servico().nome());
      servicoEmEdicao.manter(this.servico, this.cabecalho.metadados);
      m.route('/editar/visualizar/servico/' + id);
      return true;
    };
  },

  view: function (ctrl) {
    if (!permissoes.podeCriarPagina('servico')) {
      return m.component(require('acesso-negado'));
    }

    var binding = {
      servico: ctrl.servico,
      novo: routeUtils.ehNovo(),
      salvandoServico: ctrl.salvandoServico,
      caiuSessao: ctrl.caiuSessao
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
          nomeDaPagina: ctrl.servico().nome() || 'Novo serviço',
          salvar: _.bind(ctrl.salvar, ctrl),
          publicar: _.bind(ctrl.publicar, ctrl),
          visualizar: _.bind(ctrl.visualizar, ctrl),
          descartar: _.bind(ctrl.descartar, ctrl),
          cabecalho: ctrl.cabecalho,
          salvandoServico: ctrl.salvandoServico,
          caiuSessao: ctrl.caiuSessao,
          orgaoId: ctrl.servico().orgao().nome()
        }),
        m.component(require('componentes/menu/menu-lateral'), {
          menuConfig: binding,
          despublicarConfig: {
            tipo: 'servico',
            despublicar: _.bind(ctrl.despublicar, ctrl),
            metadados: ctrl.cabecalho.metadados(),
            salvandoServico: ctrl.salvandoServico,
            caiuSessao: ctrl.caiuSessao,
            orgaoId: ctrl.servico().orgao().nome()
          }
        }),
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
