/**
 * Created by frank on 2016/1/2.
 * 处理登录请求
 */


var curPage = 1;
var totalPage = 1;
var delIndex = -1;
var isLogin = false;
function Load() {
    $.ajax({
        url: "/Share",
        type: "get",
        data: {"mode": 1, "curPage": curPage},
        success: ShareResult
    });
    $.ajax({
            url: "/question",
            type: "get",
            data: {"type": 0},
            success: ClassResult
        }
    );

}
$(document).ready(
    Load()
)

//$(document).before(
//    VerifyLogin()
//)
//function VerifyLogin() {
//    var id = "49BAC005-7D5B-4231-8CEA-16939BEACD67";//GUID标识符
//    var usr = GetCookie(id);
//    console.log(usr);
//    if (usr == null) {
//        location.href = "admin-login";
//      isLogin = true;
//    }
//}
//function getCookieVal(offset) {
//    var endstr = document.cookie.indexOf(";", offset);
//    if (endstr == -1) endstr = document.cookie.length;
//    return unescape(document.cookie.substring(offset, endstr));
//}
//function GetCookie(name) {
//    var arg = name + "=";
//    var alen = arg.length;
//    var clen = document.cookie.length;
//    var i = 0;
//    while (i < clen) {
//        var j = i + alen;
//        //alert(j);
//        if (document.cookie.substring(i, j) == arg) return getCookieVal(j);
//        i = document.cookie.indexOf(" ", i) + 1;
//        if (i == 0) break;
//    }
//    return null;
//}

function ClassResult(data) {
    console.log(data);
    var html = "";
    var html1 = "";
    var index = 0, index1 = 0;
    for (var i = 0; i < data.length; i++) {
        html += '<optgroup label="' + data[i].first + '">';
        html1 += '<optgroup label="' + data[i].first + '">';
        if (data[i].name.length > 0) {
            html += '<option value="0:' + (index++) + '">' + data[i].first + '</option>';
        }
        var obj = eval(data[i].name);
        for (var j = 0; j < obj.length; j++) {

            html += '<option value="1:' + (index++) + '">';
            html += obj[j].sub + '</option>';
            html1 += '<option value="' + (index1++) + '">';
            html1 += obj[j].sub + '</option>';
        }
        html += "</optgroup>";
        html1 += "</optgroup>";
    }
    $("#select").html(html);
    $("#doc-type").html(html1);
}
function ShareResult(data) {
    console.log(data);
    if (data.length > 0) {
        curPage = data[data.length - 1].CurPage;
        curPage = parseInt(curPage);
        totalPage = data[data.length - 1].TotalPage;
        totalPage = parseInt(totalPage);
        var html = '<table class="am-table am-table-striped am-table-hover table-main"> <thead>' +
            '<tr> <th class="table-check"><input type="checkbox"/></th>' +
            '<th class="table-id">ID</th>' +
            '<th class="table-title">父主题</th>' +
            '<th class="table-type">子主题</th>' +
            '<th class="table-author am-hide-sm-only">提交人</th>' +
            '<th class="table-date am-hide-sm-only">提交日期</th>' +
            '<th class="table-set">操作</th>' +
            '</tr>' +
            '</thead>' +
            '<tbody id="t">';
    }
    if (data.length == 0)
        html += '<thead> <th class="left aligned">无搜索结果</th> </thead> </tbody></table>';
    else {
        for (var i = 0; i < data.length - 1; i++) {
            html += '<tr><td><input type="checkbox"/></td><td>' + data[i].Id + '</td>';
            html += '<td><a href="#">' + data[i].First + '</a></td>';
            html += '<td>' + data[i].Sub + '</td>';
            html += '<td class="am-hide-sm-only">' + data[i].Commit + '</td>';
            html += '<td class="am-hide-sm-only">' + data[i].Time + '</td>';
            html += '<td>' + '<div class="am-btn-group am-btn-group-xs am-dropdown" ' +
                'data-am-dropdown="{justify: \'#doc-dropdown-justify\'}">' +
                '<button type="button" class="check am-btn am-btn-default am-text-secondary" id="0' + i +
                '" ><span  class="am-icon-archive" data-id="' + data[i].Id + '" ></span>' +
                '审核 </button>';
            html += '<button type="button" class="delete am-btn am-btn-default am-btn-xs am-text-danger am-hide-sm-only" id="1' + i + '" >' +
                '<span class="am-icon-trash-o" data-id="' + data[i].Id + '"></span> 删除</button>';
            html += ' </div></td></tr>';
        }
        html += '<div class="am-modal" id="my-alert"><div class="am-modal-dialog">' +
            '<div class="am-modal-hd">后台管理</div> <div class="am-modal-bd"> 确定要删除这条记录吗?</div>' +
            '<div class="am-modal-footer"><span class="am-modal-btn" data-am-modal-confirm >确定</span>' +
            '<span class="am-modal-btn" data-am-modal-cancel>取消</span></div></div></div> ' +
            '<div class="am-modal am-modal-confirm" id="my-popup"></div>';
        html += '</tbody></table>';
        html += '<div class="am-cf">' + '共 ' + (data.length - 1) + '条记录' +
            '<div class="am-fr">' + '<ul class="am-pagination">' +
            '</ul> </div></div><hr/><p>注：.....</p>';
        var html1 = "";
        if (curPage == 1)
            html1 += '<li class="am-disabled"><a href="#">«</a></li>';
        else
            html1 += "<li><a class='page_last' href='javascript:convertPage(" + (curPage - 1) + ")'> « </a></li>";
        for (i = 1; i <= totalPage; i++) {
            console.log(i);
            if (i == curPage)
                html1 += '<li class="current">' + i + '</li>';
            else
                html1 += '<li><a href="javascript:convertPage(' + i + ')">' + i + '</a></li>';
            if (i == 5 && totalPage > 5) {
                html1 += '...';
                if (totalPage > 7)
                    i = totalPage - 2;
            }
        }
        if (curPage < totalPage)
            html1 += "<li><a class='page_last' href='javascript:convertPage(" + (curPage + 1) + ")'> » </a></li>";
        else
            html1 += '<li class="am-disabled"><a href="#"> » </a></li>';

    }

    $("#table-element").html(html);
    $('.am-pagination').html(html1);
    Delete();
    Check(data);
}

