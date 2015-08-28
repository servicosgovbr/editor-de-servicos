'use strict';

var modelos = require('modelos');
var validacoes = require('validacoes');

function itIsMandatory(campo, fn) {
  it('campo "' + campo + '" obrigatório', function () {
    expect(fn('12345')).toBeUndefined();
    expect(fn()).toBe('erro-campo-obrigatorio');
    expect(fn('')).toBe('erro-campo-obrigatorio');
  });
}

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

    itIsMandatory('nome', validacoes.Servico.nome);
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
  itIsMandatory('descricao', validacoes.Servico.descricao);

  describe('tempo total estimado', function () {
    var tte;
    beforeEach(function () {
      tte = new modelos.TempoTotalEstimado();
    });

    itIsMandatory('ate', validacoes.TempoTotalEstimado.ateMaximo);
    itIsMandatory('unidade de tempo, ate', validacoes.TempoTotalEstimado.ateTipoMaximo);
    itShouldMax('comentários ou informações adicionais', validacoes.TempoTotalEstimado.descricao, 500);
    itIsMandatory('entre minimo', validacoes.TempoTotalEstimado.entreMinimo);
    itIsMandatory('entre maximo', validacoes.TempoTotalEstimado.entreMaximo);
    itIsMandatory('unidade de tempo, entre', validacoes.TempoTotalEstimado.entreTipoMaximo);
  });

  describe('etapa', function () {
    itShouldMax('descrição', validacoes.Etapa.descricao, 500);
  });

  describe('solicitante', function () {
    itIsMandatory('tipo de solicitante', validacoes.Solicitante.tipoSolicitante);
    itShouldMax('descrição', validacoes.Solicitante.descricao, 500);
  });

});
