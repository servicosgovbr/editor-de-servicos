var models = {};

models.id = (function() {
  var counters = {};
  return function(base) {
    if (!counters[base]) {
      counters[base] = 0;
    }
    return base + '-' + counters[base]++;
  }
})();

models.Caso = function(data) {
  var data = (data || {});
  this.id = models.id('caso');
  this.descricao = m.prop(data.descricao || '');
  this.campos = m.prop(data.campos || []);
};

models.CanaisDePrestacao = function(data) {
  var data = (data || {});
  this.id = models.id('canais-de-prestacao');
  this.casoPadrao = m.prop(data.casoPadrao || new models.Caso({ descricao: 'Para todos os casos', campos: []}));
  this.outrosCasos = m.prop(data.outrosCasos || []);
};

models.CanalDePrestacao = function(data) {
  var data = (data || {});
  this.id = models.id('canal-de-prestacao');
  this.tipo = m.prop(data.tipo || '');
  this.descricao = m.prop(data.tipo || '');
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
  this.casoPadrao = m.prop(data.casoPadrao || new models.Caso({ descricao: 'Para todos os casos', campos: []}));
  this.outrosCasos = m.prop(data.outrosCasos || []);
};

models.Etapa = function(data) {
  var data = (data || {});
  this.id = models.id('etapa');
  this.titulo = m.prop(data.titulo || '');
  this.descricao = m.prop(data.descricao || '');
  this.documentos = m.prop(data.documentos || new models.Documentos());
  this.custos = m.prop(data.custos || new models.Custos());
  this.canaisDePrestacao = m.prop(data.canaisDePrestacao || new models.CanaisDePrestacao());
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
  this.etapas = m.prop(data.etapas || [new models.Etapa()]);
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
  this.ateMaximo = m.prop(data.ateMaximo || '');
  this.ateTipoMaximo = m.prop(data.ateTipoMaximo || '');
  this.entreMaximo = m.prop(data.entreMaximo || '');
  this.entreTipoMaximo = m.prop(data.entreTipoMaximo || '');
  this.descricao = m.prop(data.descricao || '');
};
