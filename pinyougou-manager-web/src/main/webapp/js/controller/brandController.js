app.controller("brandController", function ($scope, $controller, baseService) {
    /** 指定继承baseController */
    $controller('baseController', {$scope: $scope});

    /** 查询所有品牌 */
    $scope.findAll = function () {
        //发送异步请求
        baseService.sendGet('/brand/findAll').then(function (response) {
            $scope.brandList = response.data;
        });
    };
    /** 查询列表 */
    $scope.search = function (page, rows) {
        baseService.findByPage("/brand/findByPage", page, rows, $scope.serchEntity)
            .then(function (response) {
                console.log(response);
                $scope.dataList = response.data.rows;
                $scope.paginationConf.totalItems = response.data.total;
            });
    };
    /** 添加品牌 */
    $scope.saveOrUpdate = function () {
        /** 定义请求URL */
        var url = "save"; // 添加品牌
        if ($scope.entity.id != null) {
            url = "update"; // 修改品牌
        }
        baseService.sendPost("/brand/" + url, $scope.entity)
            .then(function (response) {
                if (response.data) {
                    /** 重新加载品牌数据 */
                    $scope.reload();
                } else {
                    alert("操作失败！");
                }
            });
    };
    $scope.delete = function () {
        console.log($scope.ids);
        if ($scope.ids.length > 0) {
            baseService.deleteById("/brand/delete", $scope.ids)
                .then(function (response) {
                    if (response.data) {
                        $scope.reload();
                    }
                });
        } else {
            alert("请选择要删除的品牌！");
        }
    }
});