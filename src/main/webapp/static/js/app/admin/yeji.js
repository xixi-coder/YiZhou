/**
 * Created by Administrator on 2016/9/9.
 */


$(document).ready(function () {
    var url = g.ctx + "/admin/count/yeji/list";
    var columns = [
        {
            "data": "user_name",
            'render': function (a) {
                return '<a href="' + g.ctx + "/admin/count/yeji/item?username=" + a + '">' + a + '</a>'
            }
        },
        {
            "data": "job_type",
            'render': function (a) {
                if (a == 1) {
                    return '兼职';
                } else {
                    return '全职'
                }
            }
        },
        {"data": "name"},
        {
            "data": "count",
            'render': function (a) {
                if (a == null) {
                    return 0;
                } else {
                    return Math.ceil(a);
                }
            }
        },
        {
            "data": "sum", 'render': function (a) {
            if (a == null) {
                return 0;
            } else {
                return a;
            }
        }
        },
        {
            "data": "tsum", 'render': function (a) {
            if (a == null) {
                return 0;
            } else {
                return a;
            }
        }
        },
        {
            "data": "count1",
            'render': function (a) {
                if (a == null) {
                    return 0;
                } else {
                    return Math.ceil(a);
                }
            }
        },
        {
            "data": "sum1", 'render': function (a) {
            if (a == null) {
                return 0;
            } else {
                return a;
            }
        }
        },
        {
            "data": "tsum1", 'render': function (a) {
            if (a == null) {
                return 0;
            } else {
                return a;
            }
        }
        },
        {
            "data": "count2", 'render': function (a) {
            if (a == null) {
                return 0;
            } else {
                return Math.ceil(a);
            }
        }
        },
        {
            "data": "sum2", 'render': function (a) {
            if (a == null) {
                return 0;
            } else {
                return a;
            }
        }
        },
        {
            "data": "tsum2", 'render': function (a) {
            if (a == null) {
                return 0;
            } else {
                return a;
            }
        }
        },
        {
            "data": "count3", 'render': function (a) {
            if (a == null) {
                return 0;
            } else {
                return Math.ceil(a);
            }
        }
        },
        {
            "data": "sum3", 'render': function (a) {
            if (a == null) {
                return 0;
            } else {
                return a;
            }
        }
        },
        {
            "data": "tsum3", 'render': function (a) {
            if (a == null) {
                return 0;
            } else {
                return a;
            }
        }
        },
        {
            "data": "count4", 'render': function (a) {
            if (a == null) {
                return 0;
            } else {
                return Math.ceil(a);
            }
        }
        },
        {
            "data": "sum4", 'render': function (a) {
            if (a == null) {
                return 0;
            } else {
                return a;
            }
        }
        },
        {
            "data": "tsum4", 'render': function (a) {
            if (a == null) {
                return 0;
            } else {
                return a;
            }
        }
        },
        {
            "data": "count5", 'render': function (a) {
            if (a == null) {
                return 0;
            } else {
                return Math.ceil(a);
            }
        }
        },
        {
            "data": "sum5", 'render': function (a) {
            if (a == null) {
                return 0;
            } else {
                return a;
            }
        }
        },
        {
            "data": "tsum5", 'render': function (a) {
            if (a == null) {
                return 0;
            } else {
                return a;
            }
        }
        },       {
        "data": "count6", 'render': function (a) {
            if (a == null) {
                return 0;
            } else {
                return Math.ceil(a);
            }
        }
    },
    {
        "data": "sum6", 'render': function (a) {
        if (a == null) {
            return 0;
        } else {
            return a;
        }
    }
    },
    {
        "data": "tsum6", 'render': function (a) {
        if (a == null) {
            return 0;
        } else {
            return a;
        }
    }
    }
    ];


    var table = dt.build($('#yeji-list'), url, columns, function () {

    });

    $("#search").on('click', function () {
        table.ajax.reload();
    });
})
