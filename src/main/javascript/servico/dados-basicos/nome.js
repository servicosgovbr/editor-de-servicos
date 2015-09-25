'use strict';

var slugify = require('slugify');
var idUnico = require('utils/id-unico');

module.exports = {

  controller: function (args) {
    this.servico = args.servico;

    this.validar = function(nome) {
        var idAtual = slugify(this.servico().nome());
        if (!nome) {
            return 'erro-campo-obrigatorio';
        }
        if (idAtual !== slugify(nome) && !idUnico(nome)) {
            return 'erro-nome-servico-existente';
        }
    };
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
            m('button.renomear', {
        style: {
          float: 'right'
        },
        onclick: function () {
          alertify.labels.cancel = 'Cancelar';
          alertify.labels.ok = 'Renomear';
          alertify.prompt('Novo nome do serviço:',
            function (e, novoNome) {
              if (e && servico.nome() !== novoNome) {
                if (!ctrl.validar(novoNome)) {
                    var idAtual = slugify(servico.nome());
                    m.request({
                        method: 'PATCH',
                        url: '/editar/api/servico/' + idAtual + '/' + novoNome
                    }).then(function () {
                        servico.nome(novoNome);
                        m.route('/editar/servico/' + slugify(novoNome));
                    });
                }

              }

            },
            servico.nome()
          );
        }
      }, [m('i.fa.fa-pencil'),
                'Alterar nome'])
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
