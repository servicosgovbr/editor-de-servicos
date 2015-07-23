var models = {};

models.id = (function() {
  var counter = 0;
    return function(base) {
      return(base + '-' + counter++);
  }
})();

models.Etapa = function(data) {
  var data = (data || {});
  this.id = models.id('etapa');
  this.titulo = m.prop(data.titulo || '');
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
