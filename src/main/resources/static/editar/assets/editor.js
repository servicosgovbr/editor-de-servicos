var models = {};

models.Solicitante = function (data) {
  var data = (data || {});
  this.descricao = m.prop(data.descricao || '');
  this.requisitos = m.prop(data.requisitos || '');
};

models.Servico = function (data) {
  var data = (data || {});
  this.nome = m.prop(data.nome || '');
  this.nomesPopulares = m.prop(data.nomesPopulares || '');
  this.descricao = m.prop(data.descricao || '');
  this.solicitantes = (data.solicitantes || []);
  this.tempoTotalEstimado = (data.tempoTotalEstimado || new models.TempoTotalEstimado());
  this.orgao = m.prop(data.orgao || '');
  this.segmentosDaSociedade = m.prop(data.segmentosDaSociedade || []);
  this.eventosDaLinhaDaVida = m.prop(data.eventosDaLinhaDaVida || []);
  this.areasDeInteresse = m.prop(data.areasDeInteresse || []);
  this.palavrasChave = m.prop(data.palavrasChave || '');
  this.legislacoes = m.prop(data.legislacoes || []);
};

models.TempoTotalEstimado = function(data) {
  var data = (data || {});
  this.tipo = m.prop(data.tipo || '');
  this.entreMinimo = m.prop(data.entreMinimo || '');
  this.entreTipoMinimo = m.prop(data.entreTipoMinimo || '');
  this.ateMaximo = m.prop(data.ateMaximo || '');
  this.ateTipoMaximo = m.prop(data.ateTipoMaximo || '');
  this.entreMaximo = m.prop(data.entreMaximo || '');
  this.entreTipoMaximo = m.prop(data.entreTipoMaximo || '');
  this.excecoes = m.prop(data.excecoes || '');
};

var Cabecalho = {
  controller: function () {
    this.login = m.request({ method: 'GET', url:'/editar/api/usuario' }).then(function(data) {
      return data.username;
    });
  },

  view: function (ctrl) {
    return m('header', [
      m('.auto-grid', [
        m('#logout', [
          m('span', [' ', ctrl.login(), ' ']),
          m('button', [
            m('i.fa.fa-sign-out'), m.trust('&nbsp; Sair'),
          ])
        ])
      ]),
    ]);
  }
};

var EditorMarkdown = {

  controller: function(args) {
    this.config = _.extend((args || {}), {
      style: {
        maxWidth: '100%',
        width: '100%'
      },

      onkeyup: m.withAttr('value', function(txt) {
        this.caracteres(500 - txt.length);
      }.bind(this))
    });

    this.caracteres = m.prop(500);
  },

  view: function(ctrl) {
    return m('', [
      m('.editor-barra-ferramentas', [
        m('a', {
          alt: 'Adicionar link',
          title: 'Adicionar link',
          href: 'javascript:void(0)'
        }, [m('i.fa.fa-link')]),

        m('a', {
          alt: 'Adicionar item de lista',
          title: 'Adicionar item de lista',
          href: 'javascript:void(0)'
        }, [m('i.fa.fa-list')])
      ]),

      m('textarea', ctrl.config),
      m('.counter', ['Caracteres restantes: ', m('span', ctrl.caracteres())])
    ]);
  }
};

var DadosBasicos = {
  controller: function (args) {
    this.servico = args.servico;
  },

  view: function (ctrl) {
    return m('#dados-principais', [
      m('h2', 'Dados Principais'),

      m('fieldset', [
        m('h3', 'Nome do serviço'),
        m('input[type=text]', {
          onchange: m.withAttr('value', ctrl.servico.nome),
          value: ctrl.servico.nome()
        }),

        m('h3', 'Nomes populares'),
        m('input[type=text]', {
          onchange: m.withAttr('value', ctrl.servico.nomesPopulares),
          value: ctrl.servico.nomesPopulares()
        }),

        m('h3', 'Descrição do serviço'),
        m.component(EditorMarkdown, {
          rows: 10,
          oninput: m.withAttr('value', ctrl.servico.descricao),
          value: ctrl.servico.descricao()
        })
      ])
    ]);
  }
};

var Solicitantes = {
  controller: function (args) {
    this.servico = args.servico;

    this.adicionar = function() {
      this.servico.solicitantes.push(new models.Solicitante());
    };

    this.remover = function(i) {
      this.servico.solicitantes.splice(i, 1);
    };

  },
  view: function (ctrl) {
    return m('#solicitantes', [
      m('h2', 'Quem pode utilizar este serviço?'),

      ctrl.servico.solicitantes.map(function(s, i) {
        return m('fieldset', [
          m('h3', 'Solicitante'),

          m("input.inline.inline-xg[type='text']", {
            value: s.descricao(),
            onchange: m.withAttr('value', s.descricao)
          }),

          m("button.inline.remove-peq", {
            onclick: ctrl.remover.bind(ctrl, i)
          }, [
            m("span.fa.fa-times")
          ]),

          m('h3', 'Requisitos'),
          m.component(EditorMarkdown, {
            rows: 5,
            value: s.requisitos(),
            onchange: m.withAttr('value', s.requisitos)
          })
        ]);
      }),

      m("button.adicionar-solicitante", {
        onclick: ctrl.adicionar.bind(ctrl)
      }, [
        m("i.fa.fa-plus"),
        " Adicionar solicitante "
      ])
    ]);
  }
};

