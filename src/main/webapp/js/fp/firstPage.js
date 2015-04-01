/**
 * Created by Zuobai on 2014/7/19.
 * 将exports都导入Z的简单做法
 */

window.exports = window.Z = {};
window.require = function () {
    return window.Z;
};
window.zachModule = function ( func ) {
    func( window.require );
};

/**
 * Created by Zuobai on 13-12-8.
 */

zachModule( function () {
    // region 数学函数
    // 判断一个点是否在一个矩形之内
    function inRect( tx, ty, x, y, width, height ) {
        tx -= x;
        ty -= y;
        return tx >= 0 && tx < width && ty >= 0 && ty < height;
    }

    // 如果x>b,取b,x小于a,取啊
    function range( x, a, b ) {
        if ( a <= b ) {
            return x < a ? a : x > b ? b : x;
        }
        else {
            return range( x, b, a );
        }
    }

    // 判断是否在区间
    function inRange( x, a, b ) {
        if ( a <= b ) {
            return x >= a && x < b;
        }
        else {
            return inRange( x, b, a );
        }
    }

    // 判断正负
    function sign( x ) {
        return x >= 0 ? 1 : -1;
    }

    // 生成贝塞尔曲线函数
    function Bezier( x1, y1, x2, y2, func ) {
        var xTolerance = 0.0001,
            retVal = func || function ( xTarget ) {
                    function bezier( t, p1, p2 ) {
                        var ct = 1 - t, ct2 = ct * ct;
                        var t2 = t * t, t3 = t2 * t;
                        var tct2 = t * ct2, t2ct = t2 * ct;
                        return 3 * p1 * tct2 + 3 * p2 * t2ct + t3;
                    }

                    function bezierD( t, p1, p2 ) {
                        return ( 9 * p1 - 9 * p2 + 3 ) * t * t + ( 6 * p2 - 12 * p1 ) * t + 3 * p1;
                    }

                    var t = 0.5;
                    while ( Math.abs( xTarget - bezier( t, x1, x2 ) ) > xTolerance ) {
                        t = t - ( bezier( t, x1, x2 ) - xTarget ) / bezierD( t, x1, x2 );
                    }

                    return bezier( t, y1, y2 );
                };

        retVal.arg = [x1, y1, x2, y2];
        return retVal;
    }

    // endregion

    // region 二维变换
    var transform2d = {
        translate : function ( x, y ) {
            return [1, 0, 0, 1, x, y];
        },
        scale : function ( sx, sy ) {
            return [sx, 0, 0, sy, 0, 0];
        },
        rotate : function ( a ) {
            var sin = Math.sin( a ), cos = Math.cos( a );
            return [cos, sin, -sin, cos, 0, 0];
        },
        inverse : function ( m ) {
            var det = m[0] * m[3] - m[1] * m[2];
            return [m[3] / det, -m[1] / det, -m[2] / det, m[0] / det, (m[2] * m[5] - m[3] * m[4]) / det, (m[1] * m[4] - m[0] * m[5]) / det];
        },
        transform : function ( m, p ) {
            return [m[0] * p[0] + m[2] * p[1] + m[4] * p[2], m[1] * p[0] + m[3] * p[1] + m[5] * p[2], p[2]];
        },
        combine : function ( m, n ) {
            return [m[0] * n[0] + m[2] * n[1], m[1] * n[0] + m[3] * n[1], m[0] * n[2] + m[2] * n[3], m[1] * n[2] + m[3] * n[3], m[0] * n[4] + m[2] * n[5] + m[4], m[1] * n[4] + m[3] * n[5] + m[5]];
        }
    };
    // endregion

    // region 遍历
    // 遍历次数
    function loop( t, block ) {
        for ( var i = 0; i !== t; ++i ) {
            block( i );
        }
    }

    // 遍历数组
    function loopArray( list, block ) {
        var retVal;
        for ( var i = 0, len = list.length; i !== len; ++i ) {
            if ( ( retVal = block( list[i], i ) ) !== undefined ) {
                return retVal;
            }
        }
    }

    // 异步遍历数组
    function loopArrayAsync( list, block, done, inOrder ) {
        inOrder ? function () {
            var len = list.length;

            function doItem( index ) {
                if ( index === len ) {
                    done();
                }
                else {
                    block( list[index], index, function () {
                        doItem( index + 1 );
                    } );
                }
            }

            doItem( 0 );
        }() : function () {
            var count = list.length;
            for ( var i = 0, len = list.length; i !== len; ++i ) {
                block( list[i], i, function () {
                    if ( --count === 0 ) {
                        done();
                    }
                } );
            }
        }();
    }

    // 遍历对象
    function loopObj( obj, block ) {
        for ( var key in obj ) {
            block( key, obj[key] );
        }
    }

    // 遍历字符串
    function loopString( string, func, isChar ) {
        var i, len;
        if ( isChar ) {
            for ( i = 0, len = string.length; i !== len; ++i ) {
                func( string.charAt( i ), i );
            }
        }
        else {
            for ( i = 0, len = string.length; i !== len; ++i ) {
                func( string.charCodeAt( i ), i );
            }
        }
    }

    // endregion

    // region 对象
    function merge( outObj, inObjList ) {
        loopArray( inObjList, function ( obj ) {
            loopObj( obj, function ( key, value ) {
                outObj[key] = value;
            } );
        } );
        return outObj;
    }

    // 将若干个对象合并到第一个对象中,并返回第一个对象
    function insert( obj ) {
        return merge( obj, Array.prototype.slice.call( arguments, 1 ) );
    }

    // 将若干个对象合并,返回合并后的新对象
    function extend() {
        var retVal = {};
        return merge( retVal, arguments );
    }

    // 返回一个字典的key数组
    function keys( obj ) {
        var retVal = [];
        loopObj( obj, function ( key ) {
            retVal.push( key );
        } );
        return retVal;
    }

    function defineGetter( obj, name, func ) {
        Object.defineProperty( obj, name, {
            get : func
        } );
    }

    // 定义自动对象
    function defineAutoProperty( obj, name, arg ) {
        arg = arg || {};
        var val = arg.value, write = arg.set;
        val !== undefined && write( val );

        Object.defineProperty( obj, name, {
            get : function () {
                return val;
            },
            set : function ( tVal ) {
                val = write ? write( tVal ) || tVal : tVal;
            }
        } );
    }

    // 定义一组自动对象
    function defineAutoProperties( obj, info ) {
        loopObj( info, function ( name, arg ) {
            defineAutoProperty( obj, name, arg );
        } );
    }

    // endregion

    // region 技巧
    // 对象类型判断
    var is = (function () {
        var is = {};
        loopArray( ["Array", "Boolean", "Date", "Function", "Number", "Object", "RegExp", "String", "Window", "HTMLDocument"], function ( typeName ) {
            is[typeName] = function ( obj ) {
                return Object.prototype.toString.call( obj ) == "[object " + typeName + "]";
            };
        } );
        return is;
    })();

    // 根据函数字符串调用函数
    function callFunction( funcStr ) {
        return new Function( "return " + funcStr )().apply( null, Array.prototype.slice.call( arguments, 1 ) );
    }

    // endregion

    // region 字符串
    // 字符串流
    function StringStream( str ) {
        var i = 0, len = str.length;
        return {
            cur : function () {
                return str.charAt( i );
            },
            eat : function () {
                ++i;
            },
            isEnd : function () {
                return i >= len;
            }
        };
    }

    // 将一个元组转化为字符串
    function tupleString( tupleName, list ) {
        return tupleName + "(" + list.join( "," ) + ")";
    }

    // 返回一个元祖字符串制作函数,如TupleString( "rgba" )( 2, 3, 4, 0.4 )会返回rgba(2,3,4,0.4);
    function TupleString( tupleName ) {
        return function () {
            return tupleName + "(" + Array.prototype.join.call( arguments, "," ) + ")";
        };
    }

    // 根据format制作字符串
    function replaceString( format, content ) {
        var i = 0, ch, key = null, retVal = "";

        ch = format.charAt( i++ );
        while ( ch ) {
            if ( key === null ) {
                if ( ch === "%" ) {
                    key = "";
                }
                else {
                    retVal += ch;
                }
            }
            else {
                if ( ch === "%" ) {
                    if ( key === "" ) {
                        retVal += "%";
                    }
                    else {
                        retVal += content[key] || "";
                    }
                    key = null;
                }
                else {
                    key += ch;
                }
            }
            ch = format.charAt( i++ );
        }

        return retVal;
    }

    // 将对象转化问URI字符串
    function encodeURIObject( obj ) {
        var retVal = "", i = 0;
        loopObj( obj || {}, function ( key, value ) {
            i++ && ( retVal += "&" );
            retVal += encodeURIComponent( key );
            retVal += '=';
            retVal += encodeURIComponent( value );
        } );
        return retVal;
    }


    // 解析配对连接字符串,如name=tom&class=2&grade=3
    function parsePairString( str, split1, split2, doPair ) {
        loopArray( str.split( split1 ), function ( searchPair ) {
            var keyValue = searchPair.split( split2 );
            doPair( keyValue[0], keyValue[1] );
        } );
    }

    // 为字符串提供url解析功能
    var regUrl = /(?:((?:[^:/]*):)\/\/)?([^:/?#]*)(?::([0-9]*))?(\/[^?#]*)?(\?[^#]*)?(#.*)?/;
    loopArray( ["protocol", "hostname", "port", "pathname", "search", "hash"], function ( partName, i ) {
        defineGetter( String.prototype, partName, function () {
            return regUrl.test( this ) ? decodeURIComponent( RegExp["$" + ( i + 1 )] ) : "";
        } );
    } );

    loopObj( {
        host : function () {
            return this.hostname + ( this.port ? ":" + this.port : "" );
        },
        origin : function () {
            return this.protocol + "//" + this.host;
        },
        arg : function () {
            var arg = {};
            parsePairString( this.search.substring( 1 ), "&", "=", function ( key, value ) {
                key !== "" && ( arg[key] = value );
            } );
            return arg;
        }
    }, function ( name, func ) {
        defineGetter( String.prototype, name, func );
    } );

    function concatUrlArg( url, arg ) {
        var newSearch = encodeURIObject( extend( url.arg, arg ) );
        return url.origin + url.pathname + ( newSearch ? "?" : "" ) + newSearch + url.hash;
    }

    // endregion

    // region 链表
    // 双向链表
    function LinkedList() {
        var head = null, tail = null;

        return {
            head : function () {
                return head;
            },
            tail : function () {
                return tail;
            },
            node : function ( value ) {
                return {
                    previous : null,
                    next : null,
                    value : value
                };
            },
            remove : function ( node ) {
                node.previous ? node.previous.next = node.next : head = node.next;
                node.next ? node.next.previous = node.previous : tail = node.previous;
            },
            insert : function ( tarNode, refNode ) {
                var previous = refNode ? refNode.previous : tail;
                tarNode.next = refNode;
                tarNode.previous = previous;
                previous ? previous.next = tarNode : head = tarNode;
                refNode ? refNode.previous = tarNode : tail = tarNode;
                return tarNode;
            }
        };
    }

    LinkedList.loop = function ( list, func ) {
        var retVal;
        for ( var cur = list.head(); cur !== null; cur = cur.next ) {
            if ( ( retVal = func( cur.value, cur ) ) !== undefined ) {
                return retVal;
            }
        }
    };

    LinkedList.toArray = function ( list ) {
        var arr = [];
        LinkedList.loop( list, function ( value ) {
            arr.push( value );
        } );
        return arr;
    };
    // endregion

    // region 数组
    var array = {
        remove : function ( arr, removeItem ) {
            var retVal = [];
            loopArray( arr, function ( item ) {
                item != removeItem && retVal.push( item );
            } );
            return retVal;
        },
        reverse : function ( arr ) {
            var len = arr.length - 1,
                retVal = len === -1 ? [] : new Array( len );

            loopArray( arr, function ( item, i ) {
                retVal[len - i] = item;
            } );

            return retVal;
        },
        filter : function ( arr, filter ) {
            var retVal = [];
            loopArray( arr, function ( item ) {
                filter( item ) && retVal.push( item );
            } );
            return retVal;
        }
    };

    // 提供数组的top
    Object.defineProperty( Array.prototype, "top", {
        get : function () {
            return this[this.length - 1];
        },
        set : function ( val ) {
            this[this.length - 1] = val;
        }
    } );
    // endregion

    // region 数据结构
    // 集合,根据一个数组构建一个集合,用来判断一个key是否属于集合
    function Set( arr ) {
        var dict = {};
        loopArray( arr, function ( item ) {
            dict[item] = true;
        } );

        return {
            contains : function ( key ) {
                return dict[key] === true;
            }
        };
    }

    // endregion

    // region 事件
    // 事件
    function Event() {
        var events = LinkedList();
        return {
            trig : function () {
                var arg = arguments;
                LinkedList.loop( events, function ( task ) {
                    task.apply( null, arg );
                } );
            },
            regist : function ( value ) {
                var node = events.insert( events.node( value ), null );
                return {
                    remove : function () {
                        events.remove( node );
                    }
                };
            }
        };
    }

    // 装载器
    function Loader( callback ) {
        var loadDoneEvent = Event( callback ),
            curCount = 1;

        function loadDone() {
            if ( --curCount === 0 ) {
                loadDoneEvent.trig();
            }
        }

        return {
            load : function ( task ) {
                ++curCount;
                task( loadDone );
            },
            onLoad : loadDoneEvent.regist,
            start : loadDone
        }
    }

    // 加载资源
    function loadResource( resource, onLoad ) {
        loopArrayAsync( resource, function ( resource, i, loadDone ) {
            resource.load( loadDone );
        }, onLoad )
    }

    // 资源,需要加载使用,但全局只会加载一次
    function Resource( load ) {
        var resource, onLoadList;

        return {
            load : function ( onLoad ) {
                // 如果还有装载函数表示装载没有完成
                if ( load ) {
                    // 如果没有onLoad数组,创建装载列表,并开始装载
                    if ( !onLoadList ) {
                        onLoadList = [];
                        load( function ( targetResource ) {
                            resource = targetResource;
                            loopArray( onLoadList, function ( onLoad ) {
                                onLoad( resource );
                            } );
                            onLoadList = null;
                            load = null;
                        } );
                    }

                    onLoadList.push( onLoad );
                }
                // 否则直接回调
                else {
                    onLoad( resource );
                }
            }
        };
    }

    // endregion

    // region 导出
    // math
    exports.inRect = inRect;
    exports.range = range;
    exports.inRange = inRange;
    exports.sign = sign;
    exports.Bezier = Bezier;
    exports.transform2d = transform2d;

    // 技巧
    exports.is = is;
    exports.callFunction = callFunction;

    // 遍历
    exports.loop = loop;
    exports.loopArray = loopArray;
    exports.loopArrayAsync = loopArrayAsync;
    exports.loopObj = loopObj;
    exports.loopString = loopString;

    // 对象
    exports.insert = insert;
    exports.extend = extend;
    exports.keys = keys;
    exports.defineGetter = defineGetter;
    exports.defineAutoProperty = defineAutoProperty;
    exports.defineAutoProperties = defineAutoProperties;

    // 字符串
    exports.StringStream = StringStream;
    exports.encodeURIObject = encodeURIObject;
    exports.tupleString = tupleString;
    exports.TupleString = TupleString;
    exports.replaceString = replaceString;
    exports.parsePairString = parsePairString;
    exports.concatUrlArg = concatUrlArg;

    // 数据结构
    exports.array = array;
    exports.Set = Set;
    exports.LinkedList = LinkedList;

    // 事件
    exports.Event = Event;
    exports.Loader = Loader;
    exports.Resource = Resource;
    exports.loadResource = loadResource;
    // endregion
} );

/**
 * Created by Zuobai on 14-1-10.
 * 经典浏览器封装
 */

zachModule( function ( require ) {
    // region 引入
    var util = require( "zachUtil.js" ),
        loopArray = util.loopArray,
        loopObj = util.loopObj,
        LinkedList = util.LinkedList,
        is = util.is,
        defineGetter = util.defineGetter,
        Event = util.Event;
    // endregion

    // region 浏览器能力检测
    window.ua = {
        canTouch : "ontouchstart" in document,
        ieMobile : window.navigator.msPointerEnabled
    };
    // endregion

    // region 路径
    // 将相对地址转换为绝对地址
    function toAbsURL( url ) {
        var a = document.createElement( 'a' );
        a.href = url;
        return a.href;
    }

    // endregion

    // region 浏览器扩展
    // 为元素添加pageLeft和pageTop属性,元素相对于文档的偏移
    loopArray( ["Left", "Top"], function ( direction ) {
        defineGetter( HTMLElement.prototype, "page" + direction, function () {
            var retVal = 0, cur, body = document.body;

            for ( cur = this; cur !== body; cur = cur.offsetParent || cur.parentElement ) {
                retVal += cur["offset" + direction] - ( cur === this ? 0 : cur["scroll" + direction] );
            }

            return retVal;
        } );
    } );

    // 为UIEvent添加zPageX,zPageY,zClientX,zClientY属性,统一触摸和鼠标
    loopArray( ["pageX", "pageY", "clientX", "clientY"], function ( coordinateName ) {
        Object.defineProperty( UIEvent.prototype, "z" + coordinateName.replace( /^./, function ( ch ) {
            return ch.toUpperCase();
        } ), {
            get : function () {
                return "touches" in this && this.touches[0] !== undefined ? this.touches[0][coordinateName] : this[coordinateName];
            }
        } );
    } );
    // endregion

    // region CSS
    window.nonstandardStyles = {};

    // 设置CSS值,可以设置一条或者设置一组
    function css( el, arg1, arg2 ) {
        function setStyle( styleName, styleValue ) {
            if ( styleName in nonstandardStyles ) {
                loopArray( nonstandardStyles[styleName], function ( styleName ) {
                    el.style.setProperty( styleName, styleValue, "" );
                } );
            }
            else {
                el.style.setProperty( styleName, styleValue, "" );
            }
        }

        is.String( arg1 ) ? setStyle( arg1, arg2 ) : loopObj( arg1, setStyle );
        return el;
    }

    css.size = function ( el, width, height ) {
        css( el, {
            width : width + "px",
            height : height + "px"
        } );
    };
    // endregion

    // region 动画
    // 请求连续动画
    var requestAnimate = function () {
        var timeout = null, tasks = LinkedList();

        return function ( task ) {
            var node = null;

            function start() {
                // 如果任务没有添加进链表,添加到链表中
                if ( node === null ) {
                    node = tasks.insert( tasks.node( task ), null );

                    // 如果当前没有计时,开始计时
                    if ( timeout === null ) {
                        timeout = setTimeout( function frame() {
                            var cur;
                            if ( tasks.tail() !== null ) {
                                timeout = setTimeout( frame, 1000 / 60 );
                                for ( cur = tasks.head(); cur !== null; cur = cur.next ) {
                                    cur.value();
                                }
                            }
                            else {
                                timeout = null;
                            }
                        }, 1000 / 60 );
                    }
                }
            }

            start();

            return {
                start : start,
                remove : function () {
                    node && tasks.remove( node );
                    node = null;
                }
            };
        };
    }();

    // endregion

    // region 事件
    // 绑定事件
    function bindEvent( el, eventType, response, isCapture ) {
        var remove;

        if ( el.addEventListener ) {
            el.addEventListener( eventType, response, isCapture || false );
            remove = function () {
                el.removeEventListener( eventType, response, isCapture || false );
            };
        }
        else {
            el.attachEvent( "on" + eventType, response );
            remove = function () {
                el.detachEvent( "on" + eventType, response );
            };
        }

        return {
            remove : remove
        };
    }

    // 制作一个事件绑定器
    function Bind( eventType ) {
        return function ( el, response, isCapture ) {
            return bindEvent( el, eventType, response, isCapture );
        };
    }

    // 当元素插入到文档时回调
    function onInsert( el, response ) {
        if ( document.documentElement.contains( el ) ) {
            response && response();
        }
        else {
            var insertEvent = bindEvent( el, "DOMNodeInsertedIntoDocument", function () {
                response && response( el );
                insertEvent.remove();
            } );
        }
    }

    // endregion

    // region ajax
    function ajax( arg ) {
        // 计算url
        var url = arg.url + util.encodeURIObject( arg.arg ),
            xhr = new XMLHttpRequest();

        bindEvent( xhr, "load", function () {
            var data = xhr.responseText;
            try {
                if ( arg.isJson ) {
                    data = JSON.parse( data );
                }
            }
            catch ( e ) {
                arg.onError && arg.onError( xhr );
                return;
            }

            arg.onLoad( data, xhr );
        } );

        bindEvent( xhr, "error", function () {
            arg.onError && arg.onError( xhr );
        } );

        xhr.open( arg.method || "get", url, true );

        // 添加requestHeader
        arg.requestHeader && loopObj( arg.requestHeader, function ( key, value ) {
            xhr.setRequestHeader( key, value );
        } );

        xhr.send( arg.data || null );

        return xhr;
    }

    // endregion

    // region 加载
    var onLoad = function () {
        var loadEvent = null;
        return function ( callback ) {
            if ( document.readyState === "complete" ) {
                callback();
            }
            else {
                if ( loadEvent === null ) {
                    loadEvent = Event();
                    var loadHandle = bindEvent( window, "load", function () {
                        loadEvent.trig();
                        loadHandle.remove();
                        loadEvent = null;
                    } );
                }

                loadEvent.regist( callback );
            }
        };
    }();
    // endregion

    // region 导出
    // 路径
    exports.toAbsURL = toAbsURL;

    // css
    exports.css = css;

    // 事件
    exports.bindEvent = bindEvent;
    exports.Bind = Bind;
    exports.onInsert = onInsert;

    // 动画
    exports.requestAnimate = requestAnimate;

    // ajax
    exports.ajax = ajax;

    // 加载
    exports.onLoad = onLoad;
    // endregion
} );

/**
 * Created by Zuobai on 2014/7/12.
 * zachCanvas 2d GUI系统
 */

zachModule( function ( require ) {
    // 引入
    var util = require( "zachUtil.js" ),
        insert = util.insert,
        loopArray = util.loopArray,
        Event = util.Event,
        transform2d = util.transform2d,
        combine = transform2d.combine,
        transform = transform2d.transform,
        inverse = transform2d.inverse,

        browser = require( "zachBrowser.js" ),
        Bind = browser.Bind,
        requestAnimate = browser.requestAnimate,
        css = browser.css,

        onMouseOver = Bind( "mouseover" ),
        onMouseOut = Bind( "mouseout" ),
        onMouseDown = Bind( "mousedown" ),
        onMouseUp = Bind( "mouseup" ),
        onMouseMove = Bind( "mousemove" ),
        onTouchStart = Bind( ua.ieMobile ? "MSPointerDown" : "touchstart" ),
        onTouchEnd = Bind( ua.ieMobile ? "MSPointerUp" : "touchend" ),
        onTouchMove = Bind( ua.ieMobile ? "MSPointerMove" : "touchmove" );

    var areaCount = 0;

    // 强化版gc
    function Context2D( gc ) {
        var prepare = [1, 0, 0, 1, 0, 0],
            cur = [1, 0, 0, 1, 0, 0],
            transformStack = [];

        // 设置矩阵
        function s() {
            var r = combine( prepare, cur );
            gc.setTransform( r[0], r[1], r[2], r[3], r[4], r[5] );
        }

        // 在当前基础上进行转换
        function t( m ) {
            cur = combine( cur, m );
            s();
        }

        // 几个经典转换
        function ClassicTransform( genFunc ) {
            return function () {
                t( genFunc.apply( null, arguments ) );
            }
        }

        return insert( gc, {
            // 该方法用于设置一个预矩阵,解决dpr变换
            setPrepareTransform : function ( m ) {
                prepare = m;
                s();
            },
            transform : t,
            getTransform : function () {
                return [cur[0], cur[1], cur[2], cur[3], cur[4], cur[5]];
            },
            save : function () {
                CanvasRenderingContext2D.prototype.save.call( gc );
                transformStack.push( cur );
            },
            restore : function () {
                CanvasRenderingContext2D.prototype.restore.call( gc );
                cur = transformStack.pop();
                s();
            },
            translate : ClassicTransform( transform2d.translate ),
            rotate : ClassicTransform( transform2d.rotate ),
            scale : ClassicTransform( transform2d.scale )
        } );
    }

    function Layer() {
        var layer = css( document.createElement( "canvas" ), "display", "block" ),
            gc = Context2D( layer.getContext( "2d" ) );

        // dpr属性
        util.defineAutoProperty( layer, "dpr", {
            value : ( window.devicePixelRatio || 1 ) / ( gc.webkitBackingStorePixelRatio || gc.mozBackingStorePixelRatio ||
            gc.msBackingStorePixelRatio || gc.oBackingStorePixelRatio || gc.backingStorePixelRatio || 1 ),
            set : function ( val ) {
                gc.dpr = val;
                gc.setPrepareTransform( transform2d.scale( val, val ) );
            }
        } );

        return insert( layer, {
            isDirty : true,
            clear : true,
            // 在层上进行绘制
            draw : function ( drawFunc ) {
                layer.clear && gc.clearRect( 0, 0, layer.width, layer.height );
                gc.layer = layer;
                gc.save();
                drawFunc( gc );
                gc.restore();
            },
            // 调整大小
            resize : function ( width, height ) {
                layer.width = width * layer.dpr;
                layer.height = height * layer.dpr;
                css.size( layer, layer.logicalWidth = width, layer.logicalHeight = height );
            },
            // 将该层绘制到父层上,记录该层到复层的变换以及parent关系
            drawTo : function ( parentGC ) {
                layer.parentLayer = parentGC.layer;
                layer.transformation = parentGC.getTransform();
                parentGC.drawImage( layer, 0, 0, layer.width, layer.height );
            },
            // 置脏位
            dirty : function () {
                layer.isDirty = true;
                layer.parentLayer && layer.parentLayer.dirty();
            }
        } );
    }

    // Area
    function Area() {
        var draw = null,
            area = {
                id : areaCount++,
                areaFromPoint : null,
                dirty : function () {
                    area.parentLayer && area.parentLayer.dirty();
                }
            };

        // draw方法
        Object.defineProperty( area, "draw", {
            set : function ( val ) {
                draw = val;
            },
            get : function () {
                return function ( gc ) {
                    gc.getTransform && ( area.transformation = gc.getTransform() );
                    area.parentLayer = gc.layer;
                    draw( gc );
                }
            }
        } );

        // 添加事件
        loopArray( ["cursorDown", "cursorUp", "cursorMove", "cursorEnter", "cursorLeave",
            "touchDown", "touchMove", "touchUp", "touchEnter", "touchLeave"], function ( eventName ) {
            var event = Event();
            area[eventName] = event.trig;
            area["on" + eventName.replace( /./, function ( c ) {
                return c.toUpperCase();
            } )] = event.regist;
        } );

        return area;
    }

    // 将一个点由page坐标系,转换到area的坐标系
    function coordinatePageToArea( area, p ) {
        return area && area.transformation ? transform( inverse( area.transformation ), coordinatePageToArea( area.parentLayer, p ) ) : p;
    }

    // 将一个点由area坐标系,转换到page坐标系
    function coordinateAreaToPage( area, p ) {
        return area && area.transformation ? coordinateAreaToPage( area.parentLayer, transform( area.transformation, p ) ) : p;
    }

    function Canvas() {
        var canvas = Layer(),
            hasDraw = false, // 第一次绘制,第一次绘制前不会触发光标事件
            animateEvent = Event(); // 动画事件

        // 点事件系统
        function PointEventSystem( systemName, stateName, bindMove, bindDown, bindUp, onDown ) {
            var cursorX = 0, cursorY = 0,
                isIn = false, hoverList = [],
                lastStateName = "last" + stateName, curStateName = "is" + stateName;

            function getHoverList() {
                hoverList = [];

                // 根据坐标获取区域列表
                function getAreaListA( area ) {
                    if ( area ) {
                        hoverList.push( area );
                        loopArray( area.areaFromPoint ? [].concat( area.areaFromPoint.apply( null, coordinatePageToArea( area, [cursorX, cursorY, 1] ) ) ) : [], getAreaListA );
                    }
                }

                getAreaListA( canvas.root );
            }

            // 触发光标移动
            function cursorMove( event, targetHoverList ) {
                if ( !hasDraw ) {
                    return;
                }

                // 更新hoverList
                var oldHoverList = hoverList;
                loopArray( oldHoverList, function ( area ) {
                    area[lastStateName] = area[curStateName] || false;
                    area[curStateName] = false;
                } );

                targetHoverList ? ( hoverList = targetHoverList ) : getHoverList();

                // 计算move和enter
                loopArray( hoverList, function ( area ) {
                    area[curStateName] = true;
                    trigEvent( area, systemName + "Move", event );

                    if ( !area[lastStateName] ) {
                        trigEvent( area, systemName + "Enter", event );
                    }
                } );

                // 计算leave
                loopArray( oldHoverList, function ( area ) {
                    if ( !area[curStateName] ) {
                        trigEvent( area, systemName + "Leave", event );
                    }
                    delete area[lastStateName];
                } );
            }

            // 触发事件
            function trigEvent( area, eventName, event ) {
                area[eventName] && area[eventName]( event, cursorX, cursorY );
            }

            bindMove( canvas, cursorMove );

            // down事件,在触发时同时给出该次down事件的move事件和up事件
            bindDown( canvas, function ( event ) {
                onDown && onDown( event );
                event.preventDefault();
                loopArray( hoverList, function ( area ) {
                    var cursorMoveEvent = Event(), cursorUpEvent = Event(),
                        cursorMove = bindMove( document, function ( event ) {
                            event.preventDefault();
                            event.onMove = cursorMoveEvent.regist;
                            event.onUp = cursorUpEvent.regist;
                            cursorMoveEvent.trig( event, cursorX, cursorY );
                        } ),
                        cursorUp = bindUp( document, function ( event ) {
                            cursorMove.remove();
                            cursorUp.remove();
                            cursorUpEvent.trig( event, cursorX, cursorY );
                        } );

                    trigEvent( area, systemName + "Down", insert( event, {
                        onMove : cursorMoveEvent.regist,
                        onUp : cursorUpEvent.regist
                    } ) );
                } );
            } );

            bindUp( canvas, function ( event ) {
                loopArray( hoverList, function ( area ) {
                    trigEvent( area, systemName + "Up", event );
                } );
            } );

            return {
                focus : function () {
                    isIn = true;
                },
                blur : function () {
                    isIn = false;
                    cursorMove( event, [] );
                },
                move : function ( x, y ) {
                    cursorX = x;
                    cursorY = y;
                },
                calculate : function () {
                    isIn && cursorMove( {} );
                }
            };
        }

        // 光标系统
        var cursorSystem = PointEventSystem( "cursor", "Hover", onMouseMove, onMouseDown, onMouseUp );
        onMouseMove( document, function ( event ) {
            cursorSystem.move( event.pageX, event.pageY );
        }, true );
        onMouseOver( canvas, cursorSystem.focus );
        onMouseOut( canvas, cursorSystem.blur );

        // 触摸系统
        var touchSystem = PointEventSystem( "touch", "Touch", onTouchMove, onTouchStart, onTouchEnd, function ( event ) {
            touchSystem.focus();
            touchSystem.move( event.zPageX, event.zPageY );
            touchSystem.calculate();
        } );
        onTouchMove( document, function ( event ) {
            event.preventDefault();
            touchSystem.move( event.zPageX, event.zPageY );
        }, true );
        onTouchEnd( document, touchSystem.blur );

        // 重绘激励
        requestAnimate( function () {
            // 触发动画回调
            animateEvent.trig();

            if ( canvas.isDirty ) {
                canvas.isDirty = false;
                hasDraw = true;

                // 如果有根区域,绘制它
                canvas.root && canvas.draw( canvas.root.draw );

                // 更新光标系统
                cursorSystem.calculate();
                touchSystem.calculate();
            }
        } );

        // 如果改变了位置,重新计算canvas在页面上的位置
        function alter() {
            canvas.transformation = transform2d.translate( canvas.pageLeft, canvas.pageTop );
        }

        // 插入时计算位置
        browser.onInsert( canvas, alter );

        return insert( canvas, {
            root : null,
            alter : alter,
            requestAnimate : animateEvent.regist
        } );
    }

    exports.Context2D = Context2D;
    exports.Layer = Layer;
    exports.Area = Area;
    exports.Canvas = Canvas;
    exports.coordinatePageToArea = coordinatePageToArea;
    exports.coordinateAreaToPage = coordinateAreaToPage;
} );

/**
 * Created by 白 on 2014/8/5.
 */

zachModule( function ( require ) {
    var util = require( "zachUtil.js" ),
        Bezier = util.Bezier,

        browser = require( "zachBrowser.js" ),
        requestBrowserAnimate = browser.requestAnimate;

    var Timing = {
        linear : Bezier( 1, 1, 1, 1, function ( t ) {
            return t;
        } ),
        ease : Bezier( 0.25, 0.1, 0.25, 1 ),
        easeOut : Bezier( 0, 0, .58, 1 ),
        easeInOut : Bezier( 0.42, 0, 0.58, 1 )
    };

    function fromTo( from, to, ratio ) {
        return from + ( to - from ) * ratio;
    }

    // 进度器
    function Progress( arg ) {
        var duration = ( arg.duration || 1 ) * 1000, // 持续时间,传入的是秒数,转换为毫秒
            timing = arg.timing || Timing.ease, // 缓动函数
            progress = -( arg.delay || 0 ) * 1000, // 动画进度
            lastTime = new Date(); // 上帧时间

        return {
            // 计算当前比例
            ratio : function () {
                var now = new Date();
                progress += now - lastTime; // 更新进度
                lastTime = now;

                return progress < 0 ? null : timing( progress >= duration ? 1 : progress / duration );
            },
            // 判断进度是否结束
            isEnd : function () {
                return progress >= duration;
            },
            // 快进到目标比例
            progress : function ( targetRatio ) {
                progress = targetRatio * duration;
                lastTime = new Date()
            }
        };
    }

    function animate( arg, requestAnimate ) {
        var progress = Progress( arg ),
            isFirst = true,
            start = arg.start || 0, end = arg.end || 1;

        function go( ratio ) {
            if ( ratio !== null ) {
                if ( isFirst ) {
                    arg.onStart && arg.onStart();
                    isFirst = false;
                }

                arg.onAnimate( fromTo( start, end, ratio ) );

                if ( progress.isEnd() ) {
                    arg.onEnd && arg.onEnd();
                    animateEvent.remove();
                }
            }
        }

        go( 0 );
        var animateEvent = ( requestAnimate || requestBrowserAnimate )( function () {
            go( progress.ratio() );
        } );

        return {
            remove : animateEvent.remove,
            progress : progress.progress
        }
    }

    exports.Bezier = Bezier;
    exports.Timing = Timing;
    exports.Progress = Progress;
    exports.animate = animate;
    exports.fromTo = fromTo;
} );

/**
 * Created by 白 on 2014/8/4.
 * 封装经典的指针(鼠标/触摸)交互,诸如拖动等
 */

zachModule( function ( require ) {
    var Util = require( "zachUtil.js" ),
        insert = Util.insert,
        loopArray = Util.loopArray,
        Event = Util.Event,
        extend = Util.extend,

        Browser = require( "zachBrowser.js" ),
        bindEvent = Browser.bindEvent,

        SwipeRadius = 8, // 扫半径
        HMoveRatio = 0.8; // 横向移动比例

    // 平方和
    function sumOfSquares( x, y ) {
        return x * x + y * y;
    }

    // onPointerDown事件,统一光标事件和触摸事件
    function onPointerDown( area, response, bubble ) {
        if ( area.onTouchStart || area.onCursorDown ) {
            return ( ua.canTouch ? area.onTouchDown : area.onCursorDown )( response );
        }
        else {
            function bind( startEventName, moveEventName, endEventName ) {
                return bindEvent( area, startEventName, function ( event ) {
                    var pageX = event.zPageX, pageY = event.zPageY,
                        moveEvent = Event(), upEvent = Event();
                    var moveHandle = bindEvent( document, moveEventName, function ( event ) {
                        pageX = event.zPageX;
                        pageY = event.zPageY;

                        // 将move事件和end事件的注册指令添加到event中
                        event.onMove = moveEvent.regist;
                        event.onUp = upEvent.regist;

                        moveEvent.trig( event, pageX, pageY );
                    } );

                    var endHandle = bindEvent( document, endEventName, function ( event ) {
                        moveHandle.remove();
                        endHandle.remove();
                        upEvent.trig( event, pageX, pageY );
                    } );

                    // 将move事件和end事件的注册指令添加到event中
                    event.onMove = moveEvent.regist;
                    event.onUp = upEvent.regist;
                    response( event, pageX, pageY );
                }, bubble );
            }

            return ua.canTouch ? bind( "touchstart", "touchmove", "touchend" ) : bind( "mousedown", "mousemove", "mouseup" );
        }
    }

    function onPointerUp( el, callback, bubble ) {
        return bindEvent( el, ua.canTouch ? "touchend" : "mouseup", callback, bubble );
    }

    // sense事件,根据触摸是否到达了阈值判断是否触发响应
    function sense( area, arg ) {
        return onPointerDown( area, function ( event, startX, startY ) {
            arg.onSenseStart && arg.onSenseStart( event );

            // 抬起时的失败事件
            var senseFailureHandle = event.onUp( function ( event ) {
                arg.onSenseFailure && arg.onSenseFailure( event );
            } );

            var senseHandle = event.onMove( function ( event, pageX, pageY ) {
                // 判断是否移动到了sense阈值,如果移动到了,停止判断,触发senseTrue响应
                event.distanceX = pageX - startX;
                event.distanceY = pageY - startY;

                var result = arg.isOut( event );
                if ( result !== undefined ) {
                    // 移除sense事件和senseFailure事件,并触发senseTrue响应
                    senseHandle.remove();
                    senseFailureHandle.remove();
                    result && arg.onSenseSuccess && arg.onSenseSuccess( event, pageX, pageY );
                }
            } );
        } );
    }

    // 一般的senser,判断是否超出圆
    function outCircle( event ) {
        return sumOfSquares( event.distanceX, event.distanceY ) > SwipeRadius * SwipeRadius ? true : undefined;
    }

    // 在判断是否出圆的基础上添加方向判断
    function SwipeOut( isHorizontal ) {
        return function ( event ) {
            return outCircle( event ) ?
            ( Math.abs( event.distanceY ) / Math.sqrt( sumOfSquares( event.distanceX, event.distanceY ) ) >= HMoveRatio ) ^ isHorizontal :
                undefined;
        }
    }

    // tap事件,轻触屏幕时(不引起sense)触发,在sence域时有class tap
    function onTap( area, response, arg ) {
        return sense( area, extend( arg || {}, {
            isOut : outCircle,
            onSenseFailure : response
        } ) );
    }

    // 根据一个out判断生成拖动事件
    function Drag( isOut ) {
        return function ( area, dragStart ) {
            return sense( area, {
                isOut : isOut,
                onSenseSuccess : function ( event, pageX, pageY ) {
                    function Track( initialDistance, lastPos ) {
                        var lastDirection = initialDistance === 0 ? undefined : initialDistance > 0, track = [], trackTime = 0,
                            lastTime = +new Date(),
                            startPos = lastPos;

                        return {
                            // 去抖动
                            test : function ( curPos ) {
                                return lastDirection === undefined || !( ( curPos - lastPos ) * ( lastDirection ? 1 : -1 ) < -20 );
                            },
                            track : function ( curPos ) {
                                curPos = curPos || lastPos;

                                // 计算目标位置和当前方向
                                var curTime = +new Date(),
                                    duration = curTime - lastTime,
                                    curDirection = curPos === lastPos ? lastDirection : curPos > lastPos;

                                if ( curDirection !== lastDirection || duration > 200 ) {
                                    // 如果转向或者两次移动时间间隔超过200毫秒,重新计时
                                    track = [];
                                    trackTime = 0;
                                }
                                else {
                                    // 如果一次移动大于200,清空记录
                                    if ( duration > 200 ) {
                                        track = [];
                                        trackTime = 0;
                                    }
                                    else {
                                        trackTime += duration;

                                        // 如果记录时间超过300毫秒,移除头部部分记录,使其减少到300毫秒
                                        while ( trackTime > 300 ) {
                                            trackTime -= track.shift().duration;
                                        }

                                        track.push( {
                                            duration : duration,
                                            distance : curPos - lastPos
                                        } );
                                    }
                                }

                                // 更新数据
                                lastDirection = curDirection;
                                lastPos = curPos;
                                lastTime = curTime;
                            },
                            distance : function () {
                                return lastPos - startPos + initialDistance;
                            },
                            direction : function () {
                                return lastDirection;
                            },
                            speed : function () {
                                var totalDiff = 0;
                                loopArray( track, function ( unit ) {
                                    totalDiff += unit.distance;
                                } );
                                return trackTime === 0 ? 0 : ( totalDiff + 0 ) / trackTime;
                            }
                        };
                    }

                    var startTime = new Date(),
                        dragMoveEvent = Event(), // 拖拽移动事件
                        dragEndEvent = Event(), // 拖拽停止事件
                        trackX = Track( event.distanceX, pageX ),
                        trackY = Track( event.distanceY, pageY );

                    function dragInfo() {
                        return {
                            distanceX : trackX.distance(),
                            distanceY : trackY.distance(),
                            directionX : trackX.direction(),
                            directionY : trackY.direction()
                        }
                    }

                    // 拖动开始回调
                    dragStart( insert( dragInfo(), {
                        onDragEnd : dragEndEvent.regist,
                        onDragMove : dragMoveEvent.regist
                    } ) );

                    event.onMove( function ( event, pageX, pageY ) {
                        if ( trackX.test( pageX ) && trackY.test( pageY ) ) {
                            trackX.track( pageX );
                            trackY.track( pageY );

                            dragMoveEvent.trig( dragInfo() );
                        }
                    } );

                    event.onUp( function () {
                        trackX.track();
                        trackY.track();

                        // 触发拖动结束事件
                        dragEndEvent.trig( insert( dragInfo(), {
                            speedX : trackX.speed(),
                            speedY : trackY.speed(),
                            duration : +new Date() - startTime
                        } ) );
                    } );
                }
            } );
        };
    }

    exports.onPointerDown = onPointerDown;
    exports.onPointerUp = onPointerUp;
    exports.onDragH = Drag( SwipeOut( true ) );
    exports.onDragV = Drag( SwipeOut( false ) );
    exports.onDrag = Drag( outCircle );
    exports.onTap = onTap;
} );

/**
 * Created by 白 on 2014/8/4.
 */

zachModule( function ( require ) {
    var util = require( "zachUtil.js" ),
        insert = util.insert;

    function imgMeasure( img, sx, sy, sw, sh, dx, dy, dw, dh ) {
        var arr = [img, sx, sy, sw, sh, dx, dy, dw, dh];
        arr.img = img;
        arr.x = dx;
        arr.y = dy;
        arr.width = dw;
        arr.height = dh;
        return arr;
    }

    exports.drawImageMeasure = function ( gc, measure, x, y ) {
        gc.translate( x || 0, y || 0 );
        gc.drawImage.apply( gc, measure );
        gc.translate( -( x || 0 ), -( y || 0 ) );
    };

    // 测量覆盖width和height的图片
    exports.measureCover = function ( img, dWidth, dHeight ) {
        var dRatio = dWidth / dHeight,
            nWidth = img.naturalWidth || img.width || img.clientWidth,
            nHeight = img.naturalHeight || img.height || img.clientHeight,
            nRatio = nWidth / nHeight,
            sX, sY, sHeight, sWidth;

        // 计算居中缩放
        if ( dRatio < nRatio ) {
            sY = 0;
            sHeight = ua.ios ? nHeight - 1 : nHeight; // ios的迷之bug
            sX = ( nWidth - sHeight * dRatio ) / 2 << 0;
            sWidth = nWidth - 2 * sX << 0;
        }
        else {
            sX = 0;
            sWidth = ua.ios ? nWidth - 1 : nWidth;
            sY = ( nHeight - sWidth / dRatio ) / 2 << 0;
            sHeight = nHeight - 2 * sY << 0;
        }

        return imgMeasure( img, sX, sY, sWidth, sHeight, 0, 0, dWidth, dHeight );
    };

    // 测量适应width和height的图片
    exports.measureAdjust = function ( img, dWidth, dHeight, zoomIn ) {
        var dRatio = dWidth / dHeight,
            nWidth = img.naturalWidth || img.width || img.clientWidth,
            nHeight = img.naturalHeight || img.height || img.clientHeight,
            nRatio = nWidth / nHeight,
            tHeight, tWidth;

        // 图片比目标更宽,左右撑满,上下流白
        if ( dRatio < nRatio ) {
            tWidth = zoomIn ? dWidth : Math.min( nWidth, dWidth );
            tHeight = tWidth / nRatio;
        }
        // 目标比图片更宽,上下撑满,左右留白
        else {
            tHeight = zoomIn ? dHeight : Math.min( nHeight, dHeight );
            tWidth = tHeight * nRatio;
        }

        return imgMeasure( img, 0, 0, nWidth, nHeight, ( dWidth - tWidth ) / 2, ( dHeight - tHeight ) / 2, tWidth, tHeight );
    };

    // 测量适应宽度的图片
    exports.measureAdjustWidth = function ( img, dWidth ) {
        var nWidth = img.naturalWidth || img.width || img.clientWidth,
            nHeight = img.naturalHeight || img.height || img.clientHeight,
            tH = dWidth / nWidth * nHeight;

        return imgMeasure( img, 0, 0, nWidth, nHeight, 0, 0, dWidth, tH );
    };

    exports.sprite = function ( img, ratio ) {
        return insert( img, {
            part : function ( sX, sY, sW, sH ) {
                return {
                    sprite : img,
                    ratio : ratio,
                    x : sX / ratio,
                    y : sY / ratio,
                    width : sW / ratio,
                    height : sH / ratio,
                    draw : function ( gc, x, y ) {
                        return gc.drawImage( img, sX, sY, sW, sH, x, y, sW / ratio, sH / ratio );
                    }
                };
            }
        } );
    }
} );

/**
 * Created by 白 on 2014/8/25.
 */

zachModule( function ( require ) {
    var util = require( "zachUtil.js" ),
        loopObj = util.loopObj,
        loopString = util.loopString;

    // 根据字符串制作一个ascii表,返回一个函数,用来判断一个字符是否属于这个ascii表
    function AsciiMap( string ) {
        // 一个ascii表是一个bool[128]
        var asciiMaps = new Array( 128 );
        loopString( string, function ( ch ) {
            asciiMaps[ch] = true;
        } );

        return function ( ch ) {
            return ch < 128 && ch >= 0 && asciiMaps[ch] === true;
        };
    }

    var isTokenChar = AsciiMap( "_0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ" ), // 是否是Token字符
        isBlank = AsciiMap( "\t\n " ), // 是否是空白符
        escapeMapInQuote = {
            "\\" : "\\",
            "\"" : "\"",
            "'" : "'",
            "n" : "\n",
            "t" : "\t",
            "b" : "\b"
        }, // 字符串中的转义字符表
        escapeMapInXML = {
            "lt" : "<",
            "gt" : ">",
            "amp" : "&"
        }, // xml中的转移字符
        encodeMapInXML = function () {
            var retVal = {};
            loopObj( escapeMapInXML, function ( k, v ) {
                retVal[v] = k;
            } );
            return retVal;
        }(); // xml中的编码表

    function XMLReader( str ) {
        var cur = 0, ch;

        function eat() {
            ch = str.charAt( cur++ );
        }

        // 读token
        function readToken( firstChar ) {
            var retVal = firstChar || "";
            eat();
            while ( isTokenChar( ch.charCodeAt( 0 ) ) ) {
                retVal += ch;
                eat();
            }
            return retVal;
        }

        // 读取引号包起来的字符串
        function readQuoteString() {
            eat(); // 吃掉起始的引号
            var retVal = "";

            while ( ch !== '"' ) {
                // 如果读到了\,在读一个,转义
                if ( ch === "\\" ) {
                    eat();
                    retVal += escapeMapInQuote[ch];
                }
                // 否则直接加入到结果中
                else {
                    retVal += ch;
                }
                eat();
            }
            eat(); // 吃掉结束的引号

            return retVal;
        }

        // 读属性,返回这个标签有没有结束
        function readAttribute( obj ) {
            // 读空白符
            while ( isBlank( ch.charCodeAt( 0 ) ) ) {
                eat();
            }

            // 读到了">",起始标记
            if ( ch === ">" ) {
                eat();
                obj.$type = XMLReader.nodeType.startTag;
                return false;
            }
            // 读到了"/>",对象标记
            else if ( ch === "/" ) {
                eat();
                eat();
                obj.$type = XMLReader.nodeType.object;
                return false;
            }
            else {
                var attrName = readToken( ch );
                // 读掉空白符和等号
                while ( isBlank( ch.charCodeAt( 0 ) ) || ch === "=" ) {
                    eat();
                }
                obj[attrName] = readQuoteString();
                return true;
            }
        }

        function readTag() {
            var retVal = {};
            eat();

            // 如果上来就是/,这是一个结束标记
            if ( ch === "/" ) {
                retVal.$type = XMLReader.nodeType.endTag;
                retVal.$tagName = readToken();
                eat(); // 吃掉>
            }
            // 否则是起始标记或者对象
            else {
                retVal.$tagName = readToken( ch );
                while ( readAttribute( retVal ) ) {
                }
            }

            return retVal;
        }

        eat();
        return {
            read : function () {
                var retVal = {};

                // 读到末尾,返回null
                if ( ch === "" ) {
                    return null;
                }
                // 读到<,返回tag
                else if ( ch === "<" ) {
                    retVal = readTag();
                }
                // 否则读到了字符
                else {
                    // 读到了"&",xml的转义
                    if ( ch === "&" ) {
                        eat();
                        var escapeWord = "";
                        while ( ch !== ";" ) {
                            escapeWord += ch;
                            eat();
                        }
                        ch = escapeMapInXML[escapeWord];
                    }

                    retVal = {
                        $type : XMLReader.nodeType.character,
                        ch : ch
                    };
                    eat();
                }

                return retVal;
            }
        };
    }

    XMLReader.encodeString = function ( str ) {
        var retVal = "";
        loopString( str, function ( ch ) {
            var escape = encodeMapInXML[ch];
            retVal += escape ? "&" + escape + ";" : ch;
        }, true );
        return retVal;
    };

    XMLReader.nodeType = {
        startTag : 0,
        endTag : 1,
        character : 2,
        object : 3
    };

    exports.XMLReader = XMLReader;
} );

zachModule( function ( require ) {

    var ZU = require( "zachUtil.js" );
    var insert = ZU.insert;
    var extend = ZU.extend;

    var LinkedList = function(){
        var list = ZU.LinkedList();
        list.push = function(value){
            var node = list.node(value);
            list.insert(node,null);
            return node;
        };
        list.pop = function(){
            var node = list.tail();
            list.remove(node);
            return node.value;
        };
        return list;
    };
    var loopList = ZU.LinkedList.loop;

    function extract(def,obj){
        var rst = {};
        for(var p in def){
            rst[p] = obj[p]===undefined? def[p]:obj[p];
        }
        return rst;
    }

    var ZX = require("zachXML.js");
    var XMLReader = ZX.XMLReader;
    var XML_NODE_TYPE = {
        START_TAG: XMLReader.nodeType.startTag,
        END_TAG: XMLReader.nodeType.endTag,
        CHARACTER: XMLReader.nodeType.character
    };

    var measureGC = document.createElement( "canvas" ).getContext( "2d" );

    // region common
    //===============================================================================

    var DEFAULT_STYLE = {
        fontFamily: "",
        fontSize: 0,
        fontWeight: "",
        fontVariant: "",
        fontStyle: "",
        textFillStyle: null,
        textStrokeStyle: null
    };

    var DEFAULT_FORMAT = {
        marginTop: 0,
        marginBottom: 0,
        lineBackground: null,
        lineHeight: 0,
        lineGap: 0,
        textIndent: 0,
        textBaseline: ""
    };

    //make font string
    function style2font(style){
        return [style.fontStyle,style.fontVariant,style.fontWeight,style.fontSize+"px",style.fontFamily].join(" ");
    }

    var prefix = /^[（【“‘]$/;
    var suffix = /^[）】”’，。；：？！、]$/;
    var connector = /^[0-9a-zA-Z`~!@#\$%\^&\*\(\)\-_=\+\[\{\]\}\\\|:;"'<,>\.\?\/]$/;
    var blank = /^[     　\n]$/;

    // endregion

    // region contents
    //==================================================================================

    //element
    var ELEMENT_TYPE = {
        SPAN: -1,
        UNKNOWN: 0,
        CHARACTER: 1
    };
    function Element(){
        return {
            type: ELEMENT_TYPE.UNKNOWN
        };
    }
    //character element
    function CharacterElement(){
        return {
            type: ELEMENT_TYPE.CHARACTER,
            width: null,
            character: ""
        };
    }
    //span element
    function SpanElement(){
        return {
            type: ELEMENT_TYPE.SPAN,
            clas: null
        };
    }

    //content
    function Content(){
        return {
            rootNode: null,
            clas: null
        };
    }

    // endregion

    // region blocks
    //====================================================================================

    //position has w/h & x/y
    function Position(){
        return {
            width: 0,
            height: 0,
            offsetX: 0,
            offsetY: 0
        };
    }

    function Brick(){
        return insert(Position(), {
            style: null,
            elements: LinkedList(),
            text: ""
        });
    }

    function Line(){
        return insert(Position(), {
            bricks: LinkedList()
        });
    }

    function Block(){
        return insert(Position(), {
            bricks: LinkedList(),
            format: null,
            lines: LinkedList()
        });
    }

    // endregion

    // region tree
    //==================================================================================

    function TreeNode(){
        return {
            parent: null,
            children: null
        };
    }

    function treeNode(parentNode,value){
        var node = parentNode.children.node(value);
        insert(node,TreeNode());
        return node;
    }

    function treeInsert(parentNode,node,position){
        node.parent = parentNode;
        return parentNode.children.insert(node,position);
    }

    function treeRemove(node){
        node.parent.children.remove(node);
        node.parent = null;
    }

    // region view tree
    //==================================================================================================

    //find siblings
    function FindSiblingNode(direction){
        return function(node){
            var currentNode = node;
            while(currentNode!==null){
                var nextNode = direction? currentNode.next:currentNode.previous;
                if(nextNode!==null){
                    currentNode = nextNode;
                    break;
                }
                currentNode = currentNode.parent;
            }
            return currentNode;
        };
    }
    var findNextNode = FindSiblingNode(true);
    var findPreviousNode = FindSiblingNode(false);

    //find leaves
    function FindChildNode(direction){
        return function(node){
            var currentNode = node;
            while(currentNode!==null&&currentNode.children!==null){
                currentNode = direction? currentNode.children.tail():currentNode.children.head();
            }
            return currentNode;
        }
    }
    var findLastBrickNode = FindChildNode(true);
    var findFirstBrickNode = FindChildNode(false);

    // endregion

    // region mod tree
    //==================================================================================================

    function initRootNode(element){
        var parentNode = TreeNode();
        parentNode.children = LinkedList();
        var node = treeNode(parentNode,element);
        node.children = LinkedList();
        element.nodeRef = node;
        return node;
    }

    function pushTreeNode(parentNode,element){
        var node = treeNode(parentNode,element);
        treeInsert(parentNode,node,null);
        element.nodeRef = node;
        return node;
    }

    // endregion

    //region serialization
    //================================================================================

    function parseRichText(str){
        //result
        var contents = LinkedList();
        //states
        var reader = XMLReader(str);
        var current = reader.read();
        //iterate
        while(current!==null){
            //<p> read paragraph
            if(current.$type===XML_NODE_TYPE.START_TAG&&current.$tagName==="p"){
                //create content
                var content = Content();
                contents.push(content);
                //set content value
                content.clas = current["class"];
                //root node
                var element = Element();
                initRootNode(element);
                content.rootNode = initRootNode(element);
                //root span node
                var currentSpanNode = content.rootNode;
                insert(element,SpanElement());
                element.clas = current["class"];
                //read <p>
                current = reader.read();
                //read content
                while(current!==null){
                    //read <s>
                    if(current.$type===XML_NODE_TYPE.START_TAG&&current.$tagName==="s"){
                        //new span element
                        element = Element();
                        insert(element,SpanElement());
                        element.clas = current["class"];
                        currentSpanNode = pushTreeNode(currentSpanNode,element);
                        currentSpanNode.children = LinkedList();
                        //step on
                        current = reader.read();
                    }
                    //read </s>
                    else if(current.$type===XML_NODE_TYPE.END_TAG&&current.$tagName==="s"){
                        //back
                        currentSpanNode = currentSpanNode.parent;
                        //step on
                        current = reader.read();
                    }
                    //read char
                    else if(current.$type===XML_NODE_TYPE.CHARACTER){
                        //new character element
                        element = Element();
                        insert(element,CharacterElement());
                        element.character = current.ch;
                        pushTreeNode(currentSpanNode,element);
                        //step on
                        current = reader.read();
                    }
                    //read </p>
                    else if(current.$type===XML_NODE_TYPE.END_TAG&&current.$tagName==="p"){
                        //step on
                        current = reader.read();
                        //stop content
                        break;
                    }
                    //error
                    else{
                        throw new Error("unexpected tag");
                    }
                }
            }
            //skip blanks
            else if(current.$type==XML_NODE_TYPE.CHARACTER&&blank.test(current.ch)){
                current = reader.read();
            }
            //other tag end
            else{
                break;
            }
        }
        return contents;
    }

    // endregion

    //region measure
    //==========================================================================================

    function buildBlock(tc,content){
        //block
        var block = Block();
        //format
        block.format = extract(DEFAULT_FORMAT,tc.p[content.clas]||{});
        //styles
        var currentStyles = LinkedList();
        currentStyles.push(DEFAULT_STYLE);
        var rootStyle = extract(DEFAULT_STYLE,tc.p[content.rootNode.value.clas]||{});
        currentStyles.push(rootStyle);
        measureGC.font = style2font(rootStyle);
        //states
        var currentBrick = null;
        //iterate element tree for bricks
        var currentControlNode = content.rootNode;
        var currentNode = content.rootNode.children.head();
        while(currentControlNode!==null){
            if(currentNode===null){
                var currentControl = currentControlNode.value;
                //end current control
                currentStyles.pop();
                measureGC.font = style2font(currentStyles.tail().value);
                //go to parent
                currentNode = currentControlNode.next;
                currentControlNode = currentControlNode.parent;
            }
            else{
                var currentElement = currentNode.value;
                //control element
                if(currentElement.type===ELEMENT_TYPE.SPAN){
                    //save current
                    currentStyles.push(extend(currentStyles.tail().value,tc.s[currentElement.clas]||{}));
                    measureGC.font = style2font(currentStyles.tail().value);
                    //move on
                    currentControlNode = currentNode;
                    currentNode = currentNode.children.head();
                }
                //brick element
                else{
                    //find or create brick
                    if(currentBrick===null){
                        currentBrick = Brick();
                        block.bricks.push(currentBrick);
                        currentBrick.style = currentStyles.tail().value;
                    }
                    //save element
                    currentBrick.elements.push(currentElement);
                    //save character value
                    currentElement.width = currentElement.width===null? measureGC.measureText(currentElement.character).width:currentElement.width;
                    currentBrick.width += currentElement.width;
                    currentBrick.text += blank.test(currentElement.character)? "":currentElement.character;
                    //continue brick
                    if( //go up
                        currentNode.next===null||
                        //end text
                        currentNode.next.value.type!==ELEMENT_TYPE.CHARACTER||
                        //not either connection
                        !(  //prefix is not the end
                            prefix.test(currentElement.character)&&!blank.test(currentNode.next.value.character)||
                            //suffix is not the begin
                            suffix.test(currentNode.next.value.character)&&!blank.test(currentElement.character)||
                            //connectors connect
                            connector.test(currentElement.character)&&connector.test(currentNode.next.value.character)||
                            //blanks connect
                            blank.test(currentElement.character)&&blank.test(currentNode.next.value.character))){
                        //end brick
                        currentBrick = null;
                    }
                    //move on
                    currentNode = currentNode.next;
                }
            }
        }
        //return
        return block;
    }

    function flowBlock(block,width){
        //init block
        block.width = width;
        //line offset
        var currentOffsetX = block.format.textIndent;
        //new line
        var currentLine = Line();
        block.lines.push(currentLine);
        //iterate bricks for lines
        loopList(block.bricks,function(brick){
            //new line
            if( //no line
                currentLine===null||
                //beyond width
                currentOffsetX+brick.width>width&&
                //not first
                currentLine.bricks.head()!==null&&
                //not blank
                brick.text!==""){
                //reset offset
                currentOffsetX = 0;
                //new line
                currentLine = Line();
                block.lines.push(currentLine);
            }
            //save brick
            currentLine.bricks.push(brick);
            //set brick value
            currentOffsetX += brick.width;
        });
        //shrink line
        loopList(block.lines,function(line){
            //pop blank
            var currentNode = line.bricks.tail();
            while(currentNode!==null&&currentNode.value.text===""){
                line.bricks.pop();
                currentNode = line.bricks.tail();
            }
        });
        //update y
        block.height = 0;
        loopList(block.lines,function(line){
            //line y
            line.height = block.format.lineHeight+block.format.lineGap;
            line.offsetY = block.height+block.format.lineHeight;
            //block y
            block.height += line.height;
            //brick y
            loopList(line.bricks,function(brick){
                brick.offsetY = line.offsetY;
            });
        });
        //update x
        loopList(block.lines,function(line,node){
            //line x
            line.offsetX = node.previous===null? block.format.textIndent:0;
            //line width
            line.width = width-line.offsetX;
            if(node.next===null){
                line.width = 0;
                loopList(line.bricks,function(brick){
                    line.width += brick.width;
                });
            }
            //total space
            var totalSpace = 0;
            var totalCount = 0;
            if(node.next!==null){
                totalSpace = line.width;
                loopList(line.bricks,function(brick){
                    totalSpace -= brick.width;
                    totalCount++;
                });
            }
            //brick x
            var space = totalCount>1? totalSpace/(totalCount-1):0;
            var offsetX = line.offsetX;
            var spaceCount = 0;
            loopList(line.bricks,function(brick){
                brick.offsetX = offsetX+(space*spaceCount+0.5)<<0;
                offsetX += brick.width;
                spaceCount++;
            });
        });
    }

    function flowBlocks(blocks){
        blocks.width = 0;
        blocks.height = 0;
        loopList(blocks,function(block){
            //update x
            blocks.width = Math.max(blocks.width,block.width);
            //update y
            blocks.height += block.format.marginTop;
            block.offsetY = blocks.height;
            blocks.height += block.height;
            blocks.height += block.format.marginBottom;
        });
    }

    function measureRichText(tc,contents,width){
        var blocks = LinkedList();
        loopList(contents,function(content){
            var block = buildBlock(tc,content);
            blocks.push(block);
            flowBlock(block,width);
        });
        flowBlocks(blocks);
        return blocks;
    }

    // endregion

    // region draw
    //==============================================================================================

    function drawBlock(gc,block){
        gc.textBaseline = block.format.textBaseline;
        loopList(block.lines,function(line){
            //draw bg
            if(block.format.lineBackground!==null){
                gc.fillStyle = block.format.lineBackground;
                gc.fillRect(line.offsetX,line.offsetY-line.height+block.format.lineGap,line.width,line.height);
            }
            //draw brick
            loopList(line.bricks,function(brick){
                gc.font = style2font(brick.style);
                //draw text
                if(brick.style.textFillStyle!==null){
                    gc.fillStyle = brick.style.textFillStyle;
                    gc.fillText(brick.text,brick.offsetX,brick.offsetY);
                }
                if(brick.style.textStrokeStyle!==null){
                    gc.strokeStyle = brick.style.textStrokeStyle;
                    gc.strokeText(brick.text,brick.offsetX,brick.offsetY);
                }
            });
        });
    }

    function drawBlocks(gc,blocks){
        loopList(blocks,function(block){
            gc.save();
            gc.translate(block.offsetX,block.offsetY);
            drawBlock(gc,block);
            gc.restore();
        });
    }

    // endregion

    exports.DEFAULT_STYLE = DEFAULT_STYLE;
    exports.DEFAULT_FORMAT = DEFAULT_FORMAT;

    exports.style2font = style2font;

    exports.ELEMENT_TYPE = ELEMENT_TYPE;

    exports.TreeNode = TreeNode;
    exports.treeNode = treeNode;
    exports.treeInsert = treeInsert;
    exports.treeRemove = treeRemove;

    exports.findNextNode = findNextNode;
    exports.findPreviousNode = findPreviousNode;
    exports.findLastBrickNode = findLastBrickNode;
    exports.findFirstBrickNode = findFirstBrickNode;

    exports.parseRichText = parseRichText;

    exports.buildBlock = buildBlock;
    exports.flowBlock = flowBlock;
    exports.flowBlocks = flowBlocks;
    exports.measureRichText = measureRichText;

    exports.drawBlock = drawBlock;
    exports.drawBlocks = drawBlocks;
    exports.drawRichText = drawBlocks;

});

/**
 * Created by 白 on 2014/10/29.
 * 封装一些DOM经典复用
 */

zachModule( function ( require ) {
    var util = require( "zachUtil.js" ),
        is = util.is,
        loopArray = util.loopArray,
        loopObj = util.loopObj,
        insert = util.insert,
        tupleString = util.tupleString,
        LinkedList = util.LinkedList,

        browser = require( "zachBrowser.js" ),
        css = browser.css,
        bindEvent = browser.bindEvent;

    // region 浏览器检测
    (function ( ua, appVersion, platform ) {
        insert( window.ua, {
            // win系列
            win32 : platform === "Win32",
            ie : /MSIE ([^;]+)/.test( ua ),
            ieVersion : Math.floor( (/MSIE ([^;]+)/.exec( ua ) || [0, "0"])[1] ),

            // ios系列
            ios : (/iphone|ipad/gi).test( appVersion ),
            iphone : (/iphone/gi).test( appVersion ),
            ipad : (/ipad/gi).test( appVersion ),
            iosVersion : parseFloat( ('' + (/CPU.*OS ([0-9_]{1,5})|(CPU like).*AppleWebKit.*Mobile/i.exec( ua ) || [0, ''])[1])
                .replace( 'undefined', '3_2' ).replace( '_', '.' ).replace( '_', '' ) ) || false,
            safari : /Version\//gi.test( appVersion ) && /Safari/gi.test( appVersion ),
            uiWebView : /(iPhone|iPod|iPad).*AppleWebKit(?!.*Safari)/i.test( ua ),

            // 安卓系列
            android : (/android/gi).test( appVersion ),
            androidVersion : parseFloat( "" + (/android ([0-9\.]*)/i.exec( ua ) || [0, ''])[1] ),

            // chrome
            chrome : /Chrome/gi.test( ua ),
            chromeVersion : parseInt( ( /Chrome\/([0-9]*)/gi.exec( ua ) || [0, 0] )[1], 10 ),

            // 内核
            webkit : /AppleWebKit/.test( appVersion ),

            // 其他浏览器
            uc : appVersion.indexOf( "UCBrowser" ) !== -1,
            Browser : / Browser/gi.test( appVersion ),
            MiuiBrowser : /MiuiBrowser/gi.test( appVersion ),

            // 微信
            MicroMessenger : ua.toLowerCase().match( /MicroMessenger/i ) == "micromessenger"
        } );
    })( navigator.userAgent, navigator.appVersion, navigator.platform );
    // endregion

    // region css
    insert( nonstandardStyles, {
        transform : ["-webkit-transform", "-ms-transform", "transform"],
        animation : ["-webkit-animation"],
        transition : ["-webkit-transition", "transition"],
        "backface-visibility" : ["-webkit-backface-visibility", "-mozila-backface-visibility", "backface-visibility"],
        "transform-style" : ["-webkit-transform-style", "transform-style"],
        perspective : ["-webkit-perspective", "perspective"]
    } );

    // 生成CSS样式字符串
    function cssRuleString( cssStyles ) {
        var ruleText = "";
        loopObj( cssStyles, function ( styleName, styleValue ) {
            function addRule( styleName ) {
                ruleText += styleName + ":" + styleValue + ";";
            }

            styleName in nonstandardStyles ? loopArray( nonstandardStyles[styleName], addRule ) : addRule( styleName );
        } );
        return ruleText;
    }

    // 移除CSS值,可以移除一条,或者移除一组
    function removeCss( el, styleName ) {
        function removeStyle( styleName ) {
            function doStyle( styleName ) {
                el.style.removeProperty( styleName );
            }

            styleName in nonstandardStyles ? loopArray( nonstandardStyles[styleName], doStyle ) : doStyle( styleName );
        }

        is.String( styleName ) ? removeStyle( styleName ) : is.Array( styleName ) ? loopArray( styleName, removeStyle ) :
            loopObj( styleName, removeStyle );
        return el;
    }

    // 添加CSS规则
    var insertCSSRule = function () {
        var userSheet = LinkedList(), systemSheet = LinkedList();
        return function ( ruleText, isSystem ) {
            var styleSheet = isSystem ? systemSheet : userSheet; // 选择样式链表

            // 如果节点尚未创建,创建节点,系统样式表在所有样式表的最前,用户样式表在所有样式表的最后
            if ( styleSheet.el === undefined ) {
                styleSheet.el = document.head.insertBefore( document.createElement( "style" ), isSystem ? document.head.firstChild : null );
            }

            // 创建新规则,位置上最后规则+1
            var lastRule = styleSheet.tail(),
                newRule = styleSheet.insert( styleSheet.node( lastRule === null ? 0 : lastRule.value + 1 ), null );

            styleSheet.el.sheet.insertRule( ruleText, newRule.value );

            return {
                remove : function () {
                    // 后面所有元素的位置-1
                    var pos = newRule.value;
                    for ( var curNode = newRule.next; curNode !== null; curNode = curNode.next ) {
                        curNode.value = pos++;
                    }

                    // 移除节点并删除规则
                    styleSheet.remove( newRule );
                    styleSheet.el.sheet.deleteRule( pos );
                }
            };
        }
    }();

    function insertCSSRules( arg1, arg2, arg3 ) {
        function insertRules( selector, styles, isSystem ) {
            var cssText = is.String( styles ) ? styles : cssRuleString( styles );
            insertCSSRule( selector + " {" + cssText + "}", isSystem );
        }

        if ( is.String( arg1 ) ) {
            insertRules( arg1, arg2, arg3 );
        }
        else {
            loopObj( arg1, function ( selector, styles ) {
                insertRules( selector, styles, arg2 );
            } );
        }
    }

    function n( n ) {
        return Math.abs( n ) < 0.01 ? 0 : n;
    }

    css.transform = function () {
        var style = [];
        loopArray( arguments, function ( transform, i ) {
            i !== 0 && style.push( transform );
        } );
        css( arguments[0], "transform", style.join( " " ) );
    };

    css.translate = function ( x, y, z ) {
        return tupleString( "translate3d", [n( x ) + "px", n( y ) + "px", n( z ) + "px"] );
    };

    function Rotate( name ) {
        return function ( val, unit ) {
            return tupleString( name, [n( val ) + ( unit || "rad" )] );
        };
    }

    css.rotateX = Rotate( "rotateX" );
    css.rotateY = Rotate( "rotateY" );
    css.rotateZ = Rotate( "rotateZ" );

    css.scale = function () {
        return "scale(" + Array.prototype.join.call( arguments, "," ) + ")";
    };

    css.px = function ( value ) {
        return value === 0 ? 0 : n( value ) + "px";
    };

    css.s = function ( value ) {
        return value === 0 ? 0 : n( value ) + "s";
    };

    css.url = function ( url ) {
        return "url(" + url + ")";
    };

    // 过渡
    function transition( el, transition, style, styleValue, onEnd ) {
        onEnd = is.String( style ) ? onEnd : styleValue;

        if ( ua.android && ua.androidVersion < 3 ) {
            css( el, style, styleValue );
            onEnd && onEnd();
        }
        else {
            css( el, "transition", transition );
            el.transition && el.transition.remove();

            function end() {
                if ( el.transition ) {
                    transitionEnd.remove();
                    removeEvent.remove();
                    removeCss( el, "transition" );
                    onEnd && onEnd();
                    el.transition = null;
                }
            }

            var removeEvent = bindEvent( el, "DOMNodeRemovedFromDocument", end ),
                transitionEnd = el.transition = bindEvent( el, "webkitTransitionEnd", end );

            setTimeout( function () {
                css( el, style, styleValue );
            }, 20 );
        }
    }

    // endregion

    // region DOM
    // 从文档中移除元素
    function removeNode( node ) {
        node && node.parentNode && node.parentNode.removeChild( node );
    }

    // 创建一个元素的快捷方式
    function element( arg1, arg2, arg3 ) {
        var el, elementArg = {}, parent = arg3;

        // 如果是<div></div>这种形式,直接制作成元素
        if ( arg1.charAt( 0 ) === "<" ) {
            el = document.createElement( "div" );
            el.innerHTML = arg1;
            el = el.firstElementChild;
        }
        // 否则是div.class1.class2#id这种形式
        else {
            var classIdReg = /([.#][^.#]*)/g, classId;
            el = document.createElement( arg1.split( /[#.]/ )[0] );
            while ( classId = classIdReg.exec( arg1 ) ) {
                classId = classId[0];
                classId.charAt( 0 ) === "#" ? el.id = classId.substring( 1 ) : el.classList.add( classId.substring( 1 ) );
            }
        }

        // 参数2是字符串,作为innerHTML
        if ( is.String( arg2 ) ) {
            el.innerHTML = arg2;
        }
        // 是对象的话,每个字段处理
        else if ( is.Object( arg2 ) ) {
            elementArg = arg2;
        }
        // 如果是数组,视为子元素
        else if ( is.Array( arg2 ) ) {
            elementArg.children = arg2;
        }
        // 否则视为父元素
        else {
            parent = arg2;
        }

        elementArg && loopObj( elementArg, function ( key, value ) {
            if ( value !== undefined ) {
                switch ( key ) {
                    case "classList":
                        if ( is.String( value ) ) {
                            el.classList.add( value );
                        }
                        else if ( is.Array( value ) ) {
                            loopArray( value, function ( className ) {
                                el.classList.add( className );
                            } );
                        }
                        break;
                    case "css":
                        css( el, value );
                        break;
                    case "children":
                        if ( is.Array( value ) ) {
                            loopArray( value, function ( node ) {
                                el.appendChild( node );
                            } );
                        }
                        else {
                            el.appendChild( value );
                        }
                        break;
                    default:
                        if ( key.substring( 0, 5 ) === "data-" ) {
                            el.setAttribute( key, value );
                        }
                        else {
                            el[key] = value;
                        }
                        break;
                }
            }
        } );

        parent && parent.appendChild( el );
        return el;
    }

    // 根据flag添加或删除class
    function switchClass( el, flag, className ) {
        flag ? el.classList.add( className ) : el.classList.remove( className );
    }

    // 切换状态,将class从fromState切换到toState
    function toggleState( el, fromState, toState ) {
        el.classList.remove( fromState );
        el.classList.add( toState );
    }

    // 沿着一个元素向上冒泡,直到root/document,回调每个节点
    function bubble( el, func, root ) {
        var val;
        while ( el !== null && el !== document && el !== root ) {
            if ( val = func( el ) ) {
                return val;
            }
            el = el.parentNode;
        }
    }

    // 当一个事件冒泡到document时,回调冒泡中的每个节点
    function onBubble( eventName, response ) {
        document.addEventListener( eventName, function ( event ) {
            bubble( event.target, function ( node ) {
                response( node );
            }, document.documentElement );
        }, false );
    }

    // 寻找祖先节点
    function findAncestor( el, func ) {
        return bubble( el, function ( el ) {
            if ( func( el ) ) {
                return el;
            }
        } );
    }

    // 焦点时设置focus类
    browser.onLoad( function () {
        onBubble( "focusin", function ( node ) {
            node.classList.add( "focus" );
        } );
        onBubble( "focusout", function ( node ) {
            node.classList.remove( "focus" );
        } );
    } );
    // endregion

    // region 导出
    // css
    exports.removeCss = removeCss;
    exports.cssRuleString = cssRuleString;
    exports.insertCSSRule = insertCSSRule;
    exports.insertCSSRules = insertCSSRules;
    exports.transition = transition;

    // DOM
    exports.element = element;
    exports.removeNode = removeNode;
    exports.toggleState = toggleState;
    exports.switchClass = switchClass;
    exports.bubble = bubble;
    exports.onBubble = onBubble;
    exports.findAncestor = findAncestor;

    // 导出
    exports.css = css;
    exports.toAbsURL = browser.toAbsURL;
    exports.bindEvent = bindEvent;
    exports.Bind = browser.Bind;
    exports.onInsert = browser.onInsert;
    exports.requestAnimate = browser.requestAnimate;
    exports.ajax = browser.ajax;
    exports.onLoad = browser.onLoad;
    // endregion
} );

/**
 * Created by 白 on 2014/11/19.
 * 经典的滑动面板
 */

zachModule( function ( require ) {
    var util = require( "zachUtil.js" ),
        insert = util.insert,
        extend = util.extend,
        range = util.range,
        Event = util.Event,
        loop = util.loop,
        loopArray = util.loopArray,

        DOM = require( "zachDOM.js" ),
        css = DOM.css,
        px = css.px,

        animate = require( "zachAnimate.js" ),
        pointer = require( "zachPointer.js" );

    function translate( el, left, top ) {
        css( el, "transform", css.translate( el.zLeft = left, el.zTop = top, 0 ) );
    }

    function SlideListPanel( parent, arg ) {
        if ( !SlideListPanel.hasCall ) {
            DOM.insertCSSRules( {
                ".z-slide-list-panel" : {
                    overflow : "hidden",
                    position : "relative"
                },
                ".z-slide-list-panel > ul" : {
                    height : "100%",
                    overflow : "hidden"
                },
                ".z-slide-list-panel > ul > li" : {
                    height : "100%",
                    "float" : "left",
                    "min-height" : "1px"
                }
            }, true );
            SlideListPanel.hasCall = true;
        }

        parent.classList.add( "z-slide-list-panel" );

        arg = extend( {
            width : 1,
            cycle : false,
            slideRatio : 1,
            margin : 0
        }, arg || {} );

        var ul = parent.querySelector( "ul" ), items = [],
            isCycle = arg.cycle, slideRatio = arg.slideRatio, margin = arg.margin,
            slideDistance = arg.width + margin,
            disabled = false,

            cutToEvent = Event(),
            animateStartEvent = Event(),
            slideStartEvent = Event(),

            parentWidth, itemWidth, marginWidth, maxItems,

            curCenterIndex = 0,
            inAnimate = false;

        DOM.onInsert( parent, function () {
            parentWidth = parent.offsetWidth;
            itemWidth = parentWidth * arg.width;
            marginWidth = parentWidth * margin;

            // 根据宽度计算最大项数
            maxItems = function () {
                var start = 1;
                while ( arg.width * start + margin * (start - 2) < 1 ) {
                    start += 2;
                }
                return start + slideRatio * 2;
            }();

            css( ul, {
                width : px( maxItems * itemWidth + ( margin > 0 ? marginWidth * maxItems : 0 ) ),
                "margin-left" : px( -( maxItems * arg.width - 1 ) / 2 * parentWidth )
            } );
        } );

        if ( ua.win32 ) {
            pointer.onPointerDown( parent, function ( event ) {
                event.preventDefault();
            } );
        }

        // 将ul的子节点添加到items中,并移除它们
        loopArray( ul.children, function ( li ) {
            items.push( li );
        } );
        ul.innerHTML = "";

        function getIndex( index ) {
            return isCycle ? ( index + items.length ) % items.length : index;
        }

        function slideLi( li, isFirst ) {
            css( li, "width", px( itemWidth ) );
            margin && css( li, "margin", "0 " + px( marginWidth / 2 ) );
            isFirst && margin && css( li, px( "margin-left", -marginWidth * ( maxItems - 1 ) / 2 ) );
            return li;
        }

        // 调整元素
        function adjust() {
            var lay = arg.lay,
                centerDiff = -Math.floor( ( ul.zLeft + itemWidth / 2 ) / itemWidth );

            arg.lay && loopArray( ul.children, function ( li, i ) {
                var centerI = ( i - ( maxItems - 1 ) / 2 );

                !li.zEmpty && lay( li, {
                    index : centerI - centerDiff,
                    offset : centerI * ( itemWidth + marginWidth ) + ul.zLeft,
                    width : itemWidth
                } );
            } );
        }

        // 将centerIndex为中心,摆放元素
        function lay( centerIndex ) {
            ul.innerHTML = "";

            function emptyLi() {
                var li = document.createElement( "li" );
                li.zEmpty = true;
                return li;
            }

            translate( ul, 0, 0 );
            loop( maxItems, function ( i ) {
                var targetIndex = i - (maxItems - 1) / 2 + centerIndex,
                    li = isCycle && items.length <= 2 ? i === 1 ? items[centerIndex] : emptyLi() :
                    items[getIndex( targetIndex )] || emptyLi();
                ul.appendChild( slideLi( li, i === 0 ) );
            } );
            adjust();
            curCenterIndex = centerIndex;

            cutToEvent.trig( {
                curIndex : centerIndex
            } );
        }

        function doWhenItem2( targetPos ) {
            // 取出副项和两个空项,showBlankLi是要显示的,hiddenBlankLi是未显示的
            var subLi = items[getIndex( curCenterIndex + 1 )],
                showBlankLi = ul.children[targetPos > 0 ? 0 : 2],
                hiddenBlankLi = ul.children[targetPos < 0 ? 0 : 2];

            // 如果要显示的不是副项,替换为副项
            if ( showBlankLi !== subLi ) {
                ul.replaceChild( slideLi( document.createElement( "li" ) ), hiddenBlankLi );
                ul.replaceChild( slideLi( subLi ), showBlankLi );
            }
        }

        function cutTo( step ) {
            if ( items.length === 1 ) {
                return;
            }

            var endIndex = range( getIndex( curCenterIndex + step ), 0, items.length - 1 );

            inAnimate = true;
            animateStartEvent.trig( {
                curIndex : curCenterIndex,
                targetIndex : endIndex
            } );

            if ( isCycle && items.length === 2 ) {
                doWhenItem2( -step );
            }

            function endAnimate() {
                lay( endIndex );
                inAnimate = false;
            }

            if ( arg.lay ) {
                animate( {
                    start : ul.zLeft,
                    end : ( curCenterIndex - endIndex ) * ( itemWidth + marginWidth ),
                    onAnimate : function ( pos ) {
                        translate( ul, pos << 0, 0 );
                        adjust( itemWidth );
                    },
                    onEnd : endAnimate,
                    duration : 0.2
                } );
            }
            else {
                DOM.transition( ul, "0.2s", {
                    transform : css.translate( ( isCycle ? -step : ( curCenterIndex - endIndex ) ) * ( 1 + margin ) * itemWidth, 0, 0 )
                }, endAnimate );
            }
        }

        pointer.onDragH( ul, function ( event ) {
            if ( disabled || inAnimate || ( isCycle && items.length === 1 ) ) {
                return;
            }

            slideStartEvent.trig( {
                curIndex : curCenterIndex
            } );

            event.onDragMove( function ( event ) {
                var targetPos = event.distanceX,
                    length = items.length;

                if ( !isCycle && ( ( curCenterIndex === 0 && targetPos > 0 ) || ( curCenterIndex === length - 1 && targetPos < 0 ) ) ) {
                    targetPos = Math.atan( targetPos / parentWidth / 2 ) * parentWidth * arg.width / 2;
                }
                // 当列表循环,并且只有两项的时候,往左滑时,副项在右,往右滑时,副项在左
                else if ( isCycle && length === 2 ) {
                    doWhenItem2( targetPos );
                }

                translate( ul, range( targetPos, -parentWidth + 2, parentWidth - 2 ) * slideDistance, 0 );
                adjust();
            } );

            event.onDragEnd( function ( event ) {
                // 计算移动多少项
                var direction = event.directionX ? 1 : -1,
                    step = event.duration < 200 ? -direction : -direction * ( Math.abs( ul.zLeft / parentWidth + direction * 0.3 ) > 0.5 ? 1 : 0 );

                // 触发动画
                cutTo( step );
            } );
        } );

        return insert( parent, {
            item : function ( index ) {
                return items[index];
            },
            disable : function ( val ) {
                disabled = val;
            },
            length : function () {
                return items.length;
            },
            addItem : function ( li ) {
                items.push( li );
            },
            clear : function () {
                curCenterIndex = 0;
                items = [];
            },
            curIndex : function () {
                return curCenterIndex;
            },
            display : lay,
            cutTo : function ( index ) {
                cutTo( index - curCenterIndex );
            },
            cutRight : function ( index ) {
                cutTo( index || 1 );
            },
            onCutTo : cutToEvent.regist,
            onSlideStart : slideStartEvent.regist,
            onAnimateStart : animateStartEvent.regist
        } );
    }

    exports.SlideListPanel = SlideListPanel;
} );


/**
 * Created by 白 on 2014/10/14.
 */

(function () {
    var onPointerDown = Z.onPointerDown,
        insert = Z.insert,
        loopArray = Z.loopArray,
        loopObj = Z.loopObj,
        css = Z.css,
        bindEvent = Z.bindEvent,
        px = css.px;

    insert( ua, {
        iphone4 : ua.iphone && screen.height === 480,
        iphone5 : ua.iphone && screen.height === 568,
        iphone6 : ua.iphone && screen.height > 568
    } );

    // region util
    function extract( attr, defaultAttr ) {
        var retVal = {};
        loopObj( defaultAttr, function ( key, val ) {
            retVal[key] = attr[key] === undefined ? val : attr[key];
        } );
        return retVal;
    }

    function KeyValueFunction( registFunc ) {
        return function ( k, v ) {
            if ( Z.is.Object( k ) ) {
                loopObj( k, registFunc );
            }
            else {
                registFunc( k, v );
            }
        };
    }

    // 日期转换为字符串
    function dateString( date, format ) {
        function intString( number, digitNumber ) {
            function prefix( number ) {
                var retVal = "";
                Z.loop( number, function () {
                    retVal += "0";
                } );
                return retVal;
            }

            var str = number + "";
            return digitNumber > str.length ? prefix( digitNumber - str.length ) + str : str;
        }

        date = new Date( date );
        var dict = {
            Y : date.getFullYear() + "",
            M : intString( date.getMonth() + 1, 2 ),
            D : intString( date.getDate(), 2 ),
            h : intString( date.getHours(), 2 ),
            m : intString( date.getMinutes(), 2 ),
            s : intString( date.getSeconds(), 2 )
        };

        var lastChar = "", retVal = "", curChar = "";
        for ( var i = 0, len = format.length; i !== len; ++i ) {
            curChar = format.charAt( i );
            if ( curChar === "%" ) {
                retVal += dict[lastChar] || lastChar;
                lastChar = "";
                continue;
            }
            retVal += lastChar;
            lastChar = curChar;
        }

        return retVal + lastChar;
    }

    function onTransitionEnd( el, task ) {
        var handle = bindEvent( el, "webkitTransitionEnd", function () {
            handle.remove();
            task();
        } );
    }

    // 计算图片cover指定宽高的style
    function getImageCoverStyle( img, dWidth, dHeight ) {
        var dRatio = dWidth / dHeight,
            nWidth = img.naturalWidth || img.width || img.clientWidth,
            nHeight = img.naturalHeight || img.height || img.clientHeight,
            nRatio = nWidth / nHeight,
            style = {
                position : "absolute"
            };

        // 计算居中缩放
        if ( dRatio < nRatio ) {
            style.height = px( dHeight );
            style.left = px( ( dWidth - dHeight / nHeight * nWidth) / 2 << 0 );
            style.top = 0;
        }
        else {
            style.width = px( dWidth );
            style.left = 0;
            style.top = px( ( dHeight - dWidth / nWidth * nHeight ) / 2 << 0 );
        }

        return style;
    }

    // endregion

    // region 地图相关
    var bdMapScript = Z.Resource( function ( loadDone ) {
        window.bdmapInit = function () {
            loadDone();
            delete window.bdmapInit;
        };

        // 加载百度地图脚本
        Z.element( "script", {
            src : 'http://api.map.baidu.com/api?type=quick&ak=D5a271a3083d77f21c63ff307e9f60b9&v=1.0&callback=bdmapInit'
        }, document.head );
    } );

    // 百度地图脚本的加载器
    function lbsProcedure( procedure ) {
        return function ( arg ) {
            bdMapScript.load( function () {
                procedure( arg );
            } );
        };
    }

    var markerMap = lbsProcedure( function ( arg ) {
        var oMap = Z.element( "div", {
                css : {
                    height : "100%",
                    width : "100%"
                }
            }, arg.parent ),
            map = new BMap.Map( oMap ),
            points = [];

        // 拖动地图时不触发抽屉,滚动等触摸效果
        onPointerDown( arg.parent, function ( event ) {
            event.stopPropagation();
        } );

        // 添加覆盖物,点击覆盖物会弹出大厦信息
        loopArray( arg.data, function ( item ) {
            var point = new BMap.Point( parseFloat( item.lng ), parseFloat( item.lat ) ),
                marker = new BMap.Marker( point ),
                markerIcon = new BMap.Icon( staticImgSrc( "layout-map-mark.png" ), new BMap.Size( 30, 30 ) );

            marker.setIcon( markerIcon );
            map.addOverlay( marker );
            points.push( point );

            if ( arg.make ) {
                var infoWindow = new BMap.InfoWindow( arg.make( item ) );
                marker.addEventListener( "click", function () {
                    marker.openInfoWindow( infoWindow );
                } );
            }
        } );

        // 初始化地图，设置中心点坐标和地图级别
        if ( points.length !== 0 ) {
            map.centerAndZoom( points[0], 16 );
            map.setViewport( points );
        }
        else {
            map.centerAndZoom( "北京市" );
        }

        arg.onLoad && arg.onLoad();
    } );
    // endregion

    // region 经典需求
    // 制作滑动列表的红点
    function doRedPoints( slideListPanel, redPointsWrapper ) {
        redPointsWrapper = redPointsWrapper || slideListPanel.querySelector( ".red-point .wrapper" ); // 红点容器
        var redPoints = [], curPoint = null; // 红点和当前红点

        // 创建红点
        Z.loop( slideListPanel.length(), function () {
            redPoints.push( Z.element( "span", redPointsWrapper ) );
        } );

        slideListPanel.onCutTo( function ( event ) {
            curPoint && curPoint.classList.remove( "active" );
            curPoint = redPoints[event.curIndex];
            curPoint.classList.add( "active" );
        } );
    }

    // endregion

    exports.extract = extract;
    exports.dateString = dateString;
    exports.KeyValueFunction = KeyValueFunction;
    exports.onTransitionEnd = onTransitionEnd;
    exports.getImageCoverStyle = getImageCoverStyle;
    exports.markerMap = markerMap;
    exports.doRedPoints = doRedPoints;
})();

/**
 * Created by 白 on 2014/11/24.
 * fp引擎相关
 */

(function () {
    // 静态图片地址
    window.staticImgSrc = function ( src ) {
        return contentSrc( "image/" + src );
    };

    var fp = window.fp = window.fp || {},
        loopArray = Z.loopArray,
        extract = Z.extract,
        insert = Z.insert,
        KeyValueFunction = Z.KeyValueFunction,
        element = Z.element,
        Event = Z.Event,

        specialPage = window.specialPage = {},
        layoutFormat = window.layoutFormat = {},
        functionPages = window.functionPages = {},
        enterAnimate = window.enterAnimate = {},
        switchAnimates = window.switchAnimates = [];

    // 获取组件属性
    window.componentAttr = function ( componentInfo ) {
        return extract( componentInfo, {
            x : 0,
            y : 0,
            opacity : 1,
            scale : 1,
            rotate : 0,
            "z-index" : 0
        } );
    };

    // 3个经典的定位函数
    window.center = function ( outLength, innerLength ) {
        return ( outLength - innerLength ) / 2 << 0;
    };

    window.middleY = function ( y ) {
        return y - idealHeight / 2 + clientHeight / 2 << 0;
    };

    window.middleX = function ( x ) {
        return x - idealWidth / 2 + clientWidth / 2 << 0
    };

    // 注册板式
    window.registLayout = KeyValueFunction( function ( name, func ) {
        layoutFormat[name] = func;
    } );

    // 注册入场动画
    window.registEnterAnimate = KeyValueFunction( function ( name, func ) {
        enterAnimate[name] = function ( component ) {
            return {
                component : component,
                duration : 1,
                timing : Z.Timing.ease,
                progress : func.apply( null, [component].concat( Array.prototype.slice.call( arguments, 1 ) ) )
            };
        };
    } );

    // 注册切页动画
    window.registSwitchAnimate = KeyValueFunction( function ( name, func ) {
        switchAnimates.push( func );
        switchAnimates[name] = func;
    } );

    // 注册功能页
    window.registFunctionPage = function ( name, make ) {
        return functionPages[name] = function ( arg ) {
            // 滑入页面
            function slidePageIn() {
                var page = fp.slidePage();
                make( page, arg.data );
                page.slideIn( arg.noTransition );
            }

            if ( arg.noLog || fp.isLogIn() ) {
                slidePageIn();
            }
            else {
                if ( !fp.canNotLogin ) {
                    sessionStorage.setItem( "lastPageIndex", curPageIndex );
                    sessionStorage.setItem( "functionData", JSON.stringify( {
                        name : name,
                        data : arg.data
                    } ) );
                    fp.logIn( {
                        returnUrl : location.href,
                        onLogIn : slidePageIn
                    } );
                }
                else {
                    fp.canNotLogin();
                }
            }
        };
    };

    // 注册特殊页
    window.registSpecialPage = KeyValueFunction( function ( name, load ) {
        specialPage[name] = function () {
            var pageInfo = {
                type : "special",
                load : function ( done ) {
                    load( function ( info ) {
                        pageInfo.create = function () {
                            var page = element( "div.special-page.page" ),
                                showEvent = Event(),
                                removeEvent = Event();

                            insert( page, {
                                start : showEvent.trig,
                                recycle : removeEvent.trig,
                                onShow : showEvent.regist,
                                onRemove : removeEvent.regist
                            } );

                            info.create( page );
                            return page;
                        };

                        done();
                    } );
                }
            };

            return pageInfo;
        };
    } );

    // 板式页
    window.LayoutPage = function ( pageData ) {
        var layoutData = pageData.layout,
            format = layoutFormat[layoutData.label] || layoutFormat["SingleImage"], // 根据layoutId,选择板式,如果没找到该板式,使用单图板式
            resourceList = ( format.resource || [] ).concat( [] ), imageList = layoutData.image || []; // 页面的资源列表和图片列表

        return {
            pageData : pageData,
            create : function ( page ) {
                format.create( page, layoutData );
                return page;
            },

            load : function ( done ) {
                var loader = Z.Loader();
                loader.onLoad( function () {
                    insert( layoutData, {
                        resource : resourceList,
                        image : imageList
                    } );
                    done();
                } );

                function doLoad( imgList, srcHandler ) {
                    loopArray( imgList, function ( src, i ) {
                        loader.load( function ( done ) {
                            // 将src(字符串)替换为元素
                            var img = imgList[i] = new Image();

                            if ( format.crossOrigin ) {
                                img.crossOrigin = "*";
                            }

                            img.onload = function () {
                                img.halfWidth = ( img.naturalWidth || img.width ) / 2 << 0;
                                img.halfHeight = ( img.naturalHeight || img.height ) / 2 << 0;
                                img.onload = null;
                                done();
                            };

                            // 如果图片加载失败,加载404
                            img.onerror = function () {
                                img.src = staticImgSrc( "firstPage-404.jpg" );
                            };
                            img.src = srcHandler ? srcHandler( src ) : src;
                        } );
                    } );
                }

                doLoad( imageList );
                doLoad( resourceList, staticImgSrc );

                loader.start();
            }
        };
    };
})();

/**
 * Created by 白 on 2014/9/10.
 */

(function () {
    var loopObj = Z.loopObj,
        element = Z.element,
        insert = Z.insert,
        onPointerDown = Z.onPointerDown,
        bubble = Z.bubble,
        toggleState = Z.toggleState,
        transition = Z.transition,
        onTransitionEnd = Z.onTransitionEnd;

    // 锁定屏幕,不接受鼠标动作
    function lock( isLock, el ) {
        Z.switchClass( el || document.documentElement, isLock, "lock" );
    }

    // 跳转到链接,记录当前页码
    function jump( href ) {
        sessionStorage.setItem( "lastPageIndex", curPageIndex );
        location.href = href;
    }

    // 屏蔽触摸事件
    function preventEvent( targetNode, onRemove ) {
        var body = document.body;

        if ( targetNode ) {
            // 添加class,完成屏蔽
            targetNode.classList.add( "event-all" );
            bubble( targetNode, function ( node ) {
                node.classList.add( "event-target" );
            } );
            body.classList.add( "event-mask" );

            fp.history.pushAction( function () {
                // 移除class,取消屏蔽
                targetNode.classList.remove( "event-all" );
                bubble( targetNode, function ( node ) {
                    node.classList.remove( "event-target" );
                } );
                body.classList.remove( "event-mask" );

                downHandle.remove();
                onRemove && onRemove();
            } );

            var downHandle = onPointerDown( document, function ( event ) {
                if ( !targetNode.contains( event.target ) ) {
                    event.preventDefault();
                    fp.history.back();
                }
            } );
        }
        else {
            var mask = element( "div.body-mask", body );
            fp.history.pushAction( function () {
                Z.removeNode( mask );
                onRemove && onRemove();
            } );

            onPointerDown( mask, function ( event ) {
                event.preventDefault();
                event.stopPropagation();
                fp.history.back();
            } );
        }
    }

    var cookie = function () {
        var cookie = JSON.parse( localStorage.getItem( "cookie" ) || "{}" );

        // 根据过期时间,清理cookie
        loopObj( cookie, function ( key, value ) {
            if ( value.expires < new Date() ) {
                delete cookie[key];
            }
        } );

        // 保存cookie
        function save() {
            localStorage.setItem( "cookie", JSON.stringify( cookie ) );
        }

        save();

        return {
            getItem : function ( key ) {
                return cookie[key] ? cookie[key].value : null;
            },
            setItem : function ( key, value, timeToExpires ) {
                cookie[key] = {
                    value : value,
                    expires : (new Date()).getTime() + timeToExpires * 1000
                };
                save();
            },
            expires : function ( key, timeToExpires ) {
                if ( cookie[key] ) {
                    cookie[key].expires = (new Date()).getTime() + timeToExpires * 1000;
                    save();
                }
            },
            remove : function ( key ) {
                delete cookie[key];
                save();
            }
        };
    }();

    // 历史
    var fpHistory = function () {
        var actionList = [],
            hasPush = false;

        setTimeout( function () {
            Z.bindEvent( window, "popstate", function () {
                if ( actionList.length !== 0 ) {
                    var oldTop = actionList.top;
                    actionList.pop();
                    oldTop.onPop( oldTop.actionEnd );

                    if ( actionList.length > 1 ) {
                        history.pushState( null, "", location.href );
                    }
                    else {
                        hasPush = false;
                    }
                }
            } );
        }, 0 );

        return {
            pushAction : function ( onPop ) {
                var actionEndEvent = Z.Event();
                actionList.push( {
                    onPop : onPop,
                    actionEnd : actionEndEvent.trig
                } );

                if ( !hasPush ) {
                    hasPush = true;
                    history.pushState( null, "", location.href );
                }

                return actionEndEvent.regist;
            },
            back : function () {
                history.back();
            }
        };
    }();

    // 加载动画
    var Loading = function () {
        // 全局的加载图标
        var globalLoading;

        return function ( parent, delay ) {
            if ( !globalLoading ) {
                globalLoading = element( "img.loading", {
                    src : staticImgSrc( "firstPage-loading.gif" )
                } );
            }

            // 如果不提供父元素,做全局级别的加载,屏蔽事件
            if ( !parent ) {
                lock( true );
                document.body.appendChild( globalLoading );

                return {
                    remove : function () {
                        lock( false );
                        Z.removeNode( globalLoading );
                    }
                };
            }
            // 否则做局部的加载
            else {
                var loading = delay ? null : parent.appendChild( globalLoading.cloneNode( true ) ),
                    timeout = null;

                delay && ( timeout = setTimeout( function () {
                    loading = parent.appendChild( globalLoading.cloneNode( true ) );
                }, delay ) );

                return {
                    remove : function () {
                        timeout && clearTimeout( timeout );
                        Z.removeNode( loading );
                    }
                };
            }
        }
    }();

    // 弹出消息
    var alert = function () {
        var msgBox, msg;

        return function ( text, delay ) {
            // 第一次弹出消息时创建消息框
            if ( !msgBox ) {
                msgBox = element( "div.msg-box", document.body );
                msg = element( "div.msg", msgBox );
            }

            msg.innerHTML = text;
            toggleState( msgBox, "remove", "show" );

            function removeMsg() {
                toggleState( msgBox, "show", "remove" );
                tapHandle.remove();
                clearTimeout( timeoutHandle );
            }

            var tapHandle = onPointerDown( document, removeMsg ),
                timeoutHandle = setTimeout( removeMsg, delay || 2000 );
        };
    }();

    // 滑页
    var slidePage = function () {
        var parent = element( "div.slide-page" ),
            slideInEvent = Z.Event(),
            slideOutEvent = Z.Event();

        onPointerDown( parent, function ( event ) {
            event.stopPropagation();
        } );

        return insert( parent, {
            onSlideIn : slideInEvent.regist,
            onSlideOut : slideOutEvent.regist,
            isIn : function () {
                return parent.classList.contains( "slide-in" );
            },
            slideIn : function ( noTransition ) {
                body.appendChild( parent );
                if ( noTransition ) {
                    noTransition && parent.classList.add( "no-transition" );
                    parent.classList.add( "slide-in" );
                    slideInEvent.trig();
                }
                else {
                    fp.lock( true );
                    setTimeout( function () {
                        parent.classList.add( "slide-in" );
                        onTransitionEnd( parent, function () {
                            fp.lock( false );
                            slideInEvent.trig();
                        } );
                    }, 10 );
                }

                fp.history.pushAction( function () {
                    fp.lock( true );
                    slideOutEvent.trig();
                    parent.classList.remove( "no-transition" );
                    parent.classList.remove( "slide-in" );
                    onTransitionEnd( parent, function () {
                        fp.lock( false );
                        Z.removeNode( parent );
                    } );
                } );
            }
        } );
    };

    function getSessionData( key, defaultValue ) {
        var retVal = sessionStorage.getItem( key );
        sessionStorage.removeItem( key );
        return retVal === null ? defaultValue : retVal;
    }

    insert( fp, {
        lock : lock,
        jump : jump,
        preventEvent : preventEvent,
        cookie : cookie,
        history : fpHistory,
        Loading : Loading,
        alert : alert,
        slidePage : slidePage,
        getSessionData : getSessionData
    } );
})();

/**
 * Created by 白 on 2014/11/24.
 * 初夜系统的启动
 */

(function () {
    var element = Z.element,
        Event = Z.Event,
        onPointerDown = Z.onPointerDown,
        onTap = Z.onTap;

    fp.runSystem = function () {
        if ( ua.ios ) {
            document.documentElement.classList.add( "ios" );
        }

        if ( ua.win32 ) {
            document.documentElement.classList.add( "win32" );
        }

        // 屏蔽默认事件
        onPointerDown( document, function ( event ) {
            var prevent = true;
            Z.bubble( event.target, function ( node ) {
                if ( node.classList.contains( "need-default" ) ) {
                    prevent = false;
                }
            } );
            prevent && event.preventDefault();
        } );

        window.body = element( "div.body", document.body );

        // 启动幻灯片
        window.onSystemPrepare && window.onSystemPrepare( function ( data ) {
            var rawPages = data.pages, // 原始页面
                pageNumber = window.pageNumber = rawPages.length,
                functionData = fp.getSessionData( "functionData" ),
                loading = fp.Loading(),
                pages = new Array( pageNumber ), // 已解析的页面
                context = {};

            window.color = data.color;
            pages.data = data;

            // 如果有功能数据,构建对应的页面并切出
            if ( functionData ) {
                functionData = JSON.parse( functionData );
                functionPages[functionData.name]( {
                    data : functionData.data,
                    noTransition : true
                } );
            }

            // 当某页码加载完成后回调
            context.onPageLoad = function () {
                var onLoadTable = {};

                // 加载指定页的资源,并递归加载前一页和后一页的资源
                (function loadResource( index ) {
                    var rawPage = rawPages[index];
                    if ( rawPage && pages[index] === undefined ) {
                        var pageInfo = pages[index] = rawPage.special ? specialPage[rawPage.special]() : LayoutPage( rawPage );

                        pageInfo.load( function () {
                            pageInfo.isLoad = true;
                            onLoadTable[index] && onLoadTable[index].trig( pageInfo );
                            delete onLoadTable[index];
                            delete pageInfo.load;
                            loadResource( index - 1 );
                            loadResource( index + 1 );
                        } );
                    }
                })( window.curPageIndex = parseInt( fp.getSessionData( "lastPageIndex", "0" ), 10 ) );

                return function ( index, onLoad ) {
                    var pageInfo = pages[getIndex( index )];

                    if ( pageInfo && pageInfo.isLoad ) {
                        onLoad( pageInfo );
                    }
                    else {
                        if ( !onLoadTable[index] ) {
                            onLoadTable[index] = Event();
                        }
                        return onLoadTable[index].regist( onLoad );
                    }
                };
            }();

            context.startShow = function () {
                loading.remove();

                // 音乐播放
                (function () {
                    if ( !window.noMusic && data.music && data.music.src ) {
                        var audio = element( "<audio loop></audio>", {
                                src : data.music.src
                            }, document.body ), // audio标签
                            playIcon = element( "div.music-icon.play", body ); // 播放图标

                        // 两种方式播放音乐,如果可以直接播放,那么直接播放,否则第一次触摸时播放
                        function playMusic() {
                            if ( playIcon.classList.contains( "play" ) ) {
                                audio.play();
                            }
                        }

                        setTimeout( playMusic, 0 );
                        var playMusicHandle = onPointerDown( document, function () {
                            playMusic();
                            playMusicHandle.remove();
                        } );

                        // 点击图标切换播放状态
                        onTap( playIcon, function () {
                            if ( playIcon.classList.contains( "play" ) ) {
                                playIcon.classList.remove( "play" );
                                audio.pause();
                            }
                            else {
                                playIcon.classList.add( "play" );
                                audio.play();
                            }
                        } );
                    }
                })();
            };

            // 获取页码
            window.getIndex = function ( index ) {
                return ( index + pageNumber ) % pageNumber;
            };

            // 获取页面的info,如果info没有load,返回空
            window.getPageInfo = function ( index ) {
                var pageInfo = pages[getIndex( index )];
                return pageInfo && pageInfo.isLoad ? pageInfo : null;
            };

            // 跳转到某个页面
            window.jumpPage = function () {
                var jumpLoadHandle = null;
                return function ( pageIndex ) {
                    fp.lock( false );

                    // 如果有跳转加载,停止它
                    jumpLoadHandle && jumpLoadHandle.remove();

                    var newIndex = getIndex( pageIndex ),
                        newPageInfo = getPageInfo( newIndex );

                    // 如果页面还没加载,加载后跳转到该页面
                    if ( !newPageInfo ) {
                        fp.lock( true );
                        jumpLoadHandle = fp.onPageLoad( newIndex, function () {
                            jumpPage( newIndex );
                        } );
                    }
                    // 否则进一步调用ppt.jumpPage
                    else {
                        fp.jumpPage( newIndex );
                        window.curPageIndex = newIndex;
                    }
                };
            }();

            // 启动幻灯片
            ( window.highPerformance ? CanvasSystem : DOMSystem )( pages, context );
            window.onFirstPageLoad && window.onFirstPageLoad();
        } )
    };
})();

/**
 * Created by 白 on 2014/11/13.
 */

(function () {
    var loopArray = Z.loopArray,
        element = Z.element,
        insert = Z.insert,
        loop = Z.loop,
        onTap = Z.onTap,
        onPointerDown = Z.onPointerDown,
        css = Z.css,
        px = css.px,

        allTipsShowLength = 6,
        sphereRadius, // 球半径
        maxFragmentsCount, // 最大碎片数
        sphereData; // 球的数据

    // region 表情
    // 处理表情
    var faceList = ["微笑", "撇嘴", "色", "发呆", "得意", "流泪", "害羞", "闭嘴", "睡", "大哭", "尴尬",
        "发怒", "调皮", "呲牙", "惊讶", "难过", "酷", "冷汗", "抓狂", "吐", "偷笑", "愉快", "白眼",
        "傲慢", "饥饿", "困", "惊恐", "流汗", "憨笑", "悠闲", "奋斗", "咒骂", "疑问", "嘘", "晕",
        "疯了", "衰", "骷髅", "敲打", "再见", "擦汗", "抠鼻", "鼓掌", "糗大了", "坏笑", "左哼哼",
        "右哼哼", "哈欠", "鄙视", "委屈", "快哭了", "阴险", "亲亲", "吓", "可怜", "菜刀", "西瓜",
        "啤酒", "篮球", "乒乓", "咖啡", "饭", "猪头", "玫瑰", "凋谢", "嘴唇", "爱心", "心碎", "蛋糕",
        "闪电", "炸弹", "刀", "足球", "瓢虫", "便便", "月亮", "太阳", "礼物", "拥抱", "强", "弱",
        "握手", "胜利", "抱拳", "勾引", "拳头", "差劲", "爱你", "NO", "OK", "爱情", "飞吻", "跳跳",
        "发抖", "怄火", "转圈", "磕头", "回头", "跳绳", "投降"];

    var faces = [];
    loopArray( faceList, function ( faceName, i ) {
        faces[faceName] = faces[i] = {
            name : faceName,
            element : function ( size ) {
                return element( "div.face-icon", {
                    css : {
                        "background-position" : -i * size + "px 0",
                        "background-size" : "auto " + size + "px",
                        width : px( size ),
                        height : px( size )
                    }
                } );
            }
        };
    } );

    // 评论的html,替换其中的表情
    function commentHTML( text, fontSize ) {
        return text.replace( /\[([^\]]*)]/g, function ( faceName ) {
            if ( RegExp.$1 in faces ) {
                return faces[RegExp.$1].element( fontSize ).outerHTML;
            }
            else {
                return faceName;
            }
        } );
    }

    // endregion

    // region 3d变换
    function eye() {
        return [
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1
        ];
    }

    function rotateX( angle ) {
        var sina = Math.sin( angle ), cosa = Math.cos( angle );
        return [
            1, 0, 0, 0,
            0, cosa, -sina, 0,
            0, sina, cosa, 0,
            0, 0, 0, 1
        ];
    }

    function rotateY( angle ) {
        var sina = Math.sin( angle ), cosa = Math.cos( angle );
        return [
            cosa, 0, sina, 0,
            0, 1, 0, 0,
            -sina, 0, cosa, 0,
            0, 0, 0, 1
        ];
    }

    function vec4_mat4x4_r( src, row ) {
        var rst = new Array( 4 );
        var offset = row * 4;
        loop( 4, function ( i ) {
            rst[i] = src[offset + i];
        } );
        return rst;
    }

    function vec4_mat4x4_c( src, col ) {
        var rst = new Array( 4 );
        loop( 4, function ( i ) {
            rst[i] = src[i * 4 + col];
        } );
        return rst;
    }

    function dot_vec4_vec4( l, r ) {
        var rt = 0.0;
        loop( 4, function ( i ) {
            rt += l[i] * r[i];
        } );
        return rt;
    }

    function mat4x4_vec4_4_c( src ) {
        var dest = new Array( 16 );
        loop( 4, function ( r ) {
            var offset = r * 4;
            loop( 4, function ( c ) {
                dest[offset + c] = src[c][r];
            } );
        } );
        return dest;
    }

    function mul_mat4x4_vec4( mat, vec ) {
        var rtv = new Array( 4 );
        loop( 4, function ( i ) {
            rtv[i] = dot_vec4_vec4( vec4_mat4x4_r( mat, i ), vec );
        } );
        return rtv;
    }

    function combine( l, r ) {
        var cols = new Array( 4 );
        loop( 4, function ( i ) {
            cols[i] = mul_mat4x4_vec4( l, vec4_mat4x4_c( r, i ) );
        } );
        return mat4x4_vec4_4_c( cols );
    }

    function rotate( matrix, x, y ) {
        return combine( combine( matrix, rotateX( y ) ), rotateY( -x ) );
    }

    function trans_mat4x4( src ) {
        var rows = new Array( 4 );
        loop( 4, function ( i ) {
            rows[i] = vec4_mat4x4_r( src, i );
        } );
        return mat4x4_vec4_4_c( rows );
    }

    function transform( matrix, vec ) {
        return mul_mat4x4_vec4( trans_mat4x4( matrix ), vec );
    }

    // endregion

    // region 球
    function makeSphere( fragments, r, doFragment ) {
        var data = sphereData[Math.max( fragments.length, 4 )];
        loopArray( fragments, function ( fragment, i ) {
            fragment.spherePosition = [data[i * 3] * r, data[i * 3 + 1] * r, data[i * 3 + 2] * r, 1]; // 碎片的球面位置
            doFragment && doFragment( fragment );
        } );
    }

    // endregion

    // region 评论墙
    if ( ua.win32 ) {
        sphereRadius = 126;
        maxFragmentsCount = 60;
    }
    else if ( ua.iphone6 ) {
        sphereRadius = 136;
        maxFragmentsCount = 40;
    }
    else if ( ua.iphone5 ) {
        sphereRadius = 126;
        maxFragmentsCount = 30;
    }
    else if ( ua.iphone4 ) {
        sphereRadius = 100;
        maxFragmentsCount = 20;
    }
    else {
        sphereRadius = 120;
        maxFragmentsCount = 25;
    }

    function getDirection( x, y ) {
        var v = Math.sqrt( x * x + y * y );
        return [x / v, y / v];
    }

    function CommentWall( parent, commentData ) {
        var fragmentSize = 30, // 碎片的尺寸

            sphereParent = element( "div.sphere-parent", parent ),
            tipsParent = element( "div.tips-parent", parent ),
            sphere = element( "div.sphere", sphereParent ),

            radius = sphereRadius,
            fragments = [],

            noNewTips = false, // 没有新的提示
            tipsNum = 0, // 弹框数量
            curMatrix = null, // 变换矩阵
            curDirection = getDirection( Math.random(), Math.random() ), // 转动方向
            toV = 0.003, curV = toV, // 当前速度和目标速度
            animateHandle = null;

        loopArray( [sphereParent, tipsParent], function ( parent ) {
            css( parent, {
                height : px( radius * 2 ),
                width : px( radius * 2 ),
                "margin-left" : px( -radius ),
                "margin-top" : px( -radius )
            } );
        } );
        css.size( sphere, radius * 2, radius * 2 );

        onPointerDown( parent, function ( event ) {
            event.preventDefault();
        } );

        // 设置碎片的评论
        function CommentFragment( comment, onLoad ) {
            var fragment = element( "div.item" ),
                img = new Image();

            img.onload = function () {
                Z.onInsert( img, function () {
                    css( img, Z.getImageCoverStyle( img, fragmentSize, fragmentSize ) );
                } );
                fragment.appendChild( img );
                fragment.comment = comment;
                onLoad && onLoad();
            };
            img.src = comment.avatar || staticImgSrc( "firstPage-defaultAvatar.png" );

            // 弹出提示,有缩放效果
            fragment.showTips = function () {
                Tips( fragment, {
                    scale : true
                } );
                ++tipsNum;
            };

            onTap( fragment, function () {
                if ( fragment.position[2] > radius * 0.2 ) {
                    animateHandle.remove();
                    removeTips();

                    var tips = Tips( fragment ),
                        removeHandler = Z.onPointerUp( document, function () {
                            removeHandler.remove();
                            tips.remove();
                            runRotateAnimate();
                        }, true );

                    tips.adjust();
                }
            } );

            return fragment;
        }

        loopArray( commentData, function ( commentInfo ) {
            var fragment = CommentFragment( commentInfo );
            sphere.appendChild( fragment );
            fragments.push( fragment );
        } );

        makeSphere( fragments, radius );

        // 对球进行变换
        function transformSphere( matrix ) {
            curMatrix = matrix;

            // 计算并设置每个碎片变换后的位置
            loopArray( fragments, function ( fragment ) {
                css.transform( fragment, css.translate.apply( null, fragment.position = transform( curMatrix, fragment.spherePosition ) ) );
                fragment.tips && fragment.tips.adjust();
            } );
        }

        // 提示框
        function Tips( fragment, arg ) {
            arg = arg || {};
            var position = fragment.position,
                comment = fragment.comment,

                tips = element( "div.tips", {
                    children : [
                        element( "div.name.ellipsis", comment.userName ),
                        element( "div.date", Z.dateString( comment.date, "M%-D% h%:m%" ) ),
                        element( "div.text", commentHTML( comment.text, 12 ) )
                    ]
                }, tipsParent ),
                tipsTriangle = Z.element( "div.triangle", tips ),

                tipsX = Math.min( position[0] + 40, ( clientWidth - tips.offsetWidth ) / 2 - 28 ),
                relativeX = tipsX - position[0],
                relativeY = -tips.offsetHeight / 2 - 25,

                scale = arg.scale ? 0.01 : 1,
                onFrame = arg.scale ? function () {
                    if ( scale !== 1 ) {
                        scale = Math.min( 1, scale + 0.08 );
                    }
                    else {
                        onFrame = null;
                    }
                } : null;

            css( tips, {
                "margin-top" : px( -tips.offsetHeight / 2 ),
                "-webkit-transform-origin" : [px( position[0] + 40 - tipsX + 15 ), "100%", 0].join( " " ),
                visibility : "hidden"
            } );
            css( tipsTriangle, "left", px( position[0] + 40 - tipsX + 15 ) );

            function adjust() {
                onFrame && onFrame();
                position = fragment.position;
                css( tips, "visibility", "visible" );
                css.transform( tips, css.translate( position[0] + relativeX, position[1] + relativeY, position[2] ), css.scale( scale ) );
            }

            tips.fragment = fragment;
            fragment.tips = tips;

            function remove() {
                onFrame = null;
                fragment.tips = null;
                Z.removeNode( tips );
            }

            return insert( tips, {
                adjust : adjust,
                remove : function ( canScale, onEnd ) {
                    if ( canScale ) {
                        onFrame = function () {
                            scale -= 0.08;
                            if ( scale < 0.01 ) {
                                remove();
                                scale = 0.01;
                                onEnd && onEnd();
                            }
                        };
                    }
                    else {
                        remove();
                    }
                }
            } );
        }

        // 旋转动画
        function runRotateAnimate() {
            var count = 0;
            animateHandle && animateHandle.remove();
            animateHandle = Z.requestAnimate( function () {
                curV = curV + ( toV - curV ) / 20; // 速度逼近toV
                transformSphere( rotate( curMatrix, curDirection[0] * curV, curDirection[1] * curV ) ); // 旋转球体

                if ( fragments.length > allTipsShowLength ) {
                    // 当动画运行20帧,并且速度稳定时,开始弹窗
                    if ( ++count > 20 && Math.abs( curV - toV ) < 0.001 ) {
                        var showTips = false; // 一次移动最多触发一个弹出提示
                        loopArray( fragments, function ( fragment ) {
                            // 如果碎片有提示,调整它
                            if ( fragment.tips ) {
                                fragment.tips.adjust();
                            }

                            // 根据碎片的Z轴位置,调整它的in位
                            // 如果一个碎片由非in转为in,根据随机数和当前的数量,决定它是否弹出提示
                            if ( !fragment.isIn && fragment.position[2] > radius * 0.85 ) {
                                var random = Math.random();
                                fragment.isIn = true;
                                if ( !noNewTips && !showTips &&
                                    ( tipsNum === 0 && random < 0.9 || tipsNum === 1 && random < 0.4 || tipsNum < 2 && random < 0.2 ) ) {
                                    fragment.showTips();
                                    showTips = true;
                                }
                            }
                            // 如果一个碎片由in转为非in,如果有提示,移除它
                            else if ( fragment.isIn && fragment.position[2] < radius * 0.85 ) {
                                fragment.isIn = false;
                                if ( fragment.tips ) {
                                    fragment.tips.remove( true, function () {
                                        --tipsNum;
                                    } );
                                }
                            }
                        } );
                    }
                }
                else {
                    loopArray( fragments, function ( fragment ) {
                        if ( !fragment.tips ) {
                            fragment.showTips();
                        }
                        fragment.tips.adjust();
                    } );
                }
            } );
        }

        // 停止转动动画
        function stopRotateAnimate() {
            curV = toV;
            animateHandle && animateHandle.remove();
        }

        // 移除所有提示
        function removeTips( delay ) {
            tipsNum = 0;
            loopArray( fragments, function ( fragment ) {
                fragment.isIn = false;
                if ( fragment.tips ) {
                    fragment.tips.remove();
                }
            } );

            if ( delay ) {
                noNewTips = true;
                setTimeout( function () {
                    noNewTips = false;
                }, delay );
            }
        }

        // 拖拽旋转球体
        Z.onDrag( sphereParent, function ( event ) {
            var startMatrix = curMatrix;

            sphereParent.classList.add( "lock" );
            animateHandle.remove();
            if ( fragments.length > allTipsShowLength ) {
                removeTips();
            }

            event.onDragMove( function ( event ) {
                transformSphere( rotate( startMatrix, event.distanceX / 200, event.distanceY / 200 ) );
            } );

            event.onDragEnd( function ( event ) {
                var vx = event.speedX, vy = event.speedY,
                    v = Math.sqrt( vx * vx + vy * vy );

                sphereParent.classList.remove( "lock" );

                curV = v / 10;
                if ( curV !== 0 ) {
                    curDirection = [vx / v, vy / v];
                }
                runRotateAnimate();
            } );
        } );

        transformSphere( eye() ); // 进行单位变换

        return insert( parent, {
            newComment : function ( commentInfo, onImageLoad ) {
                var newFragment = CommentFragment( commentInfo, function () {
                    onImageLoad && onImageLoad();

                    stopRotateAnimate();
                    fp.lock( true );

                    // 将新碎片加载到球中,并缩小
                    css.transform( newFragment, css.scale( 0.01 ) );
                    sphere.appendChild( newFragment );

                    // 如果数量超出最大数量,移除最后一个,将新评论放在第一个
                    if ( fragments.length > maxFragmentsCount ) {
                        Z.removeNode( fragments.pop() );
                    }
                    fragments.unshift( newFragment );

                    // 重新制作球
                    makeSphere( fragments, radius, function ( fragment ) {
                        fragment.isIn = false;
                    } );

                    Z.animate( {
                        duration : 0.5,
                        onAnimate : function ( ratio ) {
                            loopArray( fragments, function ( fragment ) {
                                var n = fragment.spherePosition, o = fragment.position;
                                if ( fragment !== newFragment ) {
                                    function value( index ) {
                                        return Z.fromTo( o[index], n[index], ratio );
                                    }

                                    css.transform( fragment, css.translate( value( 0 ), value( 1 ), value( 2 ) ) );
                                }
                                else {
                                    css.transform( newFragment, css.translate( 0, 0, n[2] ), css.scale( ratio ) );
                                }
                            } );
                        },
                        onEnd : function () {
                            // 解锁,进行单位变化,重新运行动画并弹出新碎片的提示
                            fp.lock( false );
                            transformSphere( eye() );
                            runRotateAnimate();
                            newFragment.showTips();
                        }
                    } );
                } );
            },
            fragments : fragments,
            runAnimate : runRotateAnimate,
            stopAnimate : stopRotateAnimate,
            removeTips : removeTips
        } );
    }

    // endregion

    // region 输入框
    function InputBar() {
        var inputBar = element( "div.comment-input-bar.normal", [
                element( "form.text-area.need-default", [
                    element( "div.text-area-wrapper", [element( "textarea", {
                        placeholder : "评论"
                    } )] ),
                    element( "div.send-button", "发送" ),
                    element( "div.small-icon.icon-keyboard", [element( "div" )] ),
                    element( "div.small-icon.icon-face", [element( "div" )] )
                ] ),
                element( "div.face-list", [
                    element( "ul" ),
                    element( "div.red-point", [element( "div.wrapper" )] )
                ] )
            ] ),
            sendButton = inputBar.querySelector( ".send-button" ),
            textarea = inputBar.querySelector( "textarea" ),

            oFaceList = inputBar.querySelector( "ul" ), curFaceLi = null,
            faceListPanel = null,

            commitEvent = Z.Event();

        // 输入时调整位置和发送状态
        function adjustSize() {
            Z.switchClass( sendButton, textarea.value === "", "disabled" );
            css( textarea, "height", 0 );
            css( textarea, "height", textarea.scrollHeight - 6 + "px" );
        }

        // 创建表情列表
        loopArray( faces, function ( item, i ) {
            if ( i % 20 === 0 ) {
                curFaceLi = element( "li.face-list-page", [element( "div.content" )], oFaceList );
            }

            var faceItem = element( "div.face-list-item", [
                item.element( 30 ),
                element( "div.face-list-item-tips.small-icon", [item.element( 40 ), element( "div.caption", item.name )] )
            ], curFaceLi.querySelector( ".content" ) );

            faceItem.face = item;
        } );

        // 为每一页添加一个删除键,点击时,如果当前文本最后是表情的话,删除表情
        loopArray( inputBar.querySelectorAll( ".face-list-page" ), function ( li ) {
            onTap( element( "div.delete-face.icon", li.querySelector( ".content" ) ), function () {
                if ( /\[([^\]]*)]$/.test( textarea.value ) ) {
                    var deleteLength = RegExp.$1.length + 2;
                    textarea.value = textarea.value.substring( 0, textarea.value.length - deleteLength );
                }
                adjustSize();
            } );
        } );

        // 点击键盘按钮,键盘聚焦
        Z.onPointerUp( inputBar.querySelector( ".icon-keyboard" ), function ( event ) {
            event.preventDefault();
            inputBar.classList.remove( "face-select" );
            textarea.focus();
        } );

        // 键盘聚焦时移除表情选择
        Z.bindEvent( textarea, "focus", function () {
            inputBar.classList.remove( "face-select" );
        } );

        // 点击表情按钮,切换到表情选择
        onPointerDown( inputBar.querySelector( ".icon-face" ), function ( event ) {
            event.preventDefault();
            textarea.blur();
            inputBar.classList.add( "face-select" );

            // 第一次显示slideListPanel时,注册事件
            if ( faceListPanel === null ) {
                faceListPanel = Z.SlideListPanel( oFaceList.parentNode );
                Z.doRedPoints( faceListPanel );
                faceListPanel.display( 0 );

                onPointerDown( faceListPanel, function ( event ) {
                    // 当前表情
                    var curFace = null;

                    // 处理表情,找到当前手指位置的表情,如果是表情,激活它
                    function doFace( event, addClass ) {
                        event.preventDefault();
                        var el = document.elementFromPoint( event.zClientX, event.zClientY ),
                            faceListItem = Z.findAncestor( el, function ( el ) {
                                return el.classList.contains( "face-list-item" );
                            } );

                        if ( addClass !== false ) {
                            curFace !== null && curFace.classList.remove( "active" );
                            faceListItem !== null && faceListItem.classList.add( "active" );
                        }
                        curFace = faceListItem;
                    }

                    // 如果0.3秒内没有进入滑动,进入表情选择
                    var timeout = setTimeout( function () {
                        faceListPanel.disable( true );
                        doFace( event );
                        event.onMove( doFace );
                    }, 200 );

                    // 开始滑动时,取消表情选择
                    var swipe = faceListPanel.onSlideStart( function () {
                        clearTimeout( timeout );
                        timeout = null;
                    } );

                    var lastEvent = event;
                    event.onMove( function ( event ) {
                        lastEvent = event;
                    } );

                    event.onUp( function () {
                        if ( timeout ) {
                            clearTimeout( timeout );
                            doFace( lastEvent, false );
                        }
                        if ( curFace ) {
                            curFace.classList.remove( "active" );
                            textarea.value = textarea.value + "[" + curFace.face.name + "]";
                            adjustSize();
                        }
                        faceListPanel.disable( false );
                        swipe.remove();
                    } );
                } );
            }
        } );

        // 点击发送按钮,发送消息
        onTap( sendButton, function () {
            if ( !inputBar.classList.contains( ".empty" ) ) {
                inputBar.classList.remove( "face-select" );
                commitEvent.trig( textarea.value );
            }
        } );

        // 当插入到文档中时,以及输入时调整尺寸
        Z.bindEvent( textarea, "input", adjustSize );
        Z.onInsert( inputBar, adjustSize );

        return insert( inputBar, {
            onCommit : commitEvent.regist,
            value : function ( val ) {
                if ( val !== undefined ) {
                    textarea.value = val;
                }
                else {
                    return textarea.value;
                }
            },
            focus : function () {
                textarea.focus();
            },
            blur : function () {
                textarea.blur();
            },
            onFocus : function ( task ) {
                return Z.bindEvent( textarea, "focus", task );
            },
            onBlur : function ( task ) {
                return Z.bindEvent( textarea, "blur", task );
            }
        } );
    }

    // endregion

    // region 评论页
    // 报名表单页
    registSpecialPage( "comment", function ( done ) {
        Z.ajax( {
            url : contentSrc( "sphere.json" ),
            isJson : true,
            onLoad : function ( data ) {
                sphereData = data;
                fp.getCommentSummary( function ( summaryId ) {
                    fp.getComments( function ( comments ) {
                        var commentWall = CommentWall( element( "div.comment-wall" ), comments.slice( 0, maxFragmentsCount ) );
                        if ( commentWall.fragments.length === 0 ) {
                            commentWall.classList.add( "empty" );
                        }

                        onPointerDown( commentWall.querySelector( ".sphere-parent" ), function ( event ) {
                            event.preventDefault();
                            if ( commentWall.fragments.length > 0 ) {
                                event.stopPropagation();
                            }
                        } );

                        done( {
                            create : function ( page ) {
                                var inputBar = page.appendChild( Z.InputBar() ),
                                    lastMsg = fp.getSessionData( "comment" ),
                                    blurHandle;

                                // 聚焦时,点击其他地方失焦
                                inputBar.onFocus( function () {
                                    blurHandle = onPointerDown( commentWall, function () {
                                        inputBar.blur();
                                    } );
                                } );

                                inputBar.onBlur( function () {
                                    blurHandle.remove();
                                    blurHandle = null;
                                } );

                                function sendMsg( text, onDone ) {
                                    fp.getUserInfo( function ( userInfo ) {
                                        var loading = fp.Loading( page, 300 );
                                        inputBar.blur();
                                        inputBar.classList.add( "lock" );

                                        fp.saveComment( function () {
                                            commentWall.newComment( {
                                                avatar : userInfo.HeadPhoto,
                                                userName : userInfo.NickName,
                                                date : new Date(),
                                                text : text
                                            }, function () {
                                                loading.remove();
                                                inputBar.classList.remove( "lock" );
                                                inputBar.value( "" );
                                                commentWall.removeTips( 2000 ); // 移除提示,2秒钟内没有新提示
                                                commentWall.classList.remove( "empty" );
                                            } );

                                            onDone && onDone();
                                        }, {
                                            text : text,
                                            contentSummaryId : summaryId
                                        } );
                                    } );
                                }

                                lastMsg && fp.isLogIn() && sendMsg( lastMsg, function () {
                                    fp.alert( "评论发表成功" );
                                } );

                                inputBar.onCommit( function ( text ) {
                                    if ( fp.isLogIn() ) {
                                        sendMsg( text );
                                    }
                                    else if ( fp.canNotLogin ) {
                                        fp.canNotLogin();
                                    }
                                    else {
                                        sessionStorage.setItem( "comment", text );
                                        sessionStorage.setItem( "lastPageIndex", curPageIndex );
                                        fp.logIn();
                                    }
                                } );

                                page.classList.add( "comment-page" );
                                page.appendChild( commentWall );
                                page.onShow( commentWall.runAnimate );
                                page.onRemove( function () {
                                    commentWall.stopAnimate();
                                    commentWall.removeTips();
                                } );
                            }
                        } );
                    }, {
                        contentSummaryId : summaryId
                    } );
                }, fp.getWorkInfo() );
            }
        } );
    } );
    // endregion

    exports.commentWall = CommentWall;
    exports.InputBar = InputBar;
})();

/**
 * Created by 白 on 2014/8/14.
 */

(function () {
    // 静态图片
    window.contentSrc = function ( src ) {
        return virtualPath + "/Content/" + src;
    };

    var dataForWeixin = window.dataForWeixin = {};

    // 统计分享
    function share() {
        var xhr = new XMLHttpRequest();
        xhr.open( "post", virtualPath + "/Work/Share", true );
        xhr.send( null );
    }

    document.addEventListener( 'WeixinJSBridgeReady', function () {
        var WeixinJSBridge = window.WeixinJSBridge;
        WeixinJSBridge.call( 'showOptionMenu' );

        // 发送给好友;
        WeixinJSBridge.on( 'menu:share:appmessage', function () {
            WeixinJSBridge.invoke( 'sendAppMessage', {
                "appid" : dataForWeixin.appId,
                "img_url" : dataForWeixin.picture,
                "img_width" : "120",
                "img_height" : "120",
                "link" : dataForWeixin.url,
                "desc" : dataForWeixin.desc,
                "title" : dataForWeixin.title
            }, share );
        } );

        // 分享到朋友圈;
        WeixinJSBridge.on( 'menu:share:timeline', function () {
            WeixinJSBridge.invoke( 'shareTimeline', {
                "img_url" : dataForWeixin.picture,
                "img_width" : "120",
                "img_height" : "120",
                "link" : dataForWeixin.url,
                "desc" : dataForWeixin.desc,
                "title" : dataForWeixin.title
            }, share );
        } );

        // 分享到微博;
        WeixinJSBridge.on( 'menu:share:weibo', function () {
            WeixinJSBridge.invoke( 'shareWeibo', {
                "content" : dataForWeixin.title + ' ' + dataForWeixin.url,
                "url" : dataForWeixin.url
            }, share );
        } );

        // 分享到Facebook
        WeixinJSBridge.on( 'menu:share:facebook', function () {
            WeixinJSBridge.invoke( 'shareFB', {
                "img_url" : dataForWeixin.picture,
                "img_width" : "120",
                "img_height" : "120",
                "link" : dataForWeixin.url,
                "desc" : dataForWeixin.desc,
                "title" : dataForWeixin.title
            }, share );
        } );
    }, false );

    if ( ua.ios || ua.win32 || /chuye/gi.test( navigator.userAgent ) ) {
        window.highPerformance = true;
    }

    window.onSystemPrepare = function ( load ) {
        Z.ajax( {
            url : workDetailUrl,
            isJson : true,
            onLoad : function ( sourceData ) {
                if ( sourceData.code !== 200 ) {
                    document.documentElement.classList.add( "work-404" );
                    return;
                }

                sourceData = sourceData.data;

                // 计算分享url
                var shareUrl = location.origin + location.pathname;

                if ( sourceData ) {
                    dataForWeixin = {
                        picture : sourceData.thumbnail,
                        title : sourceData.title,
                        url : shareUrl,
                        desc : sourceData.description || ""
                    };

                    var userworks = sourceData.userworks,
                        works = userworks.works,
                        pages = sourceData.pages,
                        copyrightPage = {
                            layout : {
                                label : "copyright",
                                author : sourceData.author,
                                image : [sourceData.headimgurl, works[0].thumbnail, works[1].thumbnail, works[2].thumbnail],
                                title : userworks.title,
                                works : works,
                                commentCount : 0
                            }
                        };

                    sourceData.praise && pages.push( {special : "comment"} );
                    sourceData.copyright && pages.push( copyrightPage );

                    load( {
                        mode : sourceData.mode || "push",
                        color : {
                            background : sourceData.backgroud.color
                        },
                        pageSwitch : sourceData.pageSwitch || {
                            animateId : "push"
                        },
                        music : sourceData.music,
                        pages : pages
                    } );
                }
                window.onDataLoad && window.onDataLoad( sourceData );
            }
        } );
    };

    Z.onLoad( fp.runSystem );
})();

/**
 * Created by 白 on 2014/11/5.
 */

(function () {
    if ( window.debug ) {
        return;
    }

    var is = Z.is,
        extend = Z.extend,
        token;

    function invokeApi( op ) {
        return Z.ajax( {
            method : "post",
            url : Z.concatUrlArg( "http://c.cloud7.com.cn" + op.url, token ? {_token : token} : {} ),
            data : is.String( op.data ) ? op.data : Z.encodeURIObject( op.data ),
            requestHeader : extend( {
                "Content-Type" : "application/x-www-form-urlencoded"
            }, op.requestHeader || {} ),
            isJson : true,
            onLoad : function ( data ) {
                if ( data.code === 302 ) {
                    op.on302 && op.on302( data.data );
                }
                else {
                    op.success( data.data );
                }
            }
        } );
    }

    (function () {
        var userInfo = null;

        if ( !ua.MicroMessenger ) {
            fp.canNotLogin = function () {
                fp.alert( "请在微信中使用" );
            };

            fp.isLogIn = function () {
                return false;
            };
        }
        else {
            // 如果参数中有token,说明刚登陆完
            if ( token = location.href.arg._token ) {
                fp.cookie.setItem( "token", token, 7 * 24 * 60 * 60 );

                // 获取用户信息
                fp.getUserInfo = function ( callback ) {
                    if ( userInfo ) {
                        callback( userInfo );
                    }
                    else {
                        invokeApi( {
                            url : "/api/Wechat/CurrentUser",
                            success : function ( data ) {
                                callback( userInfo = data.data );
                            }
                        } );
                    }
                };

                fp.isLogIn = function () {
                    return true;
                };
            }
            // 否则从localStorage中获取值,此值可能过期,用getUserInfo来确保它已登陆上
            else {
                token = fp.cookie.getItem( "token" );

                // 获取用户信息
                fp.getUserInfo = function ( callback ) {
                    callback( userInfo );
                };

                fp.isLogIn = function () {
                    return userInfo !== null;
                };

                // 如果有token,立即发起一次获取CurrentUser的请求,以判断是否过期
                if ( token ) {
                    var on302 = null,
                        onSuccess = null;

                    invokeApi( {
                        url : "/api/Wechat/CurrentUser",
                        on302 : function ( url ) {
                            on302 && on302( url );

                            fp.logIn = function () {
                                invokeApi( {
                                    url : "/api/Wechat/CurrentUser",
                                    on302 : fp.jump
                                } );
                            };
                        },
                        success : function ( data ) {
                            userInfo = data.data;
                            onSuccess && onSuccess();
                        }
                    } );

                    fp.logIn = function ( arg ) {
                        if ( userInfo ) {
                            arg.onLogIn();
                        }
                        else {
                            on302 = fp.jump;
                            onSuccess = arg.onLogIn;
                        }
                    };
                }
                // 如果没有token,login就是直接跳转
                else {
                    fp.logIn = function () {
                        location.href = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx9d492ee399e6a24c&redirect_uri=' +
                        encodeURIComponent( 'http://c.cloud7.com.cn/Auth?returnUrl=' +
                        encodeURIComponent( location.href ) ) +
                        '&response_type=code&scope=snsapi_base&state=#wechat_redirect';
                    };
                }
            }
        }

        return token;
    }() );

    fp.getCommentSummary = function ( callback, workInfo ) {
        invokeApi( {
            url : "/api/Blog/SaveContentSummary",
            data : workInfo,
            success : function ( data ) {
                callback( data );
            }
        } )
    };

    fp.getCommentCount = function ( callback ) {
        var workInfo = fp.getWorkInfo();

        invokeApi( {
            url : "/api/Blog/GetCommentCounts",
            data : {
                Site : workInfo.Site,
                ContentID : workInfo.ContentID
            },
            success : function ( count ) {
                callback( count[1] );
            }
        } );
    };

    fp.getComments = function ( callback, arg ) {
        invokeApi( {
            url : "/api/blog/GetCommentWall",
            data : {
                ContentSummaryID : arg.contentSummaryId
            },
            success : function ( comments ) {
                var retVal = [];
                Z.loopArray( comments, function ( data ) {
                    retVal.push( {
                        text : data.Text,
                        userName : data.NickName,
                        avatar : data.HeadPhoto || staticImgSrc( "firstPage-defaultAvatar.png" ),
                        date : new Date( data.Time )
                    } );
                } );

                callback( retVal );
            }
        } );
    };

    fp.saveComment = function ( callback, arg ) {
        invokeApi( {
            url : "/api/blog/SaveTextComment",
            data : {
                ContentSummaryID : arg.contentSummaryId,
                Text : arg.text
            },
            success : callback
        } );
    };
})();

/**
 * Created by 白 on 2014/11/5.
 */

(function () {
    if ( window.debug ) {
        return;
    }

    fp.getWorkInfo = function () {
        return {
            Site : "chuye.cloud7.com.cn",
            ContentID : window.workDetailUrl.split( "/" ).top,
            Url : location.href,
            Thumbnail : dataForWeixin.picture,
            Title : dataForWeixin.title,
            Text : dataForWeixin.desc
        };
    };

    fp.sendForm = function ( callback, data ) {
        Z.ajax( {
            url : virtualPath + "/Integra/SaveData",
            method : "post",
            requestHeader : {
                "Content-Type" : "application/x-www-form-urlencoded"
            },
            data : Z.encodeURIObject( {
                formid : data.id,
                data : JSON.stringify( data.data )
            } ),
            onLoad : callback
        } );
    };
})();

/**
 * Created by 白 on 2014/11/24.
 * Canvas点系统
 */

(function () {
    var extend = Z.extend,
        toggleState = Z.toggleState,
        animate = Z.animate,
        onTap = Z.onTap,
        removeNode = Z.removeNode,
        css = Z.css,
        removeCss = Z.removeCss,

        CanvasMode = window.CanvasMode = window.CanvasMode || {};

    // 点击触发事件,仅仅针对这一次点击
    function onTapOnce( element, response ) {
        var tapHandler = onTap( element, function () {
            tapHandler.remove();
            response();
        } );
        return tapHandler;
    }

    // 获取切换动画
    function getSwitchAnimate( data ) {
        var animateId = data && data.pageSwitch ? data.pageSwitch.animateId : null;
        return animateId ? switchAnimates[animateId === "random" ? Math.random() * switchAnimates.length << 0 : animateId] : animateId;
    }

    // 准备snapshot
    window.prepareSnapshot = function ( prev, cur, canvas, style ) {
        var body = window.body;
        prev && body.appendChild( prev );
        cur && body.appendChild( cur );
        removeNode( canvas );
        css( body, style );

        return function () {
            removeNode( prev );
            removeNode( cur );
            body.appendChild( canvas );
            removeCss( body, style );
        };
    };

    CanvasMode.click = function ( pages, fc ) {
        var startPageIndex = window.curPageIndex,
            data = pages.data,
            body = window.body,

            canvas = fc.canvas,
            makePage = fc.makePage,
            setPage = fc.setPage,
            removeCurrentPage = fc.removeCurrentPage,

            tips = startPageIndex === 0 ? Z.element( "div#tap-tips.hide.switch-tips", {
                children : [Z.element( "div.gray-circle" ), Z.element( "div.red-circle" )]
            }, body ) : null; // 点击提示

        function ClickPage( pageInfo ) {
            var newPage = makePage( pageInfo );

            // 点击快进此帧,如果全部运行完,切页
            onTap( newPage.background || newPage, function () {
                if ( !newPage.runNextFrame || !newPage.runNextFrame() ) {
                    switchPage( newPage, curPageIndex + 1 );
                }
            } );

            // 如果有提示,在该页入场动画结束后显示提示,点击移除
            if ( tips ) {
                newPage.onEnterEnd && newPage.onEnterEnd( function () {
                    tips && toggleState( tips, "hide", "show" );
                } );
            }

            return newPage;
        }

        function switchPage( prePage, curIndex ) {
            // 更新当前页索引
            window.curPageIndex = getIndex( curIndex );
            var loading = fp.Loading();

            fc.onPageLoad( curPageIndex, function ( curPageInfo ) {
                loading.remove();

                var newPage = ClickPage( curPageInfo ),
                    switchAnimation = prePage.nodeName || curPageInfo.type ? switchAnimates["fade-dom"] :
                    getSwitchAnimate( curPageInfo.pageData ) || getSwitchAnimate( data ) || switchAnimates["push"],
                    animationInfo = switchAnimation( PageLayer( prePage ), PageLayer( newPage ), canvas ),
                    fastForwardHandler;

                // 移除现有的动画
                removeCurrentPage();

                function end() {
                    animationInfo.onEnd && animationInfo.onEnd();
                    fastForwardHandler.remove();
                    setPage( newPage );
                }

                // 如果有onDraw,表明是area动画,否则是DOM动画
                if ( animationInfo.onDraw ) {
                    var progress = Z.Progress( animationInfo );

                    fastForwardHandler = onTapOnce( canvas, function () {
                        progress.progress( 1 )
                    } );

                    canvas.root = {
                        draw : function ( gc ) {
                            animationInfo.onDraw( gc, progress.ratio() );
                            if ( progress.isEnd() ) {
                                end();
                            }
                            else {
                                canvas.dirty();
                            }
                        }
                    };

                    animationInfo.onStart && animationInfo.onStart();
                }
                else {
                    var switchAnimationHandler = animate( extend( animationInfo, {
                        onEnd : function () {
                            body.appendChild( canvas );
                            end();
                        }
                    } ), canvas.requestAnimate );

                    removeNode( canvas );

                    // 点击背景,快进切换动画
                    fastForwardHandler = onTapOnce( body, function () {
                        switchAnimationHandler && switchAnimationHandler.progress( 1 );
                    } );
                }

                canvas.dirty();
            } );
        }

        fc.onPageLoad( 0, function ( pageInfo ) {
            fc.startShow();

            // 点击移除提示
            onTapOnce( body, function () {
                removeNode( tips );
                tips = null;
            } );

            setPage( ClickPage( pageInfo ) );
        } );

        // 直接跳转到某页
        fp.jumpPage = function ( index ) {
            removeCurrentPage();
            setPage( ClickPage( pages[index] ) );
        };
    };
})();

/**
 * Created by 白 on 2014/11/24.
 * Canvas推系统
 */

(function () {
    var loopArray = Z.loopArray,
        toggleState = Z.toggleState,
        animate = Z.animate,
        removeNode = Z.removeNode,
        css = Z.css,

        CanvasMode = window.CanvasMode = window.CanvasMode || {};

    CanvasMode.push = function ( pages, fc ) {
        var startPageIndex = window.curPageIndex,
            body = window.body,

            canvas = fc.canvas,
            makePage = fc.makePage,
            setPage = fc.setPage,
            removeCurrentPage = fc.removeCurrentPage;

        fc.onPageLoad( startPageIndex || 1, function () {
            var tips = startPageIndex === 0 ? Z.element( "div#slide-tips.switch-tips", body ) : null, // 滑动提示
                hasToEnd = false; // 是否已经到达最后一页

            // 向上箭头和加载下页提示
            Z.element( "div.slide-arrow.switch-tips", body );
            Z.element( "div.loading-next-page-tips", body );

            if ( startPageIndex !== 0 ) {
                toggleState( document.body, "can-not-push", "can-push" );
            }

            fc.startShow();

            // 第一页加载完后,显示第一页
            setPage( makePage( pages[startPageIndex] ) );

            Z.onDragV( body, function ( event ) {
                // 移除tips
                if ( tips ) {
                    removeNode( tips );
                    tips = null;
                }

                var preInfo = curPageIndex === 0 && !hasToEnd ? null : getPageInfo( curPageIndex - 1 ),
                    nextInfo = getPageInfo( curPageIndex + 1 );

                // 如果滑出方向的页没有加载出,不做反应
                if ( event.directionY === false && !nextInfo || event.directionY === true && !preInfo ) {
                    return;
                }

                // 隐藏当前区域,隐藏箭头
                css( fc.canvas, "display", "none" );
                removeCurrentPage();
                toggleState( document.body, "can-push", "can-not-push" );
                document.body.classList.remove( "loading-next-page" );

                // 制作快照
                function Snapshot( info, page ) {
                    return info || page ? css( body.appendChild( PageLayer( page || makePage( info ) ) ), "z-index", 5 + Math.abs( info ? 1 : 0 ) ) : null;
                }

                var preSnapshot = Snapshot( preInfo ),
                    curSnapshot = Snapshot( null, fc.curPage() ),
                    nextSnapshot = Snapshot( nextInfo ),
                    top = 0,
                    minTop = !nextSnapshot ? 0 : -clientHeight,
                    maxTop = !preSnapshot ? 0 : clientHeight,
                    moveCount = 0;

                function move( targetTop ) {
                    top = targetTop;
                    var ratio = Math.abs( top / clientHeight / 2 );
                    css.transform( curSnapshot, css.translate( 0, top / 4, 0 ), css.scale( 1 - ratio ) );
                    preSnapshot && css.transform( preSnapshot, css.translate( 0, top - clientHeight, 0 ) );
                    nextSnapshot && css.transform( nextSnapshot, css.translate( 0, top + clientHeight, 0 ) );
                }

                move( 0 );

                event.onDragMove( function ( event ) {
                    ++moveCount;
                    move( Z.range( event.distanceY, minTop, maxTop ) );
                } );

                event.onDragEnd( function ( event ) {
                    removeNode( fc.canvas );
                    Z.removeCss( fc.canvas, "display" );
                    fp.lock( true );

                    // 计算比例,根据比例选择切到哪页
                    var ratio = top / clientHeight + ( event.speedY > 0 ? 0.5 : -0.5 ),
                        sign = Z.range( event.duration < 300 || moveCount < 3 ? event.distanceY < 0 ? -1 : 1 : ratio <= -0.5 ? -1 : ratio <= 0.5 ? 0 : 1,
                            minTop / clientHeight, maxTop / clientHeight ),
                        start = top,
                        end = sign * clientHeight;

                    var curPageInfo = pages[window.curPageIndex = getIndex( curPageIndex - sign )];
                    var curPage = null;
                    if ( !curPageInfo.type ) {
                        canvas.root = curPage = makePage( curPageInfo );
                        canvas.dirty();
                    }

                    animate( {
                        duration : 0.4,
                        onAnimate : function ( ratio ) {
                            move( Z.fromTo( start, end, ratio ) );
                        },
                        onEnd : function () {
                            // 移除body上的页面,更新index,设置当前页
                            loopArray( [preSnapshot, curSnapshot, nextSnapshot], removeNode );
                            setPage( curPage || makePage( curPageInfo ) );

                            // 判断是否滑到最后一页
                            if ( curPageIndex === pageNumber - 1 ) {
                                hasToEnd = true;
                            }

                            // 如果下一页在读,锁屏幕
                            document.body.classList.add( "loading-next-page" );
                            fc.onPageLoad( curPageIndex + 1, function () {
                                document.body.classList.remove( "loading-next-page" );
                                toggleState( document.body, "can-not-push", "can-push" );
                            } );

                            // 解锁
                            fp.lock( false );
                        }
                    } );
                } );
            } );
        } );

        // 直接跳转到某页
        fp.jumpPage = function ( index ) {
            removeCurrentPage();
            setPage( makePage( pages[index] ) );
        };
    };
})();

/**
 * Created by 白 on 2014/9/10.
 */

(function () {
    var loopArray = Z.loopArray,
        loopObj = Z.loopObj,
        insert = Z.insert,
        range = Z.range,
        Event = Z.Event,
        LinkedList = Z.LinkedList,
        Timing = Z.Timing,
        css = Z.css,
        extend = Z.extend,
        animate = Z.animate,
        removeNode = Z.removeNode,
        onTap = Z.onTap;

    function Page() {
        function AbsoluteArea() {
            var area = Z.Area(),
                components = [],
                drawComponents = [];

            // 绘制绝对布局区域
            area.draw = function ( gc ) {
                gc.save();

                if ( area.drawSelf ) {
                    // 绘制时约束
                    area.opacity = range( area.opacity, 0, 1 );
                    area.scale = Math.max( area.scale, 0 );

                    // 处理透明度
                    if ( area.opacity !== 1 ) {
                        gc.globalAlpha = area.opacity;
                    }

                    // 处理变换,根据区域的中心进行变换
                    if ( area.scale !== 1 || area.rotate !== 0 ) {
                        gc.translate( area.componentWidth / 2 << 0, area.componentHeight / 2 << 0 );
                        gc.scale( area.scale, area.scale );
                        gc.rotate( area.rotate );
                        gc.translate( -area.componentWidth / 2 << 0, -area.componentHeight / 2 << 0 );
                    }

                    area.drawSelf( gc );
                }

                // 根据z-index进行排序并绘制
                drawComponents = [];
                loopArray( components.sort( function ( lhs, rhs ) {
                    return lhs["z-index"] < rhs["z-index"] ? -1 : lhs["z-index"] === rhs["z-index"] ? 0 : 1;
                } ), function ( component ) {
                    if ( component.visible ) {
                        gc.save();
                        gc.translate( component.x, component.y );
                        component.draw( gc );
                        drawComponents.push( component );
                        gc.restore();
                    }
                } );

                gc.restore();
            };

            area.areaFromPoint = function ( x, y ) {
                return loopArray( Z.array.reverse( drawComponents ), function ( component ) {
                    if ( !component.cursorThrough &&
                        Z.inRect( x, y, component.x, component.y, component.componentWidth, component.componentHeight ) ) {
                        return component;
                    }
                } );
            };

            return insert( area, {
                component : function ( info ) {
                    var content = info.content,
                        area = AbsoluteArea();

                    // 处理点击
                    if ( info.onClick ) {
                        area.cursorThrough = false;
                        onTap( area, info.onClick );
                    }
                    else {
                        area.cursorThrough = true;
                    }

                    loopObj( extend( componentAttr( info ), {
                        visible : true
                    } ), function ( name, value ) {
                        Z.defineAutoProperty( area, name, {
                            value : value,
                            set : area.dirty
                        } )
                    } );

                    components.push( area );
                    return insert( area, {
                        componentWidth : content.width,
                        componentHeight : content.height,
                        drawSelf : content.draw,
                        transition : function ( info ) {
                            return runAnimate( {
                                component : area,
                                delay : info.delay,
                                duration : info.duration,
                                timing : info.timing,
                                onEnd : info.onEnd,
                                progress : {
                                    0 : info.start,
                                    100 : info.end
                                }
                            } );
                        },
                        infiniteAnimate : function ( info ) {
                            return runAnimate( extend( info, {
                                component : area
                            } ), true );
                        },
                        remove : function () {
                            area.visible = false;
                            area.dirty();
                        }
                    } );
                }
            } );
        }

        // 动画列表
        var parent = AbsoluteArea(),
            animationList = LinkedList(),
            startEnterKeyFrame = null,
            enterEndEvent = Event(),
            removeEvent = Event(),
            showEvent = Event(),
            lastEnterKeyFrame = null;

        // 运行动画
        function runAnimate( animationInfo, infinite ) {
            var area = animationInfo.component,
                baseStyle = animationInfo.baseStyle || componentAttr( area ),
                duration = animationInfo.duration,
                timing = animationInfo.timing || Timing.ease,
                frames = [],
                remove;

            loopObj( animationInfo.progress, function ( ratio, style ) {
                frames.push( {
                    time : duration * parseInt( ratio, 10 ) / 100,
                    style : extend( baseStyle, style || {} )
                } );
            } );

            // 头和尾的处理
            if ( frames[0].time !== 0 ) {
                frames.unshift( {
                    time : 0,
                    style : baseStyle
                } );
            }
            if ( frames.top.time !== duration ) {
                frames.push( {
                    time : duration,
                    style : baseStyle
                } );
            }

            // 播放动画,并将动画句柄添加到动画
            function doAnimate( onEnd ) {
                return animate( {
                    delay : animationInfo.delay,
                    duration : duration,
                    timing : Timing.linear,
                    onStart : animationInfo.onStart,
                    onAnimate : function ( totalRatio ) {
                        var time = duration * totalRatio,
                            targetFrame = null;

                        // 找到所属的时间段
                        if ( totalRatio === 1 ) {
                            targetFrame = frames.length - 1;
                        }
                        else {
                            loopArray( frames, function ( frame, i ) {
                                if ( targetFrame === null && time < frame.time ) {
                                    targetFrame = i;
                                }
                            } );
                        }

                        // 计算当前的ratio,并设置属性
                        var start = frames[targetFrame - 1], end = frames[targetFrame],
                            ratio = timing( ( time - start.time ) / ( end.time - start.time ) );
                        loopObj( start.style, function ( key ) {
                            area[key] = Z.fromTo( start.style[key], end.style[key], ratio );
                        } );

                        parent.dirty();
                    },
                    onEnd : onEnd
                } )
            }

            // 如果不是循环动画,加入到动画列表中,可以点击快进
            if ( !infinite ) {
                var node = animationList.insert( animationList.node( doAnimate( function () {
                    animationInfo.onEnd && animationInfo.onEnd();
                    animationList.remove( node );
                } ) ), null );

                remove = function () {
                    animationList.remove( node );
                    node.value.remove();
                };
            }
            // 循环动画递归播放
            else {
                var handle = doAnimate( function loopAnimate() {
                    handle = doAnimate( loopAnimate );
                } );

                removeEvent.regist( remove = function () {
                    handle.remove();
                } );
            }

            return {
                remove : remove
            };
        }

        // 运行一组动画
        function runKeyFrame( keyFrame ) {
            loopArray( keyFrame, function ( animation ) {
                runAnimate( animation );
            } );
        }

        // 绑定关键帧的结束回调
        function onKeyFrameEnd( keyFrame, onEnd ) {
            var count = keyFrame.length;
            loopArray( keyFrame, function ( animation ) {
                animation.onEnd = function () {
                    animation.onEnter && animation.onEnter();
                    if ( --count === 0 ) {
                        onEnd && onEnd();
                    }
                }
            } );
        }

        return insert( parent, {
            // 回收该页
            recycle : function () {
                // 移除当前运行的所有动画
                LinkedList.loop( animationList, function ( animation, node ) {
                    animation.remove();
                    animationList.remove( node );
                } );

                removeEvent.trig();
            },
            // 注册进入动画
            registEnterAnimation : function ( keyFrameList ) {
                if ( keyFrameList.length === 0 ) {
                    return;
                }

                // 将入场动画串联起来
                loopArray( keyFrameList, function ( keyFrame ) {
                    // 将元素的属性设置为动画的起始属性,并记录动画的baseStyle
                    loopArray( keyFrame, function ( animation ) {
                        animation.baseStyle = componentAttr( animation.component );
                        insert( animation.component, animation.progress["0"] || {} );
                    } );

                    // 如果有上一帧,上一帧结束后播放本帧
                    if ( lastEnterKeyFrame ) {
                        onKeyFrameEnd( lastEnterKeyFrame, function () {
                            runKeyFrame( keyFrame );
                        } );
                    }

                    if ( startEnterKeyFrame === null ) {
                        startEnterKeyFrame = keyFrame;
                    }

                    lastEnterKeyFrame = keyFrame;
                } );
            },
            // 快进到下一帧
            runNextFrame : function () {
                var isFast = false;

                // 快进当前动画
                LinkedList.loop( animationList, function ( animation ) {
                    animation.progress( 1 );
                    isFast = true;
                } );

                return isFast;
            },
            // 启动进场动画
            start : function () {
                showEvent.trig();

                // 如果有入场动画,启动入场动画,当入场动画最后一帧结束时触发enterEnd事件
                if ( startEnterKeyFrame ) {
                    onKeyFrameEnd( lastEnterKeyFrame, enterEndEvent.trig );
                    runKeyFrame( startEnterKeyFrame );
                }
                // 否则直接触发enterEnd事件
                else {
                    enterEndEvent.trig();
                }
            },
            onShow : showEvent.regist,
            onRemove : removeEvent.regist,
            onEnterEnd : enterEndEvent.regist
        } );
    }

    window.PageLayer = function ( page ) {
        if ( page.nodeName ) {
            return page;
        }
        else {
            var layer = Z.Layer();
            layer.classList.add( "layer" );
            layer.resize( clientWidth, clientHeight );
            layer.draw( page.draw );
            return layer;
        }
    };

    window.CanvasSystem = function ( pages, fc ) {
        var body = window.body,
            canvas = body.appendChild( css( Z.Canvas(), {
                position : "absolute",
                left : 0,
                top : 0,
                "z-index" : 4
            } ) ),
            curPage = null; // 当前页面;

        canvas.resize( clientWidth, clientHeight );

        insert( fc, {
            canvas : canvas,

            // 根据一个页面制作一个area
            makePage : function ( pageInfo ) {
                if ( pageInfo.type ) {
                    return pageInfo.create();
                }
                else {
                    var area = Page(),
                        background = area.background = area.component( {
                            content : Content.Rect( {
                                color : "#000000",
                                width : clientWidth,
                                height : clientHeight
                            } ),
                            x : 0,
                            y : 0,
                            "z-index" : -1
                        } );
                    background.cursorThrough = false;
                    pageInfo.create( area );
                    return area;
                }
            },

            // 将区域设置为当前区域,并启动入场动画
            setPage : function ( page ) {
                if ( page.nodeName ) {
                    body.appendChild( page );
                    removeNode( canvas );
                }
                else {
                    !canvas.parentNode && body.appendChild( canvas );
                    canvas.root !== page && ( canvas.root = page );
                    canvas.dirty();
                }

                curPage = page;
                page.start();
            },

            // 返回当前页
            curPage : function () {
                return curPage;
            },

            // 移除当前区域
            removeCurrentPage : function () {
                canvas.root = null;
                curPage.nodeName && removeNode( curPage );
                curPage && curPage.recycle();
            }
        } );

        CanvasMode[pages.data.mode]( pages, fc );
    };
}());

/**
 * Created by 白 on 2014/9/10.
 */

(function ( fp ) {
    var loopArray = Z.loopArray,
        loopObj = Z.loopObj,
        insert = Z.insert,
        extend = Z.extend,
        tupleString = Z.tupleString,
        css = Z.css,
        px = css.px,
        s = css.s,
        bindEvent = Z.bindEvent,
        animate = Z.animate,

        animateCount = 0;

    function Page() {
        var parent = Z.element( "div.page.animation-prepare" ),
            showEvent = Z.Event(),
            removeEvent = Z.Event(),
            curDelay = 0, // 目前的延迟
            enterElements = []; // 已经进入的元素

        // 根据style,生成css对象
        function genCSS( style ) {
            return {
                transform : [css.translate( style.x, style.y, 0 ),
                    css.scale( Math.max( style.scale, 0.01 ) ), css.rotateZ( style.rotate )].join( " " ),
                opacity : style.opacity,
                "z-index" : style["z-index"]
            };
        }

        function animationString( el, animation, delay, arg ) {
            // 根据progress,生成css规则字符串
            var animateId = "animate" + animateCount++,
                timing = animation.timing ? tupleString( "cubic-bezier", animation.timing.arg ) : "ease",
                progressString = "";

            loopObj( animation.progress, function ( ratio, attr ) {
                progressString += ratio + "% {" + Z.cssRuleString( genCSS( extend( componentAttr( el ), attr ) ) ) + "}";
            } );

            Z.insertCSSRules( "@-webkit-keyframes " + animateId, progressString );

            return [animateId, timing, s( animation.duration ), s( delay || 0 ), arg].join( " " )
        }

        function AbsoluteElement( parent ) {
            return insert( parent, {
                component : function ( info ) {
                    var content = info.content,
                        el = parent.appendChild( content.element() ),
                        attr = componentAttr( info );

                    // 设置样式
                    css( el, extend( genCSS( attr ), {
                        position : "absolute",
                        left : 0,
                        top : 0,
                        width : px( content.width ),
                        height : px( content.height )
                    } ) );

                    // 处理点击
                    if ( info.onClick ) {
                        el.cursorThrough = false;
                        Z.onTap( el, info.onClick )
                    }

                    Z.onPointerDown( el, function ( event ) {
                        if ( !el.cursorThrough ) {
                            event.preventDefault();
                            event.stopPropagation();
                        }
                    } );

                    // 添加样式和尺寸属性
                    loopArray( ["x", "y", "opacity", "scale", "rotate", "z-index"], function ( attrName ) {
                        Object.defineProperty( el, attrName, {
                            get : function () {
                                return attr[attrName];
                            },
                            set : function ( val ) {
                                attr[attrName] = val;
                                css( el, genCSS( attr ) );
                            }
                        } );
                    } );

                    return AbsoluteElement( insert( el, {
                        cursorThrough : true,
                        componentWidth : content.width,
                        componentHeight : content.height,
                        transition : function ( arg ) {
                            var timing = arg.timing ? tupleString( "cubic-bezier", arg.timing.arg ) : "ease";
                            css( el, "transition", [timing, s( arg.duration ), s( arg.delay || 0 )].join( " " ) );
                            css( el, css( genCSS( extend( attr, arg.end ) ) ) );

                            var end = bindEvent( el, "webkitTransitionEnd", function () {
                                Z.removeCss( el, "transition" );
                                arg.onEnd && arg.onEnd( el );
                                end.remove();
                            } );

                            return {
                                remove : function () {
                                }
                            };
                        },
                        infiniteAnimate : function ( arg ) {
                            css( el, "animation", animationString( el, arg, 0, "infinite" ) );
                        },
                        remove : function () {
                            Z.removeNode( el );
                        }
                    } ) );
                }
            } );
        }

        return insert( AbsoluteElement( parent ), {
            registEnterAnimation : function ( keyFrameList ) {
                loopArray( keyFrameList, function ( keyFrame ) {
                    var duration = 0;

                    loopArray( keyFrame, function ( animation ) {
                        var el = animation.component,
                            enterDelay = animation.delay || 0; // 进入延迟;

                        // 进入动画和进入回调
                        el.enterAnimation = animationString( el, animation, curDelay + enterDelay, "backwards" );
                        el.onEnter = animation.onEnter;

                        duration = Math.max( animation.duration + enterDelay, duration ); // 计算这一帧需要的延迟

                        enterElements.push( el );
                    } );

                    curDelay += duration; // 更新延迟
                } );

                loopArray( enterElements, function ( el ) {
                    // 设置进入动画
                    css( el, "animation", el.enterAnimation );

                    // 如果有进入回调,在进入动画结束之后回调
                    if ( el.onEnter ) {
                        var end = bindEvent( el, "webkitAnimationEnd", function () {
                            el.onEnter( el );
                            end.remove();
                        } );
                    }
                } );
            },
            start : function () {
                Z.toggleState( parent, "animation-prepare", "animation-run" );
                showEvent.trig();
            },
            onShow : showEvent.regist,
            recycle : removeEvent.trig,
            onRemove : removeEvent.regist
        } );
    }

    window.DOMSystem = function ( pages, fc ) {
        document.documentElement.classList.add( "dom-mode" );

        var body = window.body,
            curPage = null, // 当前页
            startPageIndex = window.curPageIndex,
            hasToEnd = false; // 是否到最后一张

        // 制作页面,返回页面元素,如果页面尚不能制作,返回空,如果页面已经制作,返回制作好的页面
        function newPage( index ) {
            var page,
                newIndex = getIndex( index ),
                pageInfo = pages[newIndex];

            if ( pageInfo && pageInfo.isLoad ) {
                page = curPage = pageInfo.special ? pageInfo.create() : pageInfo.create( Page() );
                window.curPageIndex = newIndex;
                body.appendChild( page );

                var loadingNextPageTips = Z.element( "div.loading-next-page-tips.loading-next-page", page );
                fc.onPageLoad( newIndex + 1, function () {
                    Z.removeNode( loadingNextPageTips );
                    Z.element( "div.slide-arrow.can-push.switch-tips", page )
                } );
                if ( newIndex === pageNumber - 1 ) {
                    hasToEnd = true;
                }
            }

            return page;
        }

        // 当第二页加载完毕后显示
        fc.onPageLoad( startPageIndex || 1, function () {
            fc.startShow();

            // 建立页,并启动它
            newPage( startPageIndex );
            curPage.start();

            // 如果以第0页进入,构建提示
            var firstPageTips = null;
            if ( startPageIndex === 0 ) {
                firstPageTips = Z.element( "div#slide-tips.switch-tips", body );
                document.body.classList.add( "first" );
            }

            Z.onDragV( body, function ( event ) {
                // 如果有提示,移除它
                if ( firstPageTips ) {
                    Z.removeNode( firstPageTips );
                    firstPageTips = null;
                    document.body.classList.remove( "first" );
                }

                function cutNew( step, className ) {
                    var oldPage = curPage;
                    oldPage.recycle();
                    newPage( curPageIndex + step );

                    // 往下滑,滑出上一页
                    if ( curPage ) {
                        fp.lock( true );
                        oldPage.classList.add( "cur-" + className );
                        curPage.classList.add( "new-" + className );

                        // 结束事件
                        var end = Z.bindEvent( curPage, "webkitAnimationEnd", function () {
                            end.remove();

                            // 清理class,移除前一个页面,并重制前一个页面的动画
                            oldPage.classList.remove( "cur-" + className );
                            curPage.classList.remove( "new-" + className );
                            Z.removeNode( oldPage );

                            // 运行新页面的动画
                            curPage.start();

                            // 解除锁定
                            fp.lock( false );
                        } );
                    }
                }

                if ( event.directionY ) {
                    if ( !( curPageIndex === 0 && !hasToEnd ) ) {
                        cutNew( -1, "down" );
                    }
                }
                else {
                    cutNew( 1, "up" );
                }
            } );
        } );

        // 直接跳转到某页
        fp.jumpPage = function ( index ) {
            curPage.recycle();
            Z.removeNode( curPage );
            newPage( index );
            curPage.start();
        };
    };

    window.DOMPage = Page;
}( window.fp ));

/**
 * Created by 白 on 2014/10/13.
 */

(function () {
    var element = Z.element,
        css = Z.css,
        px = css.px,
        rgba = Z.TupleString( "rgba" ),
        loopArray = Z.loopArray,

        measureGc = document.createElement( "canvas" ).getContext( "2d" );

    window.Content = window.Content || {};
    window.Component = window.Component || {};

    // region 文本绘制
    // 根据style对象生成一个font字符串
    function Font( style ) {
        style = style || {};
        return [style.fontStyle || "normal", style.fontVariant || "normal", style.fontWeight || "normal",
            ( style.fontSize || 12 ) + "px", style.fontFamily || "sans-serif"].join( " " );
    }

    function getTextWidth( text, fontStyle ) {
        measureGc.font = Font( fontStyle );
        return measureGc.measureText( text ).width;
    }

    // 在指定宽度下测量文字,做换行工作
    function measureText( text, width, style ) {
        var canvas = document.createElement( "canvas" ),
            height = 0,
            measure = [];

        measureGc.font = Font( style );

        loopArray( text.split( "\n" ), function ( text ) {
            var lines = [],
                curLine = null,
                curX = 0;

            lines.text = text;
            height += ( style.margin || 0 ) * 2;

            Z.loopString( text, function ( ch ) {
                var curWidth = measureGc.measureText( ch ).width;
                // 如果当前字符超出行宽,新起一行
                if ( curLine === null || curWidth + curX > width ) {
                    height += style.lineHeight;
                    lines.push( curLine = [0] );
                    curX = curWidth;
                }
                // 否则加入到行尾
                else {
                    curLine.push( curX );
                    curX += curWidth;
                }
            }, true );

            measure.push( lines );
        } );

        measure.style = style;
        measure.width = width;
        measure.height = height;

        return measure;
    }

    // 绘制文字测量
    function drawTextMeasure( gc, measure ) {
        var style = measure.style, curY = 0, margin = style.margin || 0;

        gc.fillStyle = style.color;
        gc.textBaseline = "middle";
        gc.font = Font( style );

        // 绘制文字
        loopArray( measure, function ( paraLines ) {
            var curIndex = 0;
            curY += margin;
            loopArray( paraLines, function ( line ) {
                gc.fillText( paraLines.text.substring( curIndex, curIndex + line.length ), 0, curY + style.lineHeight / 2 << 0 );
                curY += style.lineHeight;
                curIndex += line.length;
            } );
            curY += margin;
        } );
    }

    // 根据info生成css字体样式
    function cssFontStyle( info ) {
        var retVal = {};
        Z.loopObj( info, function ( key, value ) {
            switch ( key ) {
                case "fontSize":
                    retVal["font-size"] = px( value );
                    break;
                case "lineHeight":
                    retVal["line-height"] = px( value );
                    break;
                case "fontWeight":
                    retVal["font-weight"] = value;
                    break;
                case "fontStyle":
                    retVal["font-style"] = value;
                    break;
                case "color":
                    retVal["color"] = value;
                    break;
            }
        } );
        return retVal;
    }

    // endregion

    // 图片
    Content.Image = function ( img, width, height ) {
        width = width || img.halfWidth;
        height = height || img.halfHeight;

        return {
            width : width,
            height : height,
            element : function () {
                return element( "div", {
                    children : css( img, {
                        position : "absolute",
                        width : px( width ),
                        height : px( height ),
                        left : 0,
                        right : 0
                    } )
                } );
            },
            draw : function ( gc ) {
                gc.drawImage( img, 0, 0, width, height );
            }
        };
    };

    // 图片覆盖
    Content.ImageCover = function ( img, width, height, style ) {
        var borderWidth = 0, borderColor = "";

        if ( style ) {
            if ( style.border ) {
                borderWidth = style.border.width;
                borderColor = style.border.color;
            }
        }
        var imgWidth = width - borderWidth * 2, imgHeight = height - borderWidth * 2;

        return {
            width : imgWidth,
            height : imgHeight,
            element : function () {
                return element( "div", {
                    css : {
                        "box-sizing" : "border-box",
                        overflow : "hidden",
                        border : ["solid", px( borderWidth ), borderColor].join( " " )
                    },
                    children : css( img, Z.getImageCoverStyle( img, imgWidth, imgHeight ) )
                } );
            },
            draw : function ( gc ) {
                var imgMeasure = img.imgMeasure || ( img.imgMeasure = Z.measureCover( img, imgWidth, imgHeight ) );

                gc.save();
                gc.translate( borderWidth, borderWidth );
                Z.drawImageMeasure( gc, imgMeasure );
                gc.restore();
                if ( borderWidth !== 0 ) {
                    gc.fillStyle = borderColor;
                    gc.fillRect( 0, 0, width, borderWidth );
                    gc.fillRect( 0, 0, borderWidth, height );
                    gc.fillRect( width - borderWidth, 0, borderWidth, height );
                    gc.fillRect( 0, height - borderWidth, width, borderWidth );
                }
            }
        };
    };

    // 矩形
    // 如未提供颜色,就是一个空矩形
    Content.Rect = function ( info ) {
        var color = info.color || "";

        return {
            width : info.width,
            height : info.height,
            element : function () {
                return element( "div", {
                    css : {
                        background : color
                    }
                } );
            },
            draw : function ( gc ) {
                if ( color ) {
                    gc.fillStyle = color;
                    gc.fillRect( 0, 0, info.width, info.height );
                }
            }
        };
    };

    // 圆形
    Content.Circle = function ( info ) {
        var r = info.r;

        return {
            width : r * 2,
            height : r * 2,
            element : function () {
                return element( "div", {
                    css : {
                        "border-radius" : px( r ),
                        background : info.color
                    }
                } );
            },
            draw : function ( gc ) {
                gc.save();
                gc.beginPath();
                gc.arc( r, r, r, 0, 2 * Math.PI );
                gc.closePath();
                gc.fillStyle = info.color;
                gc.fill();
                gc.restore();
            }
        }
    };

    // 行文本
    Content.LineText = function ( info ) {
        var font = Font( info ),
            text = info.text,
            textWidth = info.textWidth || getTextWidth( text, info );

        if ( info.overflow ) {
            if ( textWidth > info.width ) {
                for ( var i = 0; i !== info.text.length; ++i ) {
                    if ( getTextWidth( text.substring( 0, i + 1 ) + "…", info ) > info.width ) {
                        break;
                    }
                }
                text = text.substring( 0, i ) + "…";
                textWidth = getTextWidth( text, info );
            }
        }

        info.textWidth = textWidth;

        return {
            width : info.width,
            height : info.lineHeight,
            element : function () {
                var overflow = info.overflow ? {
                    overflow : "hidden",
                    "white-space" : "nowrap",
                    "text-overflow" : "ellipsis"
                } : {};

                return element( "span", {
                    css : Z.insert( cssFontStyle( info ), overflow, {
                        "text-align" : info.isLeft ? "left" : "center",
                        width : px( info.width ),
                        "white-space" : "nowrap"
                    } ),
                    innerHTML : text
                } );
            },
            draw : function ( gc ) {
                gc.font = font;
                gc.textBaseline = "middle";
                gc.fillStyle = info.color;
                var left = info.isLeft ? 0 : ( info.width - textWidth ) / 2 << 0;
                gc.fillText( text, left, info.lineHeight / 2 << 0 );
            }
        };
    };

    // 添加段
    function addP( parent, info ) {
        loopArray( info.text.split( "\n" ), function ( p ) {
            element( "p", {
                innerHTML : p || "&nbsp",
                css : Z.insert( {
                    margin : px( info.margin * 2 ) + " 0"
                }, info.breakWord ? {
                    "word-break" : "break-all",
                    "word-wrap" : "break-word"
                } : {} )
            }, parent );
        } );
    }

    // 测量一个块
    function measureBlock( info ) {
        function toXML( text ) {
            var parent = document.createElement( "div" );
            Z.loopArray( text.split( "\n" ), function ( paraText ) {
                Z.element( "p", {
                    classList : "normal",
                    innerHTML : Z.XMLReader.encodeString( paraText )
                }, parent );
            } );
            return parent.innerHTML;
        }

        var textMeasure = info.textMeasure = info.textMeasure || (
            info.breakWord ? measureText( info.text, info.width, info ) : Z.measureRichText( {
                p : {
                    normal : {
                        marginTop : info.margin,
                        marginBottom : info.margin,

                        lineHeight : info.lineHeight,
                        lineGap : 0,
                        indent : 0,

                        fontFamily : "sans-serif",
                        fontSize : info.fontSize,
                        textFillStyle : info.color
                    }
                }
            }, Z.parseRichText( toXML( info.text ) ), info.width ) );

        textMeasure.draw = function ( gc ) {
            ( info.breakWord ? drawTextMeasure : Z.drawRichText )( gc, textMeasure );
        };

        return textMeasure;
    }

    // 块文本
    Content.BlockText = function ( info ) {
        var height = 0;

        !window.highPerformance ? function () {
            var testEl = element( "div", {
                css : Z.insert( cssFontStyle( info ), {
                    position : "absolute",
                    width : px( info.width )
                } )
            }, document.body );
            // 添加段
            addP( testEl, info );
            height = testEl.offsetHeight;
            Z.removeNode( testEl );
        }() : function () {
            height = measureBlock( info ).height;
        }();

        return {
            width : info.width,
            height : height,
            element : function () {
                var el = element( "div", {
                    css : Z.insert( cssFontStyle( info ), {
                        width : px( info.width )
                    } )
                } );

                // 添加段
                addP( el, info );
                return el;
            },
            draw : function ( gc ) {
                measureBlock( info ).draw( gc );
            }
        };
    };

    // 带居中功能的块文本
    Content.MiddleBlockText = function ( info ) {
        return {
            width : info.width,
            height : info.height,
            element : function () {
                var el = element( "div", {
                        css : {
                            width : px( info.width ),
                            height : px( info.height )
                        }
                    } ),
                    table = element( "div", {
                        css : Z.insert( cssFontStyle( info ), {
                            "vertical-align" : "middle",
                            display : "table-cell",
                            width : px( info.width ),
                            height : px( info.height )
                        } )
                    }, el );

                addP( table, info );
                return el;
            },
            draw : function ( gc ) {
                var textMeasure = measureBlock( info );
                gc.save();
                gc.translate( 0, ( info.height - textMeasure.height ) / 2 << 0 );
                textMeasure.draw( gc );
                gc.restore();
            }
        };
    };

    // 背景图片覆盖
    Component.BackgroundImageCover = function ( page, img, zIndex ) {
        page.component( {
            content : Content.ImageCover( img, clientWidth, clientHeight ),
            x : 0,
            y : 0,
            "z-index" : zIndex || 0
        } );
    };

    exports.getTextWidth = getTextWidth;
})();

/**
 * Created by 白 on 2014/9/11.
 */

(function () {
    registEnterAnimate( {
        flyInto : function ( component, direct ) {
            var startX = component.x, startY = component.y;

            switch ( direct ) {
                case "left":
                    startX = -component.componentWidth;
                    break;
                case "right":
                    startX = clientWidth;
                    break;
                case "top":
                    startY = -component.componentHeight;
                    break;
                case "bottom":
                    startY = clientHeight;
                    break;
            }

            return {
                "0" : {
                    x : startX,
                    y : startY
                }
            };
        },
        emerge : function ( component, direct ) {
            var offsetX = 0, offsetY = 0;
            switch ( direct ) {
                case "left":
                    offsetX = -20;
                    break;
                case "right":
                    offsetX = 20;
                    break;
                case "top":
                    offsetY = -20;
                    break;
                default :
                    offsetY = 20;
                    break;
            }
            return {
                "0" : {
                    x : component.x + offsetX,
                    y : component.y + offsetY,
                    opacity : 0
                }
            };
        },
        scale : function () {
            return {
                "0" : {
                    scale : 0
                }
            };
        },
        fadeIn : function () {
            return {
                "0" : {
                    opacity : 0
                }
            };
        }
    } );
})();

/**
 * Created by 白 on 2014/10/17.
 * 联系我们板式
 */

(function () {
    var frameSrc = "layout-context-text-frame.png";

    registLayout( "contact", {
        resource : ["layout-contact-background.png", frameSrc, frameSrc, frameSrc, frameSrc, frameSrc],
        create : function ( page, data ) {
            var textFrame = data.resource[1],
                textFrameWidth = textFrame.halfWidth,
                textFrameHeight = textFrame.halfHeight;

            // 背景图
            page.component( {
                content : Content.ImageCover( data.image[0], clientWidth, clientHeight ),
                x : 0,
                y : 0,
                "z-index" : 0
            } );

            // 联系我们+线
            page.component( {
                content : Content.ImageCover( data.resource[0], clientWidth, clientHeight ),
                x : 0,
                y : 0,
                "z-index" : 1
            } );

            var totalCount = 0;
            Z.loopArray( data.text, function ( text ) {
                text !== "" && ++totalCount;
            } );
            var startY = middleY( 143 ),
                totalHeight = 315,
                margin = ( totalHeight - textFrameHeight * totalCount ) / ( totalCount + 1 ) << 0;

            var count = 0;
            Z.loopArray( [
                {
                    caption : "联系电话",
                    click : function ( text ) {
                        location.href = "tel:" + text;
                    }
                },
                {
                    caption : "联系邮箱",
                    click : function ( text ) {
                        location.href = "mailto:" + text;
                    }
                },
                {
                    caption : "官方网站",
                    click : function ( text ) {
                        fp.jump( text );
                    }
                },
                {
                    caption : "微信号"
                },
                {
                    caption : "微博",
                    click : function ( text ) {
                        fp.jump( "http://weibo.com/n/" + text );
                    }
                }
            ], function ( info, i ) {
                var text = data.text[i],
                    caption = info.caption + "：",
                    captionWidth = Z.getTextWidth( caption, {
                        fontSize : 14
                    } ),
                    paddingX = 14,
                    marginX = 8;

                if ( text === "" ) {
                    return;
                }

                var frame = page.component( {
                    content : Content.Image( data.resource[i + 1] ),
                    x : ( clientWidth - textFrameWidth ) / 2 << 0,
                    y : startY + margin * ( count + 1 ) + textFrameHeight * count,
                    "z-index" : 2,
                    onClick : function () {
                        info.click && info.click( text );
                    }
                } );

                frame.component( {
                    content : Content.LineText( {
                        text : caption,
                        lineHeight : 44,
                        fontSize : 14,
                        color : "#FFFFFF",
                        width : captionWidth
                    } ),
                    x : paddingX,
                    y : 0
                } );

                frame.component( {
                    content : Content.MiddleBlockText( {
                        text : text,
                        lineHeight : 16,
                        fontSize : 12,
                        color : "#FFFFFF",
                        margin : 0,
                        width : textFrameWidth - 2 * paddingX - marginX - captionWidth,
                        height : 44,
                        breakWord : true
                    } ),
                    x : paddingX + marginX + captionWidth,
                    y : 0
                } );

                ++count;
            } );
        }
    } );
})();

/**
 * Created by 白 on 2014/9/16.
 * 最后一页
 */

(function () {
    var isTrace = false;

    registLayout( "copyright", {
        resource : ["layout-cloud7-background.png"],
        create : function ( page, data ) {
            var author = data.author,
                background = data.resource[0],

                authorWidth = Z.getTextWidth( author, {
                    fontSize : 16
                } ),
                captionWidth = Z.getTextWidth( "作品", {
                    fontSize : 16
                } ),

                blankWidth = 20,
                totalWidth = authorWidth + blankWidth + captionWidth;

            // 背景+头像遮罩
            page.component( {
                content : Content.Image( background ),
                x : center( clientWidth, background.halfWidth ),
                y : center( clientHeight, background.halfHeight ),
                "z-index" : 1
            } );

            // 用户头像
            page.component( {
                content : Content.ImageCover( data.image[0], 56, 56 ),
                x : middleX( 136 ),
                y : middleY( 81 )
            } );

            // xx用户 作品
            var authorLine = page.component( {
                content : Content.Rect( {
                    width : totalWidth,
                    height : 16
                } ),
                x : center( clientWidth, totalWidth ),
                y : middleY( 154 ),
                "z-index" : 2
            } );

            authorLine.component( {
                content : Content.LineText( {
                    text : author,
                    lineHeight : 16,
                    fontSize : 16,
                    fontStyle : "italic",
                    color : "#fc5e28",
                    width : authorWidth
                } ),
                x : 0,
                y : 0
            } );

            authorLine.component( {
                content : Content.LineText( {
                    text : "作品",
                    lineHeight : 16,
                    fontSize : 16,
                    fontStyle : "italic",
                    color : "#A3AEC1",
                    width : captionWidth
                } ),
                x : authorWidth + blankWidth,
                y : 0
            } );

            // xx的作品列表
            page.component( {
                content : Content.LineText( {
                    text : data.title,
                    width : 241,
                    lineHeight : 14,
                    fontSize : 12,
                    color : "#A3AEC1",
                    isLeft : true
                } ),
                x : middleX( 40 ),
                y : middleY( 203 ),
                "z-index" : 2
            } );

            // 作品链接
            var workX = [40, 130, 220];
            Z.loopArray( data.works, function ( workData, i ) {
                if ( i > 2 ) {
                    return;
                }

                var work = page.component( {
                    content : Content.Rect( {
                        width : 60,
                        height : 83
                    } ),
                    x : middleX( workX[i] ),
                    y : middleY( 233 ),
                    "z-index" : 2,
                    onClick : function () {
                        location.href = workData.url
                    }
                } );

                // 图像
                work.component( {
                    content : Content.ImageCover( data.image[i + 1], 60, 60 ),
                    x : 0,
                    y : 0,
                    "z-index" : 2
                } );

                // 名称
                work.component( {
                    content : Content.LineText( {
                        text : workData.title,
                        width : 80,
                        lineHeight : 14,
                        fontSize : 10,
                        color : "#A3AEC1",
                        overflow : true,
                        isLeft : true
                    } ),
                    x : 0,
                    y : 69,
                    "z-index" : 2
                } );
            } );

            // 立即分享按钮
            page.component( {
                content : Content.Rect( {
                    width : 110,
                    height : 35
                } ),
                x : middleX( 40 ),
                y : middleY( 343 ),
                "z-index" : 2,
                onClick : function () {
                    var tips = document.body.appendChild( Z.element( "div.share-tips" ) );

                    Z.onTap( tips, function () {
                        Z.removeNode( tips );
                    } );
                }
            } );

            // 我要创作按钮
            page.component( {
                content : Content.Rect( {
                    width : 110,
                    height : 35
                } ),
                x : middleX( 170 ),
                y : middleY( 343 ),
                "z-index" : 2,
                onClick : function () {
                    if ( /chuye/gi.test( navigator.userAgent ) ) {
                        fp.alert( "您正在使用初页" );
                    }
                    else if ( ua.ios || ua.win32 ) {
                        window.AnalyticsDownload && window.AnalyticsDownload( {
                            title : "点击下载",
                            url : "http://chuye.cloud7.com.cn" + virtualPath + "/download/click"
                        } );

                        if ( ua.MicroMessenger ) {
                            location.href = "http://mp.weixin.qq.com/mp/redirect?url=https://itunes.apple.com/cn/app/chu-ye/id910560238?mt=8";
                        }
                        else {
                            location.href = "https://itunes.apple.com/cn/app/chu-ye/id910560238?mt=8";
                        }
                    }
                    else {
                        var systemName = ua.android ? "Android" : "非ios",
                            message = Z.element( "div.not-support-message", {
                                innerHTML : "<p>╮ (╯﹏╰）╭</p><p></p>&nbsp<p>暂不支持" + systemName + "系统</p><p>敬请期待……</p>"
                            } );

                        fp.alert(message.outerHTML);
                    }
                }
            } );

            if ( !isTrace ) {
                window.AnalyticsDownload && window.AnalyticsDownload( {
                    title : "下载页",
                    url : "http://chuye.cloud7.com.cn" + virtualPath + "/download"
                } );
                isTrace = true;
            }
        }
    } );
})();

/**
 * Created by 白 on 2014/9/12.
 * 框+图的板式
 */

(function () {
    var registFrameImageLayout = Z.KeyValueFunction( function ( name, info ) {
        registLayout( name, {
            resource : [info.frame],
            create : function ( page, data ) {
                var enterAnimation = [];

                // 遍历图片,分配区域,并计算入场动画
                Z.loopArray( data.image, function ( img, i ) {
                    var imgInfo = info.img[i], // 图片信息
                        area = page.component( {
                            content : Content.ImageCover( img, Math.ceil( imgInfo.width * xRatio ) + 1, Math.ceil( imgInfo.height * yRatio ) + 1 ),
                            x : imgInfo.x * xRatio << 0,
                            y : imgInfo.y * yRatio << 0
                        } ),
                        enterAnimationInfo = imgInfo.enterAnimate; // 进入动画信息

                    enterAnimation.push( [enterAnimate[enterAnimationInfo.name].apply(
                        null, [area].concat( [enterAnimationInfo.arg] ) )] );
                } );

                // 相框图
                page.component( {
                    content : Content.Image( data.resource[0], clientWidth, clientHeight ),
                    x : 0,
                    y : 0,
                    "z-index" : 100
                } );

                page.registEnterAnimation( enterAnimation );
            }
        } );
    } );

    registFrameImageLayout( {
        "MutipleImage02" : {
            frame : "layout-MutipleImage02-frame.png",
            img : [
                {
                    x : 25,
                    y : 16,
                    width : 280,
                    height : 157,
                    enterAnimate : {
                        name : "flyInto",
                        arg : "left"
                    }
                },
                {
                    x : 25,
                    y : 173,
                    width : 280,
                    height : 157,
                    enterAnimate : {
                        name : "flyInto",
                        arg : "right"
                    }
                },
                {
                    x : 25,
                    y : 330,
                    width : 280,
                    height : 157,
                    enterAnimate : {
                        name : "flyInto",
                        arg : "left"
                    }
                }
            ]
        },
        "MutipleImage03" : {
            frame : "layout-MutipleImage03-frame.png",
            img : [
                {
                    x : 15,
                    y : 15,
                    width : 290,
                    height : 231,
                    enterAnimate : {
                        name : "flyInto",
                        arg : "top"
                    }
                },
                {
                    x : 15,
                    y : 250,
                    width : 143,
                    height : 239,
                    enterAnimate : {
                        name : "flyInto",
                        arg : "left"
                    }
                },
                {
                    x : 162,
                    y : 250,
                    width : 143,
                    height : 239,
                    enterAnimate : {
                        name : "flyInto",
                        arg : "right"
                    }
                }
            ]
        }
    } );
})();

/**
 * Created by Zuobai on 2014/10/1.
 * 图文板式
 */

(function () {
    var rgba = Z.TupleString( "rgba" );

    function SimpleText( img, x, y ) {
        return {
            content : Content.Image( img ),
            x : middleX( x ),
            y : middleY( y ),
            "z-index" : 5
        };
    }

    // region 背景图+纯色矩形+三段文字板式
    function RectLayout( pos ) {
        return {
            create : function ( page, data ) {
                var fontSize = [27, 16, 10],
                    textTop = [22, 57, 88],
                    rectHeight = 115 * yRatio << 0,
                    rectTop, imgTop, imgBottom;

                switch ( pos ) {
                    case "top":
                        rectTop = 0;
                        imgTop = rectHeight;
                        imgBottom = clientHeight;
                        break;
                    case "middle":
                        rectTop = clientHeight * 0.6 << 0;
                        imgTop = 0;
                        imgBottom = clientHeight;
                        break;
                    case "bottom":
                        imgTop = 0;
                        rectTop = imgBottom = clientHeight - rectHeight;
                        break;
                }

                var animationList = [],
                    backgroundColor = color.background || "#FFFFFF";

                Z.loopArray( data.text, function ( text, i ) {
                    // 文字浮现
                    if ( text ) {
                        animationList.push( [
                            enterAnimate.emerge( page.component( {
                                content : Content.LineText( {
                                    text : text,
                                    lineHeight : fontSize[i],
                                    fontSize : fontSize[i],
                                    color : backgroundColor === "#FFFFFF" ? "#000000" : "#FFFFFF",
                                    width : clientWidth
                                } ),
                                x : 0,
                                y : rectTop + textTop[i] * yRatio << 0,
                                "z-index" : 2
                            } ) )
                        ] );
                    }
                } );

                // 背景图
                page.component( {
                    content : Content.ImageCover( data.image[0], clientWidth, imgBottom - imgTop ),
                    x : 0,
                    y : imgTop
                } );

                // 矩形
                page.component( {
                    content : Content.Rect( {
                        color : backgroundColor,
                        width : clientWidth,
                        height : rectHeight
                    } ),
                    x : 0,
                    y : rectTop,
                    "z-index" : 1
                } );

                page.registEnterAnimation( animationList );
            }
        };
    }

    registLayout( "ImageText01", RectLayout( "top" ) );
    registLayout( "ImageText02", RectLayout( "bottom" ) );
    registLayout( "ImageText03", RectLayout( "middle" ) );
    // endregion

    // region 单图
    registLayout( "SingleImage", {
        create : function ( page, data ) {
            Component.BackgroundImageCover( page, data.image[0] );
        }
    } );
    // endregion

    // region 背景图+透明层+文字板式
    function PureTextLayout( style ) {
        var padding = style.padding;

        return {
            create : function ( page, data ) {
                // 背景
                Component.BackgroundImageCover( page, data.image[0] );

                // 透明层
                page.component( {
                    content : Content.Rect( {
                        width : clientWidth,
                        height : clientHeight,
                        color : style.background
                    } ),
                    x : 0,
                    y : 0,
                    "z-index" : 1
                } );

                page.registEnterAnimation( [
                    [enterAnimate.emerge( page.component( {
                        content : Content.MiddleBlockText( {
                            text : data.text[0],
                            margin : style.margin,
                            lineHeight : style.lineHeight,
                            fontSize : style.fontSize,
                            color : style.color,
                            width : clientWidth - 2 * padding,
                            height : clientHeight
                        } ),
                        "z-index" : 2,
                        x : padding,
                        y : 0
                    } ) )]
                ] );
            }
        };
    }

    // 黑色透明层
    registLayout( "ImageText04", PureTextLayout( {
        margin : 5,
        lineHeight : 25,
        fontSize : 15,
        color : "#FFFFFF",
        background : rgba( 0, 0, 0, 0.8 ),
        padding : 20
    } ) );

    // 白色透明层
    registLayout( "ImageText07", PureTextLayout( {
        margin : 5,
        lineHeight : 25,
        fontSize : 14,
        color : "#333333",
        background : rgba( 255, 255, 255, 0.85 ),
        padding : 20
    } ) );
    // endregion

    // 互联网分析沙龙,电商专场
    registLayout( "ImageText05", {
        create : function ( page, data ) {
            var padding = 17,
                blockWidth = 191,
                blockHeight = 79;

            // 背景
            Component.BackgroundImageCover( page, data.image[0] );

            // 透明颜色背景
            var background = page.component( {
                content : Content.Rect( {
                    width : blockWidth,
                    height : blockHeight,
                    color : rgba( 0, 0, 0, 0.85 )
                } ),
                x : clientWidth - blockWidth,
                y : middleY( 214 ),
                "z-index" : 1
            } );

            // 文字
            background.component( {
                content : Content.MiddleBlockText( {
                    text : data.text[0],
                    width : blockWidth - 2 * padding,
                    height : blockHeight,
                    lineHeight : 30,
                    fontSize : 22,
                    color : "#FFFFFF",
                    breakWord : true
                } ),
                x : padding,
                y : 0
            } );
        }
    } );

    // 国际创新峰会,三段文字依次飞入
    registLayout( "ImageText06", {
        create : function ( page, data ) {
            var padding = 17,
                blockWidth = 250,
                blockHeight = 350;

            // 背景
            Component.BackgroundImageCover( page, data.image[0] );

            // 透明颜色背景
            var background = page.component( {
                content : Content.Rect( {
                    width : blockWidth,
                    height : blockHeight,
                    color : rgba( 0, 0, 0, 0.85 )
                } ),
                x : center( clientWidth, blockWidth ),
                y : center( clientHeight, blockHeight ),
                "z-index" : 1
            } );

            function Text( i, y ) {
                return Z.extend( enterAnimate.flyInto( background.component( {
                    content : Content.MiddleBlockText( {
                        text : data.text[i],
                        width : blockWidth - 2 * padding,
                        height : 68,
                        lineHeight : 25,
                        fontSize : 14,
                        color : "#FFFFFF",
                        breakWord : true
                    } ),
                    x : padding,
                    y : y
                } ), "right" ), {
                    delay : 0.3 * i
                } );
            }

            page.registEnterAnimation( [
                [Text( 0, 35 ), Text( 1, 132 ), Text( 2, 229 )]
            ] );
        }
    } );

    // 他们特立独行
    registLayout( "ImageText08", {
        create : function ( page, data ) {
            var textImg = data.image[1];

            // 背景
            Component.BackgroundImageCover( page, data.image[0] );

            page.registEnterAnimation( [
                [enterAnimate.emerge( page.component( {
                    content : Content.Image( textImg ),
                    x : clientWidth - textImg.halfWidth,
                    y : middleY( 354 ),
                    "z-index" : 5,
                    duration : 1
                } ) )]
            ] );
        }
    } );

    // 他们有一个共同的名字
    registLayout( "ImageText09", {
        create : function ( page, data ) {
            var textImg = data.image[1];

            // 背景
            Component.BackgroundImageCover( page, data.image[0] );

            page.registEnterAnimation( [
                [enterAnimate.emerge( page.component( {
                        content : Content.Image( textImg ),
                        x : center( clientWidth, textImg.halfWidth ),
                        y : middleY( 289 ),
                        "z-index" : 5,
                        duration : 1
                    } )
                )]
            ] );
        }
    } );

    // 有一家咖啡馆
    registLayout( "ImageText10", {
        create : function ( page, data ) {
            var textImg = data.image[1];

            // 背景
            Component.BackgroundImageCover( page, data.image[0] );

            page.registEnterAnimation( [
                [enterAnimate.emerge( page.component( {
                        content : Content.Image( textImg ),
                        x : 25,
                        y : middleY( 155 ),
                        "z-index" : 5,
                        duration : 1
                    } )
                )]
            ] );
        }
    } );

    // 越极客,越性感
    registLayout( "ImageText11", {
        create : function ( page, data ) {
            var textImg = data.image[1], textImg1 = data.image[2];

            // 背景
            Component.BackgroundImageCover( page, data.image[0] );

            page.registEnterAnimation( [
                [enterAnimate.emerge( page.component( {
                        content : Content.Image( textImg ),
                        x : center( clientWidth, textImg.halfWidth ),
                        y : middleY( 189 ),
                        "z-index" : 5,
                        duration : 1
                    } )
                )],
                [enterAnimate.emerge( page.component( {
                        content : Content.Image( textImg1 ),
                        x : center( clientWidth, textImg1.halfWidth ),
                        y : middleY( 269 ),
                        "z-index" : 5,
                        duration : 1
                    } )
                )]
            ] );
        }
    } );

    // 马云
    registLayout( "ImageText12", {
        resource : ["layout-ImageText12-mayun.jpg", "layout-ImageText12-mask.png"],
        create : function ( page, data ) {
            var textImg = data.image[1],
                imgHeight = 818 / 1008 * clientHeight,
                maskHeight = 400 / 1008 * clientHeight;

            // 马云头像
            page.component( {
                content : Content.ImageCover( data.resource[0], clientWidth / 2, imgHeight ),
                x : 0,
                y : 0
            } );

            // 上传的头像
            page.component( {
                content : Content.ImageCover( data.image[0], clientWidth / 2, imgHeight ),
                x : clientWidth / 2,
                y : 0
            } );

            // 红色遮罩
            var mask = page.component( {
                content : Content.ImageCover( data.resource[1], clientWidth, maskHeight ),
                x : 0,
                y : clientHeight - maskHeight,
                "z-index" : 5
            } );

            // 文字
            page.registEnterAnimation( [
                [enterAnimate.emerge( mask.component( {
                        content : Content.Image( textImg ),
                        x : ( clientWidth - textImg.halfWidth ) / 2 << 0,
                        y : 75,
                        duration : 1
                    } )
                )]
            ] );
        }
    } );

    // 新年大发
    registLayout( "ImageText13", {
        create : function ( page, data ) {
            var rectHeight = 248 / 2 * yRatio,
                rectTop = clientHeight - rectHeight,
                textImg = data.image[1],
                imgWidth = textImg.halfWidth * yRatio << 0;

            // 背景
            Component.BackgroundImageCover( page, data.image[0] );

            // 矩形
            var rect = page.component( {
                content : Content.Rect( {
                    color : "#FFFFFF",
                    width : clientWidth,
                    height : rectHeight
                } ),
                x : 0,
                y : rectTop
            } );

            // 文字
            page.registEnterAnimation( [
                [enterAnimate.fadeIn( rect.component( {
                    content : Content.Image( textImg, imgWidth, textImg.halfHeight * yRatio << 0 ),
                    x : center( clientWidth, imgWidth ),
                    y : ( 766 - ( 1008 - 248 ) ) / 2 * yRatio << 0,
                    "z-index" : 5,
                    duration : 1
                } ) )]
            ] );
        }
    } );

    // 黄有维,1965年,湖南岳阳人
    registLayout( "ImageText14", {
        create : function ( page, data ) {
            var textImg = data.image[1];

            // 背景
            Component.BackgroundImageCover( page, data.image[0] );

            // 文字
            page.registEnterAnimation( [
                [enterAnimate.emerge( page.component( {
                        content : Content.Image( textImg ),
                        x : clientWidth - 14 - textImg.halfWidth,
                        y : middleY( 78 ),
                        "z-index" : 5,
                        duration : 1
                    } )
                )]
            ] );
        }
    } );

    // 他的作品格调清新,充满阳光和朝气
    registLayout( "ImageText15", {
        create : function ( page, data ) {
            var textImg1 = data.image[1],
                textImg2 = data.image[2],
                textPaddingY = 40,
                textPaddingX = 23,
                margin = 15,
                textHeight = textImg1.halfHeight + textImg2.halfHeight + margin,
                rectWidth = Math.max( textImg1.halfWidth, 246 ) + textPaddingX * 2,
                rectLeft = ( clientWidth - rectWidth ) / 2,
                rectHeight = textHeight + textPaddingY * 2,
                rectTop = ( clientHeight - rectHeight ) / 2;

            // 背景
            Component.BackgroundImageCover( page, data.image[0] );

            // 矩形
            var rect = page.component( {
                content : Content.Rect( {
                    width : rectWidth,
                    height : rectHeight,
                    opacity : 0.9,
                    color : "#FFFFFF"
                } ),
                x : rectLeft,
                y : rectTop
            } );

            // 文字
            function MyText( text, x, y ) {
                return enterAnimate.emerge( rect.component( {
                    content : Content.Image( text ),
                    x : x << 0,
                    y : y << 0,
                    "z-index" : 5,
                    duration : 1
                } ) );
            }

            page.registEnterAnimation( [
                [MyText( textImg1, textPaddingX, textPaddingY )],
                [MyText( textImg2, rectWidth - textPaddingX - textImg2.halfWidth,
                    textPaddingY + textImg1.halfHeight + margin )]
            ] );
        }
    } );

    // 稻城亚丁
    registLayout( "ImageText16", {
        create : function ( page, data ) {
            var textImg1 = data.image[1], textImg2 = data.image[2];

            // 背景
            Component.BackgroundImageCover( page, data.image[0] );

            // 文字
            page.registEnterAnimation( [
                [enterAnimate.fadeIn( page.component( SimpleText( textImg1, 324 / 2, 114 / 2 ) ) )],
                [enterAnimate.fadeIn( page.component( SimpleText( textImg2, 330 / 2, 114 / 2 + textImg1.halfHeight + 5 ) ) )]
            ] );
        }
    } );

    // 沙雅
    registLayout( "ImageText17", {
        create : function ( page, data ) {
            var textImg1 = data.image[1], textImg2 = data.image[2];

            // 背景
            Component.BackgroundImageCover( page, data.image[0] );

            // 文字
            page.registEnterAnimation( [
                [enterAnimate.fadeIn( page.component( SimpleText( textImg1, 68 / 2, 696 / 2 ) ) )],
                [enterAnimate.fadeIn( page.component( SimpleText( textImg2, 76 / 2, 696 / 2 + textImg1.halfHeight + 5 ) ) )]
            ] );
        }
    } );

    var ImageText2122 = {
        create : function ( page, data ) {
            var textImg1 = data.image[1], textImg2 = data.image[2];

            // 背景
            Component.BackgroundImageCover( page, data.image[0] );

            // 文字
            page.registEnterAnimation( [
                [
                    enterAnimate.emerge( page.component( SimpleText( textImg1, 516 / 2, 195 / 2 ) ), "right" ),
                    enterAnimate.emerge( page.component( SimpleText( textImg2, 516 / 2 + textImg1.halfWidth - textImg2.halfWidth,
                        195 / 2 + textImg1.halfHeight + 5 ) ), "left" )
                ]
            ] );
        }
    };

    registLayout( "ImageText21", ImageText2122 );
    registLayout( "ImageText22", ImageText2122 );

    registLayout( "ImageText23", {
        create : function ( page, data ) {
            var textImg1 = data.image[1], textImg2 = data.image[2];

            // 背景
            Component.BackgroundImageCover( page, data.image[0] );

            // 文字
            page.registEnterAnimation( [
                [
                    enterAnimate.emerge( page.component( SimpleText( textImg1, 60 / 2, 140 / 2 ) ), "top" ),
                    enterAnimate.emerge( page.component( SimpleText( textImg2, 64 / 2, 140 / 2 + textImg1.halfHeight + 5 ) ), "bottom" )
                ]
            ] );
        }
    } );

    registLayout( "ImageText24", {
        create : function ( page, data ) {
            var textImg1 = data.image[1], textImg2 = data.image[2];

            // 背景
            Component.BackgroundImageCover( page, data.image[0] );

            // 文字
            page.registEnterAnimation( [
                [
                    enterAnimate.emerge( page.component( SimpleText( textImg1, 82 / 2, 720 / 2 ) ), "top" ),
                    enterAnimate.emerge( page.component( SimpleText( textImg2, 86 / 2, 720 / 2 + textImg1.halfHeight + 5 ) ), "bottom" )
                ]
            ] );
        }
    } );
})();

/**
 * Created by 白 on 2014/10/17.
 */

(function () {
    var element = Z.element,
        infoWindowTemplate = '<div class="map-info-window"><div class="name"></div><div class="info"><div>地址:<span class="address"></span></div></div></div>';

    registLayout( "map", {
        resource : ["layout-map-location.png"],
        create : function ( page, data ) {
            var icon = data.resource[0];

            page.component( {
                content : Content.ImageCover( data.image[0], clientWidth, clientHeight ),
                x : 0,
                y : 0
            } );

            // 地图图标
            var iconArea = page.component( {
                content : Content.Image( icon ),
                x : center( clientWidth, icon.halfWidth ),
                y : middleY( 574 / 2 )
            } );

            page.component( {
                content : Content.LineText( {
                    text : data.location[0].address,
                    lineHeight : 12,
                    fontSize : 12,
                    color : "#FFFFFF",
                    width : clientWidth
                } ),
                x : 0,
                y : middleY( 682 / 2 )
            } );

            page.onShow( function () {
                // 地图图标闪烁
                iconArea.infiniteAnimate( {
                    duration : 3,
                    progress : {
                        0 : {
                            opacity : 1
                        },
                        50 : {
                            opacity : 0.4
                        }
                    }
                } );
            } );

            // 地图切页
            var slidePage = fp.slidePage();
            slidePage.classList.add( "map-slide-page" );

            var back = slidePage.appendChild( element( "div.title-bar", {
                children : [element( "div.icon.back" ), element( "div.line" ), element( "div.caption" )]
            } ) );
            Z.onTap( back, fp.history.back );

            var isFirst = true;

            // 点击地图图标,弹出地图页
            page.component( {
                content : Content.Rect( {
                    width : 120,
                    height : 100
                } ),
                x : center( clientWidth, 120 ),
                y : middleY( 574 / 2 - 20 ),
                onClick : function () {
                    slidePage.slideIn();
                    if ( isFirst ) {
                        var loading = fp.Loading( slidePage );
                        Z.markerMap( {
                            data : data.location,
                            parent : slidePage,
                            make : function ( item ) {
                                var infoWindow = element( infoWindowTemplate );
                                infoWindow.querySelector( ".name" ).innerHTML = item.name;
                                infoWindow.querySelector( ".address" ).innerHTML = item.address;
                                return infoWindow;
                            },
                            onLoad : loading.remove
                        } );
                        isFirst = false;
                    }
                }
            } );
        }
    } );
})();

/**
 * Created by 白 on 2014/9/24.
 */

(function () {
    var loopArray = Z.loopArray;

    registLayout( "MutipleImage01", {
        create : function ( page, data ) {
            var width = 244,
                height = 410 * yRatio << 0,
                x = center( clientWidth, width ),
                y = center( clientHeight, height ),
                len = data.image.length,
                imgComponents = [],
                enterAnimation = [];

            loopArray( data.image, function ( img, i ) {
                var imgComponent = page.component( {
                    content : Content.ImageCover( img, width, height, {
                        border : {
                            width : 3,
                            color : "#FFFFFF"
                        }
                    } ),
                    x : x,
                    y : y,
                    rotate : ( i + 1 - len ) * -4 * Math.PI / 180,
                    "z-index" : 10000 + i
                } );

                imgComponents.push( imgComponent );
                enterAnimation.push( {
                    component : imgComponent,
                    duration : 0.8,
                    delay : i * 0.4,
                    progress : {
                        "0" : {
                            rotate : -Math.PI / 6,
                            scale : 3,
                            x : x - width * 2.4,
                            y : y + height * 2.4
                        }
                    }
                } );
            } );

            enterAnimation.top.onEnter = function () {
                document.body.classList.add( "MutipleImage01-show" );

                var curTopIndex = len - 1,
                    resetAnimation = null;

                function clear() {
                    if ( resetAnimation ) {
                        loopArray( resetAnimation, function ( animation ) {
                            animation.remove();
                        } );
                        resetAnimation = null;
                    }
                }

                function reset( index ) {
                    clear();
                    resetAnimation = [];
                    Z.loop( len, function ( pos ) {
                        var bottomImage = imgComponents[( ( index + pos ) % len + len ) % len];
                        resetAnimation[pos] = bottomImage.transition( {
                            end : {
                                rotate : ( pos + 1 - len ) * -4 * Math.PI / 180
                            },
                            timing : Z.Timing.easeOut,
                            delay : 0.4 * pos,
                            duration : 0.8
                        } );
                    } );
                }

                Z.onDragH( page, function ( event ) {
                    if ( !window.highPerformance ) {
                        fp.lock( true );
                    }

                    clear();
                    var flyIndex = curTopIndex,
                        flyImage = imgComponents[( flyIndex % len + len ) % len];
                    flyImage.transition( {
                        end : {
                            x : event.directionX ? clientWidth : -width,
                            y : y,
                            opacity : 0
                        },
                        duration : 0.4,
                        onEnd : function () {
                            flyImage.x = x;
                            flyImage.opacity = 1;
                            flyImage["z-index"] -= len;
                            flyImage.rotate = ( 1 - len ) * -4 * Math.PI / 180;

                            reset( flyIndex );
                            fp.lock( false );
                        }
                    } );

                    --curTopIndex;
                } );
            };

            var prev, next;
            page.onShow( function () {
                prev = Z.element( "div.prev", window.body );
                next = Z.element( "div.next", window.body );
                document.body.classList.add( "MutipleImage01" );
            } );

            page.onRemove( function () {
                Z.removeNode( prev );
                Z.removeNode( next );
                document.body.classList.remove( "MutipleImage01" );
                document.body.classList.remove( "MutipleImage01-show" );
            } );

            page.registEnterAnimation( [enterAnimation] );
        }
    } );
})();

/**
 * Created by 白 on 2014/11/3.
 * 刮刮卡板式
 */

(function () {
    registLayout( "scratch-card", {
        crossOrigin : "*",
        create : function ( page, data ) {
            page.component( {
                content : Content.ImageCover( data.image[0], clientWidth, clientHeight ),
                x : 0,
                y : 0
            } );

            if ( !data.complete ) {
                var up = page.component( {
                    content : Content.ImageCover( data.image[1], clientWidth, clientHeight ),
                    x : 0,
                    y : 0
                } );

                var scratchLayer = Z.Layer();
                page.onShow( function () {
                    // 刮层
                    ua.android && ( scratchLayer.dpr = 1 );
                    scratchLayer.resize( clientWidth, clientHeight );
                    scratchLayer.classList.add( "scratch-card" );
                    scratchLayer.draw( function ( gc ) {
                        Z.drawImageMeasure( gc, Z.measureCover( data.image[1], clientWidth, clientHeight ) );
                    } );
                    document.body.appendChild( scratchLayer );
                    document.body.classList.add( "hide-tips" );

                    var line = [];
                    var pointerHandle = Z.onPointerDown( scratchLayer, function ( event, pageX, pageY ) {
                        var points = [], isIn = true;
                        line.push( points );

                        event.preventDefault();
                        event.stopPropagation();

                        points.push( {
                            x : pageX,
                            y : pageY
                        } );

                        event.onMove( function ( event, pageX, pageY ) {
                            points.push( {
                                x : pageX,
                                y : pageY
                            } );
                        } );

                        event.onUp( function () {
                            isIn = false;
                        } );

                        // 动画循环
                        var animateHandle = Z.requestAnimate( function () {
                            scratchLayer.draw( function ( gc ) {
                                Z.drawImageMeasure( gc, Z.measureCover( data.image[1], clientWidth, clientHeight ) );
                                gc.lineCap = "round";
                                gc.lineJoin = "round";

                                gc.globalCompositeOperation = "destination-out";
                                gc.beginPath();

                                Z.loopArray( line, function ( points ) {
                                    Z.loopArray( points, function ( point, i ) {
                                        i === 0 ? gc.moveTo( point.x, point.y ) : gc.lineTo( point.x, point.y );
                                    } );

                                    gc.lineWidth = 50;
                                    if ( ua.android ) {
                                        scratchLayer.style.display = 'none';
                                        //noinspection BadExpressionStatementJS
                                        scratchLayer.offsetHeight;
                                        scratchLayer.style.display = 'inherit';
                                    }
                                    gc.stroke();
                                } );

                                if ( !isIn ) {
                                    var error = false;
                                    animateHandle.remove();

                                    try {
                                        // 抬起时判断是否划过了30%,划过后移除刮刮卡效果
                                        var imgData = gc.getImageData( 0, 0, scratchLayer.width, scratchLayer.height ),
                                            pixels = imgData.data, count = 0;

                                        for ( var i = 0, j = pixels.length; i < j; i += 4 ) {
                                            if ( pixels[i + 3] < 128 ) {
                                                ++count;
                                            }
                                        }
                                    }
                                    catch ( e ) {
                                        error = true;
                                    }

                                    if ( error || count / ( pixels.length / 4 ) > 0.3 ) {
                                        pointerHandle.remove();

                                        // 淡出动画
                                        Z.transition( scratchLayer, "0.8s", {
                                            opacity : 0
                                        }, function () {
                                            // 移除层
                                            document.body.classList.remove( "hide-tips" );
                                            data.complete = true;
                                            Z.removeNode( scratchLayer );
                                        } );
                                    }
                                }
                            } );
                        } );
                    } );

                    up.remove();
                } );

                page.onRemove( function () {
                    Z.removeNode( scratchLayer );
                } );
            }
        }
    } );
})();

/**
 * Created by 白 on 2014/9/15.
 * 按钮相关板式
 */

(function () {
    function middleY( y ) {
        return y - 504 / 2 + clientHeight / 2 << 0;
    }

    registLayout( "Sign-Up02", {
        create : function ( page, data ) {
            var yList = {
                    top : 148,
                    middle : 417,
                    bottom : 687
                },
                buttonSize = 125;

            page.component( {
                content : Content.ImageCover( data.image[0], clientWidth, clientHeight ),
                x : 0,
                y : 0
            } );

            page.component( {
                content : Content.Rect( {
                    width : buttonSize,
                    height : buttonSize
                } ),
                x : ( clientWidth - buttonSize ) / 2 << 0,
                y : middleY( yList[data.position[0]] / 2 ),
                onClick : function () {
                    fp.jump( data.actionlinks[0] );
                }
            } );
        }
    } );

    // 报名表单页
    var signUpFormPage = registFunctionPage( "sign-up", function ( page, formInfo ) {
        var formTemplate = JSON.parse( formInfo.template ), // 表单模板
            pageContent = Z.element( "div.page-content.scroll", page ), // 报名页的内容部分
            form = Z.element( "form", {
                action : "/"
            }, pageContent ),
            back = Z.element( "div.icon.back", page ), // 返回按钮
            curFocus = null,
            lastInput = null,
            inputList = [], // 输入列表
            hideField = {}; // 隐藏字段

        page.classList.add( "sign-up-form-slide-page" );

        Z.onTap( back, fp.history.back );

        // 提交表单
        function submit() {
            curFocus && curFocus.blur();
            var formData = [], unfilled = [];

            function pushField( component, value ) {
                formData.push( {
                    name : component.name,
                    label : component.label,
                    value : value
                } );
            }

            // 收集输入字段
            var errors = [];
            Z.loopArray( inputList, function ( item ) {
                var value = item.input.value;
                // 如果是必填字段,检查是否为空,若为空,添加到未填数组中
                if ( item.data.required ) {
                    if ( value === "" ) {
                        unfilled.push( item.data.label );
                        item.input.classList.add( "error" );
                    }
                    else {
                        var validateInfo = item.validate ? item.validate( value ) : null;
                        if ( validateInfo ) {
                            errors.push( validateInfo );
                            item.input.classList.add( "error" );
                        }
                        else {
                            pushField( item.data, value );
                            item.input.classList.remove( "error" );
                        }
                    }
                }
                else {
                    pushField( item.data, item.input.value );
                }
            } );

            // 如果未填数组不为空,提示
            if ( unfilled.length !== 0 || errors.length !== 0 ) {
                fp.alert( ( unfilled.length ? [unfilled.join( "，" ) + "不能为空。"] : [] ).concat( errors ).join( "<br>" ) );
            }
            else {
                var loader = Z.Loader(),
                    loading = fp.Loading( page ),
                    userInfo = {};

                fp.lock( true, pageContent );

                // 如果用户登录了,收集用户信息
                if ( fp.isLogIn() ) {
                    loader.load( function ( loadDone ) {
                        fp.getUserInfo( function ( data ) {
                            userInfo = data;
                            loadDone();
                        } );
                    } );
                }

                // 收集完信息后,整理数据,提交表单
                loader.onLoad( function () {
                    var hideData = {
                        "报名时间" : new Date().getTime(),
                        "微信昵称" : userInfo.NickName,
                        "微信头像" : userInfo.HeadPhoto,
                        "微信性别" : userInfo.Sex,
                        "微信City" : userInfo.City,
                        "微信Province" : userInfo.Province,
                        "微信Country" : userInfo.Country
                    };

                    Z.loopObj( hideField, function ( name, item ) {
                        pushField( item, hideData[name] === undefined ? "" : hideData[name] );
                    } );

                    // 发送提交表单请求
                    fp.sendForm( function () {
                        loading.remove();

                        // 弹出提示,1秒后移除页面
                        fp.alert( formTemplate.data.submitComplete.value, 1000 );
                        setTimeout( function () {
                            if ( page.isIn() ) {
                                fp.history.back();
                            }
                        }, 1000 );
                    }, {
                        id : formInfo.formId,
                        data : formData
                    } );
                } );

                loader.start();
            }
        }

        Z.bindEvent( form, "submit", function ( event ) {
            event.preventDefault();
        } );

        Z.loopArray( formTemplate.data.component, function ( component ) {
            if ( component.enable ) {
                if ( component.visiable ) {
                    // 显示字段
                    switch ( component.name ) {
                        case "textbox":
                            // 文本框
                            (function () {
                                var wrapper = {},
                                    label = Z.element( "label", form ),
                                    caption = wrapper.caption = Z.element( "div.caption", component.label + "：", label ), // 字段名
                                    input = wrapper.input = Z.element( "input", {
                                        placeholder : component.placeholder,
                                        name : component.id
                                    }, label );

                                switch ( component.label ) {
                                    case "电话":
                                        input.type = "tel";
                                        break;
                                    case "邮箱":
                                        input.type = "email";
                                        wrapper.validate = function ( value ) {
                                            return /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test( value ) ?
                                                null : "请输入正确的邮箱地址";
                                        };
                                        break;
                                }

                                // 获得焦点时,更新curFocus
                                Z.bindEvent( input, "focus", function () {
                                    curFocus = input;
                                } );

                                // 如果是必填的,添加一个必填字段坐标
                                if ( component.required ) {
                                    Z.element( "div.required.icon", caption );
                                }

                                // 如果有上一个input,按回车时更新到此焦点
                                if ( lastInput ) {
                                    Z.bindEvent( lastInput, "keypress", function ( event ) {
                                        if ( event.keyCode === 13 ) {
                                            input.focus();
                                        }
                                    } );
                                }

                                lastInput = input;
                                wrapper.data = component;
                                inputList.push( wrapper );
                            })();
                            break;
                        case "btn":
                            // 按钮,目前一律视为提交按钮
                            (function () {
                                var label = Z.element( "label", form ),
                                    button = Z.element( "div.button", {
                                        innerHTML : component.value
                                    }, label );

                                Z.onTap( button, submit );
                            })();
                            break;
                    }
                }
                else {
                    hideField[component.label] = component;
                }
            }
        } );

        if ( lastInput ) {
            Z.bindEvent( lastInput, "keypress", function ( event ) {
                if ( event.keyCode === 13 ) {
                    submit();
                }
            } );
        }
    } );

    registLayout( "Sign-Up03", {
        create : function ( page, data ) {
            page.component( {
                content : Content.ImageCover( data.image[0], clientWidth, clientHeight ),
                x : 0,
                y : 0
            } );

            var button = data.image[1];
            page.component( {
                content : Content.Image( button ),
                x : ( clientWidth - button.halfWidth ) / 2 << 0,
                y : middleY( 208 ),
                onClick : function () {
                    signUpFormPage( {
                        data : data.signup,
                        noLog : !JSON.parse( data.signup.template ).allowAnymous
                    } );
                }
            } );
        }
    } );
})();

/**
 * Created by 白 on 2014/10/17.
 */

(function () {
    function middleY( y ) {
        return y - 508 / 2 + clientHeight / 2 << 0;
    }

    registLayout( "video", {
        resource : ["layout-video-icon.png"],
        create : function ( page, data ) {
            var icon = data.resource[0],
                iconSize = icon.halfWidth,
                iconX = ( clientWidth - iconSize ) / 2 << 0,
                iconY = middleY( 436 / 2 ),
                slidePage = null;

            page.component( {
                content : Content.ImageCover( data.image[0], clientWidth, clientHeight ),
                x : 0,
                y : 0
            } );

            // 构建视频页,尝试识别iframe
            var videoSrc = data.video[0],
                div = document.createElement( "div" ),
                iframe;
            div.innerHTML = videoSrc;

            // 如果识别出了iframe,在滑页中
            if ( iframe = div.querySelector( "iframe" ) ) {
                slidePage = fp.slidePage();
                iframe.width = clientWidth;
                iframe.height = clientWidth / 16 * 9 << 0;
                slidePage.classList.add( "video-slide-page" );

                Z.css( iframe, {
                    position : "absolute",
                    left : 0,
                    top : Z.css.px( ( clientHeight - iframe.height ) / 2 << 0 )
                } );

                slidePage.appendChild( iframe );
                Z.onTap( Z.element( "div.close", slidePage ), fp.history.back );
            }

            page.component( {
                content : Content.Image( icon ),
                "z-index" : 2,
                x : iconX,
                y : iconY,
                onClick : function () {
                    if ( slidePage ) {
                        slidePage.slideIn();
                    }
                    else if ( /(^http:\/\/)|(^https:\/\/)/.test( videoSrc ) ) {
                        fp.jump( videoSrc );
                    }
                    else {
                        alert( "未识别的视频地址" );
                    }
                }
            } );

            var circle = page.component( {
                content : Content.Circle( {
                    color : "#FFFFFF",
                    r : iconSize / 2 << 0
                } ),
                "z-index" : 1,
                x : iconX,
                y : iconY
            } );

            page.onShow( function () {
                circle.infiniteAnimate( {
                    duration : 2.5,
                    progress : {
                        0 : {
                            scale : 1,
                            opacity : 0.8
                        },
                        100 : {
                            scale : 2,
                            opacity : 0
                        }
                    }
                } );
            } );
        }
    } );
})();

/**
 * Created by Json on 2014/11/18.
 */
registLayout( "ImageText18", {
    create : function ( page, data ) {
        // 背景
        Component.BackgroundImageCover( page, data.image[0] );

        page.registEnterAnimation( [
            [enterAnimate.emerge( page.component(
                {
                    content : Content.Image( data.image[1] ),
                    x : (clientWidth - data.image[1].halfWidth) / 2,
                    y : clientHeight * 0.229167
                }
            ) )],
            [enterAnimate.emerge( page.component(
                {
                    content : Content.Image( data.image[2] ),
                    x : (clientWidth - data.image[2].halfWidth ) / 2,
                    y : clientHeight * 0.229167 + data.image[1].halfHeight + 29
                }
            ) )],
            [enterAnimate.emerge( page.component(
                {
                    content : Content.Image( data.image[3] ),
                    x : (clientWidth - data.image[3].halfWidth) / 2,
                    y : clientHeight * 0.229167 + data.image[1].halfHeight + data.image[2].halfHeight + 51
                }
            ) )]
        ] );
    }
} );

registLayout( "ImageText19", {
    create : function ( page, data ) {
        // 背景
        Component.BackgroundImageCover( page, data.image[0] );

        page.registEnterAnimation( [
            [enterAnimate.emerge( page.component(
                {
                    content : Content.Image( data.image[1] ),
                    x : (clientWidth - data.image[1].halfWidth) / 2,
                    y : clientHeight * 0.84126 - data.image[3].halfHeight - data.image[2].halfHeight - data.image[1].halfHeight - 51
                }
            ) )],
            [enterAnimate.emerge( page.component(
                {
                    content : Content.Image( data.image[2] ),
                    x : (clientWidth - data.image[2].halfWidth ) / 2,
                    y : clientHeight * 0.84126 - data.image[3].halfHeight - 12 - data.image[2].halfHeight - 10
                }
            ) )],
            [enterAnimate.emerge( page.component(
                {
                    content : Content.Image( data.image[3] ),
                    x : (clientWidth - data.image[3].halfWidth) / 2,
                    y : clientHeight * 0.84126 - data.image[3].halfHeight
                }
            ) )]
        ] );
    }
} );


registLayout( "ImageText20", {
    create : function ( page, data ) {
        // 背景
        Component.BackgroundImageCover( page, data.image[0] );

        var title = page.component(
            {
                content : Content.BlockText( {
                    width : clientWidth - 150,
                    fontSize : 27,
                    lineHeight : 35,
                    text : data.text[0],
                    fontWeight : "bold",
                    color : "white"
                } ),
                x : 75,
                y : 95
            }
        );


        page.registEnterAnimation( [
            [enterAnimate.emerge( title )],
            [enterAnimate.emerge( page.component(
                {
                    content : Content.BlockText( {
                        width : clientWidth - 150,
                        fontSize : 10,
                        lineHeight : 20,
                        text : data.text[1],
                        color : "#d2d2d2"
                    } ),
                    x : 75,
                    y : 95 + title.componentHeight + 26
                }
            ) )]
        ] );
    }
} );

/**
 * Created by 白 on 2014/8/11.
 */

registSwitchAnimate( "chessboard", function ( prev, cur, canvas ) {
    var css = Z.css,
        px = css.px,

        clear = prepareSnapshot( null, null, canvas, {
            perspective : 1000,
            background : "#FFFFFF"
        } ),

        numX = 4, numY = 5,

        fragments = [], row,// 碎片矩阵(二维数组),列(一维数组),碎片(数组,[0]是前元素,[1]是当前元素)
        left = 0, top = 0, right, bottom, width, height;// 一个碎片的左,上,右,下坐标和宽高

    if ( clientWidth > clientHeight ) {
        numX = 5;
        numY = 4;
    }

    // 制作碎片
    Z.loop( numX, function ( i ) {
        right = ( i + 1 ) / numX * clientWidth << 0;
        top = 0;
        row = [];
        fragments.push( row );

        Z.loop( numY, function ( j ) {
            bottom = ( j + 1 ) / numY * clientHeight << 0;
            width = right - left;
            height = bottom - top;

            var fragment = [];
            fragment.start = Math.random(); // 启动时间
            fragment.isTurn = false; // 是否翻转过

            Z.loop( 2, function ( n ) {
                var img = n === 0 ? prev : cur,
                    layer = Z.Layer();

                layer.resize( width, height );
                layer.draw( function ( gc ) {
                    var dpr = gc.dpr;
                    gc.drawImage( img, left * dpr, top * dpr, width * dpr, height * dpr, 0, 0, width, height )
                } );

                css( layer, {
                    position : "absolute",
                    left : px( left ),
                    top : px( top ),
                    "backface-visibility" : "hidden",
                    "z-index" : 2 - n
                } );

                window.body.appendChild( layer );
                fragment.push( layer );
            } );

            row.push( fragment );
            top = bottom;
        } );

        left = right;
    } );

    function rotate( fragment, ratio ) {
        var prev = fragment[0], cur = fragment[1];
        if ( !fragment.isTurn && ratio < 0.5 ) {
            css( prev, "z-index", 1 );
            css( cur, "z-index", 0 );
            fragment.isTurn = true;
        }

        if ( fragment.isTurn && ratio >= 0.5 ) {
            css( prev, "z-index", 0 );
            css( cur, "z-index", 1 );
            fragment.isTurn = false;
        }

        css.transform( prev, css.rotateY( ratio * Math.PI ) );
        css.transform( cur, css.rotateY( ratio * Math.PI + Math.PI ) );
    }

    var turnDuration = 0.4, // 翻转时长
        speed = 1 / turnDuration, // 速度倍率
        randomRange = ( 1 - turnDuration ) / 2, // 随机范围
        lastStart = 1 - turnDuration - randomRange;  // 最后启动时间

    return {
        duration : 1.6,
        timing : Z.Timing.linear,
        onAnimate : function ( t ) {
            css( prev, "visibility", "hidden" );
            css( cur, "visibility", "hidden" );

            var fragment;
            for ( var i = 0; i !== numX; ++i ) {
                for ( var j = 0; j !== numY; ++j ) {
                    fragment = fragments[i][j];
                    rotate( fragment, Z.range( ( t - i / ( numX - 1 ) * lastStart - fragment.start * randomRange ) * speed, 0, 1 ) );
                }
            }
        },
        onEnd : function () {
            clear();

            // 移除碎片
            for ( var i = 0; i !== numX; ++i ) {
                for ( var j = 0; j !== numY; ++j ) {
                    for ( var n = 0; n !== 2; ++n ) {
                        Z.removeNode( fragments[i][j][n] );
                    }
                }
            }
        }
    };
} );

/**
 * Created by 白 on 2014/8/11.
 */

registSwitchAnimate( "cube", function ( prev, cur, canvas ) {
    var css = Z.css,
        isTurn = false,
        r = clientWidth / 2,
        clear = prepareSnapshot( prev, cur, canvas, {
            perspective : 1000,
            background : "#FFFFFF"
        } );

    return {
        duration : 1,
        timing : Z.Timing.linear,
        onAnimate : function ( ratio ) {
            if ( !isTurn && ratio < 0.5 ) {
                css( prev, "z-index", 6 );
                css( cur, "z-index", 5 );
                isTurn = true;
            }

            if ( isTurn && ratio >= 0.5 ) {
                isTurn = false;
                css( prev, "z-index", 5 );
                css( cur, "z-index", 6 );
            }

            var prevRad = ratio * Math.PI / 2,
                curRad = ratio * Math.PI / 2 - Math.PI / 2,
                prevZ = Math.cos( prevRad ) * r - r,
                pz = prevZ + Math.sin( prevRad ) * r;

            css.transform( prev, css.translate( -r * Math.sin( prevRad ), 0, prevZ - pz ), css.rotateY( -prevRad ) );
            css.transform( cur, css.translate( -r * Math.sin( curRad ), 0, Math.cos( curRad ) * r - r - pz ), css.rotateY( -curRad ) );
        },
        onEnd : clear
    };
} );

/**
 * Created by Zuobai on 2014/8/9.
 */

registSwitchAnimate( "fade", function ( prev, cur ) {
    return {
        duration : 0.8,
        timing : Z.Timing.linear,
        onDraw : function ( gc, ratio ) {
            ratio = Z.range( ratio, 0, 1 );
            gc.save();
            gc.globalAlpha = 1 - ratio;
            gc.drawImage( prev, 0, 0, clientWidth, clientHeight );
            gc.globalAlpha = ratio;
            gc.drawImage( cur, 0, 0, clientWidth, clientHeight );
            gc.restore();
        }
    };
} );

registSwitchAnimate( "fade-dom", function ( prev, cur, canvas ) {
    var css = Z.css,
        clear = prepareSnapshot( prev, cur, canvas, {} );

    return {
        duration : 0.8,
        timing : Z.Timing.linear,
        onAnimate : function ( ratio ) {
            css( prev, "opacity", 1 - ratio );
            css( cur, "opacity", ratio );
        },
        onEnd : clear
    };

} );

/**
 * Created by 白 on 2014/8/11.
 */

registSwitchAnimate( "overturn", function ( prev, cur, canvas ) {
    var css = Z.css,
        isTurn = false,
        r = clientWidth / 2,
        clear = prepareSnapshot( prev, cur, canvas, {
            perspective : 1000,
            background : "#FFFFFF"
        } );

    return {
        duration : 1,
        timing : Z.Timing.linear,
        onAnimate : function ( ratio ) {
            if ( !isTurn && ratio < 0.5 ) {
                css( prev, "z-index", 6 );
                css( cur, "z-index", 5 );
                isTurn = true;
            }

            if ( isTurn && ratio >= 0.5 ) {
                isTurn = false;
                css( prev, "z-index", 5 );
                css( cur, "z-index", 6 );
            }

            var z = Math.sin( ( 0.5 - Math.abs( ratio - 0.5 ) ) * Math.PI ) * r * 0.6;

            css.transform( prev, css.translate( 0, 0, -z ), css.rotateY( ratio * Math.PI ) );
            css.transform( cur, css.translate( 0, 0, -z ), css.rotateY( ratio * Math.PI + Math.PI ) );
        },
        onEnd : clear
    };
} );

/**
 * Created by Zuobai on 2014/8/9.
 */

registSwitchAnimate( "push", function ( prev, cur ) {
    return {
        duration : 0.8,
        onDraw : function ( gc, ratio ) {
            gc.save();
            gc.drawImage( prev, 0, -ratio * clientHeight, clientWidth, clientHeight );
            gc.drawImage( cur, 0, ( 1 - ratio ) * clientHeight, clientWidth, clientHeight );
            gc.restore();
        }
    };
} );

/**
 * Created by Zuobai on 2014/8/9.
 */

registSwitchAnimate( "switch", function ( prev, cur, canvas ) {
    var css = Z.css,
        isTurn = false,
        half = clientWidth / 2,
        clear = prepareSnapshot( prev, cur, canvas, {
            perspective : 1200,
            background : "#FFFFFF"
        } );

    return {
        duration : 1,
        timing : Z.Timing.linear,
        onAnimate : function ( ratio ) {
            ratio = ratio * 2;
            if ( ratio <= 1 ) {
                if ( !isTurn ) {
                    css( cur, "z-index", 5 );
                    css( prev, "z-index", 6 );
                    isTurn = true;
                }

                css.transform( prev, css.translate( ratio * half << 0, 0, -ratio * 150 ),
                    css.rotateY( -ratio * 30, "deg" ) );

                css.transform( cur, css.translate( -ratio * half << 0, 0, -150 + ( 1 - ratio ) * -150 ),
                    css.rotateY( ratio * 30, "deg" ) );
            }
            else {
                ratio = ratio - 1;
                var rRatio = 1 - ratio;
                if ( isTurn ) {
                    css( cur, "z-index", 6 );
                    css( prev, "z-index", 5 );
                    isTurn = false;
                }

                css.transform( prev, css.translate( rRatio * half << 0, 0, -150 + ratio * -150 ),
                    css.rotateY( -rRatio * 30, "deg" ) );

                css.transform( cur, css.translate( -rRatio * half << 0, 0, ( 1 - ratio ) * -150 ),
                    css.rotateY( rRatio * 30, "deg" ) );
            }
        },
        onEnd : clear
    };
} );

/**
 * Created by Zuobai on 2014/8/9.
 */

registSwitchAnimate( "tease", function ( prev, cur ) {
    return {
        duration : 0.8,
        timing : Z.Timing.linear,
        onDraw : function ( gc, t ) {
            var dpr = gc.dpr,
                width = clientWidth * dpr;
            gc.drawImage( cur, 0, 0, clientWidth, clientHeight );

            Z.loop( 8, function ( i ) {
                var top = i / 8 * clientHeight << 0,
                    nextTop = ( i + 1 ) / 8 * clientHeight << 0,
                    height = nextTop - top;

                var ratio = Math.max( t * 2 + ( i + 1 ) / 8 - 1, 0 );

                gc.drawImage( prev, 0, top * dpr, width, height * dpr,
                        ( i % 2 === 0 ? 1 : -1 ) * ratio * ratio * clientWidth, top, clientWidth, height );
            } );
        }
    };
} );