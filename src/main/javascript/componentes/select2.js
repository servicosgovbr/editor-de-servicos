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
  controller: function (args) {
    this.prop = args.prop;
    this.options = _.omit(args, 'prop');
  },

  view: function (ctrl) {
    return m('.clear.select2-container', m('select', {
      config: function (element, isInitialized) {
        var el = jQuery(element);

        if (!isInitialized) {
          var select2 = el.select2(_.merge({
            language: language,
            placeholder: 'Selecione...',
            minimumResultsForSearch: Infinity
          }, ctrl.options));

          select2.on('change', function (e) {
            if (!el.val() || ctrl.prop() === el.val()) {
              e.stopPropagation();
              return false;
            }

            m.startComputation();
            ctrl.prop(el.val());
            m.endComputation();
          });
        }

        el.select2('val', ctrl.prop());
      }
    }));
  }
};
