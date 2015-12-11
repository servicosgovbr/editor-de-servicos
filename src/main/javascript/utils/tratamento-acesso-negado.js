'use strict';

var avisos = require('utils/avisos');

module.exports = function(xhr) {
    if(xhr.status === 406 || xhr.status === 403) {
        avisos.erro('Acesso negado. Você não possui permissão para realizar esse tipo de operação.');

        return 'acesso_negado';
    }
};