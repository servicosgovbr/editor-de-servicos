'use strict';

var SolicitanteModel = require('servico/solicitantes/solicitante-model');

module.exports = {

  controller: function (args) {
    this.servico = args.servico;

    this.adicionar = function () {
      var solicitantes = this.servico().solicitantes();
      solicitantes.push(new SolicitanteModel());

      this.servico().solicitantes(solicitantes);
      this.adicionado = true;
    };

    this.remover = function (i) {
      var solicitantes = this.servico().solicitantes();
      solicitantes.splice(i, 1);
      this.servico().solicitantes(solicitantes);
    };
  },

  view: function (ctrl) {
    return m('fieldset#solicitantes', [
      m('h2', 'solicitantes'),

      m('h3', [
        'Quem pode utilizar este serviço?',
        m.component(require('tooltips').solicitantes)
      ]),

      ctrl.servico().solicitantes().map(function (s, i) {
        return m.component(require('servico/solicitantes/solicitante'), {
          solicitante: s,
          index: i,
          showDelete: ctrl.servico().solicitantes().length > 1,
          remover: ctrl.remover.bind(ctrl, i)
        });
      }),

      m('button.adicionar.adicionar-solicitante', {
        onclick: ctrl.adicionar.bind(ctrl)
      }, [
        m('i.fa.fa-plus'),
        ' Adicionar solicitante '
      ])

    ]);
  }

};
