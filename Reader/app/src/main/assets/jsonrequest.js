function requestJSON(String url) {
    var req = new XMLHttpRequest();

    req.onreadystatechange = function() {
        if (req.readyState !== this.DONE) {
            return false;
        }
        if (req.status !== 200) {
            return false;
        }
        return JSON.parse(req.responseText);
    }

    req.open("GET", url, true);
    req.send();
}