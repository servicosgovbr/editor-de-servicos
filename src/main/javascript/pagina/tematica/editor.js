'use strict';

var Tooltips = require('tooltips');
var CabecalhoModel = require('cabecalho/cabecalho-model');
var EditorBase = require('componentes/editor-base');

var carregarPagina = require('xml/carregar').carregarPaginaTematica;
var salvarPagina = require('xml/salvar').salvarPaginaTematica;
var publicarPagina = require('xml/publicar').publicarPaginaTematica;
var descartarPagina = require('xml/descartar').descartarPaginaTematica;
var despublicar = require('api').despublicar;
var redirecionarNovaPagina = require('redirecionador');

module.exports = {
  controller: function (args) {
    this.tipo = m.prop('pagina-tematica');

    this.modificado = m.prop(false);
    this.cabecalho = new CabecalhoModel();
    this.pagina = carregarPagina(m.route.param('id'), this.cabecalho);
    this.salvandoServico = m.prop(false);
    this.caiuSessao = m.prop(false);

    this._onOp = _.bind(function (pagina) {
      this.pagina(pagina);
      this.modificado(false);
      redirecionarNovaPagina(this.tipo(), pagina.nome());
      return pagina;
    }, this);

    this.salvar = _.bind(function () {
      return salvarPagina(this.pagina(), this.cabecalho.metadados)
        .then(this.pagina)
        .then(this._onOp);
    }, this);

    this.publicar = function () {
      return this.salvar()
        .then(_.bind(function (s) {
          return publicarPagina(s, this.cabecalho.metadados);
        }, this))
        .then(this._onOp);
    };

    this.descartar = function () {
      return descartarPagina(this.pagina(), this.cabecalho.metadados)
        .then(this._onOp);
    };

    this.despublicar = function () {
      return despublicar('pagina-tematica', this.pagina().nome(), this.cabecalho.metadados);
    };
  },

  view: function (ctrl, args) {
    if (!ctrl.pagina()) {
      return m('');
    }

    var tamanhoConteudo = 10000;
    var tooltips = {
      tipo: Tooltips.tipoPagina,
      nome: Tooltips.nomePaginaTematica,
      conteudo: Tooltips.conteudoPaginaTematica
    };

    var binding = {
      pagina: ctrl.pagina,
      nome: ctrl.pagina().nome,
      novo: m.route.param('id') === 'novo'
    };

    return m.component(EditorBase, {
      conteudoConfig: function (element, isInitialized) {
        if (isInitialized) {
          return;
        }
        jQuery(element).on('change', function () {
          ctrl.modificado(true);
        });
        jQuery(window).bind('beforeunload', function () {
          if (ctrl.modificado()) {
            return 'Suas últimas alterações ainda não foram salvas.';
          }
        });
      },

      cabecalhoConfig: {
        metadados: true,
        logout: true,
        nomeDaPagina: ctrl.pagina().nome() || 'Nova página temática',
        salvar: _.bind(ctrl.salvar, ctrl),
        publicar: _.bind(ctrl.publicar, ctrl),
        descartar: _.bind(ctrl.descartar, ctrl),
        cabecalho: ctrl.cabecalho,
        salvandoServico: ctrl.salvandoServico,
        caiuSessao: ctrl.caiuSessao
      },

      menuLateralConfig: {
        despublicarConfig: {
          tipo: 'pagina-tematica',
          despublicar: _.bind(ctrl.despublicar, ctrl),
          metadados: ctrl.cabecalho.metadados()
        }
      },

      componentes: [
        m.component(require('pagina/componentes/tipo-de-pagina'), {
          tipo: ctrl.tipo,
          tooltipTipo: tooltips.tipo
        }),

        m.component(require('pagina/componentes/nome'), _.assign(binding, {
          titulo: 'Nome da página temática',
          componente: require('componentes/input'),
          tooltipNome: tooltips.nome,
          nomeFn: _.identity
        })),

        m.component(require('pagina/componentes/conteudo'), {
          prop: ctrl.pagina().conteudo,
          maximo: tamanhoConteudo,
          tooltipConteudo: tooltips.conteudo
        })
      ]
    });
  }
};
