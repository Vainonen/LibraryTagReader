function init() {

    var jsondata = requestJSON();

    function requestJSON() {
        var req = new XMLHttpRequest();

        var series = Android.getSeries();

        var url = "https://api.finna.fi/v1/search?lookfor=series:" + series + "&filter[]=language:fin" +
                        "&field[]=title&field[]=cleanIsbn&field[]=isbns" +
                        "&field[]=year&field[]=series&field[]=format:\"0/Book&limit=100";

        req.onreadystatechange = function() {
            if (req.readyState !== this.DONE) {
                return false;
            }
            if (req.status !== 200) {
                return false;
            }
            handleResponse(req.responseText);
        }

        req.open("GET", url, true);
        req.send();
    }

    function handleResponse(jsondata) {
        var json = JSON.parse(jsondata);

/*
    var json =
    {"resultCount":90,"records":[{"title":"Askeleen j\u00e4ljess\u00e4","cleanIsbn":"9516439381","isbns":["951-643-938-1 (nid.)"],"year":"1999","series":[{"name":"Wallander"}]},{"title":"Pyramidi","cleanIsbn":"951643990X","isbns":["951-643-990-X (sid.)"],"year":"2002"},{"title":"Hymyilev\u00e4 mies","cleanIsbn":"9511201174","isbns":["951-1-20117-4 (sid.)"],"year":"2005","series":[{"name":"Wallander","number":"4"}]},{"title":"Kasvoton kuolema","cleanIsbn":"9511237926","isbns":["978-951-1-23792-1 (nid.)"],"year":"2009","series":[{"name":"Wallander","number":"1"}]},{"title":"Ennen routaa","cleanIsbn":"9524594978","isbns":["952-459-497-8 (nid.)"],"year":"2005","series":[{"name":"Wallander","number":"10"}]},{"title":"Pyramidi","cleanIsbn":"9511201263","isbns":["951-1-20126-3 (sid.)"],"year":"2005","series":[{"name":"Wallander","number":"9"}]},{"title":"Palomuuri","cleanIsbn":"9516439624","isbns":["951-643-962-4 (sid.)"],"year":"2000"},{"title":"Rauhaton mies","cleanIsbn":"9511241192","isbns":["978-951-1-24119-5 (sid.)"],"year":"2009","series":[{"name":"Wallander"}]},{"title":"V\u00e4\u00e4rill\u00e4 j\u00e4ljill\u00e4","cleanIsbn":"9511156381","isbns":["951-1-15638-1 (nid.)"],"year":"1998"},{"title":"Rauhaton mies","cleanIsbn":"9511239716","isbns":["978-951-1-23971-0 (sid.)"],"year":"2009","series":[{"name":"Wallander"}]},{"title":"Palomuuri","cleanIsbn":"9511201255","isbns":["951-1-20125-5 (sid.)"],"year":"2005","series":[{"name":"Wallander","number":"8"}]},{"title":"Viides nainen","cleanIsbn":"9511201220","isbns":["951-1-20122-0 (sid.)"],"year":"2005","series":[{"name":"Wallander","number":"6"}]},{"title":"V\u00e4\u00e4rill\u00e4 j\u00e4ljill\u00e4","cleanIsbn":"9511201182","isbns":["951-1-20118-2 (sid.)"],"year":"2005","series":[{"name":"Wallander","number":"5"}]},{"title":"Askeleen j\u00e4ljess\u00e4","cleanIsbn":"9511201247","isbns":["951-1-20124-7 (sid.)"],"year":"2005","series":[{"name":"Wallander","number":"7"}]},{"title":"Valkoinen naarasleijona","cleanIsbn":"9511201239","isbns":["951-1-20123-9 (sid.)"],"year":"2005","series":[{"name":"Wallander","number":"3"}]},{"title":"Hymyilev\u00e4 mies","cleanIsbn":"9511201174","isbns":["951-1-20117-4 (sid.)"],"year":"2005","series":[{"name":"Wallander","number":"4"}]},{"title":"Kasvoton kuolema","cleanIsbn":"9511201158","isbns":["951-1-20115-8 (sid.)"],"year":"2005","series":[{"name":"Wallander","number":"[1]"}]},{"title":"Riian verikoirat","cleanIsbn":"9511201166","isbns":["951-1-20116-6 (sid.)"],"year":"2005","series":[{"name":"Wallander","number":"[2]"}]},{"title":"Perint\u00f6","year":"2009","series":[{"name":"Wallander"}]},{"title":"Peitenimi","year":"2006","series":[{"name":"Wallander"}]}],"status":"OK"}
*/

var table = document.getElementById("table");
var result = json.resultCount;
if (result == 0) alert("ISBN:llä ei löytynyt teoksia!");
records = json.records;
var titles = document.getElementsByTagName("th");
for ( var i = 0; i < titles.length; i++ ) {
    var title = titles[i];
    title.id = i;
    title.addEventListener("click", function(eventInformation) {
    var origin = eventInformation.target;
        sortColumn(origin.id);
        drawTable();
 eventInformation.preventDefault();
    }, false);
}

var chart = [];

for (var i = 0; i < records.length; i++) {
 if (!contains(records[i].title)) {
    var obj = {};
    var editions = [];
    obj.title = records[i].title;
   try {
     var position = records[i].series[0].number.replace( /^\D+/g, '');
     obj.number = parseInt(position);
      }
      catch(err) {
      }
   editions.push(getEditionData(i));
   obj.editions = editions;
   if (editions[0].isbn !== undefined) chart.push(obj);
  }
 else {
  var index = getChartIndex(records[i].title);
  obj = getEditionData(i);
  if (obj.isbn !== undefined) chart[index].editions.push(obj);
 }
}
//fill empty number fields & sort editions by year:
for (var i = 0; i < chart.length; i++) {
 if (chart[i].number === undefined) chart[i].number = search(chart[i].title);
 if (chart[i].number === undefined || chart[i].number === null) chart[i].number = 0;
   chart[i].editions.sort(function (a, b) {
      if (a.year < b.year) return -1;
      if (a.year > b.year) return 1;
      return 0;
    });
}

initTable();
sortColumn("0");
drawTable();
//listEditions();
//drawTable();
//toggleVisibility();

function sortColumn(number) {
  chart.sort(function (a, b) {
   switch (number) {
    case "0":
      if (a.number < b.number) return -1;
      if (a.number > b.number) return 1;
      return 0;
      break;
    case "1":
      if (a.title < b.title) return -1;
      if (a.title > b.title) return 1;
      return 0;
      }
    });
  }

function initTable() {
  for (var i = 0; i < chart.length; i++) {
 var row = table.insertRow(i + 1); // i + 1 don't delete column headers
    var cell1 = row.insertCell(0);
    var cell2 = row.insertCell(1);
        cell2.addEventListener("click", function(eventInformation) {
    var origin = eventInformation.target;
        showList(origin.id);
  eventInformation.preventDefault();
    }, false);
  }
}

function drawTable() {
  for (var i = 0; i < chart.length; i++) {
    if (chart[i].number != 9999999) table.rows[i+1].cells[0].innerHTML = chart[i].number;
    else table.rows[i+1].cells[0].innerHTML = "";
    var htmltext = chart[i].title+'<ul style="display: none">';
    for (var j = 0; j < chart[i].editions.length; j++){

      htmltext += '<li><a href = "http://luettelo.helmet.fi/search*fin/?searchtype=i&SORT=D&searcharg='+chart[i].editions[j].isbn+'&searchscope=9">'+chart[i].editions[j].form+" ";
      htmltext += chart[i].editions[j].year+'</a>';
    }
    htmltext += '</ul>';
    table.rows[i+1].cells[1].innerHTML = htmltext+'</ul>';


    table.rows[i+1].cells[1].id = i;
    /*
    table.rows[i+1].cells[1].addEventListener("click", function(eventInformation) {
    var origin = eventInformation.target;
        showList(origin.id);
  eventInformation.preventDefault();
    }, false);
    */
    //table.rows[i+1].cells[1].innerHTML = '<a href = "http://luettelo.helmet.fi/search*fin/?searchtype=i&SORT=D&searcharg='+"linkki"+'&searchscope=9">'+chart[i].title+'</a>';
    //table.rows[i+1].cells[2].innerHTML = chart[i][2];
    //table.rows[i+1].cells[3].innerHTML = chart[i][3];
  }
}

function showList(id) {
 var lists = document.getElementsByTagName("ul");
    var list = lists[id];
    if (list.style.display == 'none') {
    list.style.display = '';
    }
    else list.style.display = 'none';
}

/*
function toggleVisibility() {
  if (visible) {
    for (var i = 1; i < records.length; i++) {
       if (chart[i][1] === chart[i-1][1]) table.rows[i].style.display = 'none';
      }
      visible = false;
      button.innerHTML = 'Näytä kaikki niteet';
    }
  else {
   for (var i = 1; i < records.length; i++) {
       table.rows[i].style.display = '';
      }
      visible = true;
      button.innerHTML = 'Näytä vain uniikit';
    }
}
*/

function getEditionData(index) {
 var obj = {};
    var form = JSON.stringify(records[index].isbns);
    if (form != null) {
     if (form.indexOf("nid") != -1) form = "pokkari";
      if (form.indexOf("sid") != -1) form = "kovakantinen";
      if (form !== "pokkari" && form !== "kovakantinen") form = "tuntematon";
   }
   else form = "tuntematon";
   obj.isbn = records[index].cleanIsbn;
   obj.form = form;
   obj.year = records[index].year;
   return obj;
}
  /*
function listEditions() {
  for (var i = 0; i < records.length; i++){
    var obj = {};
  var index = getChartIndex(chart[i].title);

        var isbn = JSON.stringify(records[i].isbns);
    if (isbn != null) {
     if (isbn.indexOf("nid") != -1) isbn = "pokkari";
      if (isbn.indexOf("sid") != -1) isbn = "kovakantinen";
      if (isbn !== "pokkari" && isbn !== "kovakantinen") isbn = "tuntematon";
   }
   else isbn = "tuntematon";
   obj.isbn = isbn;
   obj.year = records[i].year;
     chart[index].editions.push(obj);
  }

}
*/
function search(title){
 for (var i = 0; i < records.length; i++){
  var number = 0;
   try {
     number = parseInt(records[i].series[0].number.replace( /^\D+/g, ''));
      }
      catch(err) {
      }
     if (records[i].title === title && number !== 0) {
         return number;
        }
    }
    return 9999999;
}

function contains(title){
 for (var i = 0; i < chart.length; i++){
     if (chart[i].title == title) return true;
    }
 return false;
}

function getChartIndex(title){
 for (var i = 0; i < chart.length; i++){
     if (chart[i].title == title) return i;
    }
 return null;
}
}
}