var TempoTotalEstimado = {
  controller: function (args) {
    this.servico = args.servico;

    this.modificarTipo = function(e) {
      this.servico.tempoTotalEstimado.tipo(e.target.value);
    };
  },
  view: function (ctrl) {
    var unidades = [
      m("option[value='']", "Selecione…"),
      m("option[value='minutos']", "minutos"),
      m("option[value='horas']", "horas"),
      m("option[value='dias corridos']", "dias corridos"),
      m("option[value='dias úteis']", "dias úteis"),
      m("option[value='meses']", "meses")
    ];

    return m('#tempo-total-estimado', [
      m('fieldset', [
        m('h3', 'Tempo total estimado'),

        m("select.inline", {
          onchange: ctrl.modificarTipo.bind(ctrl)
        }, [
          m("option[value='']", "Selecione…"),
          m("option[value='entre']", "Entre"),
          m("option[value='até']", "Até")
        ]),
        ' ',
        m('span.ateTipo', {
          style: {
            display: ctrl.servico.tempoTotalEstimado.tipo() == 'até' ? 'inline' : 'none'
          }
        }, [
          m("input.inline[type='text']", {
            value: ctrl.servico.tempoTotalEstimado.ateMaximo(),
            onchange: m.withAttr('value', ctrl.servico.tempoTotalEstimado.ateMaximo)
          }),
          " ",
          m("select.inline", {
            onchange: m.withAttr('value', ctrl.servico.tempoTotalEstimado.ateTipoMaximo)
          }, unidades),
        ]),
        m('span.entreTipo', {
          style: {
            display: ctrl.servico.tempoTotalEstimado.tipo() == 'entre' ? 'inline' : 'none'
          }
        }, [
          m("input.inline[type='text']", {
            value: ctrl.servico.tempoTotalEstimado.entreMinimo(),
            onchange: m.withAttr('value', ctrl.servico.tempoTotalEstimado.entreMinimo)
          }),
          " ",
          m("select.inline", {
            onchange: m.withAttr('value', ctrl.servico.tempoTotalEstimado.entreTipoMinimo)
          }, unidades),

          " e ",
          m("input.inline[type='text']", {
            value: ctrl.servico.tempoTotalEstimado.entreMaximo(),
            onchange: m.withAttr('value', ctrl.servico.tempoTotalEstimado.entreMaximo)
          }),
          " ",
          m("select.inline", {
            onchange: m.withAttr('value', ctrl.servico.tempoTotalEstimado.entreTipoMaximo)
          }, unidades)
        ]),

        m("p", "Existem exceções ao tempo estimado? Quais?"),
        m.component(EditorMarkdown, {
          rows: 5,
          oninput: m.withAttr('value', ctrl.servico.tempoTotalEstimado.excecoes),
          value: ctrl.servico.tempoTotalEstimado.excecoes()
        })
      ])
    ]);
  }
};

var OrgaoResponsavel = {
  controller: function(args) {
    this.servico = args.servico;
    this.orgaos = m.request({ method: 'GET', url: '/editar/api/orgaos' });
  },
  view: function(ctrl) {
    return m('', [
      m("h3", "Órgão responsável"),
      m("select", {
        onchange: function(e) { ctrl.servico.orgao(e.target.value); }
      }, [m('option', {value: ''}, 'Selecione...')].concat(ctrl.orgaos().map(function(orgao) {
        return m('option', {
          value: orgao.id,
          selected: ctrl.servico.orgao() === orgao.id
        }, orgao.nome);
      })))
    ]);
  }
};

var SegmentosDaSociedade = {
  controller: function(args) {
    this.servico = args.servico;

    this.adicionar = function(e) {
      var segmento = e.target.value;
      var segmentos = this.servico.segmentosDaSociedade();

      segmentos = _.without(segmentos, segmento);
      if (e.target.checked) {
        segmentos.push(segmento);
      }

      this.servico.segmentosDaSociedade(segmentos);
    };

    this.segmentosDaSociedade = m.request({ method: 'GET', url: '/editar/api/segmentos-da-sociedade' });
  },
  view: function(ctrl) {
    return m('', [
      m("h3", "Segmentos da sociedade"),
      m("", ctrl.segmentosDaSociedade().map(function(segmento) {
        return m('label', [
          m("input[type=checkbox]", {
            value: segmento,
            checked: _.contains(ctrl.servico.segmentosDaSociedade(), segmento),
            onchange: ctrl.adicionar.bind(ctrl)
          }),
          segmento
        ]);
      }))
    ]);
  }
};

