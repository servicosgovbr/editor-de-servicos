'use strict';

module.exports = {
  controller: function (args) {
    this.prop = args.prop;
    this.options = _.omit(args, 'prop');
  },

  view: function (ctrl) {
    return m('select', {
      config: function (element, isInitialized) {
        var el = jQuery(element);

        if (!isInitialized) {
          var select2 = el.select2(ctrl.options);

          select2.on('change', function (e) {
            if (ctrl.prop() === el.select2('val')) {
              e.stopPropagation();
              return false;
            }

            m.startComputation();
            ctrl.prop(el.select2('val'));
            m.endComputation();
          });
        }

        el.select2('val', ctrl.prop());
      }
    });
  }
};
