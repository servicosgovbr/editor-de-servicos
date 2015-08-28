'use strict';

var modelos = require('modelos');
var validacoes = require('validacoes');
var ValidacoesServico = validacoes.Servico;
var ValidacoesTempoTotalEstimado = validacoes.TempoTotalEstimado;
var ValidacoesTempoTotalEstimado = validacoes.TempoTotalEstimado;
var ValidacoesEtapa = validacoes.Etapa;

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
      expect(ValidacoesServico.nome('nome de teste')).toBeUndefined();
    });

    it('nome obrigatório', function () {
      expect(ValidacoesServico.nome()).toBe('erro-campo-obrigatorio');
      expect(ValidacoesServico.nome('')).toBe('erro-campo-obrigatorio');
    });

    itShouldMax('nome', ValidacoesServico.nome, 150);

    it('deve permitir não ter sigla', function () {
      expect(ValidacoesServico.sigla('')).toBeUndefined();
    });

    itShouldMax('sigla', ValidacoesServico.sigla, 15);

    it('nomes populares não são obrigatórios', function () {
      expect(ValidacoesServico.nomesPopulares([]).length).toBe(0);
    });

    itemsShouldMax('nomes populares', ValidacoesServico.nomesPopulares, 150);

    it('deve haver no mínimo 3 palavras chave', function () {
      expect(ValidacoesServico.palavrasChave([]).msg).toBe('erro-min-3');
      expect(ValidacoesServico.palavrasChave(['p1', 'p2']).msg).toBe('erro-min-3');
      expect(ValidacoesServico.palavrasChave(['p1', 'p2', 'p3']).msg).toBeUndefined();
    });

    itemsShouldMax('palavras chave', ValidacoesServico.palavrasChave, 50, _.property('campos'));
  });

  itShouldMax('descricao', ValidacoesServico.descricao, 500);

  it('descricao é obrigatória', function () {
    expect(ValidacoesServico.descricao('')).toBe('erro-campo-obrigatorio');
    expect(ValidacoesServico.descricao()).toBe('erro-campo-obrigatorio');
  });

  describe('tempo total estimado', function () {
    var tte;
    beforeEach(function () {
      tte = new modelos.TempoTotalEstimado();
    });

    it('deve preencher tempo máximo para "ate"', function () {
      expect(ValidacoesTempoTotalEstimado.ateMaximo(tte.ateMaximo())).toBe('erro-campo-obrigatorio');
    });

    it('deve preencher a unidade de tempo para "ate"', function () {
      expect(ValidacoesTempoTotalEstimado.ateTipoMaximo(tte.ateTipoMaximo())).toBe('erro-campo-obrigatorio');
    });

    itShouldMax('comentários ou informações adicionais', ValidacoesTempoTotalEstimado.descricao, 500);

    it('entre minimo obrigatório', function () {
      expect(ValidacoesTempoTotalEstimado.entreMinimo('1')).toBeUndefined();
      expect(ValidacoesTempoTotalEstimado.entreMinimo()).toBe('erro-campo-obrigatorio');
      expect(ValidacoesTempoTotalEstimado.entreMinimo('')).toBe('erro-campo-obrigatorio');
    });

    it('entre maximo obrigatório', function () {
      expect(ValidacoesTempoTotalEstimado.entreMaximo('2')).toBeUndefined();
      expect(ValidacoesTempoTotalEstimado.entreMaximo()).toBe('erro-campo-obrigatorio');
      expect(ValidacoesTempoTotalEstimado.entreMaximo('')).toBe('erro-campo-obrigatorio');
    });

    it('deve preencher a unidade de tempo para "entre"', function () {
      expect(ValidacoesTempoTotalEstimado.entreTipoMaximo(tte.ateTipoMaximo())).toBe('erro-campo-obrigatorio');
    });

  });

  describe('etapas', function () {
    itShouldMax('descrição', ValidacoesEtapa.descricao, 500);
  });

});
