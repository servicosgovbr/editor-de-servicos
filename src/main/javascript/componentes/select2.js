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

    var message = 'Digite ' + remainingChars + ' ou mais caracteres.';

    return message;
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
  view: function (ctrl, args) {
    var prop = args.prop;
    var options = _.omit(args, 'prop');
    var erro = _.isFunction(prop.erro) ? prop.erro() : '';

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
            if (!el.val() || prop() === el.val()) {
              e.stopPropagation();
              return false;
            }

            m.startComputation();
            prop(el.val());
            m.endComputation();
          });
        }

        el.select2('val', prop());
      }
    }));
  }
};
