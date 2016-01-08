'use strict';

var avisos = require('utils/avisos');

module.exports = function(xhr) {
    if(xhr.status === 409) {
        avisos.erro('Não foi possível completar esta solicitação. Por favor, recarregue esta página pois existem alterações nesta página.');

        return 'conflito_edicao';
    }
};