// If you want to prevent dragging, uncomment this section
function preventBehavior(e) {
    e.preventDefault();
};
document.addEventListener("touchmove", preventBehavior, false);

/* USED TO DEBUG AND CHECK THE INITIALIZATION OF PHONEGAP PLUGINS
function onBodyLoad() {
   // document.addEventListener("deviceready", onDeviceReady, false);
}

/function onDeviceReady() {
    window.plugins.social.available(function(avail) {
        if (avail) {
            //alert("Social Found");
        } else {
            alert("Could not find Social");
        }
    });
    //var db = window.openDatabase("Database", "1.0", "Feed", 200000);
    
}*/

function share(subject, text) {
	var share = cordova.require("cordova/plugin/share");
	share.show({
		subject : subject,
		text : text
	}, function() {
		 console.log("PhoneGap Plugin: Share: callback success");
	}, function() {
		console.log("PhoneGap Plugin: Share: callback error");
	});
}

function shareTitle(title, url){
	share(title, url);
	//url = 'http://www.lasillavacia.com/' + url;
    //window.plugins.social.share(title, url, 'www/img/logo.png');
}

function clickAd(uid){
	//TODO window.plugins.social.click(uid);
}

function viewAd(uid){
    //TODO implement ads window.plugins.social.view(uid);
}

function success(result) {
    alert(result);
}

function failure(result) {
    alert(result);
}


//Abrir en Browser Externo    
document.addEventListener('click', function(e) {
    if (e.hasOwnProperty('target')){
        if ((e.srcElement.target === "" || e.srcElement.target === "_blank") && e.srcElement.href.indexOf("#phonegap=external") === -1) {
            if(e.srcElement.href.indexOf("LaSillaReader") == -1){
                e.preventDefault();
                e.stopPropagation();
                window.open(e.srcElement.href, '_blank', 'location=yes');
            }
        }
    }    
}, true);

//Add Event Handler
function addEventHandler(elem,eventType,handler) {
   if (elem.addEventListener)
       elem.addEventListener (eventType,handler,false);
   else if (elem.attachEvent)
       elem.attachEvent ('on'+eventType,handler); 
}


function horasDiferencia(d1, d2) {
    var t2 = d2.getTime();
    var t1 = d1.getTime();
    var dif=parseInt((t2-t1)/(3600*1000));
    var dia=1;
    if(dif>=24){
        return Math.floor(dia =dif/24)+"d";

    }else{
        return dif+"h";
    }
}

function hide(){
    var idfeed=this.id;
    idfeed=idfeed.substr(4,4);

    var item = document.getElementById('columns');
    item.className=(item.className=='hidden')?'unhidden':'hidden';

    var item = document.getElementById('columns-nota');
    item.className=(item.className=='hidden')?'unhidden':'hidden';



    var nota = document.getElementById('feed-nota'+idfeed);
    nota.className=(nota.className=='hidden')?'unhidden':'hidden';

    var item = document.getElementById('wrapper');
    item.className=(item.className=='hidden')?'unhidden':'hidden';
}

function open_win(dir)
{
    var ref = window.open(dir, '_blank', 'EnableViewPortScale=yes');
    ref.close();
}






