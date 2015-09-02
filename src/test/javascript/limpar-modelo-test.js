'use strict';

var modelos = require('modelos');
var limparModelo = require('limpar-modelo');
var Solicitante = modelos.Solicitante;
var Etapa = modelos.Etapa;
var Documentos = modelos.Documentos;
var Custos = modelos.Custos;
var Custo = modelos.Custo;
var CanaisDePrestacao = modelos.CanaisDePrestacao;
var CanalDePrestacao = modelos.CanalDePrestacao;
var Caso = modelos.Caso;
var TempoTotalEstimado = modelos.TempoTotalEstimado;

describe('deve limpar >', function () {

  var servico;

  beforeEach(function () {
    m.route.param = function () {};
    servico = new modelos.Servico({
      nome: '        ',
      sigla: '   ABC  ',
      descricao: '  blah           ',
      nomesPopulares: [null, '  nome  ', undefined, '']
    });
  });

  it('nome do servico', function () {
    expect(limparModelo(servico).nome()).toBe('');
  });

  it('sigla do servico', function () {
    expect(limparModelo(servico).sigla()).toBe('ABC');
  });

  it('descricao do servico', function () {
    expect(limparModelo(servico).descricao()).toBe('blah');
  });

  it('nomes populares do servico', function () {
    expect(limparModelo(servico).nomesPopulares()).toEqual(['nome']);
  });

  describe('solicitantes >', function () {
    beforeEach(function () {
      servico = new modelos.Servico({
        solicitantes: [
         new Solicitante(),
         new Solicitante({
            tipo: '  ',
            requisitos: '      '
          }),
         new Solicitante({
            tipo: ' a ',
            requisitos: '       '
          })
       ]
      });
    });

    it('filtrar vazios', function () {
      expect(limparModelo(servico).solicitantes().length).toBe(1);
    });

    it('campos tipo e requisios', function () {
      servico = limparModelo(servico);

      expect(servico.solicitantes()[0].tipo()).toBe('a');
      expect(servico.solicitantes()[0].requisitos()).toBe('');
    });
  });

  describe('etapas >', function () {
    beforeEach(function () {
      servico = new modelos.Servico({
        etapas: [
          new Etapa(),
          new Etapa({
            documentos: new Documentos(),
            custos: new Custos(),
            canaisDePrestacao: new CanaisDePrestacao()
          }),
          new Etapa({
            titulo: ' titulo '
          })
        ]
      });
    });

    it('filtrar vazias', function () {
      expect(limparModelo(servico).etapas().length).toBe(1);
    });

    it('titulo e descricao', function () {
      var etapa = limparModelo(servico).etapas()[0];

      expect(etapa.titulo()).toBe('titulo');
      expect(etapa.descricao()).toBe('');
    });

    describe('documentos >', function () {
      beforeEach(function () {
        servico = new modelos.Servico({
          etapas: [
            new Etapa({
              documentos: new Documentos({
                casoPadrao: new Caso('', {
                  padrao: true,
                  campos: [new modelos.Documento(), null, undefined, new modelos.Documento({ descricao: ' doc '})]
                }),
                outrosCasos: [
                  new Caso(), new Caso('', {
                    descricao: '   '
                  }),
                  new Caso('', {
                    descricao: '   a   '
                  }),
                  new Caso('', {
                    campos: [new modelos.Documento(), null, undefined, new modelos.Documento({ descricao: ' le doc     '})]
                  })]
              })
            })]
        });
      });

      it('caso padrão', function () {
        var etapa = limparModelo(servico).etapas()[0];
        expect(etapa.documentos()).toBeDefined();
        expect(etapa.documentos().casoPadrao()).toBeDefined();
        expect(etapa.documentos().casoPadrao().padrao).toBe(true);
        expect(etapa.documentos().casoPadrao().campos().length).toEqual(1);
        expect(etapa.documentos().casoPadrao().campos()[0].descricao()).toEqual('doc');
      });

      it('outros casos', function () {
        var etapa = limparModelo(servico).etapas()[0];
        expect(etapa.documentos().outrosCasos().length).toBe(2);

        var caso1 = etapa.documentos().outrosCasos()[0];
        var caso2 = etapa.documentos().outrosCasos()[1];

        expect(caso1.descricao()).toBe('a');
        expect(caso2.campos().length).toEqual(1);
        expect(caso2.campos()[0].descricao()).toEqual('le doc');
      });
    });

    describe('custos >', function () {
      beforeEach(function () {
        servico = new modelos.Servico({
          etapas: [
            new Etapa({
              custos: new Custos({
                casoPadrao: new Caso('', {
                  padrao: true,
                  campos: [new Custo({
                    moeda: ' us$ '
                  }), new Custo({
                    descricao: ' blah ',
                    moeda: ' r$',
                    valor: '   10   '
                  })]
                }),
                outrosCasos: [new Caso(), new Caso('', {
                  descricao: 'a'
                }), new Caso('', {
                  campos: [new Custo(), new Custo({
                    descricao: ' blah ',
                    moeda: ' R$',
                    valor: '   10   '
                  }), new Custo({
                    moeda: 'R$'
                  })]
                }), new Caso()]
              })
            })]
        });
      });

      it('caso padrão', function () {
        var etapa = limparModelo(servico).etapas()[0];

        expect(etapa.custos()).toBeDefined();
        expect(etapa.custos().casoPadrao()).toBeDefined();
        expect(etapa.custos().casoPadrao().padrao).toBe(true);
        expect(etapa.custos().casoPadrao().campos().length).toBe(2);

        var custo1 = etapa.custos().casoPadrao().campos()[0];
        expect(custo1.moeda()).toBe('us$');

        var custo2 = etapa.custos().casoPadrao().campos()[1];
        expect(custo2.descricao()).toBe('blah');
        expect(custo2.moeda()).toBe('r$');
        expect(custo2.valor()).toBe('10');
      });

      it('outros casos', function () {
        var etapa = limparModelo(servico).etapas()[0];

        expect(etapa.custos().outrosCasos()).toBeDefined();
        expect(etapa.custos().outrosCasos().length).toBe(2);

        var caso1 = etapa.custos().outrosCasos()[0];
        var caso2 = etapa.custos().outrosCasos()[1];

        expect(caso1.descricao()).toBe('a');

        expect(caso2.campos().length).toBe(1);
        expect(caso2.campos()[0].descricao()).toBe('blah');
        expect(caso2.campos()[0].moeda()).toBe('R$');
        expect(caso2.campos()[0].valor()).toBe('10');
      });

    });

    describe('canais de prestação >', function () {
      beforeEach(function () {
        servico = new modelos.Servico({
          etapas: [new Etapa({
            canaisDePrestacao: new CanaisDePrestacao({
              casoPadrao: new Caso('', {
                padrao: true,
                descricao: ' a ',
                campos: [new CanalDePrestacao(), new CanalDePrestacao({
                  tipo: '   canal caso padrao  ',
                  descricao: ' desc  '
                }), new CanalDePrestacao({
                  tipo: '   ',
                  descricao: '     '
                })]
              }),
              outrosCasos: [new Caso(), new Caso(), new Caso('', {
                  descricao: ' outros casos '
                }), new Caso('', {
                  campos: [new CanalDePrestacao(), new CanalDePrestacao({
                    tipo: 'tipo outros casos  ',
                    descricao: '  descricao canal outros casos'
                  }), new CanalDePrestacao()]
                })
              ]
            })
          })]
        });
      });

      it('caso padrão', function () {
        var etapa = limparModelo(servico).etapas()[0];

        expect(etapa.canaisDePrestacao()).toBeDefined();
        expect(etapa.canaisDePrestacao().casoPadrao()).toBeDefined();
        expect(etapa.canaisDePrestacao().casoPadrao().descricao()).toBe('a');
        expect(etapa.canaisDePrestacao().casoPadrao().campos().length).toBe(1);

        var canal = etapa.canaisDePrestacao().casoPadrao().campos()[0];
        expect(canal.tipo()).toBe('canal caso padrao');
        expect(canal.descricao()).toBe('desc');
      });

      it('outros casos', function () {
        var etapa = limparModelo(servico).etapas()[0];

        expect(etapa.canaisDePrestacao()).toBeDefined();
        expect(etapa.canaisDePrestacao().outrosCasos()).toBeDefined();
        expect(etapa.canaisDePrestacao().outrosCasos().length).toBe(2);

        var caso1 = etapa.canaisDePrestacao().outrosCasos()[0];
        var caso2 = etapa.canaisDePrestacao().outrosCasos()[1];

        expect(caso1.descricao()).toBe('outros casos');

        expect(caso2.campos().length).toBe(1);
        expect(caso2.campos()[0].tipo()).toBe('tipo outros casos');
        expect(caso2.campos()[0].descricao()).toBe('descricao canal outros casos');
      });
    });
  });

  describe('tempo total estimado >', function () {
    beforeEach(function () {
      servico = new modelos.Servico({
        tempoTotalEstimado: new TempoTotalEstimado({
          tipo: ' entre ',
          entreMinimo: ' 1 ',
          entreMaximo: ' 3 ',
          entreTipoMaximo: ' dias ',
          ateMaximo: ' 30 ',
          ateTipoMaximo: ' meses '
        })
      });
    });

    it('limpar os campos', function () {
      servico = limparModelo(servico);

      expect(servico.tempoTotalEstimado().tipo()).toBe('entre');
      expect(servico.tempoTotalEstimado().entreMinimo()).toBe('1');
      expect(servico.tempoTotalEstimado().entreMaximo()).toBe('3');
      expect(servico.tempoTotalEstimado().entreTipoMaximo()).toBe('dias');
      expect(servico.tempoTotalEstimado().ateMaximo()).toBe('30');
      expect(servico.tempoTotalEstimado().ateTipoMaximo()).toBe('meses');
    });

  });

  it('palavras chave', function () {
    servico = new modelos.Servico({
      palavrasChave: ['', null, undefined, '  ch1', '', null, 'ch2  ']
    });
    servico = limparModelo(servico);

    expect(servico.palavrasChave()).toEqual(['ch1', 'ch2']);
  });

  it('palavras chave', function () {
    servico = new modelos.Servico({
      legislacoes: ['', null, undefined, '  http://www.lexml.gov.br/1', '', null, '  http://www.lexml.gov.br/2']
    });
    servico = limparModelo(servico);
    expect(servico.legislacoes()).toEqual(['http://www.lexml.gov.br/1', 'http://www.lexml.gov.br/2']);
  });
});
