'use strict';

var CabecalhoModel = require('cabecalho/cabecalho-model');

module.exports = {

    controller: function (args) {
        this.cabecalho = new CabecalhoModel();
    },

    view: function (ctrl, args) {
        return m('#conteudo',
            [m('span.cabecalho-cor'),
                m('#wrapper', [
                m.component(require('cabecalho/cabecalho'), {
                    metadados: false,
                    logout: true,
                    cabecalho: ctrl.cabecalho
                }),
                m('#servico', m('.scroll', [m('a', 'a')]))
            ])]);
    }
};