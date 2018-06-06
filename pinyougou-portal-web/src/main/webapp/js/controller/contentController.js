app.controller("contentController", function ($scope, baseService) {
    $scope.findContentByCategoryId = function (categoryId) {
        baseService.sendGet("/content/findContentByCategoryId?categoryId=" + categoryId)
            .then(function (response) {
                $scope.contentList = response.data;
            });
    };
});