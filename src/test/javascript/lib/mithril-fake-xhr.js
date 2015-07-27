'use strict';

module.exports = function(base) {

  base = base || window;
  var SaveXML = base.XMLHttpRequest; 
  var pending = {};
  var unexpectedRequests = 0;
    
  base.XMLHttpRequest = (function() {

    function select(ctx, cb) {
      Object.keys(pending).forEach(function(key){
        var match = new RegExp('^'+key+'$').exec(ctx.method.toLowerCase() + ctx.url);
        if (match && match.length) {
          ctx.pending = pending[key];
          cb(pending[key],ctx);
        }
      });
    }

    return function() {
      this.$headers = {};
      this.setRequestHeader = function(key, value) {
        this.$headers[key] = value;
      };
      this.open = function(method, url, async, user, password) {
        this.method = method;
        this.url = url;
        select(this, function(pending){
          if (pending.passthrough){
            pending.xhr = new SaveXML();
            return pending.xhr.open(method, url, async, user, password);
          }
        });
      };
      this.send = function(data) {
        var xhr = this, request = this.pending;
        xhr.readyState = 4;
        xhr.status = 400;
        xhr.responseText='';
        if (request && (request.data === undefined || JSON.stringify(request.data) === data)) {
          if (request.passthrough){
            var passthroughXhr = request.xhr;
            passthroughXhr.onreadystatechange = function() {
              if (passthroughXhr.readyState === 4) {
                var response = request.passthroughCb(passthroughXhr.status,JSON.parse(passthroughXhr.responseText));
                request.response(response.data);
                xhr.status = response.status;
                xhr.responseText = JSON.stringify(response.data);
                xhr.readyState = 4;
                xhr.onreadystatechange();
              }
            };
            passthroughXhr.$headers = xhr.$headers;
            passthroughXhr.send(data);
          }
          else {
            xhr.status = request.status();
            xhr.responseText = JSON.stringify(request.response());
            xhr.onreadystatechange();
          }
        }
        else {
          fake.unexpectedRequests += 1;
          xhr.onreadystatechange();
        }
      };
    };

  }());

  var fake = function(method, url, data, headers) {

    var key = method.toLowerCase()+url;
    var prop = m.prop('');
    var count = 0;
    var status = 200;
    var response = function() {
      count ++;
      return prop.apply(this,arguments);
    };

    var api = {
      respondWith: function() {
        switch (arguments.length) {
          case 0: prop(''); break;
          case 1: prop(arguments[0]); break;
          default: {
            status = arguments[0];
            prop(arguments[1]);
            break;
          }
        }
        return api;
      },
      response:response,
      passthrough: function(cb) {
        pending[key].passthrough = true;
        pending[key].passthroughCb = cb || function(s,d){return {status:s,data:d};};
        return api;
      },
      get count() {
        return count;
      }
    };
    
    pending[key] = {
      status: function(){
        return status;
      },
      response: function(){
        return api.response.apply(this,arguments);
      },
      data:data
    };
    return api;
  };

  Object.defineProperty(fake, 'unexpectedRequests', {
      get: function() {
        // read once semantics
        var count = unexpectedRequests;
        unexpectedRequests = 0;
        return count;
      },
      set: function(val) {
          unexpectedRequests++;
      }
  });

  fake.reset = function() {
    pending = {};
    unexpectedRequests = 0;
  };
  
  return fake;

};