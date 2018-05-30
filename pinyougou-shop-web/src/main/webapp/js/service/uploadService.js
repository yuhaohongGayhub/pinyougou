/** 文件上传服务层 */
app.service("uploadService", function ($http) {
    /** 文件上传方法 */
    this.uploadFile = function () {
        /** 创建表单对象 */
        var formData = new FormData();
        /** 追加需要上传的文件 */
        formData.append("file", file.files[0]);
        /** 发送异步请求上传文件 */
        return $http({
            method: 'post', // 请求方式
            url: "/upload", // 请求URL
            data: formData, // 表单数据
            headers: {'Content-Type': undefined}, // 请求头
            transformRequest: angular.identity  // 转换对象
        });
    };
});
