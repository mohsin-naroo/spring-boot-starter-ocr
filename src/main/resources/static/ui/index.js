(function() {

    'use strict';

    var applicationName = "ApiApp";
    angular.module(applicationName, []);

    function ApiController($http, $scope) {
        var self = this;

        self.canvas = null;
        self.canvasContext = null;

        self.data = {
            language: '',
            mode: '',
            text: '',
            error: ''
        };

        self.init = function() {
            self.canvas = document.getElementById('webcanvas');
            self.canvasContext = self.canvas.getContext('2d');
            document.getElementById('file').addEventListener('change', self.fileChange);
        };

        self.fileChange = function($event) {
            if ($event.target.value) {
                var reader = new FileReader();
                reader.onload = function() {
                    const img = new Image();
                    img.onload = () => {
                        self.canvas.width = img.width;
                        self.canvas.height = img.height;
                        self.canvasContext.drawImage(img, 0, 0, self.canvas.width, self.canvas.height);
                        self.data.text = '';
                        self.data.error = '';
                        $scope.$apply();
                    };
                    img.src = reader.result;
                }
                reader.readAsDataURL($event.target.files[0]);
            }
        };

        self.extract = function() {
            self.data.text = '';
            self.data.error = '';
            self.canvas.toBlob(function(blob) {
                var formData = new FormData();
                formData.append('image', new File([blob], 'image.png', { type: 'image/png' }));
                formData.append('language', self.data.language);
                formData.append('mode', self.data.mode);
                $http({
                    method: 'POST',
                    url: '../api/v1/ocr/img2txt',
                    headers: { 'Content-Type': undefined },
                    data: formData
                }).then(function(response) {
                    self.data.text = response.data.text;
                }, function(response) {
                    self.data.error = response.data;
                });
            }, 'image/png');
        };

        self.init();
    }

    angular.module(applicationName).controller('ApiController', ['$http', '$scope', ApiController]);

}());