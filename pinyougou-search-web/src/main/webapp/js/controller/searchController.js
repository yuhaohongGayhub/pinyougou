/** 定义搜索控制器 */
app.controller('searchController',function($scope, baseService){
    /** 定义搜索参数对象 */
    $scope.searchParam = {};
    /** 定义搜索方法 */
    $scope.search = function(){
        baseService.sendPost("/Search", $scope.searchParam)
            .then(function(response){
                    /** 获取搜索结果 */
                    $scope.resultMap = response.data;
                }
            );
    };
});