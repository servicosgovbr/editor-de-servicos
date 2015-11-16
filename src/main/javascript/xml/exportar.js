'use strict';

var cdata = function (doc, selector) {
  _(doc.querySelectorAll(selector)).each(function (el) {
    var content = _(el.childNodes).map(function (cn) {
      return el.removeChild(cn).nodeValue;
    }).join('');

    el.appendChild(doc.createCDATASection(content));
  });
};

var tempoTotalEstimado = function (tempoEstimado) {
  var limite;
  if (tempoEstimado.tipo() === 'entre') {
    limite = m('entre', {
      min: tempoEstimado.entreMinimo(),
      max: tempoEstimado.entreMaximo(),
      unidade: tempoEstimado.entreTipoMaximo()
    });

  } else if (tempoEstimado.tipo() === 'ate') {
    limite = m('ate', {
      max: tempoEstimado.ateMaximo(),
      unidade: tempoEstimado.ateTipoMaximo()
    });
  }

  return m('tempo-total-estimado', [
    limite,
    m('descricao', tempoEstimado.descricao())
  ]);
};

var item = function (i) {
  return m('item', i);
};

var casos = function (e, nome, itemDoCaso) {
  return m(nome, [
    m('default', e.casoPadrao().campos().map(itemDoCaso)),
    e.outrosCasos().map(function (caso) {
      return m('caso', {
        descricao: caso.descricao()
      }, caso.campos().map(itemDoCaso));
    })
  ]);
};

var documento = function (e) {
  return m('item', e.descricao());
};

var documentos = function (e) {
  return e ? casos(e, 'documentos', documento) : '';
};

var custo = function (e) {
  return m('custo', [
      m('descricao', e.descricao()),
      m('moeda', e.moeda()),
      m('valor', e.valor())
    ]);
};

var custos = function (e) {
  return e ? casos(e, 'custos', custo) : '';
};

var canalDePrestacao = function (e) {
  return m('canal-de-prestacao', {
    tipo: e.tipo()
  }, [
   m('descricao', e.descricao())
 ]);
};

var canaisDePrestacao = function (e) {
  return e ? casos(e, 'canais-de-prestacao', canalDePrestacao) : '';
};

var etapa = function (e) {
  return m('etapa', [
    m('titulo', e.titulo()),
    m('descricao', e.descricao()),
    documentos(e.documentos()),
    custos(e.custos()),
    canaisDePrestacao(e.canaisDePrestacao())
  ]);
};

var solicitantes = function (sol) {
  return m('solicitantes', sol.map(function (s) {
    return m('solicitante', [
        m('tipo', s.tipo()),
        m('requisitos', s.requisitos())
      ]);
  }));
};

var Gratuidade = require('servico/modelos').Gratuidade;
var gratuidade = function (ehGratuito) {
  switch (ehGratuito) {
  case Gratuidade.GRATUITO:
    return true;
  case Gratuidade.PAGO:
    return false;
  }
  return undefined;
};

var xmlDoc = function (ns) {
  return document.implementation.createDocument(ns, '');
};

module.exports = function (servico) {
  var doc = xmlDoc('http://servicos.gov.br/v3/schema');

  m.render(doc, m('servico', {
    'xmlns': 'http://servicos.gov.br/v3/schema',
    'xmlns:xsi': 'http://www.w3.org/2001/XMLSchema-instance',
    'xsi:schemaLocation': 'http://servicos.gov.br/v3/schema ../servico.xsd'
  }, [
    m('nome', servico.nome()),
    m('sigla', servico.sigla()),
    m('nomes-populares', servico.nomesPopulares().map(item)),
    m('descricao', servico.descricao()),
    m('gratuito', gratuidade(servico.gratuidade())),
    solicitantes(servico.solicitantes()),
    tempoTotalEstimado(servico.tempoTotalEstimado()),
    m('etapas', servico.etapas().map(etapa)),
    m('orgao', {
        id: servico.orgao().nome()
      },
      m('contato', servico.orgao().contato())
    ),
    m('segmentos-da-sociedade', servico.segmentosDaSociedade().map(item)),
    m('areas-de-interesse', servico.areasDeInteresse().map(item)),
    m('palavras-chave', servico.palavrasChave().map(item)),
    m('legislacoes', servico.legislacoes().map(item))
  ]));

  cdata(doc, 'descricao');
  cdata(doc, 'requisitos');

  return doc;
};
