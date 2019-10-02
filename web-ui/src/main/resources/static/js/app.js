/*global Vue, VueResource */
(function() {
    'use strict';
    var svgNS = 'http://www.w3.org/2000/svg';
    Vue.use(VueResource);
    new Vue({
        el: '#app',

        data: function() {
            return {
                animation: undefined,
                animationDurationValueIndex: 2,
                animationDurationValues: [0, 1000, 5000, 10000],
                animationEasing: undefined,
                animationEasingValues: [
                    'ease',
                    'linear',
                    'ease-in',
                    'ease-out',
                    'ease-in-out',
                    'cubic-bezier(0.1,0.7,1.0,0.1)',
                ],
                animationItems: [
                    {
                        text: 'bounce',
                        value: ['bounceIn', 'bounceOut'],
                    },
                    {
                        text: 'fade',
                        value: ['fadeIn', 'fadeOut'],
                    },
                    {
                        text: 'flipX',
                        value: ['flipInX', 'flipOutX'],
                    },
                    {
                        text: 'flipY',
                        value: ['flipInY', 'flipOutY'],
                    },
                    {
                        text: 'rotate',
                        value: ['rotateIn', 'rotateOut'],
                    },
                    {
                        text: 'zoom',
                        value: ['zoomIn', 'zoomOut'],
                    },
                ],
                animationOverlapValueIndex: 1,
                animationOverlapValues: [0, 1/5, 1/2, 1],
                colorItemIndex: undefined,
                colorItems: [
                    ['#1A367A', '#2C66A6', '#5BB6A5', '#FF0053'],
                    ['#00BFFE', '#3D1159', '#A30320', '#00253E'],
                    ['#FF0053', '#5056B8', '#3DD3AD', '#00A3E1'],
                    ['#f27062', '#1ab9a5', '#2e3092', '#009fdf', '#f7dc5f'],
                ],
                drawer: true,
                fontFamily: undefined,
                fontFamilyValues: [
                    'Abril Fatface',
                    'Annie Use Your Telescope',
                    'Anton',
                    'Bahiana',
                    'Baloo Bhaijaan',
                    'Barrio',
                    'Finger Paint',
                    'Fredericka the Great',
                    'Gloria Hallelujah',
                    'Indie Flower',
                    'Life Savers',
                    'Londrina Sketch',
                    'Love Ya Like A Sister',
                    'Merienda',
                    'Nothing You Could Do',
                    'Pacifico',
                    'Quicksand',
                    'Righteous',
                    'Sacramento',
                    'Shadows Into Light',
                ],
                fontSizeRatioValueIndex: 0,
                fontSizeRatioValues: [0, 1/20, 1/5, 1/2, 1],
                progress: undefined,
                progressVisible: true,
                rotationItemIndex: undefined,
                rotationItems: [
                    {
                        value: 0,
                        svg: (function() {
                            var div = document.createElement('div');
                            div.appendChild((function() {
                                var svg = document.createElementNS(svgNS, 'svg');
                                svg.setAttribute('viewBox', '0 0 12 12');
                                svg.appendChild((function() {
                                    var path = document.createElementNS(svgNS, 'path');
                                    path.setAttribute('d', 'M0 7 L0 5 L12 5 L12 7 Z');
                                    return path;
                                })());
                                return svg;
                            })());
                            return URL.createObjectURL(new Blob([div.innerHTML]));
                        })(),
                    },
                    {
                        value: 7/8,
                        svg: (function() {
                            var div = document.createElement('div');
                            div.appendChild((function() {
                                var svg = document.createElementNS(svgNS, 'svg');
                                svg.setAttribute('viewBox', '0 0 12 12');
                                svg.appendChild((function() {
                                    var path = document.createElementNS(svgNS, 'path');
                                    path.setAttribute('d', 'M0 7 L0 5 L12 5 L12 7 Z');
                                    path.setAttribute('transform', 'rotate(315 6 6)');
                                    return path;
                                })());
                                return svg;
                            })());
                            return URL.createObjectURL(new Blob([div.innerHTML]));
                        })(),
                    },
                    {
                        value: function(word) {
                            var chance = new Chance(word[0]);
                            return chance.pickone([0, 3/4]);
                        },
                        svg: (function() {
                            var div = document.createElement('div');
                            div.appendChild((function() {
                                var svg = document.createElementNS(svgNS, 'svg');
                                svg.setAttribute('viewBox', '0 0 12 12');
                                svg.appendChild((function() {
                                    var path = document.createElementNS(svgNS, 'path');
                                    path.setAttribute('d', 'M0 7 L0 5 L12 5 L12 7 Z');
                                    return path;
                                })());
                                svg.appendChild((function() {
                                    var path = document.createElementNS(svgNS, 'path');
                                    path.setAttribute('d', 'M0 7 L0 5 L12 5 L12 7 Z');
                                    path.setAttribute('transform', 'rotate(90 6 6)');
                                    return path;
                                })());
                                return svg;
                            })());
                            return URL.createObjectURL(new Blob([div.innerHTML]));
                        })(),
                    },
                    {
                        value: function(word) {
                            var chance = new Chance(word[0]);
                            return chance.pickone([0, 1/8, 3/4, 7/8]);
                        },
                        svg: (function() {
                            var div = document.createElement('div');
                            div.appendChild((function() {
                                var svg = document.createElementNS(svgNS, 'svg');
                                svg.setAttribute('viewBox', '0 0 12 12');
                                svg.appendChild((function() {
                                    var path = document.createElementNS(svgNS, 'path');
                                    path.setAttribute('d', 'M0 7 L0 5 L12 5 L12 7 Z');
                                    return path;
                                })());
                                svg.appendChild((function() {
                                    var path = document.createElementNS(svgNS, 'path');
                                    path.setAttribute('d', 'M0 7 L0 5 L12 5 L12 7 Z');
                                    path.setAttribute('transform', 'rotate(45 6 6)');
                                    return path;
                                })());
                                svg.appendChild((function() {
                                    var path = document.createElementNS(svgNS, 'path');
                                    path.setAttribute('d', 'M0 7 L0 5 L12 5 L12 7 Z');
                                    path.setAttribute('transform', 'rotate(90 6 6)');
                                    return path;
                                })());
                                svg.appendChild((function() {
                                    var path = document.createElementNS(svgNS, 'path');
                                    path.setAttribute('d', 'M0 7 L0 5 L12 5 L12 7 Z');
                                    path.setAttribute('transform', 'rotate(315 6 6)');
                                    return path;
                                })());
                                return svg;
                            })());
                            return URL.createObjectURL(new Blob([div.innerHTML]));
                        })(),
                    },
                    {
                        value: function(word) {
                            var chance = new Chance(word[0]);
                            return chance.random();
                        },
                        svg: (function() {
                            var div = document.createElement('div');
                            div.appendChild((function() {
                                var svg = document.createElementNS(svgNS, 'svg');
                                svg.setAttribute('viewBox', '0 0 2 2');
                                svg.appendChild((function() {
                                    var circle = document.createElementNS(svgNS, 'circle');
                                    circle.setAttribute('cx', 1);
                                    circle.setAttribute('cy', 1);
                                    circle.setAttribute('r', 1);
                                    return circle;
                                })());
                                return svg;
                            })());
                            return URL.createObjectURL(new Blob([div.innerHTML]));
                        })(),
                    }
                ],
                snackbarText: '',
                snackbarVisible: false,
                spacingValueIndex: 1,
                spacingValues: [0, 1/4, 1/2, 1, 2],
                timer: '',
                wordsText: [],
                wordsTextArea: '',
            };
        },

        computed: {
            animationDuration: function() {
                return this.animationDurationValues[this.animationDurationValueIndex];
            },

            animationOverlap: function() {
                return this.animationOverlapValues[this.animationOverlapValueIndex];
            },

            color: function() {
                var colors = this.colorItems[this.colorItemIndex];
                return function() {
                    return chance.pickone(colors);
                };
            },

            enterAnimation: function() {
                return ['animated', this.animation[0]].join(' ');
            },

            fontSizeRatio: function() {
                return this.fontSizeRatioValues[this.fontSizeRatioValueIndex];
            },

            leaveAnimation: function() {
                return ['animated', this.animation[1]].join(' ');
            },

            rotation: function() {
                var item = this.rotationItems[this.rotationItemIndex];
                return item.value;
            },

            spacing: function() {
                return this.spacingValues[this.spacingValueIndex];
            },

            words: function() {
                return this.wordsText;
            },
        },

        watch: {
            progress: function(currentProgress, previousProgress) {
                if (previousProgress) {
                    this.progressVisible = false;
                }
            },
        },

        created: function() {
            this.generateWordsText("/words");
            this.randomizeSelections();
            //this.timer = setInterval(this.generateWordsText, 30000)
        },

        beforeDestroy () {
            clearInterval(this.timer)
        },

        methods: {
            randomizeSelections: function() {
                this.animation = chance.pickone(this.animationItems).value;
                this.animationEasing = chance.pickone(this.animationEasingValues);
                this.colorItemIndex = chance.integer({min: 0, max: this.colorItems.length - 1});
                this.fontFamily = chance.pickone(this.fontFamilyValues);
                this.rotationItemIndex = chance.integer({min: 0, max: this.rotationItems.length - 1});
            },

            generateWordsText: function (value) {
                this.wordsTextArea = "";
                this.wordsText = [];

                Vue.http.get(value).then(response => {
                    if (response.status === 200) {
                        const list = JSON.parse(response.bodyText);
                        list.forEach(item => {
                            this.wordsText.push({text: item.text, weight: item.weight});
                            this.wordsTextArea += item.text + ' ' + item.weight + '\n';
                        });
                        this.randomizeSelections()
                    }
                }, response => {
                    if (response.status === 404) {
                        console.warn("generateWordsText|/words is offline");
                    }
                });

            },

            loadFont: function(fontFamily, fontStyle, fontWeight, text) {
                return (new FontFaceObserver(fontFamily, {style: fontStyle, weight: fontWeight})).load(text);
            },
        },
    });

})();
