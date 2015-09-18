'use strict';

var slugify = require('slugify');
var erro = require('utils/erro-ajax');

module.exports = function (args) {

  this.publicarConteudo = _.noop;

  this.excluirConteudo = function (id) {
    m.request({
      method: 'DELETE',
      url: '/editar/api/servico/' + slugify(id),
    }).then(this.listarConteudos, erro);
  };

};
