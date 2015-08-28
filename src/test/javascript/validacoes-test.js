'use strict';

var modelos = require('modelos');
var validacoes = require('validacoes');

function itShouldMax(campo, fn, limite) {
  var noLimite = _.repeat('x', limite);
  var alemDoLimite = _.repeat('c', limite + 1);

  it('campo "' + campo + '" não pode ultrapassar ' + limite + ' caracteres', function () {
    expect(fn(noLimite)).toBeUndefined();
    expect(fn(alemDoLimite)).toBe('erro-max-' + limite);
  });
}

function itemsShouldMax(campo, fn, limite, fnCampos) {
  fnCampos = fnCampos || _.identity;

  var param = [_.repeat('x', limite), _.repeat('c', limite + 1)];

  it('campo "' + campo + '" não pode ultrapassar ' + limite + ' caracteres', function () {
    var erros = fnCampos(fn(param));

    expect(erros[0]).toBeUndefined();
    expect(erros[1]).toBe('erro-max-' + limite);
  });
}

describe('validação >', function () {

  describe('servico >', function () {

    it('deve ter nome válido', function () {
      expect(validacoes.Servico.nome('nome de teste')).toBeUndefined();
    });

    it('nome obrigatório', function () {
      expect(validacoes.Servico.nome()).toBe('erro-campo-obrigatorio');
      expect(validacoes.Servico.nome('')).toBe('erro-campo-obrigatorio');
    });

    itShouldMax('nome', validacoes.Servico.nome, 150);

    it('deve permitir não ter sigla', function () {
      expect(validacoes.Servico.sigla('')).toBeUndefined();
    });

    itShouldMax('sigla', validacoes.Servico.sigla, 15);

    it('nomes populares não são obrigatórios', function () {
      expect(validacoes.Servico.nomesPopulares([]).length).toBe(0);
    });

    itemsShouldMax('nomes populares', validacoes.Servico.nomesPopulares, 150);

    it('deve haver no mínimo 3 palavras chave', function () {
      expect(validacoes.Servico.palavrasChave([]).msg).toBe('erro-min-3');
      expect(validacoes.Servico.palavrasChave(['p1', 'p2']).msg).toBe('erro-min-3');
      expect(validacoes.Servico.palavrasChave(['p1', 'p2', 'p3']).msg).toBeUndefined();
    });

    itemsShouldMax('palavras chave', validacoes.Servico.palavrasChave, 50, _.property('campos'));
  });

  itShouldMax('descricao', validacoes.Servico.descricao, 500);

  it('descricao é obrigatória', function () {
    expect(validacoes.Servico.descricao('')).toBe('erro-campo-obrigatorio');
    expect(validacoes.Servico.descricao()).toBe('erro-campo-obrigatorio');
  });

  describe('tempo total estimado', function () {
    var tte;
    beforeEach(function () {
      tte = new modelos.TempoTotalEstimado();
    });

    it('deve preencher tempo máximo para "ate"', function () {
      expect(validacoes.TempoTotalEstimado.ateMaximo(tte.ateMaximo())).toBe('erro-campo-obrigatorio');
    });

    it('deve preencher a unidade de tempo para "ate"', function () {
      expect(validacoes.TempoTotalEstimado.ateTipoMaximo(tte.ateTipoMaximo())).toBe('erro-campo-obrigatorio');
    });

    itShouldMax('comentários ou informações adicionais', validacoes.TempoTotalEstimado.descricao, 500);

    it('entre minimo obrigatório', function () {
      expect(validacoes.TempoTotalEstimado.entreMinimo('1')).toBeUndefined();
      expect(validacoes.TempoTotalEstimado.entreMinimo()).toBe('erro-campo-obrigatorio');
      expect(validacoes.TempoTotalEstimado.entreMinimo('')).toBe('erro-campo-obrigatorio');
    });

    it('entre maximo obrigatório', function () {
      expect(validacoes.TempoTotalEstimado.entreMaximo('2')).toBeUndefined();
      expect(validacoes.TempoTotalEstimado.entreMaximo()).toBe('erro-campo-obrigatorio');
      expect(validacoes.TempoTotalEstimado.entreMaximo('')).toBe('erro-campo-obrigatorio');
    });

    it('deve preencher a unidade de tempo para "entre"', function () {
      expect(validacoes.TempoTotalEstimado.entreTipoMaximo(tte.ateTipoMaximo())).toBe('erro-campo-obrigatorio');
    });

  });

  describe('etapas', function () {
    itShouldMax('descrição', validacoes.Etapa.descricao, 500);
  });

});