function convertPage(curpage) {
    curpage = parseInt(curpage);
    SearchInfo({"mode": 1, "curPage": curpage});
}

function SearchInfo(data) {
    $.ajax({
        url: "/Share",
        type: "get",
        data: data,
        success: ShareResult
    });
}
function Delete() {
    $('.delete').
        on('click', function () {
            delIndex = $(this).attr('id').substr(1);
            console.log(delIndex);
            $('#my-alert').modal({
                relatedTarget: this,
                onConfirm: function () {
                    var $link = $(this.relatedTarget).find('span');
                    $.ajax({
                        url: "/Share",
                        type: "get",
                        data: {"id": $link.data("id"), "mode": 2},
                        success: DeleteResult
                    });
                }
            });
        });
}

function DeleteResult(data) {
    console.log(data.result);
    if (data.result == "success") {
        alert("删除成功");
        var oBtn = document.getElementById("0" + delIndex);
        oBtn.disabled = 'disabled';
        oBtn = document.getElementById("1" + delIndex);
        oBtn.disabled = 'disabled';
    }
    else {
        alert("删除失败");
    }

}


function Check(data) {
    $('.check').on('click', function () {
        var i = $(this).attr('id').substr(1);
        delIndex = i;
        console.log(i);
        var html = ' <div class="am-modal-dialog" > ' +
            '<div class="am-modal-hd">' +
            '<h4 class="am-popup-title">题目（question）</h4>' +
            ' <span data-am-modal-close class="am-close">&times;</span></div>' +
            ' <div class="am-modal-bd">' +
            '   <div data-am-widget="list_news" class="am-list-news am-list-news-default">' +
            ' <div class="am-list-news-hd am-cf">' +
            '  <a href="###"><h2>' + data[i].Text + '</h2></a></div>' +
            '<div class="am-list-news-bd"><ul class="am-list"> <li class="am-g am-list-item-dated">正确选项：' +
            data[i].Right + '</li>' + '<li>错误选项1：' + data[i].Wrong1 + '</li> <li>错误选项2：' +
            data[i].Wrong2 + '</li> <li>错误选项3：' + data[i].Wrong3 + ' </li></ul>';
        html += ' </div><div class="am-modal-footer"><span class="am-modal-btn" data-am-modal-cancel>取消</span>' +
            '<span class="am-modal-btn" id="btn-commit" data-am-modal-confirm>提交</span></div></div></div></div>';
        $("#my-popup").html(html);

        $('#my-popup').modal({
            relatedTarget: this,
            onConfirm: function () {
                var $link = $(this.relatedTarget).find('span');
                $.ajax({
                    url: "/Share",
                    type: "get",
                    data: {"id": $link.data("id"), "mode": 3},
                    success: CheckResult
                });

            }
        });
    });
}

function CheckResult(data) {
    console.log(data.result);
    if (data.result == "success") {
        alert("提交成功");
        var oBtn = document.getElementById("0" + delIndex);
        oBtn.disabled = 'disabled';
        oBtn = document.getElementById("1" + delIndex);
        oBtn.disabled = 'disabled';
    }
    else {
        alert("提交失败");
    }
}
function BtnClick(data) {
    $('.btn-search').click(function () {
        var $btn = $(this);
        $btn.button('loading');
        Search(data);
    });
}

$("#select").on('change', function () {

    var type = $(this).selected().val();
    console.log(type);
    var text = type.substr(2);
    text = parseInt(text);
    text = $(this).find('option').eq(text).text();
    type = type.substr(0, 1);
    var data = {"text": text, "type": type, "curPage": 1, "mode": 4};
    console.log(data);
    BtnClick(data);
});
function Search(data) {
    $('.btn-search').button("reset");
    $.ajax({
        url: "/Share",
        type: "get",
        data: data,
        success: ShareResult
    });
}

$("#btn-commit").on('click', function () {

    var sub = $("#doc-type").selected().val();

    sub = parseInt(sub);
    sub = $("#doc-type").find('option').eq(sub).text();
    var text = $("#doc-text").val();
    var right = $("#doc-right").val();
    var wrong1 = $("#doc-wrong1").val();
    var wrong2 = $("#doc-wrong2").val();
    var wrong3 = $("#doc-wrong3").val();
    if (text == "")
        alert("正文不能为空");
    else if (right == "")
        alert("正确选项不能为空");
    else if (wrong1 == "" || wrong2 == "" || wrong3 == "")
        alert("错误选项不能为空");
    else {
        var data = {text: text, right: right, wrong1: wrong1, wrong2: wrong2, wrong3: wrong3, sub: sub};
        console.log(data);
        $.ajax({
            url: "/share",
            type: "get",
            data: data,
            success: AddResult
        })
    }
})

function AddResult(data) {
    console.log(data);
    if (data == "success") {
        alert("提交成功");
    }
    else {
        alert("提交失败");
    }
}


