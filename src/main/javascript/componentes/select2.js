'use strict';

var language = {
  errorLoading: function () {
    return 'Erro ao carregar os resultados. Por favor, tente novamente';
  },

  inputTooLong: function (args) {
    var overChars = args.input.length - args.maximum;

    var message = 'Apague ' + overChars + ' caracter';

    if (overChars !== 1) {
      message += 'es';
    }

    return message;
  },

  inputTooShort: function (args) {
    var remainingChars = args.minimum - args.input.length;

    return 'Digite ' + remainingChars + ' ou mais caracteres.';
  },

  loadingMore: function () {
    return 'Carregando mais resultados…';
  },

  maximumSelected: function (args) {
    var message = 'Você só pode selecionar ' + args.maximum + ' ite';

    if (args.maximum === 1) {
      message += 'm';
    } else {
      message += 'ns';
    }

    return message;
  },

  noResults: function () {
    return 'Nenhum resultado encontrado.';
  },

  searching: function () {
    return 'Buscando…';
  }
};

module.exports = {
  controller: function (args) {
    this.prop = args.prop;
  },

  view: function (ctrl, args) {
    ctrl.prop = args.prop;

    var erro = (ctrl.prop.erro || _.noop)();
    var options = _.omit(args, 'prop');

    return m('.clear.select2-message-container', {
      class: erro
    }, m('select', {
      config: function (element, isInitialized) {
        var el = jQuery(element);
        if (!isInitialized) {
          var select2 = el.select2(_.merge({
            language: language,
            placeholder: 'Selecione...',
            minimumResultsForSearch: Infinity
          }, options));

          select2.on('change', function (e) {
            if (!options.allowClear && (!el.val() || ctrl.prop() === el.val())) {
              e.stopPropagation();
              return false;
            }

            m.startComputation();
            ctrl.prop(el.val());
            m.endComputation();

            if (_.isFunction(options.change)) {
              options.change(el.val());
            }
          });
        }
        if (!options.initSelection) {
          el.select2('val', ctrl.prop());
        }
      }
    }));
  }
};
