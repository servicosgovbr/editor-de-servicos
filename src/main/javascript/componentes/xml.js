'use strict';

var montaTempoEstimado = function (tempoEstimado) {

  if (tempoEstimado.tipo() === 'entre') {
    return m('entre', {
      min: tempoEstimado.entreMinimo(),
      max: tempoEstimado.entreMaximo(),
      unidade: tempoEstimado.entreTipoMaximo()
    });

  } else if (tempoEstimado.tipo() === 'até') {
    return m('ate', {
      max: tempoEstimado.ateMaximo(),
      unidade: tempoEstimado.ateTipoMaximo()
    });
  }

};

var itemSimples = function (item) {
  return m('item', item);
};

var etapa = function (e) {
  return m('etapa', [
    m('titulo', e.titulo()),
    m('descricao', e.descricao()),
    m('documentos', [
    m('default', e.documentos().casoPadrao().campos().map(itemSimples)),
    e.documentos().outrosCasos().map(function (caso) {
        return m('caso', {
          descricao: caso.descricao()
        }, caso.campos().map(itemSimples));
      })
    ]),
    m('custos', []),
    m('canais-de-prestacao', [])
  ]);
};

exports.converter = function (servico) {

  var xml = document.createDocumentFragment();
  m.render(xml, m('servico', {
    xmlns: 'http://servicos.gov.br/v3/schema'
  }, [
    m('nome', servico.nome()),
    m('sigla', servico.sigla()),
    m('nomes-populares', servico.nomesPopulares().map(itemSimples)),
    m('descricao', servico.descricao()),
    m('solicitantes', servico.solicitantes().map(function (s) {
      return m('solicitante', [
        m('descricao', s.descricao()),
        m('requisitos', s.requisitos())
      ]);
    })),
    m('tempo-total-estimado', {
      tipo: servico.tempoTotalEstimado().tipo()
    }, [
      montaTempoEstimado(servico.tempoTotalEstimado()),
      m('descricao', servico.tempoTotalEstimado().descricao())
    ]),
    m('etapas', servico.etapas().map(etapa)),
    m('orgao', {
      id: servico.orgao()
    }),
    m('segmentos-da-sociedade', servico.segmentosDaSociedade().map(itemSimples)),
    m('eventos-da-linha-da-vida', servico.eventosDaLinhaDaVida().map(itemSimples)),
    m('areas-de-interesse', servico.areasDeInteresse().map(itemSimples)),
    m('palavras-chave', servico.palavrasChave().map(itemSimples)),
    m('legislacoes', servico.legislacoes().map(itemSimples))
  ]));

  return xml;
};
