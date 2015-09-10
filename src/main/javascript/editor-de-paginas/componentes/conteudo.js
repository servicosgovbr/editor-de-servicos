'use strict';

var tooltips = require('editor-de-paginas/tooltips');

module.exports = {

    controller: function (args) {
        this.pagina = args.pagina;
    },

    view: function (ctrl, args) {
        var pagina = ctrl.pagina();

        return m('fieldset#conteudo-pagina', [
            m('h3', [
                'Conteúdo da Página',
                m.component(tooltips.conteudo)
            ]),
            m('.input-container', [
                m.component(require('componentes/editor-markdown'), {
                    rows: 10,
                    onchange: m.withAttr('value', pagina.conteudo),
                    value: ctrl.pagina().conteudo(),
                    erro: ctrl.pagina().conteudo.erro()
                })
            ])
        ]);
    }

};