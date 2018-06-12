/** 商品详细页（控制层）*/
app.controller('itemController', function ($scope) {
    /** 定义购买数量操作方法 */
    $scope.addNum = function (x) {
        $scope.num += x;
        if ($scope.num < 1) {
            $scope.num = 1;
        }
    };

    /** 记录用户选择的规格选项 */
    $scope.specItems = {};
    /** 定义用户选择规格选项的方法 */
    $scope.selectSpec = function (name, value) {
        $scope.specItems[name] = value;
        searchSku();
    };
    /** 判断某个规格选项是否被选中 */
    $scope.isSelected = function (name, value) {
        return $scope.specItems[name] == value;
    };

    /** 加载默认的SKU */
    $scope.loadSku = function () {
        $scope.sku = skuList[0];
        /** 获取SKU商品选择的选项规格 */
        $scope.specItems = JSON.parse($scope.sku.spec);
    };
    /** 根据用户选中的规格选项，查找对应的SKU商品 */
    var searchSku = function () {
        for (var i = 0; i < skuList.length; i++) {
            /** 判断规格选项是不是当前用户选中的 */
            if (skuList[i].spec == JSON.stringify($scope.specItems)) {
                $scope.sku = skuList[i];
                return;
            }
        }
    };
    /** 添加商品到购物车 */
    $scope.addToCart = function(){
        alert('skuid:' + $scope.sku.id);
    };
});