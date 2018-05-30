/** 定义控制器层 */
app.controller('goodsController', function ($scope, $controller, baseService, uploadService) {

    /** 指定继承baseController */
    $controller('baseController', {$scope: $scope});

    /** 查询全部 */
    $scope.findAll = function () {
        baseService.sendGet("/goods/findAll").then(function (response) {
            $scope.dataList = response.data;
        });
    };

    $scope.uploadFile = function () {
        uploadService.uploadFile().then(function (response) {
            if (response.data.status == 200) {
                $scope.image_entity.url = response.data.url;
            } else {
                alert("上传失败！");
            }
        });
    };

    //------------------------------------------------------

    $scope.findItemCatByParentId = function (parentId, itemCatListName) {
        baseService.sendGet("/itemCat/findItemCatByParentId?parentId=" + parentId).then(function (response) {
            $scope[itemCatListName] = response.data;
        });
    };

    /** 监听一级分类发生变化 */
    $scope.$watch("goods.category1Id", function (newValue, oldValue) {
        if (newValue) {//如果不等于undefined
            $scope.findItemCatByParentId(newValue, 'itemCatList2');
        } else {//如果没有选中
            $scope.itemCatList2 = null;
            $scope.goods.category2Id = null;
        }
    });

    /** 监听二级分类发生变化 */
    $scope.$watch("goods.category2Id", function (newValue, oldValue) {
        if (newValue) {//主动选中值
            $scope.findItemCatByParentId(newValue, 'itemCatList3');
        } else {//未选中
            $scope.itemCatList3 = null;
            $scope.goods.category3Id = null;
        }
    });

    /** 监听三级分类发生变化 */
    $scope.$watch("goods.category3Id", function (newValue, oldValue) {
        if (newValue) {//如果选中
            var temp;
            for (var i = 0; i < $scope.itemCatList3.length; i++) {
                if ($scope.itemCatList3[i].id == newValue) {
                    temp = $scope.itemCatList3[i];
                    break;
                }
            }
            $scope.goods.typeTemplateId = temp.typeId;
        } else {//如果没选中
            $scope.goods.typeTemplateId = "";
        }
    });

    /** 监听typeTemplateId */
    $scope.$watch("goods.typeTemplateId", function (newValue, oldValue) {
        if (newValue) {//如果有id
            baseService.findOne("/typeTemplate/findOne", newValue).then(function (response) {
                var typeTemplate = response.data;
                //brandIds: "[{\"id\":1,\"text\":\"联想\"},{\"id\":3,\"text\":\"三星\"}]
                $scope.typeTemplate = typeTemplate;
                $scope.typeTemplate.brandIds = JSON.parse(typeTemplate.brandIds);
                //customAttributeItems: "[{\"text\":\"分辨率\"},{\"text\":\"摄像头\"}]"
                $scope.goods.goodsDesc.customAttributeItems = JSON.parse(typeTemplate.customAttributeItems);
                //specIds: "[{\"id\":27,\"text\":\"网络\"},{\"id\":32,\"text\":\"机身内存\"}]"
            });
            /** 查询该模版对应的规格与规格选项 */
            baseService.findOne("/typeTemplate/findSpecByTemplateId", newValue).then(function (response) {
                $scope.specList = response.data;
            });
        } else {//没有模板id
            $scope.typeTemplate = null;
            $scope.goods.brandId = null;
        }
    });

    //--------------------------------------------------------

    $scope.remove_image_entity = function (index) {
        $scope.goods.goodsDesc.itemImages.splice(index, 1);
    }

    $scope.goods = {goodsDesc: {itemImages: []}};
    $scope.add_image_entity = function () {
        $scope.goods.goodsDesc.itemImages.push($scope.image_entity);
    };


    /** 定义搜索对象 */
    $scope.searchEntity = {};
    /** 分页查询 */
    $scope.search = function (page, rows) {
        baseService.findByPage("/goods/findByPage", page,
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
        if ($scope.goods.id) {
            url = "update";
        }
        $scope.goods.goodsDesc.introduction = editor.html();

        /** 发送post请求 */
        baseService.sendPost("/goods/" + url, $scope.goods)
            .then(function (response) {
                if (response.data) {
                    /** 重新加载数据 */
                    alert("添加成功!");
                    $scope.goods = {};
                    editor.html("");
                } else {
                    alert("操作失败！");
                }
            });
    };


    /** 显示修改 */
    $scope.show = function (entity) {
        $scope.entity = entity;
    };


    /** 批量删除 */
    $scope.delete = function () {
        if ($scope.ids.length > 0) {
            baseService.deleteById("/goods/delete", $scope.ids)
                .then(function (response) {
                    if (response.data) {
                        $scope.reload();
                    } else {
                        alert("删除失败！");
                    }
                });
        }
    };

});