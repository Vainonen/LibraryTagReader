function init() {

    var req = new XMLHttpRequest();

    var isbn = Android.getISBN();

    var url = "https://api.finna.fi/v1/search?lookfor=cleanIsbn:"+isbn+"&filter[]=language:fin" +
        "&field[]=title&field[]=authors&field[]=cleanIsbn&field[]=isbns" +
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
var json =
{"resultCount":1,"records":[{"authors":{"main":{"Mankell, Henning":["-"]},"secondary":{"Kivel\u00e4, P\u00e4ivi":["-"]}},"title":"Rauhaton mies","series":[{"name":"Wallander"}]}],"status":"OK"}
*/
        records = json.records;

        var authors;
        var author;
              try {
                authors = records[0].authors.main;
                author = Object.keys(authors)[0];
               }
                catch(err) {
               }

        document.getElementById("author").innerHTML = '<a href = "http://luettelo.helmet.fi/search*fin/X?SEARCH=a:('+author+')&searchscope=9&SORT=D">'+author+'</a>';

        var title;
          try {
            title = records[0].title;
          }
          catch(err) {
          }

         document.getElementById("title").innerHTML = '<a href = "http://luettelo.helmet.fi/search*fin/X?SEARCH=t:('+title+')&searchscope=9&SORT=D">'+title+'</a>';

        var series;
            try {
                series = records[0].series[0].name;
            }
                catch(err) {
            }

        Android.setSeries(series);

        if (series != null) {
            document.getElementById("series").innerHTML = '<a href = "series.html">'+series+'</a>';
        }
        else document.getElementById("series").innerHTML = '-';
    }
}