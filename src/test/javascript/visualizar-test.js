'use strict';

var visualizar = require('servico/visualizar');
var modelos = require('servico/modelos');
var servicoEmEdicao = require('servico/servico-em-edicao');
//var xhr = require('lib/mithril-fake-xhr')(window);

describe('visualizar', function () {

  xit('deve ter o título "Nome Qualquer"', function () {
    //xhr('GET', '/editar/api/ping').respondWith({profile: {id: 'id'}});

    var servico = new modelos.Servico({
      nome: 'Nome Qualquer'
    });
    servicoEmEdicao.manter(m.prop(servico));

    jQuery('body').prepend('<div id="body-teste"></div>');
    var div = jQuery('#body-teste').get(0);

    m.mount(div, visualizar);

    expect(jQuery('#conteudo h2').text()).toBe('Nome Qualquer');
  });
});
