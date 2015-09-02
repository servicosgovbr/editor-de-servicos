'use strict';

var modelos = require('modelos');
var validacoes = require('validacoes');

function quote(str) {
  str = _.trim(str);
  return str ? '"' + str + '"' : '';
}

function shouldBePresent(campo, context) {
  it('campo ' + quote(campo) + ' deve ser obrigatório', function () {
    var prop = context();

    prop(null);
    expect(prop.erro()).toBe('erro-campo-obrigatorio');

    prop('algum valor');
    expect(prop.erro()).toBeUndefined();
  });
}

function shouldNotExceed(campo, context, limite) {

  it('campo ' + quote(campo) + ' não pode ultrapassar ' + limite + ' caracteres', function () {
    var property = context();
    property(_.repeat('a', limite + 1));
    expect(property.erro()).toBe('erro-max-' + limite);
  });

  it('campo ' + quote(campo) + ' deve aceitar valores com tamanho até ' + limite + ' caracteres', function () {
    var property = context();
    property(_.repeat('b', limite));
    expect(property.erro()).toBeUndefined();
  });

}

function eachItemShouldNotExceed(campo, context, limite) {

  it('cada ' + quote(campo) + ' não pode ultrapassar ' + limite + ' caracteres', function () {
    var property = context();
    property([_.repeat('a', limite + 1)]);
    expect(property.erro()).toEqual(['erro-max-' + limite]);
  });

  it('campo ' + quote(campo) + ' deve aceitar valores com tamanho até ' + limite + ' caracteres', function () {
    var property = context();
    property([_.repeat('b', limite)]);
    expect(property.erro()).toEqual([undefined]);
  });

}

function shouldHaveMin(campo, context, min) {
    it('deve haver no minimo ' + min + ' ' + quote(campo), function () {
      var property = context();
      property([]);
      expect(property.erro()).toBe('erro-min-' + min);

      property(_.fill(new Array(min), 'x'));
      expect(property.erro()).toBeUndefined();
    });
}

function shouldBeNumeric(campo, context) {
  it(quote(campo) + ' deve aceitar ponto', function () {
    var property = context();
    property('1.000.000');
    expect(property.erro()).toBeUndefined();
  });

  it(quote(campo) + ' deve aceitar virgula', function () {
    var property = context();
    property('999,99');
    expect(property.erro()).toBeUndefined();
  });

  it(quote(campo) + ' deve aceitar apenas números', function () {
    var property = context();

    property('1234567890');
    expect(property.erro()).toBeUndefined();

    property('a1234');
    expect(property.erro()).toBe('erro-campo-numerico');

    property('abc');
    expect(property.erro()).toBe('erro-campo-numerico');

    property('123-456');
    expect(property.erro()).toBe('erro-campo-numerico');
  });

}

describe('validação >', function () {

  describe('servico >', function () {

    var servico;

    beforeEach(function () {
      servico = new modelos.Servico();
    });

    shouldBePresent('nome', function () { return servico.nome; });
    shouldBePresent('descricao', function () { return servico.descricao; });

    shouldNotExceed('nome', function () { return servico.nome; }, 150);
    shouldNotExceed('descricao', function () { return servico.descricao; }, 500);
    shouldNotExceed('sigla', function () { return servico.sigla; }, 15);

    eachItemShouldNotExceed('nome popular', function () { return servico.nomesPopulares; }, 150);

    shouldHaveMin('solicitantes', function () { return servico.solicitantes; }, 1);
    shouldHaveMin('etapas', function () { return servico.etapas; }, 1);
    shouldHaveMin('segmentos da sociedade', function () { return servico.segmentosDaSociedade; }, 1);
    shouldHaveMin('areas de interesse', function () { return servico.areasDeInteresse; }, 1);
    shouldHaveMin('legislações', function () { return servico.legislacoes; }, 1);

    it('deve haver no mínimo 3 palavras chave', function () {
      servico.palavrasChave([]);
      expect(servico.palavrasChave.erro()).toBe('erro-min-3');

      servico.palavrasChave(['a1', 'a2', 'a3']);
      expect(servico.palavrasChave.erro()).toEqual([undefined, undefined, undefined]);
    });

    it('cada palavra chave pode ter no máximo 50 caracteres', function () {
      servico.palavrasChave([_.repeat('a', 51), 'a', 'b']);
      expect(servico.palavrasChave.erro()).toEqual(['erro-max-50', undefined, undefined]);
    });

  });

  describe('tempo total estimado', function () {
    var tte;
    beforeEach(function () {
      tte = new modelos.TempoTotalEstimado();
    });

    shouldNotExceed('descricao', function () { return tte.descricao; }, 500);

    shouldBePresent('ate', function () { return tte.ateMaximo; });
    shouldBePresent('unidade de tempo, ate', function () { return tte.ateTipoMaximo; });
    shouldBePresent('entre minimo', function () { return tte.entreMinimo; });
    shouldBePresent('entre maximo', function () { return tte.entreMaximo; });
    shouldBePresent('unidade de tempo, entre', function () { return tte.entreTipoMaximo; });
  });

  describe('solicitante', function () {
    var solicitante;

    beforeEach(function () {
      solicitante = new modelos.Solicitante();
    });

    shouldBePresent('tipo de solicitante', function () { return solicitante.tipo; });

    shouldNotExceed('tipo de solicitante', function () { return solicitante.tipo; }, 150);
    shouldNotExceed('requisitos de solicitante', function () { return solicitante.requisitos; }, 500);
  });

  describe('etapa >', function () {
    var etapa;

    beforeEach(function () {
      etapa = new modelos.Etapa();
    });

    shouldNotExceed('titulo', function () { return etapa.titulo; }, 100);
    shouldNotExceed('descrição', function () { return etapa.descricao; }, 500);

    describe('caso', function () {
      var caso;
      beforeEach(function () {
        caso = new modelos.Caso();
      });

      shouldNotExceed('descricao', function () { return caso.descricao; }, 150);
    });

    describe('documento', function () {
      var documento;
      beforeEach(function () {
        documento = new modelos.Documento();
      });

      shouldNotExceed('descricao', function () { return documento.descricao; }, 150);
    });

    describe('custos > custo', function () {
      var custo;
      beforeEach(function () {
        custo = new modelos.Custo();
      });
      shouldNotExceed('descricao', function () { return custo.descricao; }, 150);
      shouldBeNumeric('valor', function () { return custo.valor; });
    });

    describe('canais de prestação > canal de prestação', function () {
      var canalDePresetacao;
      beforeEach(function () {
        canalDePresetacao = new modelos.CanalDePrestacao();
      });

      shouldBePresent('tipo', function () { return canalDePresetacao.tipo; });
      shouldNotExceed('descricao', function () { return canalDePresetacao.descricao; }, 500);
    });
  });

  describe('wrapper validação', function () {

    var campo;

    beforeEach(function () {
      campo = validacoes.prop(null, validacoes.obrigatorio);
    });

    it('não deve validar estado inicial', function () {
      expect(campo.erro()).toBeUndefined();
    });

    it('deve conter erro ao setar valor inválido', function () {
      campo('');
      expect(campo.erro()).toBe('erro-campo-obrigatorio');
    });

    it('não deve conter erro ao setar valor válido', function () {
      campo('um valor válido');
      expect(campo.erro()).toBeUndefined();
    });

  });

});
