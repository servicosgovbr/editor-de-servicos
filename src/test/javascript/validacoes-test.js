'use strict';

var modelos = require('modelos');
var validacoes = require('validacoes');
var ValidacoesServico = validacoes.Servico;
var ValidacoesTempoTotalEstimado = validacoes.TempoTotalEstimado;

describe('validação >', function () {

  describe('servico >', function () {

    it('deve ter nome válido', function () {
      expect(ValidacoesServico.nome('nome de teste')).toBeUndefined();
    });

    it('nome obrigatório', function () {
      expect(ValidacoesServico.nome()).toBe('erro-campo-obrigatorio');
      expect(ValidacoesServico.nome('')).toBe('erro-campo-obrigatorio');
    });

    it('nome deve ter no máximo 150 caracteres', function () {
      expect(ValidacoesServico.nome(_.repeat('x', 151))).toBe('erro-max-150');
    });

    it('deve permitir não ter sigla', function () {
      expect(ValidacoesServico.sigla('')).toBeUndefined();
    });

    it('sigla deve ter no máximo 15 caracteres', function () {
      var _15 = _.repeat('A', 15);
      var _16 = _.repeat('X', 16);
      expect(ValidacoesServico.sigla(_15)).toBeUndefined();
      expect(ValidacoesServico.sigla(_16)).toBe('erro-max-15');
    });

    it('nomes populares não são obrigatórios', function () {
      expect(ValidacoesServico.nomesPopulares([]).length).toBe(0);
    });

    it('nomes populares devem ter no máximo 150 caracteres', function () {
      var es = ValidacoesServico.nomesPopulares([_.repeat('a', 151), 'a']);

      expect(es).toBeDefined();
      expect(es.length).toBe(2);

      expect(es[0]).toBe('erro-max-150');
      expect(es[1]).toBeUndefined();
    });

    it('deve haver no mínimo 3 palavras chave', function () {
      expect(ValidacoesServico.palavrasChave([]).msg).toBe('erro-min-3');
      expect(ValidacoesServico.palavrasChave(['p1', 'p2']).msg).toBe('erro-min-3');
      expect(ValidacoesServico.palavrasChave(['p1', 'p2', 'p3']).msg).toBeUndefined();
    });

    it('palavras chave devem ter no máximo 50 caracteres', function () {
      var _50 = _.repeat('x', 50);
      var _51 = _.repeat('a', 51);

      var errs = ValidacoesServico.palavrasChave([_50, _51, _51, _50, _51]);
      expect(errs.campos).toBeDefined();
      expect(errs.campos.length).toBe(5);

      expect(errs.campos[0]).toBeUndefined();
      expect(errs.campos[1]).toBe('erro-max-50');
      expect(errs.campos[2]).toBe('erro-max-50');
      expect(errs.campos[3]).toBeUndefined();
      expect(errs.campos[4]).toBe('erro-max-50');
    });

  });

  it('descricao válida', function () {
    var _500 = _.repeat('b', 500);
    expect(ValidacoesServico.descricao(_500)).toBeUndefined();
  });

  it('descricao é obrigatória', function () {
    expect(ValidacoesServico.descricao('')).toBe('erro-campo-obrigatorio');
    expect(ValidacoesServico.descricao()).toBe('erro-campo-obrigatorio');
  });

  it('descrição deve ter no máximo 500 caracteres', function () {
    var _501 = _.repeat('s', 501);
    expect(ValidacoesServico.descricao(_501)).toBe('erro-max-500');
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

    it('campo de comentários não pode passar de 500 caracteres', function () {
      var _500 = _.repeat('*', 500);
      expect(ValidacoesTempoTotalEstimado.descricao(_500)).toBeUndefined();

      var _501 = _.repeat('q', 501);
      expect(ValidacoesTempoTotalEstimado.descricao(_501)).toBe('erro-max-500');
    });

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

});
