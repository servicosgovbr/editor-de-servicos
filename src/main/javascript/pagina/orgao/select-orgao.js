'use strict';

var safeGet = require('utils/code-checks').safeGet;
var erro = require('utils/erro-ajax');
var api = require('api');

module.exports = {

  view: function (ctrl, args) {
    var orgao = safeGet(args, 'prop');

    return m('div#select-orgao', m.component(require('componentes/select2'), {
      ajax: {
        url: '/editar/api/orgaos',
        dataType: 'json',
        delay: 250,
        data: function (params) {
          return {
            q: params.term
          };
        },
        processResults: function (data, page) {
          var result = _.map(data, function (o) {
            return {
              id: o.id,
              text: o.nome
            };
          });

          return {
            results: result
          };
        },
        cache: true
      },
      prop: orgao,
      width: '100%',
      minimumResultsForSearch: 1,
      minimumInputLength: 3,
      initSelection: function (element, callback) {
        api.obterNomeOrgao(orgao())
          .then(function (orgaoNome) {
            callback({
              id: orgao(),
              text: orgaoNome
            });
          }, erro);
      }
    }));
  }
};
