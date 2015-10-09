'use strict';

module.exports = {

  controller: function (args) {},

  view: function () {
    return m('form.feedback-servico', [
        m('h3.opiniao', 'Você achou as informações acima úteis?'),
        m('p.formulario-opiniao', [])
    ]);
  }
};
