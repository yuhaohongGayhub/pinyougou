/** 定义控制器层 */
app.controller('specificationController', function ($scope, $controller, baseService) {

    /** 指定继承baseController */
    $controller('baseController', {$scope: $scope});

    /** 查询全部 */
    $scope.findAll = function () {
        baseService.sendGet("/specification/findAll").then(function (response) {
            $scope.dataList = response.data;
        });
    };

    /** 定义搜索对象 */
    $scope.searchEntity = {};

    /** 分页查询 */
    $scope.search = function (page, rows) {
        baseService.findByPage("/specification/findByPage", page,
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
        baseService.sendPost("/specification/" + url, $scope.entity)
            .then(function (response) {
                if (response.data) {
                    /** 重新加载数据 */
                    $scope.reload();
                } else {
                    alert("操作失败！");
                }
            });
    };

    /** 批量删除 */
    $scope.delete = function () {
        if ($scope.ids.length > 0) {
            baseService.deleteById("/specification/delete", $scope.ids)
                .then(function (response) {
                    if (response.data) {
                        $scope.reload();
                    } else {
                        alert("删除失败！");
                    }
                });
        }
    };


    /** 添加一行 */
    $scope.addTableRow = function () {
        $scope.entity.specificationOptions.push({});
    }

    /** 删除添加页面里的规格项 */
    $scope.deleteTableRow = function (index) {
        $scope.entity.specificationOptions.splice(index, 1);
    }

    /** 显示编辑页面 */
    $scope.show = function (entity) {
        var temp = JSON.parse(JSON.stringify(entity));
        $scope.entity = temp;
        baseService.findOne("/specification/findOne", entity.id).then(function (response) {
            $scope.entity.specificationOptions = response.data;
        });
    };
});