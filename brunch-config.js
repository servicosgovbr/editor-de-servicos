module.exports.config = {

  paths: {
    watched: [
      'src/main/javascript/',
      'src/main/resources/vendor',
      'src/main/assets/stylesheets'
    ],
    public: 'src/main/resources/static/editar/assets/'
  },

  files: {
    javascripts: {
      joinTo: {
        'app.js': new RegExp('^src/main/javascript/'),
        'vendor.js': new RegExp('^(?!src/main/javascript/)'),
      }
    },
    stylesheets: {
      defaultExtension: 'scss',
      joinTo: {
        'novo.css': new RegExp('^src/main/assets/stylesheets/.*\\.scss')
      }
    }
  },

  conventions: {
    assets: function() { return false; }
  },

  modules: {
    nameCleaner: function(path) {
      return path.replace(new RegExp('^src/main/javascript/'), '');
    }
  },

  plugins: {
    jshint: {
      pattern: new RegExp('^src/main/javascript/.*\\.js$'),
    },
    sass: {
      debug: 'comments'
    },
    cleanCss: {
      removeEmpty: true
    }
  }
};
