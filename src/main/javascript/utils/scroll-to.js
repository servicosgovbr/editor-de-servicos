'use strict';

module.exports = function (seletor) {
  return function (element, isInitialized) {
    if (isInitialized) {
      return;
    }

    jQuery(element).click(function (e) {
      m.startComputation();
      jQuery(window).scrollTo(seletor, 250, {
        offset: -48 /* header */ - 8 /* 0.5em */
      });
      m.endComputation();

      e.stopPropagation();
      return false;
    });
  };

};
