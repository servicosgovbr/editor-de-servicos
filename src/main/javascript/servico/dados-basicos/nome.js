'use strict';

var slugify = require('slugify');

module.exports = {

  controller: function (args) {
    this.servico = args.servico;
  },

  view: function (ctrl, args) {
    var novo = args.novo;
    var servico = ctrl.servico();

    var componenteNome = novo ? m('div.input-container', {
      class: servico.nome.erro()
    }, m('input[type=text]', {
      onchange: m.withAttr('value', servico.nome),
      value: servico.nome(),
      autofocus: 'autofocus'
    })) : m('div', [
            m('span', servico.nome()),
            m('button.remove', {
                onclick: function () {
                    alertify.prompt('Novo nome do serviço:',
                        function (e, str) {
                            if (e) {
                                var idAtual = slugify(servico.nome());
                                var novoId = slugify(str);
                                m.request({
                                    method: 'PATCH',
                                    url: '/editar/api/servico/' + idAtual + '/' + novoId
                                }).then(function() {
                                    servico.nome(str);
                                });
                            }

                        },
                        servico.nome()
                    );
                }
            })
        ]);

    return m('fieldset#nome', [
      m('h2', 'dados básicos'),
      m('h3', [
        'Nome do serviço',
        m.component(require('tooltips').nome)
      ]),
      componenteNome
    ]);

  }

};
