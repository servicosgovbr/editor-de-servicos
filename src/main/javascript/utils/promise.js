'use strict';

function callIfFunction(fn) {
  if (_.isFunction(fn)) {
    fn();
  }
}

var pipeListen = _.curry(function (fn, first) {
  callIfFunction(fn);
  return first;
});

var rethrowListen = _.curry(function (fn, first) {
  callIfFunction(fn);
  throw first;
});

var pipe = pipeListen(null);
var rethrow = rethrowListen(null);

function onSuccOrError(promise, fn) {
  if (!_.isFunction(fn)) {
    throw '"fn" deve ser uma função';
  }
  return promise.then(pipeListen(fn), rethrowListen(fn));
}

function log(first) {
  window.console.log(first);
  return first;
}

function err(first) {
  window.console.error(first);
  throw first;
}

function _deferredPromise(fn) {
  var d = m.deferred();
  fn(d);
  return d.promise;
}

function rejected(first) {
  return _deferredPromise(function (d) {
    d.reject(first);
  });
}

function resolved(first) {
  return _deferredPromise(function (d) {
    d.resolve(first);
  });
}

module.exports = {
  pipe: pipe,
  rethrow: rethrow,
  log: log,
  err: err,
  rejected: rejected,
  resolved: resolved,
  onSuccOrError: onSuccOrError
};
