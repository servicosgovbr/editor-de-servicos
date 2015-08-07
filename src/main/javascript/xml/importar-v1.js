'use strict';

var slugify = require('slugify');
var modelos = require('modelos');

var string = function (s) {
  return jQuery(s).text().trim();
};

var custo = function (c) {
  return new modelos.Custo({
    valor: string(c).replace('R$', '').trim(),
  });
};

var custos = function (c) {
  var x = jQuery(c);
  return new modelos.Custos({
    casoPadrao: new modelos.Caso(null, {
      campos: x.get().map(custo).filter(function (s) {
        return s.length > 0;
      })
    }),
    outrosCasos: []
  });
};

var canalDePrestacao = function (c) {
  var x = jQuery(c);

  if (x.find('> link').attr('href') && x.find('> link').attr('href').trim().length > 0) {
    return new modelos.CanalDePrestacao({
      tipo: x.attr('tipo'),
      descricao: '[' + string(x.find('> descricao')) + '](' + x.find('> link').attr('href') + ')'
    });
  }

  var tipos = {
    'Aplicativo móvel': 'Aplicativo móvel',
    'E-mail': 'E-mail',
    'Fax': 'Fax',
    'Mobile': 'Aplicativo móvel',
    'Outros': 'Postal',
    'Postal': 'Postal',
    'Presencial': 'Presencial',
    'SMS': 'SMS',
    'Telefone': 'Telefone',
    'Web': 'Web',
  };

  return new modelos.CanalDePrestacao({
    tipo: slugify(tipos[x.attr('tipo').trim()]),
    descricao: string(x.find('> descricao')).replace(/\s+/g, ' ').replace('\n', '')
  });
};

var canaisDePrestacao = function (c) {
  var x = jQuery(c);
  var canais = [];

  if (x.find('url').length > 0) {
    canais.push(new modelos.CanalDePrestacao({
      tipo: 'web',
      descricao: string(x.find('url'))
    }));
  }

  if (x.find('urlAgendamento').length > 0) {
    canais.push(new modelos.CanalDePrestacao({
      tipo: 'web-agendar',
      descricao: string(x.find('urlAgendamento'))
    }));
  }

  return new modelos.CanaisDePrestacao({
    casoPadrao: new modelos.Caso(null, {
      campos: _.flatten(canais.concat(x.find('> canaisDePrestacao canalDePrestacao').get().map(canalDePrestacao)))
    }),
    outrosCasos: []
  });
};

var segmentosDaSociedade = function (x) {
  var ref = {
    'Serviços aos Cidadãos': 'Cidadãos',
    'Serviços às Empresas': 'Empresas'
  };

  return x.find('segmentosDaSociedade > segmentoDaSociedade > nome').get().map(string).map(function (i) {
    return ref[i];
  });
};

var areasDeInteresse = function (x) {
  var lookup = _.reduce(require('referencia').areasDeInteresse, function (r, n, i, k) {
    r[slugify(n)] = n;
    return r;
  }, {});

  return x.find('areasDeInteresse > areaDeInteresse > nome').get().map(string).map(function (i) {
    return lookup[slugify(i)];
  });
};

var eventosDaLinhaDaVida = function (x) {
  var lookup = _.reduce(require('referencia').eventosDaLinhaDaVida, function (r, n, i, k) {
    r[slugify(n)] = n;
    return r;
  }, {});

  return x.find('eventosDaLinhaDaVida > eventoDaLinhaDaVida > nome').get().map(string).map(function (i) {
    return lookup[slugify(i)];
  });
};

var descricao = function (x) {
  var informacoesUteis = x.find('> informacoesUteis informacaoUtil').map(function (i, n) {
    if (string(jQuery(n).find('descricao')).length > 0) {
      return '\n* [' + jQuery(n).attr('tipo') + '](' + jQuery(n).find('link').attr('href') + '): ' + string(jQuery(n).find('descricao'));
    } else {
      return '\n* [' + jQuery(n).attr('tipo') + '](' + jQuery(n).find('link').attr('href') + ')';
    }
  }).get().join('\n');

  return string(x.find('> descricao')) + '\n' + informacoesUteis;
};

var orgao = function (x) {
  return string(x.find('orgaoResponsavel id'));
};

var servico = function (x) {
  return new modelos.Servico({
    nome: string(x.find('> nome')),
    sigla: '',
    nomesPopulares: [],
    descricao: descricao(x),
    solicitantes: [],
    tempoTotalEstimado: new modelos.TempoTotalEstimado({}),
    etapas: [
      new modelos.Etapa({
        custos: custos(x.find('> custoTotalEstimado').first()),
        canaisDePrestacao: canaisDePrestacao(x),
      })
    ],
    orgao: orgao(x),
    segmentosDaSociedade: segmentosDaSociedade(x),
    eventosDaLinhaDaVida: eventosDaLinhaDaVida(x),
    areasDeInteresse: areasDeInteresse(x),
    palavrasChave: [],
    legislacoes: [],
  });
};

module.exports = function (dom) {
  return servico(jQuery('servico', dom));
};
