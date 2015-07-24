module.exports.config = {

  paths: {
    watched: [
      'src/main/javascript/',
      'src/main/resources/vendor'
    ],
    public: 'src/main/resources/static/editar/assets/'
  },

  files: {
    javascripts: {
      joinTo: 'app.js'
    }
  },

  modules: {
    nameCleaner: function(path) {
      return path.replace(new RegExp('^src/main/javascript/'), '');
    }
  },

  plugins: {
    jshint: {
      pattern: new RegExp('^src/main/javascript/.*\\.js$'),
    }
  }
};
