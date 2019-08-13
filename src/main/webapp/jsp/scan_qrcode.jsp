<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/7/16
  Time: 14:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head>
    <meta charset="utf-8"/>
    <title></title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script src="https://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>
    <script src="http://res2.wx.qq.com/open/js/jweixin-1.4.0.js "></script>
</head>

<body>
<div class="lbox_close wxapi_form">
    <h3>微信扫一扫</h3>
    <button id="scanQRCode">扫码</button>
    <input id="codeValue">
</div>
</body>

<script type="text/javascript">
    $(function () {
        //需要把当前页面的url地址传到后台，生成签名信息时需要使用到
        var tokenUrl = location.href;
        //获取签名的后台接口
        var _getWechatSignUrl = 'http://b22j83.natappfree.cc/h5api/qrcode/get_sign';

        $(document).ready(function () {
            //获取签名
            $.ajax({
                url: _getWechatSignUrl,
                data: {tokenUrl: tokenUrl},
                dataType: "json",
                success: function (res) {
                    console.log(res);
                    //获得签名之后传入配置中进行配置
                    wxConfig(res.appId, res.timestamp, res.nonceStr, res.signature);
                }
            })
        })

        function wxConfig(_appId, _timestamp, _nonceStr, _signature) {
            console.log('获取数据：' + _timestamp + '\n' + _nonceStr + '\n' + _signature);
            wx.config({
                debug: true,// 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
                appId: _appId,// 必填，公众号的唯一标识
                timestamp: _timestamp,// 必填，生成签名的时间戳
                nonceStr: _nonceStr,// 必填，生成签名的随机串
                signature: _signature,// 必填，签名，见附录1
                jsApiList: ['checkJsApi', 'scanQRCode']
                // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
            });
            wx.error(function (res) {
                // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
                console.log('has error' + res.toString());
                alert("error：" + res);
            });
        }

        $("#scanQRCode").click(function (event) {
            wx.scanQRCode({
                desc: 'scanQRCode desc',
                needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
                scanType: ["qrCode", "barCode"], // 可以指定扫二维码还是一维码，默认二者都有
                success: function (res) {
                    console.log("调用扫描成功", res);
                    var result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果
                    $("#codeValue").val(result);//显示结果
                    // alert("扫码结果为：" + result);
                },
                error: function (res) {
                    console.log("click has error" + res)
                    // alert("error：" + res);
                }
            });
        })

    });
</script>


</html>