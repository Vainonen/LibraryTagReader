function init() {

    var req = new XMLHttpRequest();

    var author = Android.getAuthor();

    var url = "https://api.finna.fi/v1/search?lookfor=author:" + author + "&sort=main_date_str%20desc" +
        "&filter[]=language:fin&field[]=edition&field[]=title&field[]=cleanIsbn&field[]=year" +
        "&field[]=format:\%220/Book&limit=10";

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
        if (json.resultCount == 0) Android.returnToPreviousActivity();
        /*
        var json =
        {"resultCount":1,"records":[{"authors":{"main":{"Mankell, Henning":["-"]},"secondary":{"Kivel\u00e4, P\u00e4ivi":["-"]}},"title":"Rauhaton mies","series":[{"name":"Wallander"}]}],"status":"OK"}
        */
                records = json.records;
        var title, isbn;
        records = json.records;
        var i = 0;

        while (title == undefined && i < records.length) {
          if (records[i].edition === undefined) {
          title = records[i].title;
          isbn = records[i].cleanIsbn;

          }
          i++;
        }
        window.location.href = "http://luettelo.helmet.fi/search*fin/?searchtype=i&SORT=D&searcharg="+isbn+"&searchscope=9"
    }
}