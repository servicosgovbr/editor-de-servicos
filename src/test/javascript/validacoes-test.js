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
      expect(ValidacoesServico.nome()).toBe('campo-obrigatorio');
      expect(ValidacoesServico.nome('')).toBe('campo-obrigatorio');
    });

    it('nome deve ter no máximo 150 caracteres', function () {
      expect(ValidacoesServico.nome(_.repeat('x', 151))).toBe('max-150');
    });

    it('deve permitir não ter sigla', function () {
      expect(ValidacoesServico.sigla('')).toBeUndefined();
    });

    it('sigla deve ter no máximo 15 caracteres', function () {
      var _15 = _.repeat('A', 15);
      var _16 = _.repeat('X', 16);
      expect(ValidacoesServico.sigla(_15)).toBeUndefined();
      expect(ValidacoesServico.sigla(_16)).toBe('max-15');
    });

    it('nomes populares não são obrigatórios', function () {
      expect(ValidacoesServico.nomesPopulares([]).length).toBe(0);
    });

    it('nomes populares devem ter no máximo 150 caracteres', function () {
      var es = ValidacoesServico.nomesPopulares([_.repeat('a', 151), 'a']);

      expect(es).toBeDefined();
      expect(es.length).toBe(1);

      expect(es[0].i).toBe(0);
      expect(es[0].msg).toBe('max-150');
    });

    it('validação nomes populares devem vir indexados', function () {
      var es = ValidacoesServico.nomesPopulares([_.repeat('a', 151), 'a', _.repeat('b', 151), 'b', _.repeat('x', 151)]);

      expect(es).toBeDefined();
      expect(es.length).toBe(3);
      expect(es[0].i).toBe(0);
      expect(es[1].i).toBe(2);
      expect(es[2].i).toBe(4);
    });

    it('deve haver no mínimo 3 palavras chave', function () {
      expect(ValidacoesServico.palavrasChave([]).msg).toBe('min-3');
      expect(ValidacoesServico.palavrasChave(['p1', 'p2']).msg).toBe('min-3');
      expect(ValidacoesServico.palavrasChave(['p1', 'p2', 'p3']).msg).toBeUndefined();
    });

    it('palavras chave devem ter no máximo 50 caracteres', function () {
      var _50 = _.repeat('x', 50);
      var _51 = _.repeat('a', 51);

      var errs = ValidacoesServico.palavrasChave([_50, _51, _51, _50, _51]);
      expect(errs.campos).toBeDefined();
      expect(errs.campos.length).toBe(3);

      var e0 = errs.campos[0];

      expect(e0.i).toBe(1);
      expect(e0.msg).toBe('max-50');

      var e1 = errs.campos[1];
      expect(e1.i).toBe(2);
      expect(e1.msg).toBe('max-50');

      var e2 = errs.campos[2];
      expect(e2.i).toBe(4);
      expect(e2.msg).toBe('max-50');
    });

  });

  it('descricao válida', function () {
    var _500 = _.repeat('b', 500);
    expect(ValidacoesServico.descricao(_500)).toBeUndefined();
  });

  it('descricao é obrigatória', function () {
    expect(ValidacoesServico.descricao('')).toBe('campo-obrigatorio');
    expect(ValidacoesServico.descricao()).toBe('campo-obrigatorio');
  });

  it('descrição deve ter no máximo 500 caracteres', function () {
    var _501 = _.repeat('s', 501);
    expect(ValidacoesServico.descricao(_501)).toBe('max-500');
  });

  describe('tempo total estimado', function() {
    var tte;
    beforeEach(function() {
      tte = new modelos.TempoTotalEstimado();
    });

    it('deve preencher tempo máximo para "ate"', function() {
      expect(ValidacoesTempoTotalEstimado.ateMaximo(tte.ateMaximo())).toBe('campo-obrigatorio');
    });

    it('deve preencher a unidade de tempo para "ate"', function() {
      expect(ValidacoesTempoTotalEstimado.ateTipoMaximo(tte.ateTipoMaximo())).toBe('campo-obrigatorio');
    }); 

    it('campo de comentários não pode passar de 500 caracteres', function() {
      var _500 = _.repeat('*', 500);
      expect(ValidacoesTempoTotalEstimado.descricao(_500)).toBeUndefined();

      var _501 = _.repeat('q', 501);
      expect(ValidacoesTempoTotalEstimado.descricao(_501)).toBe('max-500');
    });

    it('entre minimo obrigatório', function() {
      expect(ValidacoesTempoTotalEstimado.entreMinimo('1')).toBeUndefined();
      expect(ValidacoesTempoTotalEstimado.entreMinimo()).toBe('campo-obrigatorio');
      expect(ValidacoesTempoTotalEstimado.entreMinimo('')).toBe('campo-obrigatorio');
    });

    it('entre maximo obrigatório', function(){
      expect(ValidacoesTempoTotalEstimado.entreMaximo('2')).toBeUndefined();
      expect(ValidacoesTempoTotalEstimado.entreMaximo()).toBe('campo-obrigatorio');
      expect(ValidacoesTempoTotalEstimado.entreMaximo('')).toBe('campo-obrigatorio');
    });

    it('deve preencher a unidade de tempo para "entre"', function() {
      expect(ValidacoesTempoTotalEstimado.entreTipoMaximo(tte.ateTipoMaximo())).toBe('campo-obrigatorio');
    });

  });

});
