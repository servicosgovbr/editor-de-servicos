'use strict';

var erro = require('utils/erro-ajax');
var referencia = require('referencia');
var api = require('api');

module.exports = function (args) {
  this.publicarConteudo = _.noop;
  var paginasProp = m.prop([]);

  this.paginasFiltradas = function (filtro) {
    var paginas = paginasProp();
    var filtroFn = _.identity;

    if (filtro.publicados) {
      filtroFn = _.flow(_.property('temAlteracoesNaoPublicadas'));
    }

    var filtroTipos = [];
    if (filtro.filtroServicos) {
      filtroTipos.push('servico');
    }
    if (filtro.filtroOrgaos) {
      filtroTipos.push('orgao');
    }
    if (filtro.filtroPaginasTematicas) {
      filtroTipos.push('pagina-tematica');
    }

    if (filtroTipos.length > 0) {
      filtroFn = _.flow(function (pg) {
        return _.contains(filtroTipos, _.get(pg, 'conteudo.tipo'));
      });
    }

    paginas = _.filter(paginas, filtroFn);

    if (_.isEmpty(_.trim(filtro.busca))) {
      return _.sortBy(paginas, 'id');
    }

    paginas = new Fuse(paginas, {
      keys: ['conteudo.nome'],
      threshold: 0.2,
      distance: 5000,
      maxPatternLength: 150
    }).search(filtro.busca);

    return paginas;
  };

  this.listarConteudos = _.debounce(function () {
    m.request({
        method: 'GET',
        url: '/editar/api/conteudos'
      })
      .then(paginasProp, erro);
  }.bind(this), 500);

  this.excluirConteudo = function (id, tipo, pagina) {
    alertify.labels.cancel = 'Cancelar';
    alertify.labels.ok = 'Remover';
    alertify.confirm('Você tem certeza que deseja remover o(a) ' + referencia.tipoDePagina(tipo) + '?', function (result) {
      if (result) {
        pagina.excluindo = m.prop(true);
        m.redraw();
        api.excluir(tipo, id)
          .then(this.listarConteudos)
          .then(function () {
            alertify.success(referencia.tipoDePagina(tipo) + ' excluído(a) com sucesso!', 0);
            pagina.excluindo(false);
          });
      }
    }.bind(this));
  };

  this.listarConteudos();
};
