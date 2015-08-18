'use strict';

module.exports = function(ctrl) {
  return function(element, isInitialized) {
    if(!isInitialized && ctrl.adicionado) { 
  	  element.focus(); 
  	  ctrl.adicionado = false;
    }
  };
};