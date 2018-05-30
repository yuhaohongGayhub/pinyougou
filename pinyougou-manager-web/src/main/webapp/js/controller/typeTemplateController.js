/** 定义控制器层 */
app.controller('typeTemplateController', function ($scope, $controller, baseService) {

    /** 指定继承baseController */
    $controller('baseController', {$scope: $scope});

    /** 查询全部 */
    $scope.findAll = function () {
        baseService.sendGet("/typeTemplate/findAll").then(function (response) {
            $scope.dataList = response.data;
        });
    };

    /** 定义搜索对象 */
    $scope.searchEntity = {};
    /** 分页查询 */
    $scope.search = function (page, rows) {
        baseService.findByPage("/typeTemplate/findByPage", page,
            rows, $scope.searchEntity)
            .then(function (response) {
                $scope.dataList = response.data.rows;
                /** 更新总记录数 */
                $scope.paginationConf.totalItems = response.data.total;
            });
    };

    /** 添加或修改 */
    $scope.saveOrUpdate = function () {
        var url = "save";
        if ($scope.entity.id) {
            url = "update";
        }
        /** 发送post请求 */
        baseService.sendPost("/typeTemplate/" + url, $scope.entity)
            .then(function (response) {
                if (response.data) {
                    /** 重新加载数据 */
                    $scope.reload();
                } else {
                    alert("操作失败！");
                }
            });
    };

    /** 显示修改 */
    $scope.show = function (entity) {
        console.log(entity);
        $scope.entity = JSON.parse(JSON.stringify(entity));
        $scope.entity.brandIds = JSON.parse($scope.entity.brandIds);
        $scope.entity.specIds = JSON.parse($scope.entity.specIds);
        $scope.entity.customAttributeItems = JSON.parse($scope.entity.customAttributeItems);
    };

    /** 批量删除 */
    $scope.delete = function () {
        if ($scope.ids.length > 0) {
            baseService.deleteById("/typeTemplate/delete", $scope.ids)
                .then(function (response) {
                    if (response.data) {
                        $scope.reload();
                    } else {
                        alert("删除失败！");
                    }
                });
        }
    };

    $scope.findBrandList = function () {
        baseService.sendGet("/brand/selectBrandList").then(function (response) {
            $scope.brandList = {data: response.data};
        });
    }

    $scope.findSpecList = function () {
        baseService.sendGet("/specification/selectSpecList")
            .then(function (response) {
                $scope.specList = {data: response.data};
            });
    };

    $scope.addTableRow = function () {
        $scope.entity.customAttributeItems.push({});
    };

    $scope.deleteTabRow = function (rowIndex) {
        $scope.entity.customAttributeItems.splice(rowIndex, 1);
    }
});