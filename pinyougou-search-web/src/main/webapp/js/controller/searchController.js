/** 定义搜索控制器 */
app.controller('searchController', function ($scope, baseService) {
    /** 定义搜索参数对象 */
    $scope.searchParam = {'keywords': '', 'category': '', 'brand': '', 'spec': {}};

    /** 定义搜索方法 */
    $scope.search = function () {
        baseService.sendPost("/Search", $scope.searchParam)
            .then(function (response) {
                    /** 获取搜索结果 */
                    $scope.resultMap = response.data;
                }
            );
    };

    $scope.addSearchItem = function (key, value) {
        if (key == 'category' || key == 'brand') {
            $scope.searchParam[key] = value;
        } else {
            $scope.searchParam.spec[key] = value;
        }
        /** 执行搜索 */
        $scope.search();
    };

    $scope.removeSearchItem = function (key) {
        if (key == 'category' || key == 'brand') {
            $scope.searchParam[key] = "";
        } else {
            delete $scope.searchParam.spec[key];
        }
        /** 执行搜索 */
        $scope.search();
    }
});