var models = {};

models.id = (function() {
  var counter = 0;
    return function(base) {
      return(base + '-' + counter++);
  }
})();

models.Caso = function(data) {
  var data = (data || {});
  this.id = models.id('caso');
  this.descricao = m.prop(data.descricao || '');
  this.campos = m.prop(data.campos || []);
};

models.Documentos = function(data) {
  var data = (data || {});
  this.id = models.id('documentos');
  this.casoPadrao = m.prop(data.casoPadrao || new models.Caso({ descricao: 'Para todos os casos', campos: []}));
  this.outrosCasos = m.prop(data.outrosCasos || []);
};

models.Custo = function(data) {
  var data = (data || {});
  this.id = models.id('custo');
  this.descricao = m.prop(data.descricao || '');
  this.moeda = m.prop(data.moeda || '');
  this.valor = m.prop(data.valor || '');
};

models.Custos = function(data) {
  var data = (data || {});
  this.id = models.id('custos');
  this.casoPadrao = m.prop(data.casoPadrao || new models.Caso({ descricao: 'Para todos os casos', campos: [ new models.Custo({descricao: 'custo 1', moeda: 'R$', valor: '10,00'})]}));
  this.outrosCasos = m.prop(data.outrosCasos || [ new models.Caso(({ descricao: 'No exterior', campos: [ new models.Custo({descricao: 'custo 2', moeda: 'US$', valor: '25,99'})]}))]);
};

models.Etapa = function(data) {
  var data = (data || {});
  this.id = models.id('etapa');
  this.titulo = m.prop(data.titulo || '');
  this.descricao = m.prop(data.descricao || '');
  this.documentos = m.prop(data.documentos || new models.Documentos());
  this.custos = m.prop(data.custos || new models.Custos());
};

models.Solicitante = function (data) {
  var data = (data || {});
  this.id = models.id('solicitante');
  this.descricao = m.prop(data.descricao || '');
  this.requisitos = m.prop(data.requisitos || '');
};

models.Servico = function (data) {
  var data = (data || {});
  this.id = models.id('servico');
  this.nome = m.prop(data.nome || '');
  this.nomesPopulares = m.prop(data.nomesPopulares || []);
  this.descricao = m.prop(data.descricao || '');
  this.solicitantes = m.prop(data.solicitantes || []);
  this.tempoTotalEstimado = m.prop(data.tempoTotalEstimado || new models.TempoTotalEstimado());
  this.etapas = m.prop(data.etapas || []);
  this.orgao = m.prop(data.orgao || '');
  this.segmentosDaSociedade = m.prop(data.segmentosDaSociedade || []);
  this.eventosDaLinhaDaVida = m.prop(data.eventosDaLinhaDaVida || []);
  this.areasDeInteresse = m.prop(data.areasDeInteresse || []);
  this.palavrasChave = m.prop(data.palavrasChave || []);
  this.legislacoes = m.prop(data.legislacoes || []);
};

models.TempoTotalEstimado = function(data) {
  var data = (data || {});
  this.id = models.id('tempo-total-estimado');
  this.tipo = m.prop(data.tipo || '');
  this.entreMinimo = m.prop(data.entreMinimo || '');
  this.entreTipoMinimo = m.prop(data.entreTipoMinimo || '');
  this.ateMaximo = m.prop(data.ateMaximo || '');
  this.ateTipoMaximo = m.prop(data.ateTipoMaximo || '');
  this.entreMaximo = m.prop(data.entreMaximo || '');
  this.entreTipoMaximo = m.prop(data.entreTipoMaximo || '');
  this.excecoes = m.prop(data.excecoes || '');
};
