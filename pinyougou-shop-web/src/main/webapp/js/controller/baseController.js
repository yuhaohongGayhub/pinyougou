app.controller("baseController", function ($scope) {
    /** 分页控件配置信息  */
    $scope.paginationConf = {
        currentPage: 1, // 当前页码
        totalItems: 0,  // 总记录数
        itemsPerPage: 10, // 每页显示的记录数
        perPageOptions: [10, 20, 30], // 页码下拉列表框
        onChange: function () { // 改变事件
            $scope.reload(); //重新加载
        }
    };
    $scope.reload = function () {
        /** 当前页码，每页条数 */
        $scope.ids = [];
        $scope.search($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
    };
    /** 查询条件 */
    $scope.serchEntity = {};
    /** 显示编辑页面 */
    $scope.show = function (entity) {
        var temp = JSON.parse(JSON.stringify(entity));
        $scope.entity = temp;
    };
    /** 需要删除的id列表 */
    $scope.ids = [];
    /** 需要删除的id集合 */
    $scope.checkAllStatus = false;
    $scope.checkAllValue = false;
    /** 跟新删除列表 */
    $scope.updateSelection = function ($event, id) {
        /** 判断是勾选还是没勾选 */
        if ($event.target.checked) {
            $scope.ids.push(id);
        } else {
            var idx = $scope.ids.indexOf(id);
            /** 删除数组中的元素  */
            $scope.ids.splice(idx, 1);
        }
        console.log($scope.ids.length == $scope.dataList.length);
        if ($scope.ids.length != $scope.dataList.length) {
            $scope.checkAllStatus = false;
        } else {
            console.log("in true");
            $scope.checkAllStatus = true;
            $scope.checkAllValue = true;
        }
        console.log($scope.ids);
    }
    /** 为全选checkbox绑定点击事件 */
    $scope.checkAll = function ($event) {
        //如果选中
        $scope.checkAllValue = false;
        $scope.checkAllStatus = false;
        alert($event.target.checked);
        if ($event.target.checked) {
            $scope.ids = [];
            for (var i = 0; i < $scope.dataList.length; i++) {
                $scope.ids.push($scope.dataList[i].id);
            }
            $scope.checkAllStatus = true;
            $scope.checkAllValue = true;
        } else {
            //$scope.checkAllStatus = false;
            //$scope.checkAllValue = false;
            $scope.ids = [];
        }
        console.log($scope.ids);
    };

    /** 格式化输出结果 */
    $scope.json2Str = function (jsonStr, key) {
        var jsonArr = JSON.parse(jsonStr);
        var arr = new Array();
        for (var i = 0; i < jsonArr.length; i++) {
            arr.push(jsonArr[i][key]);
        }
        return arr.join(",");
    }
});