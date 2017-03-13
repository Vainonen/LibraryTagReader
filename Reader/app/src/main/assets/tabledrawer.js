function init() {

    var req = new XMLHttpRequest();

    var series = "Wallander";

    var url = "https://api.finna.fi/v1/search?lookfor=series:" + series + "&filter[]=language:fin" +
                    "&field[]=title&field[]=cleanIsbn&field[]=isbns" +
                    "&field[]=year&field[]=series&field[]=format:\"0/Book";

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

    function handleResponse(jsondata) {
        var json = JSON.parse(jsondata);


    /*
    myObj =
    {"resultCount":90,"records":[{"title":"Askeleen j\u00e4ljess\u00e4","cleanIsbn":"9516439381","isbns":["951-643-938-1 (nid.)"],"year":"1999","series":[{"name":"Wallander"}]},{"title":"Pyramidi","cleanIsbn":"951643990X","isbns":["951-643-990-X (sid.)"],"year":"2002"},{"title":"Hymyilev\u00e4 mies","cleanIsbn":"9511201174","isbns":["951-1-20117-4 (sid.)"],"year":"2005","series":[{"name":"Wallander","number":"4"}]},{"title":"Kasvoton kuolema","cleanIsbn":"9511237926","isbns":["978-951-1-23792-1 (nid.)"],"year":"2009","series":[{"name":"Wallander","number":"1"}]},{"title":"Ennen routaa","cleanIsbn":"9524594978","isbns":["952-459-497-8 (nid.)"],"year":"2005","series":[{"name":"Wallander","number":"10"}]},{"title":"Pyramidi","cleanIsbn":"9511201263","isbns":["951-1-20126-3 (sid.)"],"year":"2005","series":[{"name":"Wallander","number":"9"}]},{"title":"Palomuuri","cleanIsbn":"9516439624","isbns":["951-643-962-4 (sid.)"],"year":"2000"},{"title":"Rauhaton mies","cleanIsbn":"9511241192","isbns":["978-951-1-24119-5 (sid.)"],"year":"2009","series":[{"name":"Wallander"}]},{"title":"V\u00e4\u00e4rill\u00e4 j\u00e4ljill\u00e4","cleanIsbn":"9511156381","isbns":["951-1-15638-1 (nid.)"],"year":"1998"},{"title":"Rauhaton mies","cleanIsbn":"9511239716","isbns":["978-951-1-23971-0 (sid.)"],"year":"2009","series":[{"name":"Wallander"}]},{"title":"Palomuuri","cleanIsbn":"9511201255","isbns":["951-1-20125-5 (sid.)"],"year":"2005","series":[{"name":"Wallander","number":"8"}]},{"title":"Viides nainen","cleanIsbn":"9511201220","isbns":["951-1-20122-0 (sid.)"],"year":"2005","series":[{"name":"Wallander","number":"6"}]},{"title":"V\u00e4\u00e4rill\u00e4 j\u00e4ljill\u00e4","cleanIsbn":"9511201182","isbns":["951-1-20118-2 (sid.)"],"year":"2005","series":[{"name":"Wallander","number":"5"}]},{"title":"Askeleen j\u00e4ljess\u00e4","cleanIsbn":"9511201247","isbns":["951-1-20124-7 (sid.)"],"year":"2005","series":[{"name":"Wallander","number":"7"}]},{"title":"Valkoinen naarasleijona","cleanIsbn":"9511201239","isbns":["951-1-20123-9 (sid.)"],"year":"2005","series":[{"name":"Wallander","number":"3"}]},{"title":"Hymyilev\u00e4 mies","cleanIsbn":"9511201174","isbns":["951-1-20117-4 (sid.)"],"year":"2005","series":[{"name":"Wallander","number":"4"}]},{"title":"Kasvoton kuolema","cleanIsbn":"9511201158","isbns":["951-1-20115-8 (sid.)"],"year":"2005","series":[{"name":"Wallander","number":"[1]"}]},{"title":"Riian verikoirat","cleanIsbn":"9511201166","isbns":["951-1-20116-6 (sid.)"],"year":"2005","series":[{"name":"Wallander","number":"[2]"}]},{"title":"Perint\u00f6","year":"2009","series":[{"name":"Wallander"}]},{"title":"Peitenimi","year":"2006","series":[{"name":"Wallander"}]}],"status":"OK"}
    */

    var table = document.getElementById("table");

    records = json.records;

    var chart = new Array(records.length);
    for (var i = 0; i < records.length; i++) {
        chart[i] = new Array(5);
        try {
            var position = records[i].series[0].number.replace( /^\D+/g, '');
            chart[i][0] = parseInt(position);
        }
        catch(err) {
        }

        chart[i][2] = records[i].year;
        chart[i][1] = records[i].title;
        var str = JSON.stringify(records[i].isbns);
        if (str != null) {
            if (str.indexOf("nid") != -1) chart[i][3] = "pokkari";
            if (str.indexOf("sid") != -1) chart[i][3] = "kovakantinen";
        }
        chart[i][4] = records[i].cleanIsbn;
    }

    //fill empty number fields:
    for (var i = 0; i < records.length; i++) {
        if (chart[i][0] === undefined) chart[i][0] = search(chart[i][1]);
        if (chart[i][0] === undefined || chart[i][0] === null) chart[i][0] = 0;
    }

    sortColumn(0);

    function sortColumn(number) {

        chart.sort(function (a, b) {

            if (a[number] === b[number]) {
                return 0;
            }
            else {
                return (a[number] < b[number]) ? -1 : 1;
            }
        });
      }

    for (var i = 0; i < records.length; i++) {
    var row = table.insertRow(i + 1); // i + 1 don't delete column headers

    var cell1 = row.insertCell(0);
    var cell2 = row.insertCell(1);
    var cell3 = row.insertCell(2);
    var cell4 = row.insertCell(3);

    cell1.innerHTML = chart[i][0];
    cell2.innerHTML = '<a href = "http://luettelo.helmet.fi/search*fin/?searchtype=i&SORT=D&searcharg='+chart[i][4]+'&searchscope=9">'+chart[i][1]+'</a>';
    cell3.innerHTML = chart[i][2];
    cell4.innerHTML = chart[i][3];


    }

    function search(title){
     for (var i = 0; i < records.length; i++){
         if (chart[i][1] == title && chart[i][0] !== undefined) {
             return chart[i][0];
            }
        }
    }
    }
}