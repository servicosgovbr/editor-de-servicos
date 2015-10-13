'use strict';

var slugify = require('slugify');
var erro = require('utils/erro-ajax');
var referencia = require('referencia');

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

  this.excluirConteudo = function (id, tipo, s) {
    alertify.labels.cancel = 'Cancelar';
    alertify.labels.ok = 'Remover';
    alertify.confirm('Você tem certeza que deseja remover o(a) ' + referencia.tipoDePagina(tipo) + '?', function (result) {
      if (result) {
        s.excluindo = m.prop(true);
        m.redraw();
        m.request({
            method: 'DELETE',
            url: '/editar/api/pagina/servico/' + slugify(id),
          }).then(this.listarConteudos, erro)
          .then(function () {
            alertify.success(referencia.tipoDePagina(tipo) + ' excluído(a) com sucesso!');
            s.excluindo(false);
          });
      }
    }.bind(this));


  };

  this.listarConteudos();
};
