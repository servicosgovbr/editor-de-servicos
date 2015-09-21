'use strict';

var slugify = require('slugify');
var erro = require('utils/erro-ajax');

module.exports = function (args) {

  this.publicarConteudo = _.noop;

  var paginasProp = m.prop([]);

  this.paginasFiltradas = function (filtro) {
    var paginas = paginasProp();

    var filtroFn = _.identity;

    if (filtro.publicados) {
      filtroFn = _.flow(_.property('temAlteracoesNaoPublicadas'));
    }
    if (filtro.orgao) {
    filtroFn = _.flow(function (pg) {
      return filtro.orgao === _.property('conteudo.orgao.id')(pg);
    });
    }

    paginas = _.filter(paginas, filtroFn);

    if (_.isEmpty(_.trim(filtro.busca))) {
      return _.sortBy(paginas, 'id');
    }

    paginas = new Fuse(paginas, {
      keys: ['conteudo.nome']
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

  this.excluirConteudo = function (id) {
    m.request({
      method: 'DELETE',
      url: '/editar/api/servico/' + slugify(id),
    }).then(this.listarConteudos, erro);
  };

  this.listarConteudos();
};
