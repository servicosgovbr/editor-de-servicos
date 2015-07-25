'use strict';

function cdata(doc, selector) {

  var elements = doc.querySelectorAll(selector);

  _.each(elements, function (el) {
    var content = '';
    for(var i = 0; i < el.childNodes.length; i++) {
      content += el.childNodes[0].nodeValue;
    }
    el.innerHTML = '';
    el.appendChild(doc.createCDATASection(content));
  });

}

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

var documentos = function (e) {
  return m('documentos', [
    m('default', e.casoPadrao().campos().map(itemSimples)),
    e.outrosCasos().map(function (caso) {
      return m('caso', {
        descricao: caso.descricao()
      }, caso.campos().map(itemSimples));
    })
  ]);
};

var custo = function (e) {
  return m('custo', [
      m('descricao', e.descricao()),
      m('moeda', e.moeda()),
      m('valor', e.valor())
    ]);
};

var custos = function (e) {
  return m('custos', [
    m('default', e.casoPadrao().campos().map(custo)),
    e.outrosCasos().map(function (caso) {
      return m('caso', {
        descricao: caso.descricao()
      }, caso.campos().map(custo));
    })
  ]);
};

var etapa = function (e) {
  return m('etapa', [
    m('titulo', e.titulo()),
    m('descricao', e.descricao()),
    documentos(e.documentos()),
    custos(e.custos()),
    m('canais-de-prestacao', [])
  ]);
};

exports.converter = function (servico) {

  var doc = document.implementation.createDocument('http://servicos.gov.br/v3/schema', '');
  m.render(doc, m('servico', {
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

  cdata(doc, 'servico > descricao');
  cdata(doc, 'servico > tempo-total-estimado > descricao');
  cdata(doc, 'servico > solicitantes > solicitante > requisitos');
  cdata(doc, 'servico > etapas > etapa > descricao');

  return doc;
};
