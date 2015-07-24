'use strict';

var models = require('modelos');

var montaTempoEstimado = function(tempoEstimado) {

  if(tempoEstimado.tipo() === 'entre') {
    return m('entre', {
      min: tempoEstimado.entreMinimo(),
      max: tempoEstimado.entreMaximo(),
      unidade: tempoEstimado.entreTipoMaximo()
    });

  } else if(tempoEstimado.tipo() === 'até') {
    return m('ate', {
      max: tempoEstimado.ateMaximo(),
      unidade: tempoEstimado.ateTipoMaximo()
    });
  }

};

var itemSimples = function(item) {
  return m('item', item);
};

var etapa = function(e) {
  return m('etapa', [
    m('titulo', e.titulo()),
    m('descricao', e.descricao()),
    m('documentos', [
      m('default', e.documentos().casoPadrao().campos().map(itemSimples)),
      e.documentos().outrosCasos().map(function(caso) {
        return m('caso', { descricao: caso.descricao() }, caso.campos().map(itemSimples));
      })
    ]),
    m('custos', []),
    m('canais-de-prestacao', [])
  ]);
};

module.exports = {

  controller: function () {
    this.servico = new models.Servico();

    this.debug = function () {
      var xml = document.createDocumentFragment();
      m.render(xml, m('servico', { xmlns: 'http://servicos.gov.br/v3/schema' }, [
        m('nome', this.nome()),
        m('sigla', this.sigla()),
        m('nomes-populares', this.nomesPopulares().map(itemSimples)),
        m('descricao', this.descricao()),
        m('solicitantes', this.solicitantes().map(function(s) {
          return m('solicitante', [
            m('descricao', s.descricao()),
            m('requisitos', s.requisitos())
          ]);
        })),
        m('tempo-total-estimado', {
          tipo: this.tempoTotalEstimado().tipo()
        }, [
          montaTempoEstimado(this.tempoTotalEstimado()),
          m('descricao', this.tempoTotalEstimado().descricao())
        ]),
        m('etapas', this.etapas().map(etapa)),
        m('orgao', { id: this.orgao() }),
        m('segmentos-da-sociedade', this.segmentosDaSociedade().map(itemSimples)),
        m('eventos-da-linha-da-vida', this.eventosDaLinhaDaVida().map(itemSimples)),
        m('areas-de-interesse', this.areasDeInteresse().map(itemSimples)),
        m('palavras-chave', this.palavrasChave().map(itemSimples)),
        m('legislacoes', this.legislacoes().map(itemSimples))
      ]));

      console.log(this); // jshint ignore:line
      console.log(new XMLSerializer().serializeToString(xml)); // jshint ignore:line
    };
  },

  view: function (ctrl) {
    return m('', [
      m.component(require('componentes/menu-lateral'), {
        servico: ctrl.servico
      }),

      m('#principal.auto-grid', [

        m.component(require('componentes/dados-basicos'), {
          servico: ctrl.servico
        }),

        m.component(require('componentes/solicitantes'), {
          solicitantes: ctrl.servico.solicitantes()
        }),

        m.component(require('componentes/etapas'), {
          etapas: ctrl.servico.etapas(),
          gratuidade: ctrl.servico.gratuidade
        }),

        m.component(require('componentes/dados-complementares'), {
          servico: ctrl.servico
        }),

        m('button.debug', {
          onclick: ctrl.debug.bind(ctrl.servico)
        }, [
          m('i.fa.fa-bug'),
          ' Debug '
        ])
      ])
    ]);
  }
};
