'use strict';

var modelos = require('modelos');

var str = function () {
  return '';
};

var um = function (lista, fn) {
  if (lista && lista.length > 0) {
    return lista;
  }
  return [fn()];
};

var item = function (i, n) {
  return n.textContent;
};

var solicitantes = function (i, n) {
  var t = jQuery(n);
  return new modelos.Solicitante({
    tipo: t.find('tipo').text(),
    requisitos: t.find('requisitos').text(),
  });
};

var tempoTotalEstimado = function (i, n) {
  var tag, t = jQuery(n);

  if (t.find('entre').length !== 0) {
    tag = t.find('entre');
    return new modelos.TempoTotalEstimado({
      tipo: 'entre',
      entreMinimo: tag.attr('min'),
      entreMaximo: tag.attr('max'),
      entreTipoMaximo: tag.attr('unidade'),
      descricao: t.find('descricao').text()
    });
  }

  if (t.find('ate').length !== 0) {
    tag = t.find('ate');
    return new modelos.TempoTotalEstimado({
      tipo: 'ate',
      ateMaximo: tag.attr('max'),
      ateTipoMaximo: tag.attr('unidade'),
      descricao: t.find('descricao').text()
    });
  }

  return new modelos.TempoTotalEstimado();
};

var caso = function (tipo) {
  return function (i, c) {
    var t = jQuery(c);
    return new modelos.Caso(null, {
      descricao: t.attr('descricao'),
      campos: t.find('> *').map(tipo).get()
    });
  };
};

var documentos = function (d) {
  var t = jQuery(d);

  var toDocumento = function (i, n) {
    return new modelos.Documento({
      descricao: item(i, n)
    });
  };

  return new modelos.Documentos({
    casoPadrao: new modelos.Caso(null, {
      campos: um(t.find('> default item').map(toDocumento).get(), function () {
        return new modelos.Documento();
      }),
    }),
    outrosCasos: t.find('caso').map(caso(toDocumento)).get()
  });
};

var custo = function (i, c) {
  var t = jQuery(c);
  return new modelos.Custo({
    descricao: t.find('descricao').text(),
    moeda: t.find('moeda').text(),
    valor: t.find('valor').text()
  });
};

var custos = function (c) {
  var t = jQuery(c);
  return new modelos.Custos({
    casoPadrao: new modelos.Caso(null, {
      campos: um(t.find('> default custo').map(custo).get(), custo),
    }),
    outrosCasos: t.find('caso').map(caso(custo)).get()
  });
};

var canalDePrestacao = function (i, c) {
  var t = jQuery(c);
  return new modelos.CanalDePrestacao({
    tipo: t.attr('tipo'),
    descricao: t.find('descricao').text()
  });
};

var canaisDePrestacao = function (c) {
  var t = jQuery(c);
  return new modelos.CanaisDePrestacao({
    casoPadrao: new modelos.Caso(null, {
      campos: um(t.find('> default canal-de-prestacao').map(canalDePrestacao).get(), canalDePrestacao),
    }),
    outrosCasos: t.find('caso').map(caso(canalDePrestacao)).get()
  });
};

var etapas = function (i, e) {
  var t = jQuery(e);
  return new modelos.Etapa({
    titulo: t.find('> titulo').text(),
    descricao: t.find('> descricao').text(),
    documentos: documentos(t.find('> documentos').get(0)),
    custos: custos(t.find('> custos').get(0)),
    canaisDePrestacao: canaisDePrestacao(t.find('> canais-de-prestacao').get(0))
  });
};

var servico = function (x) {
  return new modelos.Servico({
    nome: x.find('> nome').text(),
    sigla: x.find('> sigla').text(),
    nomesPopulares: um(x.find('> nomes-populares > item').map(item).get(), str),
    descricao: x.find('> descricao').text(),
    gratuidade: (x.find('> gratuito').text() === 'true') ? true : (x.find('> gratuito').text() === 'false' ? false : undefined),
    tempoTotalEstimado: x.find('> tempo-total-estimado').map(tempoTotalEstimado).get(0),
    solicitantes: um(x.find('> solicitantes > solicitante').map(solicitantes).get(), solicitantes),
    etapas: um(x.find('etapas > etapa', x).map(etapas).get(), etapas),
    orgao: x.find('servico > orgao').attr('id'),
    segmentosDaSociedade: x.find('servico > segmentos-da-sociedade > item').map(item).get(),
    areasDeInteresse: x.find('servico > areas-de-interesse > item').map(item).get(),
    palavrasChave: um(x.find('servico > palavras-chave > item').map(item).get(), str),
    legislacoes: um(x.find('servico > legislacoes > item').map(item).get(), str),
  });
};

module.exports = function (dom) {
  return servico(jQuery('servico', dom));
};
