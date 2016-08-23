// ==UserScript==
// @name NewsBlock
// @author Isaly Sultan
// @version 1.0
// @description example
// @unwrap
// @icon http://www.vipmir.ru/files/image/foto_1/arm_6378_0.jpg
// @grant window.close
// @run-at document-end
// @include http://*/*
// @include https://*/
// @match http://*/*
// @match https://*/
// ==/UserScript==

/**
 * При входе на сайт новостей закрывает вкладку с этим сайтом
 * Работает на англо и русскоязычных сайтах.
 * В скрипте разбираем html документ, извлекаем теги meta, и находим в них значение content.
 * Если в content есть какой-либо кусок из массива block (['news','News','новости',
 * 'Новости','Novosti','novosti','новостей','Новостей']), то закрываем вкладку.
 */
(function(){
    //в Данном куске кода осуществляем кроссбраузерный доступ к окну.
    var unsafeWindow= this.unsafeWindow;
    (function(){
        var test_scr= document.createElement("script");
        var tid= ("t" + Math.random() + +(new Date())).replace(/\./g, "");
        test_scr.text= "window." + tid + "=true";
        document.querySelector("body").appendChild(test_scr);
        if (typeof(unsafeWindow) == "undefined" || !unsafeWindow[tid]) {
            if (window[tid]) {
                unsafeWindow= window;
            } else {
                var scr= document.createElement("script");
                scr.text= "(" +
                    (function() {
                        var el= document.createElement('unsafeWindow');
                        el.style.display= 'none';
                        el.onclick=function()
                        {
                            return window;
                        };
                        document.body.appendChild(el);
                    }).toString() + ")()";
                document.querySelector("body").appendChild(scr);
                this.unsafeWindow= document.querySelector("unsafeWindow").onclick();
                unsafeWindow= window.unsafeWindow;
            }
        }
    })();
    //проверка для избежаний многократных вызовов на страницах с фреймами
    if (unsafeWindow.self != unsafeWindow.top) {
        return;
    }
    //разбираем html документ, извлекаем из тега meta content
    var meta_case = document.getElementsByTagName('meta');
    var contents = new Array();
    for (var i = 0; i < meta_case.length; i++) {
        contents.push(meta_case[i].getAttribute("content"));
    }
    //ищем в contents сочетания news, новости, Novisti и т. п.
    //возможно breaking news
    var block = ['news','News','новости','Новости','Novosti','novosti','новостей','Новостей'];
    var news = false;
    outer: for (i = 0; i < contents.length; i++)
    {
        for (var j = 0; j < block.length;++j)
        {
            if (contents[i] === null)
                continue outer;
            if (contents[i].indexOf(block[j]) !== -1)
            {
                //если мы здесь, значит мы попали на страницу новостей
                news = true;
                break outer;
            }
        }
    }
    //блокируем, если страница новостей
    if (news)
    {
        close();
    }
})();