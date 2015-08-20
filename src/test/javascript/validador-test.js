'use strict';

var modelos = require('modelos');
var validador = require('validador');

describe('validação >', function () {
  describe('servico >', function () {
    var validadorServico;

    beforeEach(function () {
      validadorServico = new m.validator(validador);
    });

    it('nome é obrigatório', function () {
      validadorServico.validate(new modelos.Servico());

      expect(validadorServico.hasErrors()).toBe(1);
      expect(validadorServico.hasError('nome')).toBe('Nome do serviço é obrigatório');
    });

  });

});
