// This Gruntfile.js is a modified version based on the file generated
// on 2016-01-13 using generator-jhipster 2.26.2

'use strict';
var fs = require('fs');

var parseString = require('xml2js').parseString;
// Returns the second occurence of the version number
var parseVersionFromPomXml = function() {
    var version;
    var pomXml = fs.readFileSync('pom.xml', "utf8");
    parseString(pomXml, function (err, result){
        if (result.project.version && result.project.version[0]) {
            version = result.project.version[0];
        } else if (result.project.parent && result.project.parent[0] && result.project.parent[0].version && result.project.parent[0].version[0]) {
            version = result.project.parent[0].version[0]
        } else {
            throw new Error('pom.xml is malformed. No version is defined');
        }
    });
    return version;
};

// usemin custom step
var useminAutoprefixer = {
    name: 'autoprefixer',
    createConfig: function(context, block) {
        if(block.src.length === 0) {
            return {};
        } else {
            return require('grunt-usemin/lib/config/cssmin').createConfig(context, block) // Reuse cssmins createConfig
        }
    }
};

module.exports = function (grunt) {
    require('load-grunt-tasks')(grunt);
    require('time-grunt')(grunt);

    grunt.initConfig({
        yeoman: {
            // configurable paths
            app: require('./bower.json').appPath || 'app',
            dist: 'src/main/webapp/dist'
        },
        watch: {
            injector: {
                files: ['src/main/webapp/scripts/**/*.js'],
                tasks: ['injector']
            },
            bower: {
                files: ['bower.json'],
                tasks: ['wiredep']
            },
            ngconstant: {
                files: ['Gruntfile.js', 'pom.xml'],
                tasks: ['ngconstant:dev']
            },
            sass: {
                files: ['src/main/scss/**/*.{scss,sass}'],
                tasks: ['sass:server']
            }
        },
        autoprefixer: {
            // src and dest is configured in a subtask called "generated" by usemin
        },
        wiredep: {
            app: {
                src: ['src/main/webapp/index.html'],
                exclude: [
                    /angular-i18n/  // localizations are loaded dynamically
                ]
            }
        },
        browserSync: {
            dev: {
                bsFiles: {
                    src : [
                        'src/main/webapp/**/*.html',
                        'src/main/webapp/**/*.json',
                        'src/main/webapp/assets/styles/**/*.css',
                        'src/main/webapp/scripts/**/*.{js,html}',
                        'src/main/webapp/assets/images/**/*.{png,jpg,jpeg,gif,webp,svg}',
                        'tmp/**/*.{css,js}'
                    ]
                }
            },
            options: {
                watchTask: true,
                proxy: "localhost:8080"
            }
        },
        clean: {
            dist: {
                files: [{
                    dot: true,
                    src: [
                        '.tmp',
                        '<%= yeoman.dist %>/*',
                        '!<%= yeoman.dist %>/.git*'
                    ]
                }]
            },
            server: '.tmp'
        },
        jshint: {
            options: {
                jshintrc: '.jshintrc'
            },
            all: [
                'Gruntfile.js',
                'src/main/webapp/scripts/app.js',
                'src/main/webapp/scripts/app/**/*.js',
                'src/main/webapp/scripts/components/**/*.js'
            ]
        },
        sass: {
            options: {
                includePaths: [
                    'src/main/webapp/bower_components'
                ]
            },
            server: {
                files: [{
                    expand: true,
                    cwd: 'src/main/scss',
                    src: ['*.scss'],
                    dest: 'src/main/webapp/assets/styles',
                    ext: '.css'
                }]
            }
        },
        concat: {
            // src and dest is configured in a subtask called "generated" by usemin
        },
        uglifyjs: {
            // src and dest is configured in a subtask called "generated" by usemin
        },
        rev: {
            dist: {
                files: {
                    src: [
                        '<%= yeoman.dist %>/scripts/**/*.js',
                        '<%= yeoman.dist %>/assets/styles/**/*.css',
                        '<%= yeoman.dist %>/assets/images/**/*.{png,jpg,jpeg,gif,webp,svg}',
                        '<%= yeoman.dist %>/assets/fonts/bootstrap/*'
                    ]
                }
            }
        },
        useminPrepare: {
            html: 'src/main/webapp/**/*.html',
            options: {
                dest: '<%= yeoman.dist %>',
                flow: {
                    html: {
                        steps: {
                            js: ['concat', 'uglifyjs'],
                            css: ['cssmin', useminAutoprefixer] // Let cssmin concat files so it corrects relative paths to fonts and images
                        },
                            post: {}
                        }
                    }
            }
        },
        usemin: {
            html: ['<%= yeoman.dist %>/**/*.html'],
            css: ['<%= yeoman.dist %>/assets/styles/**/*.css'],
            js: ['<%= yeoman.dist %>/scripts/**/*.js'],
            options: {
                assetsDirs: ['<%= yeoman.dist %>', '<%= yeoman.dist %>/assets/styles', '<%= yeoman.dist %>/assets/images', '<%= yeoman.dist %>/assets/fonts'],
                patterns: {
                    js: [
                        [/(assets\/images\/.*?\.(?:gif|jpeg|jpg|png|webp|svg))/gm, 'Update the JS to reference our revved images']
                    ]
                },
                dirs: ['<%= yeoman.dist %>']
            }
        },
        imagemin: {
            dist: {
                files: [{
                    expand: true,
                    cwd: 'src/main/webapp/assets/images',
                    src: '**/*.{jpg,jpeg}', // we don't optimize PNG files as it doesn't work on Linux. If you are not on Linux, feel free to use '**/*.{png,jpg,jpeg}'
                    dest: '<%= yeoman.dist %>/assets/images'
                }]
            }
        },
        svgmin: {
            dist: {
                files: [{
                    expand: true,
                    cwd: 'src/main/webapp/assets/images',
                    src: '**/*.svg',
                    dest: '<%= yeoman.dist %>/assets/images'
                }]
            }
        },
        cssmin: {
            // src and dest is configured in a subtask called "generated" by usemin
        },
        ngtemplates:    {
            dist: {
                cwd: 'src/main/webapp',
                src: ['scripts/app/**/*.html', 'scripts/components/**/*.html',],
                dest: '.tmp/templates/templates.js',
                options: {
                    module: 'silq2App',
                    usemin: 'scripts/app.js',
                    htmlmin: '<%= htmlmin.dist.options %>'
                }
            }
        },
        htmlmin: {
            dist: {
                options: {
                    removeCommentsFromCDATA: true,
                    // https://github.com/yeoman/grunt-usemin/issues/44
                    collapseWhitespace: true,
                    collapseBooleanAttributes: true,
                    conservativeCollapse: true,
                    removeAttributeQuotes: true,
                    removeRedundantAttributes: true,
                    useShortDoctype: true,
                    removeEmptyAttributes: true,
                    keepClosingSlash: true
                },
                files: [{
                    expand: true,
                    cwd: '<%= yeoman.dist %>',
                    src: ['*.html'],
                    dest: '<%= yeoman.dist %>'
                }]
            }
        },
        // Put files not handled in other tasks here
        copy: {
            dist: {
                files: [{
                    expand: true,
                    dot: true,
                    cwd: 'src/main/webapp',
                    dest: '<%= yeoman.dist %>',
                    src: [
                        '*.html',
                        'scripts/**/*.html',
                        'assets/images/**/*.{png,gif,webp,jpg,jpeg,svg}',
                        'assets/fonts/**'
                    ]
                }, {
                    expand: true,
                    cwd: '.tmp/assets/images',
                    dest: '<%= yeoman.dist %>/assets/images',
                    src: [
                        'generated/*'
                    ]
                }]
            },
            generateOpenshiftDirectory: {
                    expand: true,
                    dest: 'deploy/openshift',
                    src: [
                        'pom.xml',
                        'src/main/**'
                ]
            }
        },
        ngAnnotate: {
            dist: {
                files: [{
                    expand: true,
                    cwd: '.tmp/concat/scripts',
                    src: '*.js',
                    dest: '.tmp/concat/scripts'
                }]
            }
        },
        buildcontrol: {
            options: {
                commit: true,
                push: false,
                connectCommits: false,
                message: 'Built %sourceName% from commit %sourceCommit% on branch %sourceBranch%'
            },
            openshift: {
                options: {
                    dir: 'deploy/openshift',
                    remote: 'openshift',
                    branch: 'master'
                }
            }
        },
        ngconstant: {
            options: {
                name: 'silq2App',
                deps: false,
                wrap: '"use strict";\n// DO NOT EDIT THIS FILE, EDIT THE GRUNT TASK NGCONSTANT SETTINGS INSTEAD WHICH GENERATES THIS FILE\n{%= __ngModule %}'
            },
            dev: {
                options: {
                    dest: 'src/main/webapp/scripts/app/app.constants.js'
                },
                constants: {
                    ENV: 'dev',
                    VERSION: parseVersionFromPomXml(),
                    LANDING_PAGE: 'home'
                }
            },
            prod: {
                options: {
                    dest: '.tmp/scripts/app/app.constants.js'
                },
                constants: {
                    ENV: 'prod',
                    VERSION: parseVersionFromPomXml()
                }
            }
        },
        injector: {
            options: {
                relative: true,
                addRootSlash: false
            },
            scripts: {
                files: {
                    'src/main/webapp/index.html': ['src/main/webapp/scripts/**/*.js']
                }
            },
        },
        protractor_webdriver: {
            options: {},
            all: {},
          },
        protractor: {
            options: {
              configFile: 'src/test/javascript/protractor.conf.js',
              keepAlive: false,
              noColor: false,
              args: {}
            },
            all: {}
        },
    });

    grunt.registerTask('serve', [
        'clean:server',
        'wiredep',
        'injector',
        'ngconstant:dev',
        'sass:server',
        'browserSync',
        'watch'
    ]);

    grunt.registerTask('server', function (target) {
        grunt.log.warn('The `server` task has been deprecated. Use `grunt serve` to start a server.');
        grunt.task.run([target ? ('serve:' + target) : 'serve']);
    });

    grunt.registerTask('test', [
        'protractor_webdriver',
        'protractor'
    ]);

    grunt.registerTask('build', [
        'clean:dist',
        'wiredep:app',
        'injector',
        'ngconstant:prod',
        'useminPrepare',
        'ngtemplates',
        'sass:server',
        'imagemin',
        'svgmin',
        'concat',
        'copy',
        'ngAnnotate',
        'cssmin',
        'autoprefixer',
        'uglify',
        'rev',
        'usemin',
        'htmlmin'
    ]);

    grunt.registerTask('buildOpenshift', [
        'build',
        'copy:generateOpenshiftDirectory',
    ]);

    grunt.registerTask('deployOpenshift', [
        'build',
        'copy:generateOpenshiftDirectory',
        'buildcontrol:openshift'
    ]);


    grunt.registerTask('default', ['serve']);
};
