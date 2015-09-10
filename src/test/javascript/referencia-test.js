'use strict';

var referencia = require('referencia');

describe('referencia >', function () {

  describe('canais de prestação >', function () {
    it('deve ter tipos', function () {
      expect(referencia.tiposDeCanalDePrestacao).not.toBeUndefined();
    });

    it('deve ter descrições', function () {
      expect(referencia.descricoesDeCanaisDePrestacao).not.toBeUndefined();
    });

    it('deve ter mesma quantia de descrições e tipos', function () {
      expect(_.keys(referencia.descricoesDeCanaisDePrestacao).length).toBe(referencia.tiposDeCanalDePrestacao.length);
    });

    it('deve ter uma descrição para o id de cada tipo', function () {
      referencia.tiposDeCanalDePrestacao.map(function (tipo) {
        expect(referencia.descricoesDeCanaisDePrestacao[tipo.id]).toBeDefined();
      });
    });
  });

  describe('tipos de páginas >', function () {
    it('devem ter sido definidos', function () {
      expect(referencia.tiposDePagina).not.toBeUndefined();
    });
  });
});
