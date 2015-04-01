(function() {
	var a = 0;
	window.zachModule = function(b) {
		zachModule[a++] = {};
		b();
	};
	window.main = function(b) {
		b();
	};
})();
zachModule(function() {
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

	// 遍历对象
    function loopObj( obj, block ) {
        for ( var key in obj ) {
            block( key, obj[key] );
        }
    }
   // 遍历字符串
	function loopString(str, func, J) {
		
		for ( var i=0;i<str.length;i++) {
			func( J ?str.charAt(i) : str.charCodeAt(i), i);
		}
	}

	function defaultValue(val, defaultVal) {
		return val === undefined ? val : defaultVal;
	}

// region 对象
    function merge( outObj, inObjList ) {
        loopArray( inObjList, function ( obj ) {
            loopObj( obj, function ( key, value ) {
                outObj!== undefined && (outObj[key] = value);
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


	function extract(H, I) {
		var G = {};
		loopObj(I, function(J, K) {
			G[J] = defaultValue(H[J], K);
		});
		return G;
	}

	function exclude(J, G) {
		var H = A(G), I = {};
		loopObj(J, function(K, L) {
			!H.contains(K) && (I[K] = L);
		});
		return I;
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
    /*
	function defineGetter(I, H, G) {
		request(function(J) {
			is.String(H) ? J(H, G) : g(H, J);
		}, function(J, K) {
			Object.defineProperty(I, J, {
				get : K
			});
		});
	}
	*/

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
    /*
	function defineAutoProperty(I, H, G) {
		require(function(J) {
			is.String(H) ? J(H, G) : g(H, J);
		}, function(K, J) {
			J = J || {};
			var M = J.value, L = J.set;
			M !== undefined && L(M);
			Object.defineProperty(I, K, {
				get : function() {
					return M;
				},
				set : function(N) {
					M = L ? y(L(N), N) : N;
				}
			});
		});
	}
   */
  
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


	function removeUrlArg(G, H) {
		var I = encodeURIObject(exclude(G.arg, H));
		return G.origin + G.pathname + ( I ? "?" : "") + I + G.hash;
	}

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
	
	//del
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
    //del
	   // 提供数组的top
	Object.defineProperty(Array.prototype, "top", {
		get : function() {
			return this[this.length - 1];
		},
		set : function(G) {
			this[this.length - 1] = G;
		}
	});
	
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
	


	function recursion(G) {
		G.apply(null, Array.prototype.slice.call(arguments, 1));
	}

/*
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
*/
    // 装载器
	function Loader(H) {
		var G = [];
		return {
			load : function(I) {
				G.push(I);
			},
			start : function(I) {
				if (G.length === 0) {
					I();
				} else {
					if (H) {
						recursion(function K(M) {
							var L = G[M];
							L ? L(function() {
								K(M + 1);
							}) : I();
						}, 0);
					} else {
						var J = G.length;
						loopArray(G, function(L) {
							L(function() {
								--J === 0 && I();
							});
						});
					}
				}
			}
		};
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
 
	

	function procedure(I) {
		var G = I.length;
		recursion(function H(K, J) {
			var L = I[K];
			if (L) {
				L.apply(null, K === G - 1 ? J : [
				function() {
					H(K + 1, Array.prototype.slice.call(arguments, 0));
				}].concat(J));
			}
		}, 0, []);
	}

	function request(G, H) {
		return G(H);
	}

/*
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
    */
   // 异步遍历数组   
	function loopArrayAsync(K, H, J, I) {
		var G = Loader(I);
		loopArray(K, function(M, L) {
			G.load(function(N) {
				H(N, M, L);
			});
		});
		G.start(J);
	}
	zachModule["0"].is = q;
	zachModule["0"].callFunction = j;
	zachModule["0"].loop = D;
	zachModule["0"].loopArray = w;
	zachModule["0"].loopObj = g;
	zachModule["0"].loopString = m;
	zachModule["0"].defaultValue = y;
	zachModule["0"].insert = c;
	zachModule["0"].extend = C;
	zachModule["0"].extract = x;
	zachModule["0"].exclude = b;
	zachModule["0"].keys = v;
	zachModule["0"].defineGetter = e;
	zachModule["0"].defineAutoProperty = t;
	zachModule["0"].encodeURIObject = B;
	zachModule["0"].tupleString = z;
	zachModule["0"].TupleString = u;
	zachModule["0"].parsePairString = a;
	zachModule["0"].concatUrlArg = l;
	zachModule["0"].removeUrlArg = k;
	zachModule["0"].Set = A;
	zachModule["0"].LinkedList = o;
	zachModule["0"].Event = F;
	zachModule["0"].Loader = s;
	zachModule["0"].Resource = f;
	zachModule["0"].procedure = r;
	zachModule["0"].recursion = n;
	zachModule["0"].request = h;
	zachModule["0"].loopArrayAsync = E;
});
zachModule(function() {
	//p = h.insert, l = h.loopArray, e = h.loopObj, a = h.LinkedList, g = h.is, b = h.defineGetter, o = h.Event;
	var h = zachModule["0"], insert = h.insert, loopArray = h.loopArray, loopObj = h.loopObj, LinkedList = h.LinkedList, is = h.is, defineGetter = h.defineGetter, Event = h.Event;
	(function(t, s, r) {
		insert(window.ua = window.ua || {}, {
			win32 : r === "Win32",
			ie : !!window.ActiveXObject || "ActiveXObject" in window,
			ieVersion : Math.floor((/MSIE ([^;]+)/.exec(t)||[0,"0"])[1]),
			ios : (/iphone|ipad/gi).test(s),
			iphone : (/iphone/gi).test(s),
			ipad : (/ipad/gi).test(s),
			iosVersion : parseFloat(("" + (/CPU.*OS ([0-9_]{1,5})|(CPU like).*AppleWebKit.*Mobile/i.exec(t)||[0,""])[1]).replace("undefined", "3_2").replace("_", ".").replace("_", "")) || false,
			safari : /Version\//gi.test(s) && /Safari/gi.test(s),
			uiWebView : /(iPhone|iPod|iPad).*AppleWebKit(?!.*Safari)/i.test(t),
			android : (/android/gi).test(s),
			androidVersion : parseFloat("" + (/android ([0-9\.]*)/i.exec(t)||[0,""])[1]),
			chrome : /Chrome/gi.test(t),
			chromeVersion : parseInt((/Chrome\/([0-9]*)/gi.exec(t)||[0,0])[1], 10),
			webkit : /AppleWebKit/.test(s),
			uc : s.indexOf("UCBrowser") !== -1,
			Browser : / Browser/gi.test(s),
			MiuiBrowser : /MiuiBrowser/gi.test(s),
			MicroMessenger : t.toLowerCase().match(/MicroMessenger/i) == "micromessenger",
			canTouch : "ontouchstart" in document,
			msPointer : window.navigator.msPointerEnabled
		});
	})(navigator.userAgent, navigator.appVersion, navigator.platform);
	function toAbsURL(s) {
		var r = document.createElement("a");
		r.href = s;
		return r.href;
	}

	loopArray(["Left", "Top"], function(r) {
		defineGetter(HTMLElement.prototype, "page" + r, function() {
			var t = 0, u, s = document.body;
			for ( u = this; u !== s; u = u.offsetParent || u.parentElement) {
				t += u["offset" + r] - (u === this ? 0 : u["scroll" + r]);
			}
			return t;
		});
	});
	loopArray(["pageX", "pageY", "clientX", "clientY"], function(r) {
		Object.defineProperty(UIEvent.prototype, "z" + r.replace(/^./, function(s) {
			return s.toUpperCase();
		}), {
			get : function() {
				return "touches" in this && this.touches[0] !== undefined ? this.touches[0][r] : this[r];
			}
		});
	});
	window.nonstandardStyles = {};
	function css(t, s, r) {
		function u(w, v) {
			if ( w in nonstandardStyles) {
				loopArray(nonstandardStyles[w], function(x) {
					t.style.setProperty(x, v, "");
				});
			} else {
				t.style.setProperty(w, v, "");
			}
		}
		g.String(s) ? u(s, r) : e(s, u);
		return t;
	}
	css.size = function(t, s, r) {
		i(t, {
			width : s + "px",
			height : r + "px"
		});
	};
	var requestAnimate = function() {
		var r = null, s = a();
		return function(t) {
			var u = null;
			function v() {
				if (u === null) {
					u = s.insert(a.Node(t), null);
					if (r === null) {
						r = setTimeout(function w() {
							var x;
							if (s.tail() !== null) {
								r = setTimeout(w, 1000 / 60);
								for ( x = s.head(); x !== null; x = x.next) {
									x.value();
								}
							} else {
								r = null;
							}
						}, 1000 / 60);
					}
				}
			}

			v();
			return {
				start : v,
				remove : function() {
					u && s.remove(u);
					u = null;
				}
			};
		};
	}();
	function bindEvent(v, u, s, t) {
		var r;
		if (v.addEventListener) {
			v.addEventListener(u, s, t || false);
			r = function() {
				v.removeEventListener(u, s, t || false);
			};
		} else {
			v.attachEvent("on" + u, s);
			r = function() {
				v.detachEvent("on" + u, s);
			};
		}
		return {
			remove : r
		};
	}

	function Bind(r) {
		return function(u, s, t) {
			return k(u, r, s, t);
		};
	}

	function onInsert(t, r) {
		if (document.documentElement.contains(t)) {
			r && r();
		} else {
			var s = bindEvent(t, "DOMNodeInsertedIntoDocument", function() {
				r && r(t);
				s.remove();
			});
		}
	}

	function ajax(r) {
		var s = r.url + h.encodeURIObject(r.arg), t = new XMLHttpRequest();
		k(t, "load", function() {
			var u = t.responseText;
			try {
				if (r.isJson) {
					u = JSON.parse(u);
				}
			} catch(v) {
				r.onError && r.onError(t);
				return;
			}
			r.onLoad && r.onLoad(u, t);
		});
		k(t, "error", function() {
			r.onError && r.onError(t);
		});
		t.open(r.method || "get", s, true);
		r.requestHeader && e(r.requestHeader, function(u, v) {
			t.setRequestHeader(u, v);
		});
		t.send(r.data || null);
		return t;
	}

	var onLoad = function() {
		var r = null;
		return function(t) {
			if (document.readyState === "complete") {
				t();
			} else {
				if (r === null) {
					r = o();
					var s = k(window, "load", function() {
						r.trig();
						s.remove();
						r = null;
					});
				}
				r.regist(t);
			}
		};
	}();
	zachModule["1"].toAbsURL = c;
	zachModule["1"].css = i;
	zachModule["1"].bindEvent = k;
	zachModule["1"].Bind = q;
	zachModule["1"].onInsert = n;
	zachModule["1"].requestAnimate = f;
	zachModule["1"].ajax = m;
	zachModule["1"].onLoad = j;
});
zachModule(function() {
	//var a = zachModule["0"], i = a.is, m = a.loopArray, e = a.loopObj, b = a.insert, v = a.tupleString, h = a.LinkedList, z = zachModule["1"], k = z.css, g = z.bindEvent;
	var a = zachModule["0"], is = a.is, loopArray = a.loopArray, loopObj = a.loopObj, insert = a.insert, tupleString = a.tupleString, LinkedList = a.LinkedList
	, z = zachModule["1"], css = z.css, bindEvent= z.bindEvent;
	insert(nonstandardStyles, {
		transform : ["-webkit-transform", "-ms-transform", "transform"],
		"transform-origin" : ["-webkit-transform-origin", "transform-origin"],
		animation : ["-webkit-animation"],
		transition : ["-webkit-transition", "transition"],
		"backface-visibility" : ["-webkit-backface-visibility", "-mozila-backface-visibility", "backface-visibility"],
		"transform-style" : ["-webkit-transform-style", "transform-style"],
		perspective : ["-webkit-perspective", "perspective"]
	});
	function cssRuleString(B) {
		var n = "";
		loopObj(B, function(E, D) {
			function C(F) {
				n += F + ":" + D + ";";
			}
			E in nonstandardStyles ? m(nonstandardStyles[E], C) : C(E);
		});
		return n;
	}

	function removeCss(B, n) {
		function C(D) {
			function E(F) {
				B.style.removeProperty(F);
			}
			D in nonstandardStyles ? loopArray(nonstandardStyles[D], E) : E(D);
		}
		i.String(n) ? C(n) : i.Object(n) ? loopObj(n, C) : loopArray(n, C);
		return B;
	}

	var insertCSSRule = function() {
		var n = LinkedList(), B = LinkedList();
		return function(C, D) {
			var G = D ? B : n;
			if (G.el === undefined) {
				G.el = document.head.insertBefore(document.createElement("style"), D ? document.head.firstChild : null);
			}
			var F = G.tail(), E = G.insert(LinkedList.Node(F === null ? 0 : F.value + 1), null);
			G.el.sheet.insertRule(C, E.value);
			return {
				remove : function() {
					var I = E.value;
					for (var H = E.next; H !== null; H = H.next) {
						H.value = I++;
					}
					G.remove(E);
					G.el.sheet.deleteRule(I);
				}
			};
		};
	}();
	function insertCSSRules(C, B, n) {
		function D(E, H, F) {
			var G = i.String(H) ? H : A(H);
			insertCSSRule(E + " {" + G + "}", F);
		}

		if (i.String(C)) {
			D(C, B, n);
		} else {
			loopObj(C, function(E, F) {
				D(E, F, B);
			});
		}
	}

	function p(B) {
		return Math.abs(B) < 0.01 ? 0 : B;
	}
	css.transform = function() {
		var n = [];
		loopArray(arguments, function(B, C) {
			C !== 0 && n.push(B);
		});
		css(arguments[0], "transform", n.join(" "));
	};
	css.matrix = function(n) {
		return tupleString("matrix", [p(n[0]), p(n[1]), p(n[2]), p(n[3]), p(n[4]), p(n[5])]);
	};
	css.translate = function(n, C, B) {
		return tupleString("translate3d", [p(n) + "px", p(C) + "px", p(B) + "px"]);
	};
	function r(n) {
		return function(C, B) {
			return v(n, [p(C) + (B || "rad")]);
		};
	}
	css.rotateX = r("rotateX");
	css.rotateY = r("rotateY");
	css.rotateZ = r("rotateZ");
	css.scale = function() {
		return "scale(" + Array.prototype.join.call(arguments, ",") + ")";
	};
	css.px = function(n) {
		return n === 0 ? 0 : p(n) + "px";
	};
	css.s = function(n) {
		return n === 0 ? 0 : p(n) + "s";
	};
	css.url = function(n) {
		return "url(" + n + ")";
	};
	function onTransitionEnd(B, func) {
		var C = g(B, "webkitTransitionEnd", function() {
			C.remove();
			func();
		});
	}

	function transition(F, H, E, C, D) {
		D = i.String(E) ? D : C;
		if (ua.android && ua.androidVersion < 3) {
			css(F, E, C);
			D && D();
		} else {
			css(F, "transition", H);
			F.transition && F.transition.remove();
			function B() {
				if (F.transition) {
					n.remove();
					G.remove();
					l(F, "transition");
					D && D();
					F.transition = null;
				}
			}

			var G = bindEvent(F, "DOMNodeRemovedFromDocument", B), n = F.transition = g(F, "webkitTransitionEnd", B);
			setTimeout(function() {
				css(F, E, C);
			}, 20);
		}
	}

	function removeNode(n) {
		n && n.parentNode && n.parentNode.removeChild(n);
	}

	function element(D, C, n) {
		var G, E = {}, F = n;
		if (D.charAt(0) === "<") {
			G = document.createElement("div");
			G.innerHTML = D;
			G = G.firstElementChild;
		} else {
			var B = /([.#][^.#]*)/g, H;
			G = document.createElement(D.split(/[#.]/)[0]);
			while ( H = B.exec(D)) {
				H = H[0];
				H.charAt(0) === "#" ? G.id = H.substring(1) : G.classList.add(H.substring(1));
			}
		}
		if (is.String(C)) {
			G.innerHTML = C;
		} else {
			if (is.Object(C)) {
				E = C;
			} else {
				if (is.Array(C)) {
					E.children = C;
				} else {
					F = C;
				}
			}
		}
		E && loopObj(E, function(I, J) {
			if (J !== undefined) {
				switch(I) {
					case"classList":
						if (is.String(J)) {
							G.classList.add(J);
						} else {
							if (is.Array(J)) {
								m(J, function(K) {
									G.classList.add(K);
								});
							}
						}
						break;
					case"css":
						css(G, J);
						break;
					case"children":
						if (is.Array(J)) {
							m(J, function(K) {
								G.appendChild(K);
							});
						} else {
							G.appendChild(J);
						}
						break;
					default:
						if (I.substring(0, 5) === "data-") {
							G.setAttribute(I, J);
						} else {
							G[I] = J;
						}
						break;
				}
			}
		});
		F && F.appendChild(G);
		return G;
	}

	function switchClass(C, n, B) {
		n ? C.classList.add(B) : C.classList.remove(B);
	}

	function toggleState(B, C, n) {
		B.classList.remove(C);
		B.classList.add(n);
	}

	function bubble(B, C, n) {
		var D;
		while (B !== null && B !== document && B !== n) {
			if ( D = C(B)) {
				return D;
			}
			B = B.parentNode;
		}
	}

	function onBubble(B, func) {
		document.addEventListener(B, function(C) {
			bubble(C.target, function(D) {
				func(D);
			}, document.documentElement);
		}, false);
	}

	function findAncestor(n, B) {
		return onBubble(n, function(C) {
			if (B(C)) {
				return C;
			}
		});
	}
	z.onLoad(function() {
		x("focusin", function(n) {
			n.classList.add("focus");
		});
		onBubble("focusout", function(n) {
			n.classList.remove("focus");
		});
	});
	zachModule["2"].removeCss = l;
	zachModule["2"].cssRuleString = A;
	zachModule["2"].insertCSSRule = f;
	zachModule["2"].insertCSSRules = q;
	zachModule["2"].onTransitionEnd = y;
	zachModule["2"].transition = w;
	zachModule["2"].element = c;
	zachModule["2"].removeNode = s;
	zachModule["2"].toggleState = j;
	zachModule["2"].switchClass = u;
	zachModule["2"].bubble = o;
	zachModule["2"].onBubble = x;
	zachModule["2"].findAncestor = t;
	zachModule["2"].css = k;
	zachModule["2"].toAbsURL = z.toAbsURL;
	zachModule["2"].bindEvent = g;
	zachModule["2"].Bind = z.Bind;
	zachModule["2"].onInsert = z.onInsert;
	zachModule["2"].requestAnimate = z.requestAnimate;
	zachModule["2"].ajax = z.ajax;
	zachModule["2"].onLoad = z.onLoad;
});
zachModule(function() {
	function a(i) {
		return i >= 0 ? 1 : -1
	}

	function h(l, k, j, n, m, i) {
		l -= j;
		k -= n;
		return l >= 0 && l < m && k >= 0 && k < i
	}

	function b(j, k, i) {
		if (k <= i) {
			return j < k ? k : j > i ? i : j
		} else {
			return b(j, i, k)
		}
	}

	function f(j, k, i) {
		if (k <= i) {
			return j >= k && j < i
		} else {
			return f(j, i, k)
		}
	}

	function g(i, j) {
		return Math.sqrt(i * i + j * j)
	}

	function e(i, j) {
		return i / g(i, j)
	}

	function c(k, m, j, l, n) {
		var i = 0.0001, o = n ||
		function(q) {
			function s(B, A, z) {
				var x = 1 - B, C = x * x, w = B * B, v = w * B, y = B * C, u = w * x;
				return 3 * A * y + 3 * z * u + v
			}

			function r(u, w, v) {
				return (9 * w - 9 * v + 3) * u * u + (6 * v - 12 * w) * u + 3 * w
			}

			var p = 0.5;
			while (Math.abs(q - s(p, k, j)) > i) {
				p = p - (s(p, k, j) - q) / r(p, k, j)
			}
			return s(p, m, l)
		};
		o.arg = [k, m, j, l];
		return o
	}
	zachModule["3"].sign = a;
	zachModule["3"].inRect = h;
	zachModule["3"].range = b;
	zachModule["3"].inRange = f;
	zachModule["3"].distance = g;
	zachModule["3"].sin2 = e;
	zachModule["3"].Bezier = c
});
zachModule(function() {
	var k = zachModule["0"], q = k.insert, m = k.loopArray, o = k.Event, n = zachModule["3"], p = zachModule["1"], l = p.bindEvent, c = 8, h = 0.8;
	function a(t, s, r) {
		function u(w, v, x) {
			return k.request(function(y) {
				return t.onTouchStart || t.onCursorDown ? ua.msPointer || !ua.canTouch ? t.onCursorDown(y) : t.onTouchDown(y) : l(t, w, y, r)
			}, function(z) {
				var C = z.zPageX, B = z.zPageY, E = C, D = B, y = o(), F = o(), A = l(document, v, function(H) {
					C = H.zPageX;
					B = H.zPageY;
					H.distanceX = C - E;
					H.distanceY = B - D;
					H.onMove = y.regist;
					H.onUp = F.regist;
					y.trig(H, C, B)
				}), G = l(document, x, function(H) {
					A.remove();
					G.remove();
					F.trig(H, C, B)
				});
				z.onMove = y.regist;
				z.onUp = F.regist;
				s(z, C, B)
			})
		}
		return ua.canTouch ? u("touchstart", "touchmove", "touchend") : u("mousedown", "mousemove", "mouseup")
	}

	function j(s, t, r) {
		return s.tagName ? l(s, ua.canTouch ? "touchend" : "mouseup", t, r) : ua.msPointer || !ua.canTouch ? s.onCursorUp(t) : s.onTouchUp(t)
	}

	function e(r) {
		return n.distance(r.distanceX, r.distanceY) > c
	}

	function i(r, s) {
		return e(r) ? (Math.abs(n.sin2(r.distanceY, r.distanceX)) >= h) ^ s : undefined
	}

	function g(s, r, t) {
		t = t || e;
		return a(s, function(v) {
			var w = j(s, function(x) {
				r(x)
			}), u = v.onMove(function(x) {
				if (t(x)) {
					u.remove();
					w.remove()
				}
			});
			v.onUp(function() {
				w.remove()
			})
		})
	}

	function f(s, r, t) {
		t = k.defaultValue(t, function(u) {
			return e(u) || undefined
		});
		return a(s, function(v) {
			var u = v.onMove(function(w, B, A) {
				var x = !t || t(w);
				if (x !== undefined) {
					u.remove();
					if (!x) {
						return
					}
					function G(M, K) {
						var J = M === 0 ? undefined : M > 0, H = [], N = 0, L = +new Date(), I = K;
						return {
							test : function(O) {
								return J === undefined || !((O - K) * ( J ? 1 : -1) < -20)
							},
							track : function(O) {
								O = O || K;
								var P = +new Date(), R = P - L, Q = O === K ? J : O > K;
								if (Q !== J || R > 200) {
									H = [];
									N = 0
								} else {
									if (R > 200) {
										H = [];
										N = 0
									} else {
										N += R;
										while (N > 300) {
											N -= H.shift().duration
										}
										H.push({
											duration : R,
											distance : O - K
										})
									}
								}
								J = Q;
								K = O;
								L = P
							},
							distance : function() {
								return K - I + M
							},
							direction : function() {
								return J
							},
							speed : function() {
								var O = 0;
								m(H, function(P) {
									O += P.distance
								});
								return N === 0 ? 0 : O / N
							}
						}
					}

					var y = new Date(), C = o(), F = o(), E = G(w.distanceX, B), D = G(w.distanceY, A);
					function z() {
						return {
							distanceX : E.distance(),
							distanceY : D.distance(),
							directionX : E.direction(),
							directionY : D.direction()
						}
					}

					r(q(z(), {
						onDragEnd : F.regist,
						onDragMove : C.regist
					}));
					w.onMove(function(J, I, H) {
						if (E.test(I) && D.test(H)) {
							E.track(I);
							D.track(H);
							C.trig(z())
						}
					});
					w.onUp(function() {
						E.track();
						D.track();
						F.trig(q(z(), {
							speedX : E.speed(),
							speedY : D.speed(),
							duration : +new Date() - y
						}))
					})
				}
			})
		})
	}

	function b(r) {
		return function(t, s) {
			return f(t, s, function(u) {
				return i(u, r)
			})
		}
	}
	zachModule["4"].onPointerDown = a;
	zachModule["4"].onPointerUp = j;
	zachModule["4"].onTap = g;
	zachModule["4"].onDrag = f;
	zachModule["4"].onDragH = b(true);
	zachModule["4"].onDragV = b(false)
});
zachModule(function() {
	var k = zachModule["0"], q = k.insert, n = k.loopArray, g = k.loopObj, h = zachModule["2"], m = h.css, p = m.px, c = zachModule["4"], b = c.onPointerDown;
	function i(s, t) {
		var u = {};
		g(t, function(v, w) {
			u[v] = s[v] === undefined ? w : s[v]
		});
		return u
	}

	function f(s) {
		return function(u, t) {
			if (k.is.Object(u)) {
				g(u, s)
			} else {
				s(u, t)
			}
		}
	}

	function o(w, z) {
		function u(C, B) {
			function D(F) {
				var G = "";
				k.loop(F, function() {
					G += "0"
				});
				return G
			}

			var E = C + "";
			return B > E.length ? D(B - E.length) + E : E
		}

		w = new Date(w);
		var v = {
			Y : w.getFullYear() + "",
			M : u(w.getMonth() + 1, 2),
			D : u(w.getDate(), 2),
			h : u(w.getHours(), 2),
			m : u(w.getMinutes(), 2),
			s : u(w.getSeconds(), 2)
		};
		var A = "", t = "", s = "";
		for (var x = 0, y = z.length; x !== y; ++x) {
			s = z.charAt(x);
			if (s === "%") {
				t += v[A] || A;
				A = "";
				continue
			}
			t += A;
			A = s
		}
		return t + A
	}

	function l(w, x, v, A) {
		var t = x / v, y = w.naturalWidth || w.width || w.clientWidth, z = w.naturalHeight || w.height || w.clientHeight, u = y / z, s = {
			position : "absolute"
		};
		if (t < u) {
			s.height = p(v);
			s.left = p((x - v / z * y) / 2 << 0);
			s.top = 0;
			ua.ie && (s.width = p(v * u));
			A && (A.h = v)
		} else {
			s.width = p(x);
			s.left = 0;
			s.top = p((v - x / y * z) / 2 << 0);
			ua.ie && (s.height = p(x / u));
			A && (A.h = x / u)
		}
		return s
	}

	function a(v, u) {
		u = u || v.querySelector(".red-point .wrapper");
		var t = [], s = null;
		k.loop(v.length(), function() {
			t.push(h.element("span", u))
		});
		v.onCutTo(function(w) {
			s && s.classList.remove("active");
			s = t[w.curIndex];
			s.classList.add("active")
		})
	}

	var e = k.Resource(function(s) {
		window.bdmapInit = function() {
			s();
			delete window.bdmapInit
		};
		h.element("script", {
			src : "http://api.map.baidu.com/api?type=quick&ak=D5a271a3083d77f21c63ff307e9f60b9&v=1.0&callback=bdmapInit"
		}, document.head)
	});
	function j(s) {
		return function(t) {
			e.load(function() {
				s(t)
			})
		}
	}

	var r = j(function(s) {
		var t = h.element("div", {
			css : {
				height : "100%",
				width : "100%"
			}
		}, s.parent), v = new BMap.Map(t), u = [];
		b(s.parent, function(w) {
			w.stopPropagation()
		});
		n(s.data, function(A) {
			var x = new BMap.Point(parseFloat(A.lng), parseFloat(A.lat)), y = new BMap.Marker(x), w = new BMap.Icon(staticImgSrc("layout-map-mark.png"), new BMap.Size(30, 30));
			y.setIcon(w);
			v.addOverlay(y);
			u.push(x);
			if (s.make) {
				var z = new BMap.InfoWindow(s.make(A));
				y.addEventListener("click", function() {
					y.openInfoWindow(z)
				})
			}
		});
		if (u.length !== 0) {
			v.centerAndZoom(u[0], 16);
			v.setViewport(u)
		} else {
			v.centerAndZoom("北京市")
		}
		s.onLoad && s.onLoad()
	});
	zachModule["5"].extract = i;
	zachModule["5"].dateString = o;
	zachModule["5"].KeyValueFunction = f;
	zachModule["5"].getImageCoverStyle = l;
	zachModule["5"].doRedPoints = a;
	zachModule["5"].markerMap = r
});
zachModule(function() {
	var i = zachModule["3"], e = i.Bezier, g = zachModule["1"], c = g.requestAnimate, h = zachModule["0"], f = {
		linear : e(1, 1, 1, 1, function(k) {
			return k
		}),
		ease : e(0.25, 0.1, 0.25, 1),
		easeOut : e(0, 0, 0.58, 1),
		easeInOut : e(0.42, 0, 0.58, 1)
	};
	function a(n, m, k) {
		if (h.is.Array(n)) {
			var l = [];
			h.loopArray(n, function(p, o) {
				l.push(a(p, m[o], k))
			});
			return l
		} else {
			return n + (m - n) * k
		}
	}

	function j(k) {
		var o = (k.duration || 1) * 1000, m = k.timing || f.ease, l = -(k.delay || 0) * 1000, n = new Date();
		return {
			ratio : function() {
				var p = new Date();
				l += p - n;
				n = p;
				return l < 0 ? null : m(l >= o ? 1 : l / o)
			},
			isEnd : function() {
				return l >= o
			},
			progress : function(p) {
				l = p * o;
				n = new Date()
			}
		}
	}

	function b(l, r) {
		var o = j(l), k = true, q = l.start || 0, m = l.end || 1;
		function p(s) {
			if (s !== null) {
				if (k) {
					l.onStart && l.onStart();
					k = false
				}
				l.onAnimate(a(q, m, s));
				if (o.isEnd()) {
					l.onEnd && l.onEnd();
					n.remove()
				}
			}
		}

		p(0);
		var n = (r || c)(function() {
			p(o.ratio())
		});
		return {
			remove : n.remove,
			progress : o.progress
		}
	}
	b.Bezier = e;
	b.Timing = f;
	b.Progress = j;
	b.animate = b;
	b.fromTo = a;
	zachModule["6"] = b
});
zachModule(function() {
	var c = {
		translate : function(h, i) {
			return [1, 0, 0, 1, h, i]
		},
		scale : function(i, h) {
			return [i, 0, 0, h, 0, 0]
		},
		rotate : function(h) {
			var i = Math.sin(h), j = Math.cos(h);
			return [j, i, -i, j, 0, 0]
		}
	};
	function a(h, i) {
		return h[0] === i[0] && h[1] === i[1] && h[2] === i[2] && h[3] === i[3] && h[4] === i[4] && h[5] === i[5]
	}

	function b(h) {
		var i = h[0] * h[3] - h[1] * h[2];
		return [h[3] / i, -h[1] / i, -h[2] / i, h[0] / i, (h[2] * h[5] - h[3] * h[4]) / i, (h[1] * h[4] - h[0] * h[5]) / i]
	}

	function f(h, i) {
		return [h[0] * i[0] + h[2] * i[1] + h[4] * i[2], h[1] * i[0] + h[3] * i[1] + h[5] * i[2], i[2]]
	}

	function g(h, i) {
		return [h[0] * i[0] + h[2] * i[1], h[1] * i[0] + h[3] * i[1], h[0] * i[2] + h[2] * i[3], h[1] * i[2] + h[3] * i[3], h[0] * i[4] + h[2] * i[5] + h[4], h[1] * i[4] + h[3] * i[5] + h[5]]
	}

	function e(i, h, j) {
		return g(g(c.translate(h, j), i), c.translate(-h, -j))
	}
	zachModule["7"].isTransformEqual = a;
	zachModule["7"].matrix = c;
	zachModule["7"].inverse = b;
	zachModule["7"].transform = f;
	zachModule["7"].combine = g;
	zachModule["7"].transformOrigin = e
});
zachModule(function() {
	var b = zachModule["0"], c = b.loopArray, a = b.loop;
	zachModule["8"] = {
		remove : function(e, g) {
			var f = [];
			c(e, function(h) {
				h != g && f.push(h)
			});
			return f
		},
		reverse : function(f) {
			var e = f.length - 1, g = e === -1 ? [] : new Array(e);
			c(f, function(j, h) {
				g[e - h] = j
			});
			return g
		},
		zip : function(e) {
			var f = [];
			a(e[0].length, function(g) {
				c(e, function(h) {
					f.push(h[g])
				})
			});
			return f
		},
		loopSection : function(e, g) {
			var f = null;
			c(e, function(h) {
				f && g(f, h);
				f = h
			});
			g(f, null)
		}
	}
});
zachModule(function() {
	var i = zachModule["8"];
	function a(h) {
		return h.naturalWidth || h.width || h.clientWidth
	}

	function c(h) {
		return h.naturalHeight || h.height || h.clientHeight
	}

	function g(l, j, m) {
		var k = a(l), h = c(l);
		return j / m < k / h ? m / h : j / k
	}

	function f(l, j, m) {
		var k = a(l), h = c(l);
		return j / m < k / h ? j / k : m / h
	}

	function e(k, h) {
		var j = h.width, n = h.height, p = h.align, l = h.size(k, h.width, h.height);
		l = h.noStretch ? Math.min(l, 1) : l;
		function m(r, q, t) {
			var s = (r - q * l) * t;
			return s > 0 ? [0, q, s, q * l] : [-s / l, r / l, 0, r]
		}

		var o = [k].concat(i.zip([m(j, a(k), p[0]), m(n, c(k), p[1])]));
		o.ratio = l;
		return o
	}

	function b(v, n) {
		var m = n[0], x = n.ratio, w = m.naturalWidth, p = m.naturalHeight, j = n[1], h = n[2], k = n[3], u = n[4], s = n[5], r = n[6], t = n[7], o = n[8];
		if (ua.ios) {
			var q = 1 - k * u / w / p;
			if (q < 0.02) {
				v.drawImage(m, s, r, t, o)
			} else {
				if (q < 0.05 || t * o < 6500) {
					v.save();
					v.beginPath();
					v.rect(s, r, t, o);
					v.clip();
					v.drawImage(m, -j / k * t, -h / u * o, w * x, p * x);
					v.restore()
				} else {
					v.drawImage.apply(v, n)
				}
			}
		} else {
			v.drawImage.apply(v, n)
		}
	}
	zachModule["9"].drawImageLayout = b;
	zachModule["9"].layImageByFrame = e;
	zachModule["9"].Size = {
		contain : f,
		cover : g
	}
});
zachModule(function() {
	var a = zachModule["0"], e = a.insert, u = a.loopArray, D = a.Event, n = a.recursion, v = zachModule["7"], y = v.matrix, B = v.combine, r = v.transform, f = v.inverse, A = zachModule["1"], k = A.Bind, z = A.requestAnimate, q = A.css, x = k(ua.msPointer ? "MSPointerOver" : "mouseover"), t = k(ua.msPointer ? "MSPointerOut" : "mouseout"), o = k(ua.msPointer ? "MSPointerDown" : "mousedown"), h = k(ua.msPointer ? "MSPointerUp" : "mouseup"), s = k(ua.msPointer ? "MSPointerMove" : "mousemove"), g = k("touchstart"), c = k("touchend"), i = k("touchmove"), C = 0;
	function m(I) {
		var F = [1, 0, 0, 1, 0, 0], J = [1, 0, 0, 1, 0, 0], K = [];
		function H() {
			var L = B(F, J);
			I.setTransform(L[0], L[1], L[2], L[3], L[4], L[5])
		}

		function G(L) {
			J = B(J, L);
			H()
		}

		function E(L) {
			return function() {
				G(L.apply(null, arguments))
			}
		}
		return e(I, {
			setPrepareTransform : function(L) {
				F = L;
				H()
			},
			transform : G,
			getTransform : function() {
				return [J[0], J[1], J[2], J[3], J[4], J[5]]
			},
			save : function() {
				CanvasRenderingContext2D.prototype.save.call(I);
				K.push(J)
			},
			restore : function() {
				CanvasRenderingContext2D.prototype.restore.call(I);
				J = K.pop();
				H()
			},
			translate : E(y.translate),
			rotate : E(y.rotate),
			scale : E(y.scale)
		})
	}

	function j() {
		var E = q(document.createElement("canvas"), "display", "block"), G = m(E.getContext("2d")), F = 1;
		a.defineAutoProperty(E, "dpr", {
			value : (window.devicePixelRatio || 1) / (G.webkitBackingStorePixelRatio || G.mozBackingStorePixelRatio || G.msBackingStorePixelRatio || G.oBackingStorePixelRatio || G.backingStorePixelRatio || 1),
			set : function(H) {
				G.dpr = F = H;
				G.setPrepareTransform(y.scale(H, H))
			}
		});
		return e(E, {
			isDirty : true,
			clear : true,
			draw : function(H) {
				E.clear && G.clearRect(0, 0, E.width, E.height);
				G.layer = E;
				G.save();
				H(G);
				G.restore()
			},
			resize : function(I, H) {
				E.width = I * E.dpr;
				E.height = H * E.dpr;
				q.size(E, E.logicalWidth = I, E.logicalHeight = H);
				E.dpr = F
			},
			drawTo : function(H) {
				E.parentLayer = H.layer;
				E.transformation = H.getTransform();
				H.drawImage(E, 0, 0, E.width, E.height)
			},
			dirty : function() {
				E.isDirty = true;
				E.parentLayer && E.parentLayer.dirty()
			}
		})
	}

	function b() {
		var E = null, F = {
			id : C++,
			areaFromPoint : null,
			dirty : function() {
				F.parentLayer && F.parentLayer.dirty()
			}
		};
		Object.defineProperty(F, "draw", {
			set : function(G) {
				E = G
			},
			get : function() {
				return function(G) {
					G.getTransform && (F.transformation = G.getTransform());
					F.parentLayer = G.layer;
					E(G)
				}
			}
		});
		u(["cursorDown", "cursorUp", "cursorMove", "cursorEnter", "cursorLeave", "touchDown", "touchMove", "touchUp", "touchEnter", "touchLeave"], function(G) {
			var H = D();
			F[G] = H.trig;
			F["on" + G.replace(/./, function(I) {
				return I.toUpperCase()
			})] = H.regist
		});
		return F
	}

	function l(E, F) {
		return E && E.transformation ? r(f(E.transformation), l(E.parentLayer, F)) : F
	}

	function p(E, F) {
		return E && E.transformation ? p(E.parentLayer, r(E.transformation, F)) : F
	}

	function w() {
		var G = j(), J, I, H = false, F = D();
		function K(Y, Z, M, V, T, W) {
			var R = 0, Q = 0, S = false, L = [], P = "last" + Z, X = "is" + Z;
			function U() {
				L = [];
				n(function aa(ab) {
					if (ab) {
						L.push(ab);
						u(ab.areaFromPoint ? [].concat(ab.areaFromPoint.apply(null, l(ab, [R, Q, 1]))) : [], aa)
					}
				}, G.root);
				L.reverse()
			}

			function O(ab, ac) {
				if (!H) {
					return
				}
				var aa = L;
				u(aa, function(ad) {
					ad[P] = ad[X] || false;
					ad[X] = false
				});
				ac ? ( L = ac) : U();
				u(L, function(ad) {
					ad[X] = true;
					N(ad, Y + "Move", ab);
					if (!ad[P]) {
						N(ad, Y + "Enter", ab)
					}
				});
				u(aa, function(ad) {
					if (!ad[X]) {
						N(ad, Y + "Leave", ab)
					}
					delete ad[P]
				})
			}

			function N(ac, aa, ab) {
				ac[aa] && ac[aa](ab, R, Q)
			}

			M(G, O);
			V(G, function(aa) {
				W && W(aa);
				aa.preventDefault();
				u(L, function(ab) {
					N(ab, Y + "Down", aa)
				})
			});
			T(G, function(aa) {
				u(L, function(ab) {
					N(ab, Y + "Up", aa)
				})
			});
			return {
				focus : function() {
					S = true
				},
				blur : function() {
					S = false;
					O(event, [])
				},
				move : function(aa, ab) {
					R = aa;
					Q = ab
				},
				calculate : function() {
					S && O({})
				}
			}
		}

		J = K("cursor", "Hover", s, o, h);
		s(document, function(L) {
			J.move(L.pageX, L.pageY)
		}, true);
		x(G, J.focus);
		t(G, J.blur);
		if (!ua.msPointer) {
			I = K("touch", "Touch", i, g, c, function(L) {
				I.focus();
				I.move(L.zPageX, L.zPageY);
				I.calculate()
			});
			i(document, function(L) {
				I.move(L.zPageX, L.zPageY)
			}, true);
			c(document, I.blur)
		}
		z(function() {
			F.trig();
			if (G.isDirty) {
				G.isDirty = false;
				H = true;
				G.root && G.draw(G.root.draw);
				J.calculate();
				I && I.calculate()
			}
		});
		function E() {
			G.transformation = y.translate(G.pageLeft, G.pageTop)
		}
		A.onInsert(G, E);
		return e(G, {
			root : null,
			alter : E,
			requestAnimate : F.regist
		})
	}
	w.Context2D = m;
	w.Layer = j;
	w.Area = b;
	w.coordinatePageToArea = l;
	w.coordinateAreaToPage = p;
	zachModule["10"] = w
});
zachModule(function() {
	var a = zachModule["0"], b = a.is, c = a.LinkedList;
	c.loopNodes = function(f, k, j, i) {
		var h, g, l, m, e;
		if (b.Function(k)) {
			h = null;
			g = k;
			l = j
		} else {
			h = k;
			g = j;
			l = i
		}
		for ( m = f; m !== h; m = l ? m.previous : m.next) {
			if (( e = g(m.value, m)) !== undefined) {
				return e
			}
		}
	};
	c.loopSection = function(f, g) {
		var h = f.head(), e = h.value;
		c.loopNodes(h.next, function(i) {
			g(e, i);
			e = i
		});
		g(e, null)
	};
	c.toArray = function(f) {
		var e = [];
		c.loop(f, function(g) {
			e.push(g)
		});
		return e
	};
	c.push = function(f, e) {
		return f.insert(c.Node(e), null)
	};
	c.pop = function(f) {
		var e = f.tail();
		f.remove(e);
		return e.value
	};
	c.isBefore = function(f, e) {
		for (; e && e !== f; e = e.next) {
		}
		return e === null
	};
	zachModule["11"] = c
});
zachModule(function() {
	var c = zachModule["11"];
	var h = /^[（【“‘]$/;
	var n = /^[）】”’，。；：？！、]$/;
	var f = /^[0-9a-zA-Z`~!@#\$%\^&\*\(\)\-_=\+\[\{\]\}\\\|:;"'<,>\.\?\/]$/;
	var g = /^[ 	　]$/;
	var j = /^[\n\r]$/;
	function i(o) {
		return o.character || ""
	}

	function k(p, o) {
		return p && o && (h.test(p) && !g.test(o) || !g.test(p) && n.test(o) || f.test(p) && f.test(o) || g.test(p) && g.test(o))
	}

	function m(p, o) {
		return function(s, v, q, r) {
			var t = r;
			var x = s;
			var z = 0;
			var u = "";
			var w = s;
			var y = [];
			c.loopNodes(s, v, function(A, B) {
				z += A.width;
				u += i(A);
				if (p(i(A), B.next === v ? "" : i(B.next.value))) {
					if (j.test(u)) {
						y.push(x);
						x = B.next;
						t = r
					} else {
						if (w !== x && t + z > q && !(o && g.test(i(w.value)))) {
							y.push(x);
							x = w;
							t = z
						} else {
							t += z
						}
					}
					u = "";
					z = 0;
					w = B.next
				}
			});
			y.push(x);
			return y
		}
	}

	var a = m(function(p, o) {
		return true
	}, false);
	var b = m(function(p, o) {
		return !k(p, o)
	}, true);
	function l(s, p, q, r) {
		var o = r;
		c.loopNodes(s, p, function(t) {
			t.offsetX = o;
			o += t.width
		})
	}

	function e(t, x, q, u) {
		var p = t;
		c.loopNodes(t, x, function(z, A) {
			if (!z.character || !g.test(z.character)) {
				p = A
			}
		});
		var y = 0;
		var w = 0;
		c.loopNodes(t, p.next, function(z, A) {
			w += z.width;
			if (A.next !== p.next && !k(z.character, A.next.value.character)) {
				y++
			}
		});
		var o = y > 0 ? (q - u - w) / y : 0;
		var v = u;
		var r = 0;
		var s = 0;
		c.loopNodes(t, x, function(z, A) {
			z.offsetX = v + r;
			v += z.width;
			if (A.next !== x && !k(z.character, A.next.value.character)) {
				s++;
				r = (o * Math.min(s, y) + 0.5) << 0
			}
		})
	}
	zachModule["12"].buildAllBreakLines = a;
	zachModule["12"].buildWordBreakLines = b;
	zachModule["12"].alignLeftLine = l;
	zachModule["12"].alignSideLine = e
});
zachModule(function() {
	var f = zachModule["12"], e = zachModule["0"], j = zachModule["11"], h = zachModule["8"];
	var a = function() {
		var k;
		return function() {
			return k ? k : k = document.createElement("canvas").getContext("2d")
		}
	}();
	function g(k) {
		k = k || {};
		return [k.fontStyle || "normal", k.fontVariant || "normal", k.fontWeight || "normal", (k.fontSize || 12) + "px", k.fontFamily || "sans-serif"].join(" ")
	}

	function b(m, k) {
		var l = a();
		l.font = g(k);
		return l.measureText(m)
	}

	function i(q, m, l) {
		var o = a(), p = 0, k = 0, n = j(), r = l.align;
		o.font = g(l);
		e.loopString(q.replace(/\r/g, ""), function(s) {
			j.push(n, {
				character : s,
				width : s === "\n" ? 0 : o.measureText(s).width
			});
			if (s === "\n") {++p
			}
		}, true);
		h.loopSection(l.lineBreak(n.head(), null, m, 0), function(t, s) {
			t && (t.value.lineStart = true);
			r(t, s, m, 0);
			++k
		});
		n.style = l;
		n.width = m;
		n.height = k * l.lineHeight + p * (l.margin || (l.margin = 0));
		return n
	}

	function c(p, n) {
		var m = n.style, l = m.lineHeight, o = m.margin, q = -l, k = l / 2 << 0;
		p.font = g(m);
		p.fillStyle = m.color;
		p.textBaseline = "middle";
		j.loop(n, function(r) {
			if (r.lineStart) {
				q += l
			}
			if (r.character === "\n") {
				q += o
			}
			p.fillText(r.character, r.offsetX, q + k)
		})
	}
	zachModule["13"].LineBreak = {
		breakAll : f.buildAllBreakLines,
		normal : f.buildWordBreakLines
	};
	zachModule["13"].Align = {
		left : f.alignLeftLine,
		side : function(m, k, l) {
			(k && k.previous.value.character !== "\n" ? f.alignSideLine : f.alignLeftLine)(m, k, l, 0)
		}
	};
	zachModule["13"].Font = g;
	zachModule["13"].measureText = b;
	zachModule["13"].layText = i;
	zachModule["13"].drawTextLayout = c
});
(function() {
	var a = zachModule["0"], n = a.loopArray, p = a.extract, b = a.insert, s = a.Event, r = zachModule["2"], c = r.element, l = r.css, j = zachModule["5"], i = j.KeyValueFunction, f = zachModule["6"], h = window.fp = window.fp || {}, o = window.specialPage = {}, g = window.layoutFormat = {}, m = window.functionPages = {}, q = window.enterAnimate = {}, k = window.pageEffects = {}, e = window.switchAnimates = [];
	b(ua, {
		iphone4 : ua.iphone && screen.height === 480,
		iphone5 : ua.iphone && screen.height === 568,
		iphone6 : ua.iphone && screen.height > 568,
		mi4 : /Mi 4LTE/gi.test(navigator.userAgent)
	});
	window.staticImgSrc = function(t) {
		return contentSrc("image/" + t)
	};
	window.componentAttr = function(t) {
		return p(t, {
			x : 0,
			y : 0,
			opacity : 1,
			scale : 1,
			rotate : 0,
			"z-index" : 0,
			transform : 0
		})
	};
	window.center = function(u, t) {
		return (u - t) / 2 << 0
	};
	window.middleY = function(u, t) {
		return (u - idealHeight / 2) * (t || 1) + clientHeight / 2 << 0
	};
	window.middleX = function(t, u) {
		return (t - idealWidth / 2) * (u || 1) + clientWidth / 2 << 0
	};
	window.registLayout = i(function(t, u) {
		g[t] = u
	});
	window.registPageEffect = i(function(t, u) {
		k[t] = u
	});
	window.registEnterAnimate = i(function(t, u) {
		q[t] = (!window.highPerformance ? u.fallback : undefined) ||
		function(w) {
			var v = u.progress ? u.progress.apply(null, [w].concat(Array.prototype.slice.call(arguments, 1))) : undefined;
			if (v) {
				var x = {};
				a.loopObj(v, function(y, z) {
					n(y.split(" "), function(A) {
						x[A] = z
					})
				});
				v = x
			}
			return {
				component : w,
				duration : u.duration || 1,
				timing : u.timing || f.Timing.ease,
				progress : v,
				onAnimate : u.onAnimate
			}
		}

	});
	window.registSwitchAnimate = i(function(t, u) {
		e.push(u);
		e[t] = u
	});
	window.registFunctionPage = function(u, t) {
		return m[u] = function(v) {
			function w() {
				var x = h.slidePage();
				t(x, v.data);
				x.slideIn(v.noTransition)
			}

			if (v.noLog || h.isLogIn()) {
				w()
			} else {
				if (!h.canNotLogin) {
					sessionStorage.setItem("lastPageIndex", curPageIndex);
					sessionStorage.setItem("functionData", JSON.stringify({
						name : u,
						data : v.data
					}));
					h.logIn({
						returnUrl : location.href,
						onLogIn : w
					})
				} else {
					h.canNotLogin()
				}
			}
		}
	};
	window.registSpecialPage = i(function(t, u) {
		o[t] = function() {
			var v = {
				type : "special",
				load : function(w) {
					u(function(x) {
						v.create = function() {
							var z = c("div.special-page.page"), y = s(), A = s();
							b(z, {
								start : y.trig,
								recycle : A.trig,
								onShow : y.regist,
								onRemove : A.regist
							});
							x.create(z);
							return z
						};
						w()
					})
				}
			};
			return v
		}
	});
	window.isImageRect = function(t) {
		return /^#/.test(t) || /^rgba/gi.test(t)
	};
	window.LayoutPage = function(u) {
		var v = u.layout, x = g[v.label] || g.SingleImage, w = (x.resource || []).concat([]), z = v.image || [], t = u.pageEffect ? k[u.pageEffect] : null, y = t ? (t.resource || []).concat([]) : null;
		return {
			pageData : u,
			create : function(A) {
				x.create(A, v);
				t && t.create(A, y);
				return A
			},
			load : function(C) {
				var B = a.Loader(), D = window.shareImg;
				function A(F, E) {
					n(F, function(G, H) {
						B.load(function(I) {
							if (isImageRect(G)) {
								I()
							} else {
								var J = F[H] = new Image();
								if (x.crossOrigin) {
									J.crossOrigin = "*"
								}
								J.onload = function() {
									J.halfWidth = (J.naturalWidth || J.width) / 2 << 0;
									J.halfHeight = (J.naturalHeight || J.height) / 2 << 0;
									J.onload = null;
									if (!E && D && D.isSmall && J.halfWidth >= 150 && J.halfHeight >= 150) {
										D.isSmall = false;
										D.src = J.src
									}
									I()
								};
								J.onerror = function() {
									J.src = staticImgSrc("firstPage-404.jpg")
								};
								J.src = E ? E(G) : G
							}
						})
					})
				}

				A(z);
				A(w, staticImgSrc);
				y && A(y, staticImgSrc);
				B.start(function() {
					b(v, {
						resource : w,
						image : z
					});
					C()
				})
			}
		}
	};
	window.bindDataSource = function(u, v, t) {
		if (u.nodeName) {
			u.classList.add("layout-component-from-data");
			u.dataSource = {
				from : v,
				index : t
			}
		}
		return u
	};
	window.Icon = function(u, w, t, v) {
		var x = new Image();
		x.src = u;
		l(x, {
			width : l.px(w),
			display : "block",
			position : "absolute"
		});
		x.componentWidth = w;
		x.componentHeight = t;
		x.pos = function(z, A) {
			l(x, {
				x : l.px(z),
				y : l.px(A)
			})
		};
		v && v.appendChild(x);
		return x
	}
})();
(function() {
	window.d = function(f) {
		return f * globalScale << 0
	};
	function e(f, i, h, g) {
		return f.componentWidth * i - h.componentWidth * g
	}

	function b(f, i, h, g) {
		return f.componentHeight * i - h.componentHeight * g
	}

	function c(g, f) {
		return function(i, h) {
			return e(i, g, h, f)
		}
	}

	function a(g, f) {
		return function(i, h) {
			return b(i, g, h, f)
		}
	}
	window.position = {
		relativeX : e,
		relativeY : b,
		leftIn : c(0, 0),
		leftTo : c(0, 1),
		rightIn : c(1, 1),
		rightTo : c(1, 0),
		center : c(0.5, 0.5),
		topIn : a(0, 0),
		topTo : a(0, 1),
		bottomIn : a(1, 1),
		bottomTo : a(1, 0),
		middle : a(0.5, 0.5)
	}
})();
( function(i) {
		var a = zachModule["0"], m = a.loopArray, f = a.loopObj, c = a.insert, t = a.extend, r = a.tupleString, u = a.Event, q = zachModule["2"], j = q.css, n = j.px, l = j.s, g = q.bindEvent, e = q.element, p = q.removeNode, o = zachModule["4"], h = zachModule["6"], b = 0;
		function k() {
			var z = e("div.page.animation-prepare"), y = u(), B = u(), x = 0, w = [];
			z.x = z.y = 0;
			z.componentWidth = clientWidth;
			z.componentHeight = clientHeight;
			function s(E) {
				var D, C;
				if (E.transform === 0) {
					D = [j.translate(E.x, E.y, 0), j.scale(Math.max(E.scale, 0.01)), j.rotateZ(E.rotate)]
				} else {
					D = [j.translate(E.x, E.y, 0), j.matrix(E.transform), j.scale(Math.max(E.scale, 0.01)), j.rotateZ(E.rotate)];
					C = "left top 0"
				}
				return c({
					transform : D.join(" "),
					"transform-origin" : C,
					opacity : E.opacity
				}, E["z-index"] === undefined ? {} : {
					"z-index" : E["z-index"]
				})
			}

			function A(F, I, E, C) {
				var D = "animate" + b++, G = I.timing ? r("cubic-bezier", I.timing.arg) : "ease", H = "";
				f(I.progress, function(K, J) {
					H += K + "% {" + q.cssRuleString(s(a.exclude(t(componentAttr(F), J), ["z-index"]))) + "}"
				});
				q.insertCSSRules("@-webkit-keyframes " + D, H);
				return [D, G, l(I.duration), l(E || 0), C].join(" ")
			}

			function v(C) {
				return c(C, {
					component : function(G) {
						if (G.content === undefined) {
							G = {
								content : G
							}
						}
						var F = G.content, E = C.appendChild(F.element()), D = componentAttr(G);
						j(E, t(s(D), {
							position : "absolute",
							left : 0,
							top : 0,
							width : n(F.width),
							height : n(F.height)
						}));
						m(["x", "y", "opacity", "scale", "rotate", "z-index"], function(H) {
							Object.defineProperty(E, H, {
								get : function() {
									return D[H]
								},
								set : function(I) {
									D[H] = I;
									j(E, s(D))
								}
							})
						});
						return v(c(E, {
							componentWidth : F.width,
							componentHeight : F.height,
							transition : function(H) {
								var J = H.timing ? r("cubic-bezier", H.timing.arg) : "ease";
								j(E, "transition", [J, l(H.duration), l(H.delay || 0)].join(" "));
								j(E, j(s(t(D, H.end))));
								var I = g(E, "webkitTransitionEnd", function() {
									q.removeCss(E, "transition");
									H.onEnd && H.onEnd(E);
									I.remove()
								});
								return {
									remove : function() {
									}
								}
							},
							infiniteAnimate : function(H) {
								j(E, "animation", A(E, H, 0, "infinite"))
							},
							remove : function() {
								q.removeNode(E)
							}
						}))
					}
				})
			}
			return c(v(z), {
				registEnterAnimation : function(C) {
					m(C, function(E) {
						var D = 0;
						m(E, function(H) {
							var G = H.component, F = H.delay || 0;
							G.enterAnimation = A(G, H, x + F, "backwards");
							G.onEnter = H.onEnter;
							D = Math.max(H.duration + F, D);
							w.push(G)
						});
						x += D
					});
					m(w, function(E) {
						j(E, "animation", E.enterAnimation);
						if (E.onEnter) {
							var D = g(E, "webkitAnimationEnd", function() {
								E.onEnter(E);
								D.remove()
							})
						}
					})
				},
				start : function() {
					q.toggleState(z, "animation-prepare", "animation-run");
					y.trig()
				},
				onShow : y.regist,
				recycle : B.trig,
				onRemove : B.regist
			})
		}
		window.DOMSystem = function(w, v) {
			document.documentElement.classList.add("dom-mode");
			var z = window.body, B = null, x = window.curPageIndex, C, A = e("div.slide-arrow.switch-tips", document.body), y = false;
			i.LoadingNextPageTips(window.body);
			function D() {
				v.loadPage(window.curPageIndex + 1);
				v.loadPage(window.curPageIndex - 1)
			}

			function s(E) {
				var H, F = getIndex(E), G = w[F];
				if (G && G.isLoad) {
					H = G.special ? G.create() : G.create(k());
					window.curPageIndex = F;
					z.appendChild(H);
					if (F === pageNumber - 1) {
						y = true;
						document.body.classList.add("last-page")
					}
					i.trackPageView();
					B = H;
					return true
				}
			}
			v.loadPage(x, function() {
				v.startShow();
				s(x);
				B.start();
				var E = null;
				if (x === 0) {
					E = e("div#slide-tips.switch-tips", z);
					document.body.classList.add("first")
				}
				D();
				o.onDragV(z, function(F) {
					if (E) {
						p(E);
						E = null;
						document.body.classList.remove("first")
					}
					function G() {
						function H(K, J) {
							var L = B;
							document.body.classList.remove("show-copy-tips");
							document.body.classList.remove("last-page");
							A.classList.add("can-push");
							if (s(curPageIndex + K)) {
								i.lock(true);
								L && L.recycle();
								L.classList.add("cur-" + J);
								B.classList.add("new-" + J);
								var I = q.bindEvent(B, "webkitAnimationEnd", function() {
									D();
									I.remove();
									L.classList.remove("cur-" + J);
									B.classList.remove("new-" + J);
									p(L);
									B.start();
									i.lock(false)
								})
							}
						}
						document.body.classList.remove("loading-next-page");
						if (F.directionY) {
							if (!(curPageIndex === 0 && !y)) {
								H(-1, "down")
							}
						} else {
							H(1, "up")
						}
					}
					C && C.remove();
					C = null;
					if (F.directionY === true && curPageIndex === 0 && !y) {
					} else {
						if (!v.isPageLoad(curPageIndex + (F.directionY ? -1 : 1))) {
							document.body.classList.add("loading-next-page");
							C = v.loadPage(curPageIndex + (F.directionY ? -1 : 1), function() {
								G()
							})
						} else {
							G()
						}
					}
				})
			});
			i.jumpPage = function(E) {
				B.recycle();
				p(B);
				s(E);
				B.start()
			}
		};
		window.DOMPage = k
	}(window.fp));
(function() {
	var a = zachModule["6"], c = zachModule["7"];
	function b(e) {
		return e / 180 * Math.PI
	}

	registEnterAnimate({
		flyInto : {
			progress : function(g, h) {
				var f = g.x, e = g.y;
				switch(h) {
					case"left":
						f = -g.componentWidth;
						break;
					case"right":
						f = clientWidth;
						break;
					case"top":
						e = -g.componentHeight;
						break;
					case"bottom":
						e = clientHeight;
						break
				}
				return {
					"0" : {
						x : f,
						y : e
					}
				}
			}
		},
		emerge : {
			progress : function(f, h) {
				var e = 0, g = 0;
				switch(h) {
					case"left":
						e = -20;
						break;
					case"right":
						e = 20;
						break;
					case"top":
						g = -20;
						break;
					default:
						g = 20;
						break
				}
				return {
					"0" : {
						x : f.x + e,
						y : f.y + g,
						opacity : 0
					}
				}
			}
		},
		scale : {
			progress : function() {
				return {
					"0" : {
						scale : 0
					}
				}
			}
		},
		shrink : {
			duration : 0.6,
			timing : a.Bezier(0.52, 0.21, 0.8, 0.51),
			progress : function() {
				return {
					"0" : {
						scale : 5,
						opacity : 0
					}
				}
			}
		},
		fadeIn : {
			progress : function() {
				return {
					"0" : {
						opacity : 0
					}
				}
			}
		},
		circleRound : {
			progress : function() {
				return {
					"0" : {
						scale : 0,
						opacity : 0,
						rotate : Math.PI * 2.5
					}
				}
			},
			duration : 0.6
		},
		roundFromFarAndNear : {
			progress : function() {
				return {
					"0" : {
						scale : 0,
						opacity : 0,
						rotate : Math.PI * 0.65
					}
				}
			}
		},
		fallDownAndShake : {
			timing : a.Timing.easeOut,
			duration : 0.7,
			progress : function(f) {
				var e = f.rotate;
				return {
					"0" : {
						y : -f.componentHeight * 2,
						rotate : e + b(-15)
					},
					"40" : {
						rotate : e + b(-15)
					},
					"45" : {
						rotate : e + b(13)
					},
					"52" : {
						rotate : e + b(-8)
					},
					"62" : {
						rotate : e + b(5)
					},
					"74" : {
						rotate : e + b(-3)
					},
					"87" : {
						rotate : e + b(1)
					}
				}
			}
		}
	});
	registEnterAnimate({
		curveUp : {
			onAnimate : function(i, h, f) {
				var e = 100, g = (1 - h) * 3;
				i.scale = (1 - h) * 0.4 + 1;
				i.opacity = h;
				i.x = f.x + e * g * Math.cos(g);
				i.y = f.y + e * g * Math.sin(g)
			},
			duration : 1,
			fallback : enterAnimate.circleRound
		}
	});
	registEnterAnimate({
		flash : {
			timing : a.Timing.linear,
			duration : 1,
			progress : function() {
				return {
					"0 50 100" : {
						opacity : 1
					},
					"25 75" : {
						opacity : 0
					}
				}
			}
		},
		shake : {
			timing : a.Timing.linear,
			duration : 1,
			progress : function(e) {
				return {
					"10 30 50 70 90" : {
						x : e.x - 10
					},
					"20 40 60 80" : {
						x : e.x + 10
					}
				}
			}
		},
		swing : {
			duration : 1,
			progress : function(f) {
				function e(g) {
					return c.transformOrigin(c.matrix.rotate(f.rotate + b(g)), f.componentWidth / 2 << 0, 0)
				}
				return {
					"0 100" : {
						transform : e(0)
					},
					"20" : {
						transform : e(15)
					},
					"40" : {
						transform : e(-10)
					},
					"60" : {
						transform : e(5)
					},
					"80" : {
						transform : e(-5)
					}
				}
			}
		},
		tada : {
			timing : a.Timing.linear,
			duration : 1,
			progress : function(e) {
				return {
					"10 20" : {
						scale : 0.9,
						rotate : e.rotate + b(-3)
					},
					"30 50 70 90" : {
						scale : 1.1,
						rotate : e.rotate + b(3)
					},
					"40 60 80" : {
						scale : 1.1,
						rotate : e.rotate + b(-3)
					}
				}
			}
		},
		wobble : {
			timing : a.Timing.linear,
			duration : 0.8,
			progress : function(e) {
				var f = e.componentWidth;
				return {
					"15" : {
						x : e.x + f * -0.25,
						rotate : e.rotate + b(-5)
					},
					"30" : {
						x : e.x + f * 0.2,
						rotate : e.rotate + b(3)
					},
					"45" : {
						x : e.x + f * -0.15,
						rotate : e.rotate + b(-3)
					},
					"60" : {
						x : e.x + f * 0.1,
						rotate : e.rotate + b(2)
					},
					"75" : {
						x : e.x + f * -0.05,
						rotate : e.rotate + b(-1)
					}
				}
			}
		},
		bounceIn : {
			timing : a.Bezier(0.215, 0.61, 0.355, 1),
			duration : 0.75,
			progress : function() {
				return {
					"0" : {
						opacity : 0,
						scale : 0.3
					},
					"20" : {
						scale : 1.1
					},
					"40" : {
						scale : 0.9
					},
					"60" : {
						scale : 1.03
					},
					"80" : {
						scale : 0.97
					}
				}
			}
		},
		bounceFlying : {
			timing : a.Bezier(0.215, 0.61, 0.355, 1),
			duration : 0.75,
			progress : function(g, f) {
				var e = g.x, j = g.y, i = 0, h = 0;
				switch(f) {
					case"left":
						i = 1;
						break;
					case"right":
						i = -1;
						break;
					case"top":
						h = 1;
						break;
					case"bottom":
						h = -1;
						break
				}
				return {
					"0" : {
						x : -3000 * i,
						y : -3000 * h
					},
					"60" : {
						x : e + 25 * i,
						y : j + 25 * h
					},
					"75" : {
						x : e + -10 * i,
						y : j + -10 * h
					},
					"90" : {
						x : e + 5 * i,
						y : j + 5 * h
					}
				}
			}
		},
		rubberBand : {
			duration : 1,
			progress : function(e) {
				function f(g, h) {
					return c.transformOrigin(c.matrix.scale(g, h), e.componentWidth / 2 << 0, e.componentHeight / 2 << 0)
				}
				return {
					"0 100" : {
						transform : f(1, 1)
					},
					"30" : {
						transform : f(1.25, 0.75)
					},
					"40" : {
						transform : f(0.75, 1.25)
					},
					"50" : {
						transform : f(1.15, 0.85)
					},
					"65" : {
						transform : f(0.95, 1.05)
					},
					"75" : {
						transform : f(1.05, 0.95)
					}
				}
			}
		}
	})
})();
(function() {
	var e = zachModule["2"], f = e.element, h = e.css, l = h.px, g = zachModule["0"], j = g.loopArray, a = zachModule["4"], b = zachModule["6"], k = zachModule["3"], c = zachModule["9"], i = zachModule["5"];
	window.Content = window.Content || {};
	window.Component = window.Component || {};
	Content.Custom = function(n, o, m) {
		if (isImageRect(n)) {
			return Content.Rect({
				color : n,
				width : o,
				height : m
			})
		} else {
			return Content.Image(n, o, m)
		}
	};
	Content.Image = function(o, p, n) {
		var q, m;
		if (n === undefined) {
			q = o.halfWidth;
			m = o.halfHeight
		} else {
			q = p;
			m = n
		}
		if (p !== undefined && n === undefined) {
			q *= p;
			m *= p
		}
		h(o, {
			position : "absolute",
			width : l(q),
			height : l(m),
			left : 0,
			top : 0
		});
		return {
			width : q,
			height : m,
			element : function() {
				return o.hasChild || (ua.ios && !ua.win32) || (ua.safari && !ua.android) ? f("div", [o.cloneNode(true)]) : o.cloneNode(true)
			},
			draw : function(r) {
				r.drawImage(o, 0, 0, q, m)
			}
		}
	};
	Content.ImageCover = function(n, o, m) {
		var p = c.layImageByFrame(n, {
			width : o,
			height : m,
			size : c.Size.cover,
			align : [0.5, 0.5]
		});
		return {
			width : o,
			height : m,
			element : function() {
				return f("div", {
					css : {
						overflow : "hidden"
					},
					children : h(n.cloneNode(false), i.getImageCoverStyle(n, o, m))
				})
			},
			draw : function(q) {
				c.drawImageLayout(q, p)
			}
		}
	};
	Content.Border = function(r, p) {
		var o = p.width || 0, s = p.color || "transparent", n = p.radius || 0, q = r.width, m = r.height;
		return {
			width : q + o,
			height : m + o,
			element : function() {
				return h(r.element(), {
					overflow : "hidden",
					"box-sizing" : "border-box",
					border : ["solid", l(o), s].join(" "),
					"border-radius" : l(n)
				})
			},
			draw : function(t) {
				t.save();
				if (n) {
					t.beginPath();
					t.moveTo(n, 0);
					t.lineTo(q - n, 0);
					t.arcTo(q, 0, q, n, n);
					t.lineTo(q, m - n);
					t.arcTo(q, m, q - n, m, n);
					t.lineTo(n, m);
					t.arcTo(0, m, 0, m - n, n);
					t.lineTo(0, n);
					t.arcTo(0, 0, n, 0, n);
					t.clip()
				}
				t.save();
				t.translate(o, o);
				r.draw(t);
				t.restore();
				if (o) {
					t.fillStyle = s;
					t.fillRect(0, 0, q, o);
					t.fillRect(0, 0, o, m);
					t.fillRect(q, 0, o, m + o);
					t.fillRect(0, m, q + o, o)
				}
				t.restore()
			}
		}
	};
	Content.FrameImage = function(w) {
		var o = w.img, m = w.frame, u = w.frameWidth << 0, n = w.frameHeight << 0, r = w.imgX << 0, q = w.imgY << 0, t = w.imgWidth + 1 << 0, s = w.imgHeight + 1 << 0, p = c.layImageByFrame(o, {
			width : t,
			height : s,
			size : c.Size.cover,
			align : [0.5, 0.5]
		});
		function v(x) {
			x.save();
			x.translate(r, q);
			c.drawImageLayout(x, p);
			x.restore();
			x.drawImage(m, 0, 0, u, n)
		}
		return {
			width : u,
			height : n,
			element : function() {
				return f("div", {
					css : {
						overflow : "hidden"
					},
					children : [f("div", {
						css : {
							position : "absolute",
							overflow : "hidden",
							left : h.px(r),
							top : h.px(q),
							width : h.px(t),
							height : h.px(s)
						},
						children : h(o.cloneNode(false), i.getImageCoverStyle(o, t, s))
					}), h(m.cloneNode(false), {
						position : "absolute",
						left : 0,
						right : 0,
						top : 0,
						bottom : 0,
						width : h.px(u),
						height : h.px(n),
						"z-index" : 1
					})]
				})
			},
			draw : v
		}
	};
	Component.BackgroundImage = function(n, m, o) {
		return n.component({
			content : Content.ImageCover(m, clientWidth, clientHeight),
			x : 0,
			y : 0,
			"z-index" : o || 0
		})
	};
	Component.MultiImageArea = function(A) {
		var v = A.page, x = A.parent, u = A.contents.length, o = x.componentWidth, y = x.componentHeight, t = [], m = [], q = k.range(3 / u, 0.08, 0.6), r = k.range(1.5 / u, 0.04, 0.3), n = A.sign * Math.min(25 / u * Math.PI / 180, 4 * Math.PI / 180), w = A.icon, z;
		bindDataSource(x, "multi-image");
		j(A.contents, function(B, C) {
			B["z-index"] = 10000 + C;
			B.rotate = (C + 1 - u) * n;
			m.push({
				component : B,
				duration : 0.6,
				delay : C * 0.3,
				progress : {
					"0" : {
						rotate : -Math.PI / 6,
						scale : !ua.ios && !ua.iphone6 ? 1 : 3,
						x : -o * 2.4,
						y : y * 2.4
					}
				}
			});
			t.push(B)
		});
		g.request(function(B) {
			A.noAnimation === true ? v.onShow(B) : m.top.onEnter = B
		}, function() {
			if (w) {
				h(w.prev, "opacity", 1);
				h(w.next, "opacity", 1)
			}
			var C = u - 1, F = null;
			function B() {
				if (F) {
					j(F, function(G) {
						G.remove()
					});
					F = null
				}
			}

			function E(G) {
				B();
				F = [];
				g.loop(u, function(I) {
					var H = t[((G + I) % u + u) % u];
					F[I] = H.transition({
						end : {
							rotate : (I + 1 - u) * n
						},
						timing : b.Timing.easeOut,
						delay : r * I / 2,
						duration : q / 2
					})
				})
			}

			function D(H) {
				if (!window.highPerformance) {
					fp.lock(true)
				}
				B();
				var G = C, I = t[(G % u + u) % u];
				I.transition({
					end : {
						x : ( H ? clientWidth : -o) - x.x,
						y : 0,
						opacity : 0
					},
					duration : 0.3,
					onEnd : function() {
						I.x = 0;
						I.opacity = 1;
						I["z-index"] -= u;
						I.rotate = (1 - u) * n;
						E(G);
						fp.lock(false)
					}
				});
				--C
			}
			a.onDragH(x, function(G) {
				D(G.directionX)
			});
			if (A.auto) {
				z = setTimeout(function() {
					D(Math.random() > 0.5);
					z = setTimeout(arguments.callee, 3000)
				}, 1500)
			}
		});
		v.onRemove(function() {
			z && clearTimeout(z)
		});
		if (w) {
			var p = w.prev, s = w.next;
			j([p, s], function(B) {
				h(B, {
					"z-index" : "10000",
					opacity : 0,
					"-webkit-transition" : "0.8s"
				})
			});
			h(p, {
				left : h.px(position.leftIn(v, p) + 15),
				top : h.px(position.middle(x, p) + x.y),
				"-webkit-animation" : "guidePrev 1.5s infinite"
			});
			h(s, {
				left : h.px(position.rightIn(v, s) - 15),
				top : h.px(position.middle(x, s) + x.y),
				"-webkit-animation" : "guideNext 1.5s infinite"
			});
			v.onShow(function() {
				window.body.appendChild(p);
				window.body.appendChild(s)
			});
			v.onRemove(function() {
				e.removeNode(p);
				e.removeNode(s)
			})
		}
		return {
			enterAnimation : m
		}
	}
})();
(function() {
	var c = zachModule["2"], a = zachModule["10"], b = zachModule["0"];
	registPageEffect("flake", {
		resource : ["firstpage-flake.png"],
		create : function(f, e) {
			var h = null, g = null;
			f.onShow(function() {
				h = a.Layer();
				var i = [], j = 20;
				if (ua.iphone4) {
					j = 25
				} else {
					if (ua.iphone5) {
						j = 30
					} else {
						if (ua.iphone6 && ua.win32) {
							j = 40
						}
					}
				}
				c.css(h, {
					position : "absolute",
					left : 0,
					right : 0,
					top : 0,
					bottom : 0,
					"z-index" : 100,
					"pointer-events" : "none"
				});
				h.resize(clientWidth, clientHeight);
				function k() {
					return {
						x : Math.random() * clientWidth << 0,
						y : (Math.random() - 1) * clientHeight << 0,
						omega : Math.random() * Math.PI,
						size : (Math.random() * 8 + 10) << 0,
						speed : Math.random() + 1,
						a : Math.random() * 10 + 2
					}
				}
				b.loop(j, function() {
					i.push(k())
				});
				window.body.appendChild(h);
				g = c.requestAnimate(function() {
					h.draw(function(l) {
						b.loopArray(i, function(n, p) {
							var q = n.y += n.speed, m = n.x + Math.sin(n.y * 0.02 + n.omega) * n.a, o = n.size;
							l.drawImage(e[0], m, q, o, o);
							if (n.y >= clientHeight) {
								n = k();
								n.y = -20;
								i[p] = n
							}
						})
					})
				})
			});
			f.onRemove(function() {
				g.remove();
				c.removeNode(h)
			})
		}
	})
})();
(function() {
	var e = zachModule["2"], c = e.element, b = e.css, a = b.px;
	window.Content = window.Content || {};
	Content.Rect = function(g) {
		var f = g.color || "";
		return {
			width : g.width,
			height : g.height,
			element : function() {
				return c("div", {
					css : {
						background : f
					}
				})
			},
			draw : function(h) {
				if (f) {
					h.fillStyle = f;
					h.fillRect(0, 0, g.width, g.height)
				}
			}
		}
	};
	Content.Circle = function(g) {
		var f = g.r;
		return {
			width : f * 2,
			height : f * 2,
			element : function() {
				return c("div", {
					css : {
						"border-radius" : a(f),
						background : g.color
					}
				})
			},
			draw : function(h) {
				h.save();
				h.beginPath();
				h.arc(f, f, f, 0, 2 * Math.PI);
				h.closePath();
				h.fillStyle = g.color;
				h.fill();
				h.restore()
			}
		}
	}
})();
(function() {
	var e = zachModule["2"], f = e.element, h = e.css, k = h.px, g = zachModule["0"], l = g.insert, j = g.loopArray, b = zachModule["13"], i = b.Font;
	window.Content = window.Content || {};
	function a(n) {
		var m = {};
		g.loopObj(n, function(o, p) {
			switch(o) {
				case"fontSize":
					m["font-size"] = k(p);
					break;
				case"lineHeight":
					m["line-height"] = k(p);
					break;
				case"fontWeight":
					m["font-weight"] = p;
					break;
				case"fontStyle":
					m["font-style"] = p;
					break;
				case"color":
					m.color = p;
					break
			}
		});
		return m
	}

	function c(m) {
		var n;
		m = m.cloneNode(true);
		document.body.appendChild(m);
		h(m, {
			position : "absolute"
		});
		n = {
			width : m.offsetWidth,
			height : m.offsetHeight
		};
		e.removeNode(m);
		return n
	}
	Content.Label = function(n) {
		var o = n.text, m = f("span", {
			css : l(a(n), {
				display : "inline-block",
				"white-space" : "nowrap"
			}),
			innerHTML : o
		});
		return {
			width : window.highPerformance ? b.measureText(o, n).width : c(m).width,
			height : n.lineHeight,
			element : function() {
				return m.cloneNode(true)
			},
			draw : function(p) {
				p.font = i(n);
				p.textBaseline = "middle";
				p.fillStyle = n.color;
				p.fillText(o, 0, n.lineHeight / 2 << 0)
			}
		}
	};
	Content.LineText = function(o) {
		var p = o.text, m = o.width, n;
		return {
			width : m,
			height : o.lineHeight,
			element : function() {
				return f("span", {
					css : l(a(o), {
						"text-align" : o.isLeft ? "left" : "center",
						width : k(o.width),
						"white-space" : "nowrap"
					}, o.overflow ? {
						overflow : "hidden",
						"white-space" : "nowrap",
						"text-overflow" : "ellipsis"
					} : {}),
					innerHTML : p
				})
			},
			draw : function(s) {
				s.font = i(o);
				s.textBaseline = "middle";
				s.fillStyle = o.color;
				function q(t) {
					return s.measureText(t).width
				}

				if (n === undefined) {
					if (o.overflow && q(p) > m) {
						for (var r = 0; r !== p.length; ++r) {
							if (q(p.substring(0, r + 1) + "…") > m) {
								break
							}
						}
						n = p.substring(0, r) + "…"
					} else {
						n = p
					}
				}
				s.fillText(n, o.isLeft ? 0 : center(m, q(n)), o.lineHeight / 2 << 0)
			}
		}
	};
	Content.BlockText = function(o) {
		var p = o.text, n, m;
		if (window.highPerformance) {
			m = b.layText(p, o.width, l(o, {
				lineBreak : o.breakWord ? b.LineBreak.breakAll : b.LineBreak.normal,
				align : o.breakWord ? b.Align.left : b.Align.side
			}))
		} else {
			n = f("div", {
				css : l(a(o), {
					width : k(o.width)
				})
			});
			j(p.split("\n"), function(q) {
				f("p", {
					innerHTML : q || "&nbsp",
					css : l({
						margin : k(o.margin * 2) + " 0"
					}, o.breakWord ? {
						"word-break" : "break-all",
						"word-wrap" : "break-word"
					} : {})
				}, n)
			})
		}
		return {
			width : o.width,
			height : window.highPerformance ? m.height : c(n).height,
			element : function() {
				return n.cloneNode(true)
			},
			draw : function(q) {
				b.drawTextLayout(q, m)
			}
		}
	}
})();
(function() {
	var a = zachModule["0"], c = zachModule["5"], b = zachModule["4"];
	registLayout("contact", {
		resource : ["layout-contact-background.png", "layout-context-text-frame.png"],
		create : function(i, f) {
			var k = f.resource[1], n = k.halfWidth, m = k.halfHeight;
			Component.BackgroundImage(i, f.image[0]);
			i.component({
				content : Content.ImageCover(f.resource[0], clientWidth, clientHeight),
				x : 0,
				y : 0,
				"z-index" : 1
			});
			var h = [];
			a.loopArray([{
				caption : "联系电话",
				click : function(o) {
					location.href = "tel:" + o
				}
			}, {
				caption : "联系邮箱",
				click : function(o) {
					location.href = "mailto:" + o
				}
			}, {
				caption : "官方网站",
				click : function(o) {
					fp.jump(o)
				}
			}, {
				caption : "微信号"
			}, {
				caption : "微博",
				click : function(o) {
					fp.jump("http://weibo.com/n/" + o)
				}
			}], function(q, s) {
				if (f.text[s] === "") {
					return
				}
				var r = 14, v = 8, o = Content.Label({
					text : q.caption + "：",
					lineHeight : 44,
					fontSize : 14,
					color : "#FFFFFF"
				}), t = o.width, w = f.text[s], u = Content.BlockText({
					text : f.text[s],
					lineHeight : 16,
					fontSize : 12,
					color : "#FFFFFF",
					margin : 0,
					width : n - 2 * r - v - t,
					breakWord : true
				});
				f.resource[1].hasChild = true;
				var p = i.component({
					content : Content.Image(f.resource[1]),
					x : (clientWidth - n) / 2 << 0,
					"z-index" : 2
				});
				p.component({
					content : o,
					x : r,
					y : 0
				});
				p.component({
					content : u,
					x : r + v + t,
					y : center(44, u.height)
				});
				b.onTap(p, function() {
					window.preventJump = true;
					q.click && q.click(w)
				});
				h.push(p)
			});
			var g = middleY(143), j = 315, l = h.length, e = (j - m * l) / (l + 1) << 0;
			a.loopArray(h, function(p, o) {
				p.y = g + e * (o + 1) + m * o
			})
		}
	})
})();
(function() {
	var a = zachModule["0"], e = zachModule["2"], c = zachModule["4"], b = false;
	registSpecialPage("copyright", function(f) {
		e.ajax({
			url : window.copyrightUrl,
			onLoad : function(i) {
				var h = e.element("div", i), g = h.querySelector("#content");
				a.loopArray(h.querySelectorAll("style"), function(j) {
					document.head.appendChild(j)
				});
				f({
					create : function(j) {
						j.innerHTML = g.outerHTML;
						a.loopArray(j.querySelectorAll("a"), function(k) {
							c.onPointerDown(k, function(l) {
								l.preventDefault()
							});
							c.onTap(k, function() {
								location.href = k.href
							})
						})
					}
				})
			}
		})
	});
	registLayout("copyright", {
		resource : ["layout-copyright-background.png"],
		create : function(o, k) {
			var i = k.author, f = k.resource[0], q = Content.Label({
				text : i,
				lineHeight : 16,
				fontSize : 16,
				fontStyle : "italic",
				color : "#fc5e28"
			}), h = q.width, g = Content.Label({
				text : "作品",
				lineHeight : 16,
				fontSize : 16,
				fontStyle : "italic",
				color : "#A3AEC1"
			}), m = g.width, p = 20, l = h + p + m;
			o.component({
				content : Content.Image(f),
				x : center(clientWidth, f.halfWidth),
				y : center(clientHeight, f.halfHeight) + 15,
				"z-index" : 1
			});
			o.component({
				content : Content.ImageCover(k.image[0], 56, 56),
				x : middleX(136),
				y : middleY(81)
			});
			var n = o.component({
				content : Content.Rect({
					width : l,
					height : 16
				}),
				x : center(clientWidth, l),
				y : middleY(154),
				"z-index" : 2
			});
			n.component({
				content : q,
				x : 0,
				y : 0
			});
			n.component({
				content : g,
				x : h + p,
				y : 0
			});
			o.component({
				content : Content.LineText({
					text : k.title,
					width : 241,
					lineHeight : 14,
					fontSize : 12,
					color : "#A3AEC1",
					isLeft : true
				}),
				x : middleX(40),
				y : middleY(203),
				"z-index" : 2
			});
			var j = [40, 130, 220];
			a.loopArray(k.works, function(s, t) {
				if (t > 2) {
					return
				}
				var r = o.component({
					content : Content.Rect({
						width : 60,
						height : 83
					}),
					x : middleX(j[t]),
					y : middleY(233),
					"z-index" : 2
				});
				r.component({
					content : Content.ImageCover(k.image[t + 1], 60, 60),
					x : 0,
					y : 0,
					"z-index" : 2
				});
				r.component({
					content : Content.LineText({
						text : s.title,
						width : 80,
						lineHeight : 14,
						fontSize : 10,
						color : "#A3AEC1",
						overflow : true,
						isLeft : true
					}),
					x : 0,
					y : 69,
					"z-index" : 2
				});
				c.onTap(r, function() {
					window.preventJump = true;
					location.href = s.url
				})
			});
			c.onTap(o.component({
				content : Content.Rect({
					width : 140,
					height : 40
				}),
				x : center(clientWidth, 140),
				y : middleY(343),
				"z-index" : 2
			}), fp.downloadFirstPage);
			c.onTap(o.component({
				content : Content.Rect({
					width : 150,
					height : 40
				}),
				x : center(clientWidth, 150),
				y : middleY(418),
				"z-index" : 2
			}), function() {
				location.href = "http://chuye.cloud7.com.cn"
			});
			if (!b) {
				window.AnalyticsDownload && window.AnalyticsDownload({
					title : "下载页",
					url : "http://chuye.cloud7.com.cn" + virtualPath + "/download/" + fp.getWorkInfo().ContentID
				});
				b = true
			}
		}
	})
})();
(function() {
	var a = zachModule["0"], c = zachModule["3"], b = {
		"fly-into-left" : {
			func : enterAnimate.flyInto,
			arg : ["left"]
		},
		"fly-into-top" : {
			func : enterAnimate.flyInto,
			arg : ["top"]
		},
		"fly-into-right" : {
			func : enterAnimate.flyInto,
			arg : ["right"]
		},
		"fly-into-bottom" : {
			func : enterAnimate.flyInto,
			arg : ["bottom"]
		},
		"emerge-left" : {
			func : enterAnimate.emerge,
			arg : ["left"]
		},
		"emerge-top" : {
			func : enterAnimate.emerge,
			arg : ["top"]
		},
		"emerge-right" : {
			func : enterAnimate.emerge,
			arg : ["right"]
		},
		"emerge-bottom" : {
			func : enterAnimate.emerge,
			arg : ["bottom"]
		},
		scale : enterAnimate.scale,
		"fade-in" : enterAnimate.fadeIn,
		"circle-round" : enterAnimate.circleRound,
		"round-from-far-and-near" : enterAnimate.roundFromFarAndNear,
		"curve-up" : enterAnimate.curveUp,
		"fall-down-and-shake" : enterAnimate.fallDownAndShake,
		shrink : enterAnimate.shrink,
		flash : enterAnimate.flash,
		shake : enterAnimate.shake,
		wobble : enterAnimate.wobble,
		tada : enterAnimate.tada,
		"bounce-in" : enterAnimate.bounceIn,
		"bounce-in-down" : {
			func : enterAnimate.bounceFlying,
			arg : ["bottom"]
		},
		"bounce-in-up" : {
			func : enterAnimate.bounceFlying,
			arg : ["top"]
		},
		"bounce-in-left" : {
			func : enterAnimate.bounceFlying,
			arg : ["left"]
		},
		"bounce-in-right" : {
			func : enterAnimate.bounceFlying,
			arg : ["right"]
		},
		swing : enterAnimate.swing,
		"rubber-band" : enterAnimate.rubberBand
	};
	registLayout("custom", {
		create : function(i, h) {
			var g = [], f = h.imageinfo;
			a.loopArray(f, function(q, o) {
				var m = h.image[o], l;
				function n(r) {
					return q.maskRadius ? Content.Border(r, {
						radius : q.maskRadius
					}) : r
				}

				if (q !== null) {
					var p = q.width, k = m.halfWidth ? p / m.halfWidth * m.halfHeight : q.height;
					l = i.component({
						content : n(Content.Custom(m, p * globalScale, k * globalScale)),
						x : c.range(middleX(q.x, globalScale), 0, clientWidth - p * globalScale),
						y : middleY(q.y, globalScale),
						rotate : q.rotate || 0
					})
				} else {
					l = i.component({
						content : (isImageRect(m) ? Content.Custom : Content.ImageCover)(m, clientWidth, clientHeight)
					})
				}
				l["z-index"] = o;
				bindDataSource(g[o] = l, "image", o)
			});
			var e = 0, j = [];
			a.loopArray(f, function(n, k) {
				if (n && n.animation) {
					var m = (b[n.animation] || b["fly-into-left"]), l = m.func || m, o = a.insert(l.apply(null, [g[k]].concat(m.arg || [])), {
						delay : n["animation-delay"],
						duration : n["animation-duration"]
					});
					if (o.delay === undefined || o.delay === null) {
						o.delay = e
					}
					e = o.delay + o.duration;
					j.push(o)
				}
			});
			i.registEnterAnimation([j])
		}
	})
})();
(function() {
	var a = zachModule["0"], c = zachModule["5"];
	var b = c.KeyValueFunction(function(e, f) {
		registLayout(e, {
			resource : [f.frame],
			create : function(i, h) {
				var g = [];
				a.loopArray(h.image, function(j, l) {
					var n = f.img[l], m = bindDataSource(i.component({
						content : Content.ImageCover(j, Math.ceil(n.width * xRatio) + 1, Math.ceil(n.height * yRatio) + 1),
						x : n.x * xRatio << 0,
						y : n.y * yRatio << 0
					}), "image", l), k = n.enterAnimate;
					g.push([enterAnimate[k.name].apply(null, [m].concat([k.arg]))])
				});
				i.component({
					content : Content.Image(h.resource[0], clientWidth, clientHeight),
					x : 0,
					y : 0,
					"z-index" : 100
				});
				i.registEnterAnimation(g)
			}
		})
	});
	b({
		MutipleImage02 : {
			frame : "layout-MutipleImage02-frame.png",
			img : [{
				x : 25,
				y : 16,
				width : 280,
				height : 157,
				enterAnimate : {
					name : "flyInto",
					arg : "left"
				}
			}, {
				x : 25,
				y : 173,
				width : 280,
				height : 157,
				enterAnimate : {
					name : "flyInto",
					arg : "right"
				}
			}, {
				x : 25,
				y : 330,
				width : 280,
				height : 157,
				enterAnimate : {
					name : "flyInto",
					arg : "left"
				}
			}]
		},
		MutipleImage03 : {
			frame : "layout-MutipleImage03-frame.png",
			img : [{
				x : 15,
				y : 15,
				width : 290,
				height : 231,
				enterAnimate : {
					name : "flyInto",
					arg : "top"
				}
			}, {
				x : 15,
				y : 250,
				width : 143,
				height : 239,
				enterAnimate : {
					name : "flyInto",
					arg : "left"
				}
			}, {
				x : 162,
				y : 250,
				width : 143,
				height : 239,
				enterAnimate : {
					name : "flyInto",
					arg : "right"
				}
			}]
		}
	})
})();
(function() {
	var b = zachModule["0"], c = b.TupleString("rgba"), i = zachModule["2"], a = zachModule["10"];
	function g(k, j, l) {
		return {
			content : Content.Image(k),
			x : middleX(j),
			y : middleY(l),
			"z-index" : 5
		}
	}

	function f(j) {
		return {
			create : function(n, m) {
				var t = [27, 16, 10], l = [22, 57, 88], o = 115 * yRatio << 0, q, p, s;
				switch(j) {
					case"top":
						q = 0;
						p = o;
						s = clientHeight;
						break;
					case"middle":
						q = clientHeight * 0.6 << 0;
						p = 0;
						s = clientHeight;
						break;
					case"bottom":
						p = 0;
						q = s = clientHeight - o;
						break
				}
				var k = [], r = color.background || "#FFFFFF";
				b.loopArray(m.text, function(w, v) {
					var u = bindDataSource(n.component({
						content : Content.LineText({
							text : w,
							lineHeight : t[v],
							fontSize : t[v],
							color : r.toUpperCase() === "#FFFFFF" ? "#000000" : "#FFFFFF",
							width : clientWidth
						}),
						x : 0,
						y : q + l[v] * yRatio << 0,
						"z-index" : 2
					}), "text", v);
					if (w) {
						k.push([enterAnimate.emerge(u)])
					}
				});
				bindDataSource(n.component({
					content : Content.ImageCover(m.image[0], clientWidth, s - p),
					x : 0,
					y : p
				}), "image", 0);
				n.component({
					content : Content.Rect({
						color : r,
						width : clientWidth,
						height : o
					}),
					x : 0,
					y : q,
					"z-index" : 1
				});
				n.registEnterAnimation(k)
			}
		}
	}

	registLayout("ImageText01", f("top"));
	registLayout("ImageText02", f("bottom"));
	registLayout("ImageText03", f("middle"));
	registLayout("SingleImage", {
		create : function(k, j) {
			bindDataSource(Component.BackgroundImage(k, j.image[0]), "image", 0)
		}
	});
	function e(j) {
		var k = j.padding;
		return {
			create : function(m, l) {
				var n = Content.BlockText({
					text : l.text[0],
					margin : j.margin,
					lineHeight : j.lineHeight,
					fontSize : j.fontSize,
					color : j.color,
					width : clientWidth - 2 * k
				});
				bindDataSource(Component.BackgroundImage(m, l.image[0]), "image", 0);
				m.component({
					content : Content.Rect({
						width : clientWidth,
						height : clientHeight,
						color : j.background
					}),
					x : 0,
					y : 0,
					"z-index" : 1
				});
				m.registEnterAnimation([[enterAnimate.emerge(bindDataSource(m.component({
					content : n,
					"z-index" : 2,
					x : k,
					y : center(clientHeight, n.height)
				}), "text", 0))]])
			}
		}
	}

	registLayout("ImageText04", e({
		margin : 5,
		lineHeight : 25,
		fontSize : 15,
		color : "#FFFFFF",
		background : c(0, 0, 0, 0.8),
		padding : 20
	}));
	registLayout("ImageText07", e({
		margin : 5,
		lineHeight : 25,
		fontSize : 14,
		color : "#333333",
		background : c(255, 255, 255, 0.85),
		padding : 20
	}));
	registLayout("ImageText05", {
		create : function(o, n) {
			var p = 17, m = 191, l = Content.BlockText({
				text : n.text[0],
				width : m - 2 * p,
				lineHeight : 30,
				fontSize : 22,
				color : "#FFFFFF",
				breakWord : true
			}), j = Math.max(l.height + 20, 60);
			bindDataSource(Component.BackgroundImage(o, n.image[0]), "image", 0);
			var k = o.component({
				content : Content.Rect({
					width : m,
					height : j,
					color : c(0, 0, 0, 0.85)
				}),
				x : clientWidth - m,
				y : center(clientHeight, j),
				"z-index" : 1
			});
			bindDataSource(k.component({
				content : l,
				x : p,
				y : center(j, l.height)
			}), "text", 0)
		}
	});
	registLayout("ImageText06", {
		create : function(n, m) {
			var p = 17, l = 250, o = 350;
			bindDataSource(Component.BackgroundImage(n, m.image[0]), "image", 0);
			var j = n.component({
				content : Content.Rect({
					width : l,
					height : o,
					color : c(0, 0, 0, 0.85)
				}),
				x : center(clientWidth, l),
				y : center(clientHeight, o),
				"z-index" : 1
			});
			function k(q, s) {
				var r = Content.BlockText({
					text : m.text[q],
					width : l - 2 * p,
					lineHeight : 25,
					fontSize : 14,
					color : "#FFFFFF",
					breakWord : true
				});
				return b.extend(enterAnimate.flyInto(bindDataSource(j.component({
					content : r,
					x : p,
					y : s + center(97, r.height)
				}), "text", q), "right"), {
					delay : 0.3 * q
				})
			}
			n.registEnterAnimation([[k(0, 35), k(1, 132), k(2, 229)]])
		}
	});
	registLayout("ImageText08", {
		create : function(l, k) {
			bindDataSource(Component.BackgroundImage(l, k.image[0]), "image", 0);
			var j = Content.Image(k.image[1], globalScale);
			l.registEnterAnimation([[enterAnimate.emerge(bindDataSource(l.component({
				content : j,
				x : clientWidth - j.width,
				y : middleY(354, globalScale),
				"z-index" : 5,
				duration : 1
			}), "image", 1))]])
		}
	});
	registLayout("ImageText09", {
		create : function(l, k) {
			var j = Content.Image(k.image[1], globalScale);
			bindDataSource(Component.BackgroundImage(l, k.image[0]), "image", 0);
			l.registEnterAnimation([[enterAnimate.emerge(bindDataSource(l.component({
				content : j,
				x : center(clientWidth, j.width),
				y : middleY(289, globalScale),
				"z-index" : 5,
				duration : 1
			}), "image", 1))]])
		}
	});
	registLayout("ImageText10", {
		create : function(k, j) {
			bindDataSource(Component.BackgroundImage(k, j.image[0]), "image", 0);
			k.registEnterAnimation([[enterAnimate.emerge(bindDataSource(k.component({
				content : Content.Image(j.image[1], globalScale),
				x : 25,
				y : middleY(155, globalScale),
				"z-index" : 5,
				duration : 1
			}), "image", 1))]])
		}
	});
	registLayout("ImageText11", {
		create : function(m, l) {
			var k = Content.Image(l.image[1], globalScale), j = Content.Image(l.image[2], globalScale);
			bindDataSource(Component.BackgroundImage(m, l.image[0]), "image", 0);
			m.registEnterAnimation([[enterAnimate.emerge(bindDataSource(m.component({
				content : k,
				x : center(clientWidth, k.width),
				y : middleY(189, globalScale),
				"z-index" : 5,
				duration : 1
			}), "image", 1))], [enterAnimate.emerge(bindDataSource(m.component({
				content : j,
				x : center(clientWidth, j.width),
				y : middleY(269, globalScale),
				"z-index" : 5,
				duration : 1
			}), "image", 2))]])
		}
	});
	registLayout("ImageText12", {
		resource : ["layout-ImageText12-mayun.jpg", "layout-ImageText12-mask.png"],
		create : function(o, n) {
			var m = n.image[1], j = 818 / 1008 * clientHeight, l = 400 / 1008 * clientHeight;
			o.component({
				content : Content.ImageCover(n.resource[0], clientWidth / 2, j),
				x : 0,
				y : 0
			});
			bindDataSource(o.component({
				content : Content.ImageCover(n.image[0], clientWidth / 2, j),
				x : clientWidth / 2,
				y : 0
			}), "image", 0);
			var k = o.component({
				content : Content.ImageCover(n.resource[1], clientWidth, l),
				x : 0,
				y : clientHeight - l,
				"z-index" : 5
			});
			o.registEnterAnimation([[enterAnimate.emerge(bindDataSource(k.component({
				content : Content.Image(m),
				x : (clientWidth - m.halfWidth) / 2 << 0,
				y : 75,
				duration : 1
			}), "image", 1))]])
		}
	});
	registLayout("ImageText13", {
		create : function(p, o) {
			var l = 248 / 2 * yRatio, j = clientHeight - l, m = o.image[1], k = m.halfWidth * yRatio << 0;
			bindDataSource(Component.BackgroundImage(p, o.image[0]), "image", 0);
			var n = p.component({
				content : Content.Rect({
					color : "#FFFFFF",
					width : clientWidth,
					height : l
				}),
				x : 0,
				y : j
			});
			p.registEnterAnimation([[enterAnimate.fadeIn(bindDataSource(n.component({
				content : Content.Image(m, k, m.halfHeight * yRatio << 0),
				x : center(clientWidth, k),
				y : (766 - (1008 - 248)) / 2 * yRatio << 0,
				"z-index" : 5,
				duration : 1
			}), "image", 1))]])
		}
	});
	registLayout("ImageText14", {
		create : function(l, k) {
			var j = Content.Image(k.image[1], globalScale);
			bindDataSource(Component.BackgroundImage(l, k.image[0]), "image", 0);
			l.registEnterAnimation([[enterAnimate.emerge(bindDataSource(l.component({
				content : j,
				x : clientWidth - 14 - j.width,
				y : middleY(78),
				"z-index" : 5,
				duration : 1
			}), "image", 1))]])
		}
	});
	registLayout("ImageText15", {
		create : function(r, p) {
			var n = p.image[1], l = p.image[2], v = 40, w = 23, k = 15, u = n.halfHeight + l.halfHeight + k, m = Math.max(n.halfWidth, 246) + w * 2, j = (clientWidth - m) / 2, q = u + v * 2, t = (clientHeight - q) / 2;
			bindDataSource(Component.BackgroundImage(r, p.image[0]), "image", 0);
			var s = r.component({
				content : Content.Rect({
					width : m,
					height : q,
					color : "rgba(255,255,255,0.9)"
				}),
				x : j,
				y : t
			});
			function o(B, z, C, A) {
				return enterAnimate.emerge(bindDataSource(s.component({
					content : Content.Image(B),
					x : z << 0,
					y : C << 0,
					"z-index" : 5,
					duration : 1
				}), "image", A))
			}
			r.registEnterAnimation([[o(n, w, v, 1)], [o(l, m - w - l.halfWidth, v + n.halfHeight + k, 2)]])
		}
	});
	registLayout("ImageText16", {
		create : function(m, l) {
			var k = l.image[1], j = l.image[2];
			bindDataSource(Component.BackgroundImage(m, l.image[0]), "image", 0);
			m.registEnterAnimation([[enterAnimate.fadeIn(bindDataSource(m.component(g(k, 324 / 2, 114 / 2)), "image", 1))], [enterAnimate.fadeIn(bindDataSource(m.component(g(j, 330 / 2, 114 / 2 + k.halfHeight + 5)), "image", 2))]])
		}
	});
	registLayout("ImageText17", {
		create : function(m, l) {
			var k = l.image[1], j = l.image[2];
			bindDataSource(Component.BackgroundImage(m, l.image[0]), "image", 0);
			m.registEnterAnimation([[enterAnimate.fadeIn(bindDataSource(m.component(g(k, 68 / 2, 696 / 2)), "image", 1))], [enterAnimate.fadeIn(bindDataSource(m.component(g(j, 76 / 2, 696 / 2 + k.halfHeight + 5)), "image", 2))]])
		}
	});
	var h = {
		create : function(m, l) {
			var k = l.image[1], j = l.image[2];
			bindDataSource(Component.BackgroundImage(m, l.image[0]), "image", 0);
			m.registEnterAnimation([[enterAnimate.emerge(bindDataSource(m.component(g(k, 516 / 2, 195 / 2)), "image", 1), "right"), enterAnimate.emerge(bindDataSource(m.component(g(j, 516 / 2 + k.halfWidth - j.halfWidth, 195 / 2 + k.halfHeight + 5)), "image", 2), "left")]])
		}
	};
	registLayout("ImageText21", h);
	registLayout("ImageText22", h);
	registLayout("ImageText23", {
		create : function(m, l) {
			var k = l.image[1], j = l.image[2];
			bindDataSource(Component.BackgroundImage(m, l.image[0]), "image", 0);
			m.registEnterAnimation([[enterAnimate.emerge(bindDataSource(m.component(g(k, 60 / 2, 140 / 2)), "image", 1), "top"), enterAnimate.emerge(bindDataSource(m.component(g(j, 64 / 2, 140 / 2 + k.halfHeight + 5)), "image", 2), "bottom")]])
		}
	});
	registLayout("ImageText24", {
		create : function(m, l) {
			var k = l.image[1], j = l.image[2];
			bindDataSource(Component.BackgroundImage(m, l.image[0]), "image", 0);
			m.registEnterAnimation([[enterAnimate.emerge(bindDataSource(m.component(g(k, 82 / 2, 720 / 2)), "image", 1), "top"), enterAnimate.emerge(bindDataSource(m.component(g(j, 86 / 2, 720 / 2 + k.halfHeight + 5)), "image", 2), "bottom")]])
		}
	});
	registLayout("ImageText25", {
		create : function(k, j) {
			var l = Content.Image(j.image[1], globalScale);
			bindDataSource(Component.BackgroundImage(k, j.image[0]), "image", 0);
			k.registEnterAnimation([[enterAnimate.emerge(bindDataSource(k.component({
				content : l,
				x : center(clientWidth, l.width),
				y : middleY(idealHeight - 40 - j.image[1].halfHeight, globalScale)
			}, "top"), "image", 1))]])
		}
	});
	registLayout("ImageText26", {
		create : function(k, j) {
			var l = Content.Image(j.image[1], globalScale), m = Content.Image(j.image[2], globalScale);
			bindDataSource(Component.BackgroundImage(k, j.image[0]), "image", 0);
			k.registEnterAnimation([[enterAnimate.emerge(bindDataSource(k.component({
				content : l,
				x : 0,
				y : middleY(588 / 2, globalScale)
			}), "image", 1), "right")], [enterAnimate.emerge(bindDataSource(k.component({
				content : m,
				x : middleX(144 / 2, globalScale),
				y : middleY(588 / 2, globalScale) + l.height
			}), "image", 2), "right")]])
		}
	});
	registLayout("ImageText27", {
		resource : ["firstpage-flake.png"],
		create : function(n, m) {
			var p = Content.Image(m.image[1], globalScale);
			bindDataSource(Component.BackgroundImage(n, m.image[0]), "image", 0);
			n.registEnterAnimation([[enterAnimate.emerge(bindDataSource(n.component({
				content : p,
				x : center(clientWidth, p.width),
				y : middleY(503 / 2, globalScale)
			}), "image", 1))]]);
			if (window.highPerformance) {
				var q = a.Layer(), j = [], k = 40;
				if (ua.iphone4) {
					k = 25
				} else {
					if (ua.iphone5) {
						k = 30
					} else {
						if (ua.iphone6) {
							k = 40
						}
					}
				}
				i.css(q, {
					position : "absolute",
					left : 0,
					right : 0,
					top : 0,
					bottom : 0,
					"z-index" : 100,
					"pointer-events" : "none"
				});
				q.resize(clientWidth, clientHeight);
				function l() {
					return {
						x : Math.random() * clientWidth << 0,
						y : (Math.random() - 1) * clientHeight << 0,
						omega : Math.random() * Math.PI,
						size : (Math.random() * 8 + 10) << 0,
						speed : Math.random() + 1,
						a : Math.random() * 10 + 2
					}
				}
				b.loop(k, function() {
					j.push(l())
				});
				var o = null;
				n.onShow(function() {
					window.body.appendChild(q);
					o = i.requestAnimate(function() {
						q.draw(function(r) {
							b.loopArray(j, function(t, v) {
								var w = t.y += t.speed, s = t.x + Math.sin(t.y * 0.02 + t.omega) * t.a, u = t.size;
								r.drawImage(m.resource[0], s, w, u, u);
								if (t.y >= clientHeight) {
									t = l();
									t.y = -20;
									j[v] = t
								}
							})
						})
					})
				});
				n.onRemove(function() {
					o.remove();
					i.removeNode(q)
				})
			}
		}
	})
})();
(function() {
	var f = zachModule["5"], e = zachModule["2"], c = zachModule["4"], b = e.element, a = '<div class="map-info-window"><div class="name"></div><div class="info"><div>地址:<span class="address"></span></div></div></div>';
	registLayout("map", {
		resource : ["layout-map-location.png"],
		create : function(k, j) {
			var h = j.resource[0], i;
			bindDataSource(Component.BackgroundImage(k, j.image[0]), "image", 0);
			var g = bindDataSource(k.component({
				content : Content.Image(h),
				x : center(clientWidth, h.halfWidth),
				y : middleY(574 / 2)
			}), "map", 0);
			k.component({
				content : Content.LineText({
					text : j.location[0].address,
					lineHeight : 12,
					fontSize : 12,
					color : "#FFFFFF",
					width : clientWidth
				}),
				x : 0,
				y : middleY(682 / 2)
			});
			k.onShow(function() {
				g.infiniteAnimate({
					duration : 3,
					progress : {
						0 : {
							opacity : 1
						},
						50 : {
							opacity : 0.4
						}
					}
				})
			});
			c.onTap(k.component({
				content : Content.Rect({
					width : 120,
					height : 100
				}),
				x : center(clientWidth, 120),
				y : middleY(574 / 2 - 20)
			}), function() {
				window.preventJump = true;
				if (!i) {
					i = fp.slidePage();
					i.classList.add("map-slide-page");
					var l = i.appendChild(b("div.title-bar", {
						children : [b("div.icon.back"), b("div.line"), b("div.caption")]
					})), m = fp.Loading(i);
					c.onTap(l, fp.history.back);
					f.markerMap({
						data : j.location,
						parent : i,
						make : function(o) {
							var n = b(a);
							n.querySelector(".name").innerHTML = o.name;
							n.querySelector(".address").innerHTML = o.address;
							return n
						},
						onLoad : m.remove
					})
				}
				i.slideIn()
			})
		}
	})
})();
(function() {
	var a = zachModule["0"], b = a.loopArray;
	registLayout("MutipleImage01", {
		create : function(g, f) {
			var e = [], i = Math.min(1, yRatio) * globalScale, h = g.component({
				content : Content.Rect({
					width : 244 * i << 0,
					height : 410 * i << 0
				})
			});
			b(f.image, function(j) {
				e.push(h.component(Content.Border(Content.ImageCover(j, h.componentWidth, h.componentHeight), {
					width : 3,
					color : "#FFFFFF"
				})))
			});
			h.x = position.center(g, h);
			h.y = position.middle(g, h);
			var c = Component.MultiImageArea({
				page : g,
				contents : e,
				sign : -1,
				parent : h,
				icon : {
					prev : Icon(staticImgSrc("layout-MutipleImage01-arrow-left.png"), 20, 32),
					next : Icon(staticImgSrc("layout-MutipleImage01-arrow-right.png"), 20, 32)
				}
			});
			g.registEnterAnimation([c.enterAnimation])
		}
	});
	registLayout("MutipleImage04", {
		resource : ["layout-MutipleImage04-background.jpg"],
		create : function(m, e) {
			var c = [];
			m.component(Content.Image(e.resource[0], clientWidth, clientHeight));
			var k = bindDataSource(m.component(Content.Image(e.image[0], globalScale)), "image", 0), i = bindDataSource(m.component(Content.Image(e.image[1], globalScale)), "image", 1), g = bindDataSource(m.component(Content.Image(e.image[2], globalScale)), "image", 2), n = m.component(Content.Rect({
				width : 356 / 2 * globalScale << 0,
				height : 518 / 2 * globalScale << 0
			}));
			var j = d(11), h = d(19), f = d(39);
			b([k, i, g, n], function(o) {
				o.x = position.center(m, o)
			});
			k.y = (clientHeight - (k.componentHeight + i.componentHeight + g.componentHeight + n.componentHeight + j + h + f)) / 2 << 0;
			i.y = position.bottomTo(k, i) + j + k.y;
			g.y = position.bottomTo(i, g) + h + i.y;
			n.y = position.bottomTo(g, n) + f + g.y;
			b(e.image.slice(3), function(o) {
				c.push(n.component(Content.Border(Content.ImageCover(o, n.componentWidth, n.componentHeight), {
					width : 1,
					color : "#FFFFFF"
				})))
			});
			var l = Component.MultiImageArea({
				page : m,
				contents : c,
				sign : -1,
				parent : n,
				icon : {
					prev : Icon(staticImgSrc("layout-MutipleImage04-arrow-left.png"), 14, 22),
					next : Icon(staticImgSrc("layout-MutipleImage04-arrow-right.png"), 14, 22)
				}
			});
			m.registEnterAnimation([[enterAnimate.emerge(k)], [enterAnimate.emerge(i)], [enterAnimate.emerge(g)], l.enterAnimation])
		}
	})
})();
(function() {
	var a = zachModule["0"], i = zachModule["5"], h = zachModule["4"], g = zachModule["2"], e = g.element, c = g.css, b = ua.android && location.href.arg.ifeng;
	function f(j, k) {
		registLayout(j, {
			resource : ["layout-razzies-background-single.png", "layout-razzies-background-double.png", "layout-razzies-banner-left.png", "layout-razzies-banner-center.png", "layout-razzies-banner-right.png", "layout-razzies-cup.png"],
			create : function(r, n) {
				var x = n.text[2].split("\n");
				Component.BackgroundImage(r, n.resource[ k ? 0 : 1], 1);
				var o = Content.Label({
					fontSize : 15 * globalScale << 0,
					lineHeight : 15 * globalScale << 0,
					color : "#fdf1c8",
					text : n.text[0]
				}), v = o.width + 50 * globalScale << 0;
				var u = r.component({
					content : Content.Rect({
						width : v,
						height : 36 * globalScale << 0
					}),
					x : center(clientWidth, v),
					y : middleY(153, globalScale),
					"z-index" : 2
				}), p = 20 * globalScale << 0;
				u.component({
					content : Content.Image(n.resource[2], globalScale),
					x : 0,
					y : 0
				});
				u.component({
					content : Content.Image(n.resource[3], u.componentWidth - p * 2 + 8, u.componentHeight),
					x : p - 3,
					y : 0
				});
				u.component({
					content : Content.Image(n.resource[4], globalScale),
					x : u.componentWidth - p,
					y : 0
				});
				bindDataSource(u.component({
					content : o,
					x : center(u.componentWidth, o.width),
					y : center(30 * globalScale << 0, o.height)
				}), "text", 0);
				var q = 250 * globalScale;
				var w = bindDataSource(r.component({
					content : Content.BlockText({
						width : q,
						lineHeight : 20 * globalScale << 0,
						fontSize : 12 * globalScale << 0,
						text : n.text[1],
						color : "#fdf1c9"
					}),
					x : center(clientWidth, q),
					y : middleY(200, globalScale),
					"z-index" : 2
				}), "text", 1);
				if (k) {
					bindDataSource(r.component({
						content : Content.ImageCover(n.image[0], 104 * globalScale << 0, 104 * globalScale << 0),
						x : middleX(108, globalScale),
						y : middleY(41, globalScale)
					}), "image", 0)
				} else {
					bindDataSource(r.component({
						content : Content.ImageCover(n.image[0], 104 * globalScale << 0, 104 * globalScale << 0),
						x : middleX(56, globalScale),
						y : middleY(41, globalScale)
					}), "image", 0);
					bindDataSource(r.component({
						content : Content.ImageCover(n.image[1], 104 * globalScale << 0, 104 * globalScale << 0),
						x : middleX(161, globalScale),
						y : middleY(41, globalScale)
					}), "image", 1)
				}
				n.resource[5].hasChild = true;
				var y = r.component({
					content : Content.Image(n.resource[5], globalScale),
					x : middleX(132 / 2, globalScale),
					y : middleY(566 / 2, globalScale),
					"z-index" : 2
				}), m = 85 * globalScale << 0;
				var t = bindDataSource(y.component({
					content : Content.Rect({
						height : 37 * globalScale << 0,
						width : m
					}),
					x : center(y.componentWidth, m) - 1,
					y : 129 * globalScale << 0
				}), "text", 2), l = 15 * globalScale << 0, s = "#40234a";
				if (x.length === 1) {
					t.component({
						content : Content.LineText({
							fontSize : l,
							lineHeight : l,
							fontWeight : "bold",
							width : m,
							text : x[0],
							color : s
						}),
						y : center(t.height, l),
						x : 0
					})
				} else {
					t.component({
						content : Content.LineText({
							fontSize : l,
							lineHeight : l,
							fontWeight : "bold",
							width : m,
							text : x[0],
							color : s
						}),
						y : 0,
						x : 0
					});
					t.component({
						content : Content.LineText({
							fontSize : l,
							lineHeight : l,
							fontWeight : "bold",
							width : m,
							text : x[1],
							color : s
						}),
						y : 20 * globalScale << 0,
						x : 0
					})
				}
				r.registEnterAnimation([[enterAnimate.fallDownAndShake(u)], [enterAnimate.emerge(w)], [a.insert(enterAnimate.shrink(y), {
					delay : 0.3
				})]])
			}
		})
	}

	f("razzies-single", true);
	f("razzies-double", false);
	window.setRazziesShareData = function(j) {
		var k = j.text;
		window.setShareData({
			url : a.removeUrlArg(a.concatUrlArg(location.href, {
				razzies : j.id
			}), ["ifeng"]),
			title : "2014中国自媒体金酸媒奖：凤凰新闻与" + k[2] + "联合颁发",
			desc : k[0] + "等大咖喜获“金酸媒”奖。 也想颁奖？点我！"
		})
	};
	window.RazziesPreviewPage = function(m, l) {
		var n = m.text;
		var k = LayoutPage({
			layout : {
				label : "razzies-custom",
				image : [m.image],
				text : n,
				preview : JSON.stringify({
					id : m.id,
					image : [m.image],
					text : n
				})
			}
		}), j = DOMPage();
		window.setRazziesShareData(m);
		k.load(function() {
			window.highPerformance = false;
			k.create(j);
			g.toggleState(document.body, "can-push", "can-not-push");
			l && l(j);
			j.start()
		});
		h.onPointerDown(j, function(o) {
			o.preventDefault();
			o.stopPropagation()
		});
		c(j, {
			width : c.px(clientWidth),
			height : c.px(clientHeight),
			"z-index" : 5,
			background : "#000000"
		})
	};
	registSpecialPage("razzies", function(k) {
		var j = a.Loader();
		function o(q) {
			var p = new Image();
			j.load(function(r) {
				p.onload = function() {
					r();
					p.onload = r
				};
				p.src = staticImgSrc(q)
			});
			return p
		}

		var n = o("layout-razzies-make.png"), l = o( b ? "layout-razzies-default-cup.png" : "layout-razzies-add-photo.png"), m = o("layout-razzies-copyright.png");
		j.start(function() {
			var p;
			k({
				create : function(v) {
					if (!p) {
						c(n, i.getImageCoverStyle(n, clientWidth, clientHeight));
						var s = false;
						p = e("form", {
							css : {
								position : "absolute",
								left : 0,
								right : 0,
								top : 0,
								bottom : 0,
								"z-index" : 0
							}
						});
						c(n, {
							"pointer-events" : "none",
							"z-index" : 3
						});
						p.appendChild(n);
						function u(C, B, A, z, E, D) {
							c(C, {
								position : "absolute",
								left : c.px(middleX(z / 2 << 0, globalScale)),
								top : c.px(middleY(E / 2 << 0, globalScale)),
								width : c.px(B / 2 * globalScale << 0),
								height : c.px(A / 2 * globalScale << 0),
								"box-sizing" : "border-box",
								"z-index" : D
							});
							h.onPointerDown(C, function(y) {
								y.stopPropagation()
							});
							return C
						}

						var r = null;
						function w(y) {
							var z = e(y.textArea ? "textarea" : "input", {
								classList : "text",
								css : {
									border : "none",
									"line-height" : c.px(y.lineHeight || (y.height / 2 * globalScale << 0)),
									"font-size" : c.px(y.fontSize * globalScale << 0),
									padding : y.textArea ? "4px 6px" : "0 4px",
									resize : "none",
									color : y.color,
									background : "transparent",
									"text-align" : y.textArea ? "start" : "center",
									"font-weight" : y.bold || "normal"
								},
								name : y.name
							}, p);
							z.arg = y;
							z.maxLength = y.max;
							y.className && (z.classList.add(y.className));
							y.placeholder && (z.placeholder = y.placeholder);
							g.bindEvent(z, "focus", function() {
								r = z
							});
							h.onPointerDown(z, function(A) {
								A.stopPropagation()
							});
							return u(z, y.width, y.height, y.x, y.y, 3)
						}

						var x = u(e("div", p), 216, 216, 212, 62, 2);
						c(x, {
							"pointer-events" : "none"
						});
						x.appendChild(l);
						c(l, i.getImageCoverStyle(l, 108 * globalScale << 0, 108 * globalScale << 0));
						if (!b) {
							var q = u(e("input", {
								type : "file",
								name : "picture",
								accept : "image/*"
							}, p), 216, 216, 212, 62, 1);
							s = "请上传照片";
							q.onchange = function() {
								var z = q.files[0];
								var y = new FileReader();
								y.onload = function() {
									s = false;
									l.onload = function() {
										g.removeCss(l, "width");
										g.removeCss(l, "height");
										c(l, i.getImageCoverStyle(l, 108, 108))
									};
									l.src = z.type ? y.result : "data:application/octet-stream;" + y.result.substr(y.result.indexOf("base64,"))
								};
								y.readAsDataURL(z)
							}
						}
						w({
							width : 130,
							height : 46,
							x : 312,
							y : 306,
							fontSize : 14,
							name : "Honoree",
							caption : "获奖人",
							index : 0,
							color : "#fdf1c8",
							min : 2,
							max : 4,
							bold : "bold"
						});
						c.transform(w({
							width : 134,
							height : 40,
							x : 484,
							y : 576,
							fontSize : 14,
							name : "Awards",
							caption : "颁奖人",
							index : 2,
							bold : "bold",
							color : "#d8271c",
							min : 2,
							max : 4
						}), c.rotateZ(-20, "deg"));
						w({
							width : 504,
							height : 134,
							x : 68,
							y : 390,
							fontSize : 12,
							lineHeight : 18,
							name : "Reason",
							className : "reason",
							textArea : true,
							index : 1,
							caption : "获奖理由",
							placeholder : "获奖理由：",
							min : 10,
							max : 60,
							color : "#2d3e0a"
						});
						g.insertCSSRules({
							"::-webkit-input-placeholder" : {
								color : "#2d3e0a"
							}
						});
						w({
							width : 210,
							height : 46,
							x : 236,
							y : 680,
							fontSize : 20,
							name : "AwardsName",
							caption : "获奖名称",
							index : 3,
							min : 2,
							max : 5,
							color : "#fdf1c8",
							bold : "bold"
						});
						h.onPointerDown(p, function() {
							r && r.blur()
						});
						var t = u(e("div", p), 206, 84, 219, 803, 3);
						h.onTap(t, function() {
							r && r.blur();
							var y = "", A = [];
							function z(C) {
								y !== "" && (y += "<br>");
								y += C
							}

							if (s !== false) {
								z(s)
							}
							a.loopArray(p.querySelectorAll(".text"), function(D) {
								var C = D.arg;
								if (D.value.length < C.min) {
									z(C.caption + "至少" + C.min + "个字")
								}
								A[C.index] = D.value
							});
							if (y) {
								fp.alert(y)
							} else {
								fp.lock(true, p);
								var B = fp.Loading(body);
								g.ajax({
									method : "post",
									url : "http://chuye.cloud7.com.cn/beta/Event/AwardsCustom",
									data : new FormData(p),
									isJson : true,
									onLoad : function(C) {
										RazziesPreviewPage({
											image : l.src,
											text : A,
											id : C.data
										}, function(D) {
											B.remove();
											body.appendChild(D)
										})
									}
								})
							}
						});
						p.appendChild(u(m, 326, 20, 158, 948, 3));
						h.onTap(m, fp.downloadFirstPage)
					}
					v.appendChild(p)
				}
			})
		})
	});
	registLayout("razzies-custom", {
		resource : ["layout-razzies-background-custom.png", "layout-razzies-banner-left-new.png", "layout-razzies-banner-center-new.png", "layout-razzies-banner-right-new.png", "layout-razzies-print.png", "layout-razzies-share.png", "layout-razzies-more.png", "layout-razzies-copyright.png", "layout-razzies-cup.png", "layout-razzies-tips-continue.png", "layout-razzies-default-cup.png"],
		create : function(p, l) {
			var q = !l.image || l.image.length === 0 || !l.image[0];
			Component.BackgroundImage(p, l.resource[0], 1);
			var m = Content.Label({
				fontSize : 15 * globalScale << 0,
				lineHeight : 15 * globalScale << 0,
				color : "#fdf1c8",
				text : "获奖人：" + l.text[0]
			}), t = m.width + 50 * globalScale << 0;
			var s = p.component({
				content : Content.Rect({
					width : t,
					height : 35 * globalScale << 0
				}),
				x : center(clientWidth, t),
				y : middleY(150, globalScale),
				"z-index" : 2
			}), n = 20 * globalScale << 0;
			s.component({
				content : Content.Image(l.resource[1], globalScale),
				x : 0,
				y : 0
			});
			s.component({
				content : Content.Image(l.resource[2], s.componentWidth - n * 2 + 8, s.componentHeight),
				x : n - 3,
				y : 0
			});
			s.component({
				content : Content.Image(l.resource[3], globalScale),
				x : s.componentWidth - n,
				y : 0
			});
			s.component({
				content : m,
				x : center(s.componentWidth, m.width),
				y : center(30 * globalScale << 0, m.height)
			});
			var o = 250 * globalScale;
			var v = bindDataSource(p.component({
				content : Content.BlockText({
					width : o,
					lineHeight : 20 * globalScale << 0,
					fontSize : 12 * globalScale << 0,
					text : l.text[1],
					color : "#2d3e0a"
				}),
				x : center(clientWidth, o),
				y : middleY(196, globalScale),
				"z-index" : 2
			}), "text", 1);
			p.component({
				content : Content.ImageCover( q ? l.resource[10] : l.image[0], 104 * globalScale << 0, 104 * globalScale << 0),
				x : middleX(108, globalScale),
				y : middleY(33, globalScale)
			});
			l.resource[4].hasChild = true;
			var j = p.component({
				content : Content.Image(l.resource[4], globalScale),
				x : middleX(462 / 2, globalScale),
				y : middleY(496 / 2, globalScale),
				rotate : -20 / 180 * Math.PI,
				"z-index" : 2
			}), k = j.componentHeight;
			var r = j.component({
				content : Content.Rect({
					height : k,
					width : k
				}),
				x : 0,
				y : 0
			}), u = 14 * globalScale << 0, w = "#d8271c";
			r.component({
				content : Content.LineText({
					fontSize : u,
					lineHeight : u,
					fontWeight : "bold",
					width : k,
					text : "颁奖人",
					color : w
				}),
				y : 25 * globalScale << 0,
				x : 0
			});
			r.component({
				content : Content.LineText({
					fontSize : u,
					lineHeight : u,
					fontWeight : "bold",
					width : k,
					text : l.text[2],
					color : w
				}),
				y : 45 * globalScale << 0,
				x : 0
			});
			if (l.preview) {
				(function() {
					var x = 20 * globalScale << 0, A = p.component({
						content : Content.LineText({
							fontSize : x,
							lineHeight : x,
							fontWeight : "bold",
							width : clientWidth,
							text : "年度" + l.text[3] + "奖",
							color : "#fdf1c8"
						}),
						y : middleY(680 / 2, globalScale),
						x : 0,
						"z-index" : 2
					});
					function B() {
						localStorage.setItem("razzies", l.preview);
						fp.downloadFirstPage()
					}
					h.onTap(p.component({
						content : Content.Image(l.resource[7], globalScale),
						x : middleX(158 / 2, globalScale),
						y : middleY(948 / 2, globalScale),
						"z-index" : 2
					}), B);
					var z = p.component({
						content : Content.Image(l.resource[5], globalScale),
						x : middleX(96 / 2, globalScale),
						y : middleY(804 / 2, globalScale),
						"z-index" : 2
					});
					h.onTap(z, function() {
						var D = ua.MicroMessenger ? ua.ios ? "firstpage-tips-up-ios.png" : "firstpage-tips-up-android.png" : ua.ios ? "firstpage-tips-down-ios.png" : "firstpage-tips-down-android.png";
						var C = e("div", {
							css : {
								position : "absolute",
								left : 0,
								right : 0,
								top : 0,
								bottom : 0,
								"z-index" : 10000,
								background : a.tupleString("url", [staticImgSrc(D)]),
								"background-position" : ua.MicroMessenger ? "right top" : "center bottom",
								"background-size" : "cover",
								"background-color" : "rgba(0,0,0,0.9)"
							}
						}, document.body);
						h.onTap(C, function() {
							g.removeNode(C)
						})
					});
					var y = p.component({
						content : Content.Image(l.resource[6], globalScale),
						x : middleX(344 / 2, globalScale),
						y : middleY(804 / 2, globalScale),
						"z-index" : 2
					});
					h.onTap(y, B);
					p.registEnterAnimation([[enterAnimate.fallDownAndShake(s)], [enterAnimate.emerge(v)], [enterAnimate.circleRound(A)], [a.insert(enterAnimate.shrink(j), {
						delay : 0.3
					})], [enterAnimate.emerge(z), enterAnimate.emerge(y)]])
				})()
			} else {
				if (q) {
					(function() {
						var x = 20 * globalScale << 0, z = p.component({
							content : Content.LineText({
								fontSize : x,
								lineHeight : x,
								fontWeight : "bold",
								width : clientWidth,
								text : "年度" + l.text[3] + "奖",
								color : "#fdf1c8"
							}),
							y : middleY(680 / 2, globalScale),
							x : 0,
							"z-index" : 2
						});
						var y = p.component({
							content : Content.Image(l.resource[9], globalScale),
							x : middleX(201 / 2, globalScale),
							y : middleY(853 / 2, globalScale),
							"z-index" : 2
						});
						p.registEnterAnimation([[enterAnimate.fallDownAndShake(s)], [enterAnimate.emerge(v)], [enterAnimate.circleRound(z)], [a.insert(enterAnimate.shrink(j), {
							delay : 0.3
						})], [a.insert(enterAnimate.fadeIn(y), {
							delay : 2
						})]])
					})()
				} else {
					(function() {
						l.resource[8].hasChild = true;
						var x = p.component({
							content : Content.Image(l.resource[8], globalScale),
							x : middleX(132 / 2, globalScale),
							y : middleY(551 / 2, globalScale),
							"z-index" : 2
						}), A = 85 * globalScale << 0;
						var y = x.component({
							content : Content.Rect({
								height : 37 * globalScale << 0,
								width : A
							}),
							x : center(x.componentWidth, A) - 1,
							y : 129 * globalScale << 0
						}), B = 15 * globalScale << 0, z = "#40234a";
						y.component({
							content : Content.LineText({
								fontSize : B,
								lineHeight : B,
								fontWeight : "bold",
								width : A,
								text : "年度",
								color : z
							}),
							y : 0,
							x : 0
						});
						y.component({
							content : Content.LineText({
								fontSize : B,
								lineHeight : B,
								fontWeight : "bold",
								width : A,
								text : l.text[3] + "奖",
								color : z
							}),
							y : 20 * globalScale << 0,
							x : 0
						});
						p.registEnterAnimation([[enterAnimate.fallDownAndShake(s)], [enterAnimate.emerge(v)], [a.insert(enterAnimate.shrink(x), {
							delay : 0.3
						})], [a.insert(enterAnimate.shrink(j), {
							duration : 0.4
						})]])
					})()
				}
			}
		}
	})
})();
(function() {
	var c = zachModule["0"], f = zachModule["2"], e = zachModule["4"], b = zachModule["10"], a = zachModule["9"];
	registLayout("scratch-card", {
		crossOrigin : "*",
		create : function(k, j) {
			k.component({
				content : Content.ImageCover(j.image[0], clientWidth, clientHeight),
				x : 0,
				y : 0
			});
			if (!j.complete) {
				var g = k.component({
					content : Content.ImageCover(j.image[1], clientWidth, clientHeight),
					x : 0,
					y : 0
				}), i = b.Layer(), h = a.layImageByFrame(j.image[1], {
					width : clientWidth,
					height : clientHeight,
					size : a.Size.cover,
					align : [0.5, 0.5]
				});
				k.onShow(function() {
					i.resize(clientWidth, clientHeight);
					i.classList.add("scratch-card");
					i.draw(function(n) {
						a.drawImageLayout(n, h)
					});
					document.body.appendChild(i);
					document.body.classList.add("hide-tips");
					var m = [];
					var l = e.onPointerDown(i, function(q, p, n) {
						var o = [], s = true;
						m.push(o);
						q.preventDefault();
						q.stopPropagation();
						o.push({
							x : p,
							y : n
						});
						q.onMove(function(v, u, t) {
							o.push({
								x : u,
								y : t
							})
						});
						q.onUp(function() {
							s = false
						});
						var r = f.requestAnimate(function() {
							i.draw(function(x) {
								a.drawImageLayout(x, h);
								x.lineCap = "round";
								x.lineJoin = "round";
								x.globalCompositeOperation = "destination-out";
								x.beginPath();
								c.loopArray(m, function(B) {
									c.loopArray(B, function(C, D) {
										D === 0 ? x.moveTo(C.x, C.y) : x.lineTo(C.x, C.y)
									});
									x.lineWidth = 50;
									if (ua.android) {
										i.style.display = "none";
										i.offsetHeight
										i.style.display = "inherit"
									}
									x.stroke()
								});
								if (!s) {
									var u = false;
									r.remove();
									try {
										var z = x.getImageData(0, 0, i.width, i.height), A = z.data, w = 0;
										for (var v = 0, t = A.length; v < t; v += 4) {
											if (A[v + 3] < 128) {++w
											}
										}
									} catch(y) {
										u = true
									}
									if (u || w / (A.length / 4) > 0.3) {
										l.remove();
										f.transition(i, "0.8s", {
											opacity : 0
										}, function() {
											document.body.classList.remove("hide-tips");
											j.complete = true;
											f.removeNode(i)
										})
									}
								}
							})
						})
					});
					g.remove()
				});
				k.onRemove(function() {
					f.removeNode(i)
				})
			}
		}
	})
})();
(function() {
	var a = zachModule["0"], g = zachModule["2"], c = g.element, f = zachModule["4"];
	function e(h) {
		return h - 504 / 2 + clientHeight / 2 << 0
	}

	registLayout("Sign-Up02", {
		create : function(k, j) {
			var i = {
				top : 148,
				middle : 417,
				bottom : 687
			}, h = 125;
			bindDataSource(k.component({
				content : Content.ImageCover(j.image[0], clientWidth, clientHeight),
				x : 0,
				y : 0
			}), "image", 0);
			f.onTap(bindDataSource(k.component({
				content : Content.Rect({
					width : h,
					height : h
				}),
				x : (clientWidth - h) / 2 << 0,
				y : e(i[j.position[0]] / 2)
			}), "link", 0), function() {
				fp.jump(j.actionlinks[0])
			})
		}
	});
	var b = registFunctionPage("sign-up", function(o, h) {
		var q = JSON.parse(h.template), i = c("div.page-content", o), k = c("form", {
			action : "/"
		}, i), l = c("div.icon.back", o), m = null, p = null, j = [], r = {};
		o.classList.add("sign-up-form-slide-page");
		o.classList.add("scroll");
		f.onTap(l, fp.history.back);
		function n() {
			m && m.blur();
			var v = [], w = [];
			function u(z, A) {
				v.push({
					name : z.name,
					label : z.label,
					value : A
				})
			}

			var y = [];
			a.loopArray(j, function(z) {
				var A = z.input.value;
				if (z.data.required) {
					if (A === "") {
						w.push(z.data.label);
						z.input.classList.add("error")
					} else {
						var B = z.validate ? z.validate(A) : null;
						if (B) {
							y.push(B);
							z.input.classList.add("error")
						} else {
							u(z.data, A);
							z.input.classList.remove("error")
						}
					}
				} else {
					u(z.data, z.input.value)
				}
			});
			if (w.length !== 0 || y.length !== 0) {
				fp.alert((w.length ? [w.join("，") + "不能为空。"] : []).concat(y).join("<br>"))
			} else {
				var s = a.Loader(), x = fp.Loading(o), t = {};
				fp.lock(true, i);
				if (fp.isLogIn()) {
					s.load(function(z) {
						fp.getUserInfo(function(A) {
							t = A;
							z()
						})
					})
				}
				s.start(function() {
					var z = {
						"报名时间" : new Date().getTime(),
						"微信昵称" : t.NickName,
						"微信头像" : t.HeadPhoto,
						"微信性别" : t.Sex,
						"微信City" : t.City,
						"微信Province" : t.Province,
						"微信Country" : t.Country
					};
					a.loopObj(r, function(A, B) {
						u(B, z[A] === undefined ? "" : z[A])
					});
					fp.sendForm(function() {
						x.remove();
						fp.alert(q.data.submitComplete.value, 1000);
						setTimeout(function() {
							if (o.isIn()) {
								fp.history.back()
							}
						}, 1000)
					}, {
						id : h.formId,
						data : v
					})
				})
			}
		}
		g.bindEvent(k, "submit", function(s) {
			s.preventDefault()
		});
		a.loopArray(q.data.component, function(s) {
			if (s.enable) {
				if (s.visiable) {
					switch(s.name) {
						case"textbox":
							(function() {
								var w = {}, v = c("label", k), u = w.caption = c("div.caption", s.label + "：", v), t = w.input = c("input", {
									placeholder : s.placeholder,
									name : s.id
								}, v);
								switch(s.label) {
									case"电话":
										t.type = "tel";
										break;
									case"邮箱":
										t.type = "email";
										w.validate = function(x) {
											return /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(x) ? null : "请输入正确的邮箱地址"
										};
										break
								}
								g.bindEvent(t, "focus", function() {
									m = t
								});
								if (s.required) {
									c("div.required.icon", u)
								}
								if (p) {
									g.bindEvent(p, "keypress", function(x) {
										if (x.keyCode === 13) {
											t.focus()
										}
									})
								}
								p = t;
								w.data = s;
								j.push(w)
							})();
							break;
						case"btn":
							(function() {
								var t = c("label", k), u = c("div.button", {
									innerHTML : s.value
								}, t);
								f.onTap(u, n)
							})();
							break
					}
				} else {
					r[s.label] = s
				}
			}
		});
		if (p) {
			g.bindEvent(p, "keypress", function(s) {
				if (s.keyCode === 13) {
					n()
				}
			})
		}
	});
	registLayout("Sign-Up03", {
		create : function(j, i) {
			j.component({
				content : Content.ImageCover(i.image[0], clientWidth, clientHeight),
				x : 0,
				y : 0
			});
			var h = i.image[1];
			f.onTap(j.component({
				content : Content.Image(h),
				x : (clientWidth - h.halfWidth) / 2 << 0,
				y : e(208)
			}), function() {
				window.preventJump = true;
				b({
					data : i.signup,
					noLog : !JSON.parse(i.signup.template).allowAnymous
				})
			})
		}
	})
})();
(function() {
	var b = zachModule["6"], a = zachModule["0"], c = zachModule["7"];
	registLayout("valentine-01", {
		resource : ["layout-valentine-01-background.png"],
		create : function(h, g) {
			a.loopArray([{
				x : 62,
				y : 608,
				width : 230,
				height : 324,
				rotate : -6 / 180 * Math.PI
			}, {
				x : 348,
				y : 608,
				width : 230,
				height : 324
			}, {
				x : 222,
				y : 90,
				width : 356,
				height : 462
			}], function(k, j) {
				h.component({
					content : Content.ImageCover(g.image[j], k.width / 2 * globalScale, k.height / 2 * globalScale),
					x : middleX(k.x / 2, globalScale),
					y : middleY(k.y / 2, globalScale),
					rotate : k.rotate || 0
				})
			});
			Component.BackgroundImage(h, g.resource[0], 1);
			var f = bindDataSource(h.component(Content.Image(g.image[3], globalScale)), "image", 3), e = bindDataSource(h.component(Content.Image(g.image[4], globalScale)), "image", 4);
			f["z-index"] = e["z-index"] = 2;
			e.x = f.x = middleX(70 / 2, globalScale);
			f.y = middleY(82 / 2, globalScale);
			e.y = position.bottomTo(f, e) + f.y + d(9);
			h.registEnterAnimation([[enterAnimate.fadeIn(f)], [enterAnimate.fadeIn(e)]])
		}
	});
	registLayout("valentine-02", {
		resource : ["layout-valentine-02-background.jpg", "layout-valentine-02-frame.png", "layout-valentine-02-love.png", "layout-valentine-02-rose.png"],
		create : function(j, i) {
			var h = [];
			j.component(Content.Image(i.resource[0], clientWidth, clientHeight));
			var k = j.component({
				content : Content.Rect({
					width : 191 * yRatio << 0,
					height : 200 * yRatio << 0
				})
			});
			a.loopArray(i.image.slice(1), function(m) {
				h.push(k.component(Content.FrameImage({
					frame : i.resource[1],
					img : m,
					imgX : 13 * yRatio,
					imgY : 15 * yRatio,
					imgWidth : 164 * yRatio,
					imgHeight : 162 * yRatio,
					frameWidth : k.componentWidth,
					frameHeight : k.componentHeight
				})))
			});
			Component.MultiImageArea({
				page : j,
				contents : h,
				sign : -1,
				parent : k,
				noAnimation : true,
				auto : true
			});
			var g = j.component(Content.Image(i.resource[2], yRatio));
			var f = j.component(Content.Rect({
				width : 78 * yRatio,
				height : 16 * yRatio << 0
			}));
			var l = bindDataSource(j.component(Content.Image(i.image[0], yRatio)), "image", 0);
			var e = j.component(Content.Image(i.resource[3], yRatio));
			a.loopArray([k, g, f, e], function(m) {
				m.x = center(clientWidth, m.componentWidth)
			});
			k.y = Math.round(74 / 2 * yRatio);
			g.y = position.bottomTo(k, g) + k.y + Math.round(25 * yRatio);
			f.y = position.bottomTo(g, f) + g.y + Math.round(20 * yRatio);
			l.x = position.center(f, l) + f.x;
			l.y = position.middle(f, l) + f.y;
			e.y = clientHeight - e.componentHeight + 2;
			j.onShow(function() {
				function m(n) {
					return c.transformOrigin(c.matrix.rotate(n), e.componentWidth / 2 << 0, e.componentHeight * 1.5 << 0)
				}
				e.infiniteAnimate({
					timing : b.Timing.linear,
					duration : 3.6,
					progress : {
						0 : {
							transform : m(0)
						},
						25 : {
							transform : m(0.2)
						},
						50 : {
							transform : m(0)
						},
						75 : {
							transform : m(-0.2)
						},
						100 : {
							transform : m(0)
						}
					}
				})
			})
		}
	})
})();
(function() {
	var c = zachModule["2"], b = zachModule["4"];
	function a(e) {
		return e - 508 / 2 + clientHeight / 2 << 0
	}

	registLayout("video", {
		resource : ["layout-video-icon.png"],
		create : function(i, h) {
			var g = h.resource[0], e = g.halfWidth, l = (clientWidth - e) / 2 << 0, k = a(436 / 2), f = h.video[0];
			bindDataSource(Component.BackgroundImage(i, h.image[0]), "image", 0);
			b.onTap(bindDataSource(i.component({
				content : Content.Image(g),
				"z-index" : 2,
				x : l,
				y : k
			}), "video", 0), function() {
				window.preventJump = true;
				var n, m;
				if ( m = c.element("div", f).querySelector("iframe")) {
					n = fp.slidePage();
					n.onSlideIn(function() {
						window.stopAudio && window.stopAudio()
					});
					n.onSlideOut(function() {
						window.playAudio && window.playAudio()
					});
					n.classList.add("video-slide-page");
					m.width = clientWidth;
					m.height = clientWidth / 16 * 9 << 0;
					c.css(m, {
						position : "absolute",
						left : 0,
						top : c.css.px((clientHeight - m.height) / 2 << 0)
					});
					var o = fp.Loading(n);
					m.onload = function() {
						o.remove();
						m.onload = null
					};
					n.appendChild(m);
					b.onTap(c.element("div.close", n), fp.history.back)
				}
				if (n) {
					n.slideIn()
				} else {
					if (/(^http:\/\/)|(^https:\/\/)/.test(f)) {
						fp.jump(f)
					} else {
						alert("未识别的视频地址")
					}
				}
			});
			var j = i.component({
				content : Content.Circle({
					color : "#FFFFFF",
					r : e / 2 << 0
				}),
				"z-index" : 1,
				x : l,
				y : k
			});
			i.onShow(function() {
				j.infiniteAnimate({
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
				})
			})
		}
	})
})();
registLayout("ImageText18", {
	create : function(b, a) {
		bindDataSource(Component.BackgroundImage(b, a.image[0]), "image", 0);
		b.registEnterAnimation([[enterAnimate.emerge(bindDataSource(b.component({
			content : Content.Image(a.image[1]),
			x : (clientWidth - a.image[1].halfWidth) / 2,
			y : clientHeight * 0.229167
		}), "image", 1))], [enterAnimate.emerge(bindDataSource(b.component({
			content : Content.Image(a.image[2]),
			x : (clientWidth - a.image[2].halfWidth) / 2,
			y : clientHeight * 0.229167 + a.image[1].halfHeight + 29
		}), "image", 2))], [enterAnimate.emerge(bindDataSource(b.component({
			content : Content.Image(a.image[3]),
			x : (clientWidth - a.image[3].halfWidth) / 2,
			y : clientHeight * 0.229167 + a.image[1].halfHeight + a.image[2].halfHeight + 51
		}), "image", 3))]])
	}
});
registLayout("ImageText19", {
	create : function(b, a) {
		bindDataSource(Component.BackgroundImage(b, a.image[0]), "image", 0);
		b.registEnterAnimation([[enterAnimate.emerge(bindDataSource(b.component({
			content : Content.Image(a.image[1]),
			x : (clientWidth - a.image[1].halfWidth) / 2,
			y : clientHeight * 0.84126 - a.image[3].halfHeight - a.image[2].halfHeight - a.image[1].halfHeight - 51
		}), "image", 1))], [enterAnimate.emerge(bindDataSource(b.component({
			content : Content.Image(a.image[2]),
			x : (clientWidth - a.image[2].halfWidth) / 2,
			y : clientHeight * 0.84126 - a.image[3].halfHeight - 12 - a.image[2].halfHeight - 10
		}), "image", 2))], [enterAnimate.emerge(bindDataSource(b.component({
			content : Content.Image(a.image[3]),
			x : (clientWidth - a.image[3].halfWidth) / 2,
			y : clientHeight * 0.84126 - a.image[3].halfHeight
		}), "image", 3))]])
	}
});
registLayout("ImageText20", {
	create : function(b, a) {
		bindDataSource(Component.BackgroundImage(b, a.image[0]), "image", 0);
		var c = bindDataSource(b.component({
			content : Content.BlockText({
				width : clientWidth - 150,
				fontSize : 27,
				lineHeight : 35,
				text : a.text[0],
				fontWeight : "bold",
				color : "white"
			}),
			x : 75,
			y : 95
		}), "text", 0);
		b.registEnterAnimation([[enterAnimate.emerge(c)], [enterAnimate.emerge(bindDataSource(b.component({
			content : Content.BlockText({
				width : clientWidth - 150,
				fontSize : 10,
				lineHeight : 20,
				text : a.text[1],
				color : "#d2d2d2"
			}),
			x : 75,
			y : 95 + c.componentHeight + 26
		}), "text", 1))]])
	}
});
(function() {
	var e = zachModule["2"], b = zachModule["9"], c = e.css, a = window.ppt = {};
	fp.history = {};
	fp.slidePage = function() {
		return document.createElement("div")
	};
	a.initial = function(f) {
		window.idealWidth = 320;
		window.idealHeight = 504;
		window.clientWidth = f.width;
		window.clientHeight = f.height;
		window.xRatio = clientWidth / idealWidth;
		window.yRatio = clientHeight / idealHeight;
		window.contentSrc = f.contentSrc;
		window.globalScale = b.Size.cover({
			width : idealWidth,
			height : idealHeight
		}, clientWidth, clientHeight)
	};
	a.layoutPage = function(h, g) {
		var f = LayoutPage({
			layout : h
		}), i = DOMPage();
		i.registEnterAnimation = function() {
		};
		f.load(function() {
			f.create(i);
			g && g()
		});
		c(i, {
			"pointer-events" : "none",
			position : "relative",
			width : c.px(clientWidth),
			height : c.px(clientHeight),
			background : "#000000"
		});
		return i
	}
})(); 