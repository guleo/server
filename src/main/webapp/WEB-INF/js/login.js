/**
 * Created by frank on 2016/1/11.
 * 管理员后台网页登录处理请求
 */


$(document).ready(function () {
    GetLastUser();
});


jQuery(document).ready(function () {
    $("#btn_submit").click(function () {
        var username = $('#username').val();
        var pwd = $('#password').val();
        if (username == "")
            alert("用户名不能为空");
        else if (pwd == "")
            alert("密码不能为空");
        else {
            if (document.getElementById('remember-me').checked == true) {
                SetLastUser(username, pwd);
            } else {
                ResetCookie();
            }
            pwd = hex_md5(pwd);
            LoginInfo({"username": username, "password": pwd});
        }
    });
});
function LoginInfo(data) {
    $.ajax({
            url: "/Login",
            type: "get",
            data: data,
            success: LoginResult
        }
    )
}
function LoginResult(data) {
    $('#my-login').modal('close');
    if (data.result == "success") {
        location.href = "main";
    }
    else {
        alert("对不起，登录失败");
    }

}

//window.onload = function onLoginLoaded() {
//
//        GetLastUser();
//}

function GetLastUser() {
    var id = "49BAC005-7D5B-4231-8CEA-16939BEACD67";//GUID标识符
    var usr = GetCookie(id);
    if (usr != null) {
        document.getElementById('username').value = usr;
        GetPwdAndChk();
    }
}


function SetLastUser(usr, pwd) {
    var id = "49BAC005-7D5B-4231-8CEA-16939BEACD67";
    var expdate = new Date();
    //当前时间加上两周的时间
    expdate.setTime(expdate.getTime() + 14 * (24 * 60 * 60 * 1000));
    SetCookie(id, usr, pwd, expdate);
}
//用户名失去焦点时调用该方法

function GetPwdAndChk() {
    //  var usr = document.getElementById('username').value;
    var pwd = GetCookie("pwd");
    if (pwd != null) {
        document.getElementById('remember-me').checked = true;
        document.getElementById('password').value = pwd;
    } else {
        document.getElementById('remember-me').checked = false;
        document.getElementById('password').value = "";
    }
}
//取Cookie的值

/**
 * @return {null}
 */
function GetCookie(name) {
    var arg = name + "=";
    var alen = arg.length;
    var clen = document.cookie.length;
    var i = 0;
    while (i < clen) {
        var j = i + alen;
        //alert(j);
        if (document.cookie.substring(i, j) == arg) return getCookieVal(j);
        i = document.cookie.indexOf(" ", i) + 1;
        if (i == 0) break;
    }
    return null;
}


function getCookieVal(offset) {
    var endstr = document.cookie.indexOf(";", offset);
    if (endstr == -1) endstr = document.cookie.length;
    return unescape(document.cookie.substring(offset, endstr));
}
//写入到Cookie

function SetCookie(name, value, pwd, expires) {


    var argv = SetCookie.arguments;
    //本例中length = 3
    var argc = SetCookie.arguments.length;
    var expires = (argc > 4) ? argv[3] : null;
    var path = (argc > 4) ? argv[4] : null;
    var domain = (argc > 5) ? argv[5] : null;
    var secure = (argc > 6) ? argv[6] : false;
    document.cookie = name + "=" + escape(value) + "; pwd=" + pwd + ((expires == null) ? "" : ("; expires=" + expires.toGMTString())) + ((path == null) ? "" : ("; path=" + path)) + ((domain == null) ? "" : ("; domain=" + domain)) + ((secure == true) ? "; secure" : "");
}

function ResetCookie() {
    var exp = new Date();
    exp.setTime(exp.getTime() + (-1 * 24 * 60 * 60 * 1000));
    var user = document.getElementById('username').value;
    var id = "49BAC005-7D5B-4231-8CEA-16939BEACD67";
    SetCookie(id,'', '', exp);
}



