/** 定义搜索控制器 */
app.controller('searchController', function ($scope, baseService) {
    /** 定义搜索参数对象 */
    $scope.searchParam = {
        keywords: '',
        category: '', 'brand': '',
        spec: {},
        price: '',
        curPage: 1,
        pageSize: 20,
        sortField: '',
        sortDir: ''
    };

    //是否显示价格区间选项
    $scope.showPrice = false;

    $scope.initSearch = function () {
        var keyWords = $scope.searchParam.keywords;
        $scope.searchParam = {
            keywords: '',
            category: '', 'brand': '',
            spec: {},
            price: '',
            curPage: 1,
            pageSize: 20,
            sortField: '',
            sortDir: ''
        };
        $scope.searchParam.keywords = keyWords;
        $scope.search();
    };

    /** 定义搜索方法 */
    $scope.search = function () {
        baseService.sendPost("/Search", $scope.searchParam)
            .then(function (response) {
                    /** 获取搜索结果 */
                    $scope.resultMap = response.data;

                    //是否显示价格区间
                    if ($scope.searchParam.keywords && !$scope.searchParam.price) {
                        $scope.showPrice = true;
                    } else {
                        $scope.showPrice = false;
                    }

                    //格式化分页栏
                    $scope.initPageNum();
                }
            );
    };

    $scope.sortSearch = function (key, value) {
        $scope.searchParam.sortField = key;
        $scope.searchParam.sortDir = value;
        $scope.search();
    };

    $scope.initPageNum = function () {
        $scope.pageNum = [];
        $scope.showPreDot = false;
        $scope.showPostDot = false;
        var total = $scope.resultMap.totalPages;
        var curPage = $scope.searchParam.curPage;
        var startNum = 1;
        var endNum = total;

        if (total < 5) {
            //全部显示
        } else {
            if (curPage <= 3) {
                $scope.showPostDot = true;
                endNum = 5;
            } else if (curPage >= total - 2) {
                startNum = total - 4;
                $scope.showPreDot = true;
            } else {
                $scope.showPreDot = true;
                $scope.showPostDot = true;
                startNum = curPage - 2;
                endNum = curPage + 2;
            }
        }
        for (var i = startNum; i <= endNum; i++) {
            $scope.pageNum.push(i);
        }
        console.log($scope.pageNum);
    };

    $scope.addSearchItem = function (key, value) {
        if (key == 'category' || key == 'brand' || key == 'price') {
            $scope.searchParam[key] = value;
        } else {
            $scope.searchParam.spec[key] = value;
        }
        /** 执行搜索 */
        $scope.search();
    };

    $scope.removeSearchItem = function (key) {
        if (key == 'category' || key == 'brand' || key == 'price') {
            $scope.searchParam[key] = "";
        } else {
            delete $scope.searchParam.spec[key];
        }
        /** 执行搜索 */
        $scope.search();
    }
});