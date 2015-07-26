'use strict';

module.exports = {

  controller: function (args) {
    this.servico = args.servico;
  },

  view: function (ctrl) {
    return m('#menu-lateral', [
      m('ul', [
        m('li', [
          m('a[href="#dados-basicos"]', 'Dados básicos'),
          m('ul', [
            m('li', m('a[href="#nome"]', 'Nome')),
            m('li', m('a[href="#sigla"]', 'Sigla')),
            m('li', m('a[href="#nomes-populares"]', 'Nomes populares')),
            m('li', m('a[href="#descricao"]', 'Descrição')),
            m('li', m('a[href="#tempo-total-estimado"]', 'Tempo total estimado')),
            m('li', m('a[href="#gratuidade"]', 'Gratuidade')),
          ])
        ]),
        m('li', m('a[href="#solicitantes"]', 'Solicitantes')),
        m('li', [
          m('a[href="#etapas"]', 'Etapas'),
          m('ul', ctrl.servico().etapas().map(function (etapa) {
            return m('li', m('a', {
              href: '#' + etapa.id
            }, etapa.titulo() || m('em', '(sem título)')));
          }))
        ]),
        m('li', [
          m('a[href="#dados-complementares"]', 'Dados complementares'),
          m('ul', [
            m('li', m('a[href="#orgao-responsavel"]', 'Órgão responsável')),
            m('li', m('a[href="#segmentos-da-sociedade"]', 'Segmentos da sociedade')),
            m('li', m('a[href="#eventos-da-linha-da-vida"]', 'Eventos da linha da vida')),
            m('li', m('a[href="#areas-de-interesse"]', 'Áreas de interesse')),
            m('li', m('a[href="#palavras-chave"]', 'Palavras-chave')),
            m('li', m('a[href="#legislacoes"]', 'Legislações')),
          ])
        ]),
      ]),
    ]);
  }

};
