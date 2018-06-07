/** 定义控制器层 */
app.controller('itemCatController', function ($scope, $controller, baseService) {

    /** 指定继承baseController */
    $controller('baseController', {$scope: $scope});

    /** 定义变量记录父级id */
    $scope.parentId = 0;
    /** 根据父Id查询子分类 */
    $scope.findItemCatByParentId = function (parentId) {
        $scope.parentId = parentId;
        baseService.sendGet("/itemCat/findItemCatByParentId", "parentId=" + parentId)
            .then(function (response) {
                $scope.dataList = response.data;
            });
    };

    /** 加载下拉列表 */
    $scope.findTypeTemplateList = function () {
        baseService.sendGet("/itemCat/finTypeTemplateIdAndName")
            .then(function (response) {
                $scope.typeTemplateList = response.data;
            });
    };

    /** 更新缓存 */
    $scope.updateCache = function () {
        baseService.sendGet("/itemCat/updateCache")
            .then(function (response) {
                if (response.data) {
                    alert("同步缓存成功")
                } else {
                    alert("同步缓存失败")
                }
            });
    };

    $scope.level = 1;
    /** 点击下一级别搜索 */
    $scope.selectList = function (entity, level) {
        $scope.level = level;
        if (level == 1) { //首页
            $scope.entity_level2 = null;
            $scope.entity_level3 = null;
        } else if (level == 2) {
            $scope.entity_level2 = entity;
            $scope.entity_level3 = null;
        } else if (level == 3) {
            $scope.entity_level3 = entity;
        }
        $scope.findItemCatByParentId(entity.id);
    };
    /** 添加或修改 */
    $scope.saveOrUpdate = function () {
        var url = "save";
        if ($scope.itemCat.id) {
            url = "update";
        }
        $scope.itemCat.parentId = $scope.parentId;
        console.log($scope.itemCat);
        baseService.sendPost("/itemCat/" + url, $scope.itemCat)
            .then(function (response) {
                if (response.data) {
                    $scope.findItemCatByParentId($scope.parentId);
                } else {
                    alert("操作失败！");
                }
            });
    };

    /** 显示编辑页面 */
    $scope.showItemCat = function (itemCat) {
        var temp = JSON.parse(JSON.stringify(itemCat));
        $scope.itemCat = temp;
    };

    /** 批量删除 */
    $scope.delete = function () {
        if ($scope.ids.length > 0) {
            baseService.deleteById("/itemCat/delete", $scope.ids)
                .then(function (response) {
                    if (response.data) {
                        alert("删除成功!");
                        $scope.findItemCatByParentId($scope.parentId);
                        $scope.ids = [];
                    } else {
                        alert("删除失败！");
                    }
                });
        }
    };

});