var EventosDaLinhaDaVida = {
  controller: function(args) {
    this.servico = args.servico;

    this.eventosDaLinhaDaVida = m.request({ method: 'GET', url: '/editar/api/eventos-da-linha-da-vida' });
    this.adicionar = function(e) {
        var evento = e.target.value;
        var eventos = this.servico.eventosDaLinhaDaVida();

        eventos = _.without(eventos, evento);
        if (e.target.checked) {
            eventos.push(evento);
        }
        this.servico.eventosDaLinhaDaVida(eventos);
    }
  },
  view: function(ctrl) {
    return m('', [
      m("h3", "Eventos da linha da vida"),
      m("", ctrl.eventosDaLinhaDaVida().map(function(evento) {
        return m('label', [
          m("input[type=checkbox]", {
            value: evento,
            checked: _.contains(ctrl.servico.eventosDaLinhaDaVida(), evento),
            onchange: ctrl.adicionar.bind(ctrl)
          }),
          evento
        ]);
      }))
    ]);
  }
};

var AreasDeInteresse = {
  controller: function(args) {
    this.servico = args.servico;
    this.areasDeInteresse = m.request({ method: 'GET', url: '/editar/api/areas-de-interesse' });

    this.adicionar = function(e) {
      var area = e.target.value;
      var areas = this.servico.areasDeInteresse();

      areas = _.without(areas, area);
      if (e.target.checked) {
        areas.push(area);
      }
      this.servico.areasDeInteresse(areas);
    }
  },
  view: function(ctrl) {
    return m('', [
      m("h3", "Áreas de interesse"),
      m("", ctrl.areasDeInteresse().map(function(area) {
        return m('label', [
          m("input[type=checkbox]", {
            value: area,
            checked: _.contains(ctrl.servico.areasDeInteresse(), area),
            onchange: ctrl.adicionar.bind(ctrl)
          }),
          area
        ]);
      }))
    ]);
  }
};

var PalavrasChave = {
  controller: function(args) {
    this.servico = args.servico;
  },
  view: function(ctrl) {
    return m('', [
      m('h3', 'Palavras-chave'),
      m('input[type=text]', {
        onchange: m.withAttr('value', ctrl.servico.palavrasChave),
        value: ctrl.servico.palavrasChave()
      })
    ]);
  }
};

var Legislacao = {
  controller: function(args) {
    this.servico = args.servico;

    this.adicionar = function() {
      this.servico.legislacoes().push('');
    };

    this.remover = function(i) {
      this.servico.legislacoes().splice(i, 1);
    };
  },
  view: function(ctrl) {
    return m('', [
      m('h3', 'Legislações relacionadas ao serviço'),
      m('.legislacoes', ctrl.servico.legislacoes().map(function(legislacao, i) {
        return [
          m('input.inline.inline-xg[type=text]', {
            value: legislacao,
            onchange: function(e) {
              ctrl.servico.legislacoes()[i] = e.target.value;
            }
          }),
          m('button.inline.remove-peq', {
            onclick: ctrl.remover.bind(ctrl, i)
          }, [
            m("span.fa.fa-times")
          ])
        ];
      })),
      m('button.adicionar-legislacao', {
        onclick: ctrl.adicionar.bind(ctrl)
      }, [
        m("i.fa.fa-plus"),
        " Adicionar legislação "
      ])
    ]);
  }
};

var DadosComplementares = {
  controller: function(args) {
    this.servico = args.servico;
  },
  view: function(ctrl) {
    return m('#dados-complementares', [
      m('h2', 'Dados Complementares'),

      m('fieldset', [
        m.component(OrgaoResponsavel, { servico: ctrl.servico }),
        m.component(SegmentosDaSociedade, { servico: ctrl.servico }),
        m.component(EventosDaLinhaDaVida, { servico: ctrl.servico }),
        m.component(AreasDeInteresse, { servico: ctrl.servico }),
        m.component(PalavrasChave, { servico: ctrl.servico }),
        m.component(Legislacao, { servico: ctrl.servico })
      ])
    ]);
  }
};

var EditorDeServicos = {
  controller: function () {
    this.servico = new models.Servico();

    this.debug = function () {
      console.log(JSON.stringify(this));
    };
  },

  view: function (ctrl) {
    return m('#principal.auto-grid', [
      m.component(DadosBasicos, {
        servico: ctrl.servico
      }),

      m.component(Solicitantes, {
        servico: ctrl.servico
      }),

      m.component(TempoTotalEstimado, {
        servico: ctrl.servico
      }),

      m.component(DadosComplementares, {
        servico: ctrl.servico
      }),

      m('button', {
        onclick: ctrl.debug.bind(ctrl.servico),
        style: {
          backgroundColor: '#d00'
        }
      }, [
        m("i.fa.fa-bug"),
        " Debug "
      ])
    ]);
  }
};

m.module(document.getElementById('cabecalho'), Cabecalho);
m.module(document.getElementById('conteudo'), EditorDeServicos);