/** 定义控制器层 */
app.controller('goodsController', function ($scope, $controller, $location, baseService, uploadService) {
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

        /** 根据主键id查询单个商品 */
        $scope.findOne = function () {
            var id = $location.search().id;
            if (id) {
                baseService.findOne("/goods/findOne", id).then(function (response) {
                    //获取到商品对象
                    $scope.goods = response.data;
                    //富文本编辑内容
                    editor.html($scope.goods.goodsDesc.introduction);
                    //图片
                    $scope.goods.goodsDesc.itemImages = JSON.parse($scope.goods.goodsDesc.itemImages);
                    //扩展属性
                    $scope.goods.goodsDesc.customAttributeItems = JSON.parse($scope.goods.goodsDesc.customAttributeItems);
                    /** 把规格json字符串转化成数组 */
                    $scope.goods.goodsDesc.specificationItems =
                        JSON.parse($scope.goods.goodsDesc.specificationItems);
                    for (var i = 0; i < $scope.goods.items.length; i++) {
                        $scope.goods.items[i].spec = JSON.parse($scope.goods.items[i].spec);
                    }
                });
            }
        };
        $scope.checkAttribute = function ($event, specName, optionName) {
            //[{"attributeValue":["联通4G","移动4G","电信4G"],"attributeName":"网络"},
            // {"attributeValue":["64G","128G"],"attributeName":"机身内存"}]"
            var specificationItems = $scope.goods.goodsDesc.specificationItems;
            var obj = $scope.searchJsonByKey(specificationItems, 'attributeName', specName)
            if (obj) {
                var index = obj['attributeValue'].indexOf(optionName);
                return index >= 0;
            } else {
                return false;
            }
        };


        //------------------- start -----------------------------------
        $scope.goods = {
            goodsDesc: {
                specificationItems: [],
                itemImages: []
            }
        };
        /** 生成SKU列表 */
        $scope.createItems = function () {
            /** 定义SKU商品变量，并初始化 */
            /** spec:{"网络":"联通4G","机身内存":"64G"} */
            $scope.goods.items = [{spec: {}, price: 0, num: 9999, status: '0', isDefault: '0'}];
            /** 定义规格选项数组 */
            var specItems = $scope.goods.goodsDesc.specificationItems;
            /** 生成SKU列表 */
            /** [{"attributeName":"网络","attributeValue":["移动3G","移动4G"]},
             {"attributeName":"机身内存","attributeValue":["16G","32G"]}]
             */
            /** 需要添加多少种规格*/
            for (var i = 0; i < specItems.length; i++) {
                var attributeName = specItems[i].attributeName;//规格名字
                var attributeValue = specItems[i].attributeValue;//多个规格选项
                var newItems = new Array();
                /** 获取原来每一条SKU进行修改 */
                for (var j = 0; j < $scope.goods.items.length; j++) {
                    var origItem = $scope.goods.items[j]
                    /** 每一条都添加N个规格属性 */
                    for (var k = 0; k < attributeValue.length; k++) {
                        var newItem = JSON.parse(JSON.stringify(origItem));
                        newItem.spec[attributeName] = attributeValue[k];
                        newItems.push(newItem);
                    }
                }
                /** 替换之前的SKU列表 */
                $scope.goods.items = newItems;
            }
        }

        /** 更新选中的规格 $event:点击事件 name:规格  value:规格选项 */
        $scope.updateSpecAttr = function ($event, name, value) {
            //{"attributeValue":["联通4G","移动4G","电信4G"],"attributeName":"网络"}
            //判断有没有添加过
            var hasAddSpec = $scope.searchJsonByKey($scope.goods.goodsDesc.specificationItems, 'attributeName', name);
            if (hasAddSpec) {//如果添加过
                var isAddOption = $event.target.checked;//判断是否是打勾
                if (isAddOption) {//添加到数组中
                    hasAddSpec.attributeValue.push(value);
                } else {//从数组中移除
                    var removeIndex = hasAddSpec.attributeValue.indexOf(value);
                    hasAddSpec.attributeValue.splice(removeIndex, 1);
                    //移除后判断属性是否为空，空了就移除这个规格
                    var releaseLength = hasAddSpec.attributeValue.length;
                    if (releaseLength == 0) {
                        var spicIndex = $scope.goods.goodsDesc.specificationItems.indexOf(hasAddSpec);
                        $scope.goods.goodsDesc.specificationItems.splice(spicIndex, 1);
                    }
                }
            } else {//没有添加过
                $scope.goods.goodsDesc.specificationItems.push({'attributeName': name, attributeValue: [value]});
            }
        };

        $scope.searchJsonByKey = function (jsonArr, key, keyValue) {
            for (var i = 0; i < jsonArr.length; i++) {
                if (jsonArr[i][key] == keyValue) {
                    return jsonArr[i];
                }
            }
            return false;
        }

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
                    if (!$location.search().id) {
                        $scope.goods.goodsDesc.customAttributeItems = JSON.parse(typeTemplate.customAttributeItems);
                    }
                    //specIds: "[{\"id\":27,\"text\":\"网络\"},{\"id\":32,\"text\":\"机身内存\"}]"
                });
                /** 查询该模版对应的规格与规格选项 */
                baseService.findOne("/typeTemplate/findSpecByTemplateId", newValue).then(function (response) {
                    $scope.specList = response.data;
                });
            } else {//没有模板id
                $scope.typeTemplate = null;
                $scope.goods.brandId = null;
                $scope.specList = null;
            }
        });

        //--------------------------------------------------------
        $scope.remove_image_entity = function (index) {
            $scope.goods.goodsDesc.itemImages.splice(index, 1);
        }
        $scope.add_image_entity = function () {
            $scope.goods.goodsDesc.itemImages.push($scope.image_entity);
        };


        /** 定义搜索对象 */
        $scope.searchEntity = {};
        /** 定义商品状态数组 */
        $scope.status = ['未审核', '已审核', '审核未通过', '关闭'];
        /** 分页查询 */
        $scope.search = function (page, rows) {
            baseService.findByPage("/goods/search", page,
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

    }
);