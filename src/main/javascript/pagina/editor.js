'use strict';

var safeGet = require('utils/code-checks').safeGet;
var CabecalhoModel = require('cabecalho/cabecalho-model');
var EditorBase = require('componentes/editor-base');
var carregarPagina = require('pagina/carregar');
var salvarPagina = require('pagina/salvar');
var slugify = require('slugify');

function redirecionarNovaPagina(tipo, nome) {
  var oldId = m.route.param('id');
  var newId = slugify(nome);
  if (!_.isEqual(oldId, newId)) {
    m.route('/editar/' + tipo + '/' + newId);
  }
}

module.exports = {
  controller: function (args) {
    var tipo = safeGet(args, 'tipo');
    var tamanhoConteudo = safeGet(args, 'tamanhoConteudo');

    this.modificado = m.prop(false);
    this.cabecalho = new CabecalhoModel();

    this.pagina = carregarPagina({
      tipo: tipo,
      id: m.route.param('id')
    }, this.cabecalho).then(function (pg) {
      pg.tamanhoConteudo(tamanhoConteudo);
      return pg;
    });

    this.salvar = _.bind(function () {
      return salvarPagina(tipo, this.pagina())
        .then(this.pagina)
        .then(_.bind(function (pagina) {
          redirecionarNovaPagina(tipo, pagina.nome());
        }));
    }, this);
  },

  view: function (ctrl, args) {
    if (!ctrl.pagina()) {
      return m('');
    }

    var tipo = safeGet(args, 'tipo');
    var tituloNome = safeGet(args, 'tituloNome');
    var componenteNome = safeGet(args, 'componenteNome');
    var tamanhoConteudo = safeGet(args, 'tamanhoConteudo');

    var tooltips = {
      tipo: safeGet(args, 'tooltips.tipo'),
      nome: safeGet(args, 'tooltips.nome'),
      conteudo: safeGet(args, 'tooltips.conteudo')
    };

    ctrl.pagina().tipo(tipo);

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
        salvar: _.bind(ctrl.salvar, ctrl),
        cabecalho: ctrl.cabecalho
      },

      componentes: [
        m.component(require('pagina/componentes/tipo-de-pagina'), {
          tipo: ctrl.pagina().tipo,
          tooltipTipo: tooltips.tipo
        }),
        m.component(require('pagina/componentes/nome'), _.assign(binding, {
          titulo: tituloNome,
          componente: componenteNome,
          tooltipNome: tooltips.nome
        })),
        m.component(require('pagina/componentes/conteudo'), _.assign(binding, {
          maximo: tamanhoConteudo,
          tooltipConteudo: tooltips.conteudo
        }))
      ]
    });
  }
};
