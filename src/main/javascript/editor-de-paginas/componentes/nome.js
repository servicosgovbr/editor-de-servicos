'use strict';

var tooltips = require('editor-de-paginas/tooltips');

module.exports = {

    controller: function (args) {
        this.pagina = args.pagina;
    },

    view: function (ctrl, args) {
        var pagina = ctrl.pagina;

        return m('fieldset#nome', [
            m('h3', [
                'Nome da Página',
                m.component(tooltips.nome)
            ]),
            m('span', pagina.nome)
        ]);
    }
};