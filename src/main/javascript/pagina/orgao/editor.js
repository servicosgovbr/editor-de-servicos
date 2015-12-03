'use strict';

var Tooltips = require('tooltips');
var CabecalhoModel = require('cabecalho/cabecalho-model');
var EditorBase = require('componentes/editor-base');

var carregarPagina = require('xml/carregar').carregarOrgao;
var salvarOrgao = require('xml/salvar').salvarOrgao;
var publicarOrgao = require('xml/publicar').publicarOrgao;
var descartarOrgao = require('xml/descartar').descartarOrgao;
var despublicar = require('api').despublicar;
var redirecionarNovaPagina = require('redirecionador');

function ehNovo() {
  return m.route.param('id') === 'novo';
}

module.exports = {
  controller: function (args) {
    this.tipo = m.prop('orgao');

    this.modificado = m.prop(false);
    this.cabecalho = new CabecalhoModel();
    this.pagina = carregarPagina(m.route.param('id'), this.cabecalho);
    this.salvandoServico = m.prop(false);

    this._onOp = _.bind(function (pagina) {
      this.pagina(pagina);
      this.modificado(false);
      redirecionarNovaPagina(this.tipo(), pagina.url());
      return pagina;
    }, this);

    this.salvar = _.bind(function () {
      return salvarOrgao(this.pagina(), this.cabecalho.metadados)
        .then(this.pagina)
        .then(this._onOp);
    }, this);

    this.publicar = function () {
      return this.salvar()
        .then(_.bind(function (s) {
          return publicarOrgao(s, this.cabecalho.metadados);
        }, this))
        .then(this._onOp);
    };

    this.descartar = function () {
      return descartarOrgao(this.pagina(), this.cabecalho.metadados)
        .then(this._onOp);
    };

    this.despublicar = function () {
      return despublicar('orgao', this.pagina().url(), this.cabecalho.metadados);
    };
  },

  view: function (ctrl, args) {
    if (!ctrl.pagina()) {
      return m('');
    }

    var tamanhoConteudo = 10000;
    var tooltips = {
      tipo: Tooltips.tipoPagina,
      nome: Tooltips.escolhaOrgao,
      conteudo: Tooltips.conteudoOrgao,
      contato: Tooltips.contatoOrgao
    };

    var binding = {
      pagina: ctrl.pagina,
      nome: ehNovo() ? ctrl.pagina().url : ctrl.pagina().nome,
      novo: ehNovo()
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
        nomeDaPagina: ctrl.pagina().nome() || 'Novo orgão',
        salvar: _.bind(ctrl.salvar, ctrl),
        publicar: _.bind(ctrl.publicar, ctrl),
        descartar: _.bind(ctrl.descartar, ctrl),
        cabecalho: ctrl.cabecalho,
        salvandoServico: ctrl.salvandoServico
      },

      menuLateralConfig: {
        despublicarConfig: {
          tipo: 'orgao',
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
          titulo: 'Selecione o órgão',
          componente: require('pagina/orgao/select-orgao'),
          tooltipNome: tooltips.nome,
        })),

        m.component(require('pagina/componentes/conteudo'), {
          prop: ctrl.pagina().conteudo,
          maximo: tamanhoConteudo,
          tooltipConteudo: tooltips.conteudo
        }),

        m.component(require('pagina/componentes/conteudo'), {
          label: 'Contato do órgão',
          prop: ctrl.pagina().contato,
          maximo: tamanhoConteudo,
          tooltipConteudo: tooltips.contato
        }),
      ]
    });
  }
};
