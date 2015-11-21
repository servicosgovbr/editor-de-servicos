'use strict';

module.exports = function (elementId) {
    var selector = document.getElementById(elementId);
    jQuery(selector).mask('999.999.999-99');
};


