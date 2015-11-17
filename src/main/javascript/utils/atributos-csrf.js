'use strict';

module.exports = (function () {
  return {
    token: jQuery('meta[name=\'_csrf_token\']').attr('content'),
    name: jQuery('meta[name=\'_csrf_name\']').attr('content'),
    header: jQuery('meta[name=\'_csrf_header\']').attr('content')
  };
})();
