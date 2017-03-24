var http = require('http'),
    url = require('url'),
    path = require("path"),
    fs = require("fs");


function serveFile(filename, res) {
  fs.createReadStream(filename).pipe(res);
}

http.createServer(function(req, res) {
  if (req.url == '/') {
    res.setHeader('Content-Type', 'text/html; charset=utf-8');
    res.writeHead(200);
    serveFile(path.join(__dirname, "statics/index.html"), res);
  } else {
    var requestUrl = url.parse(req.url);
    var filename = path.join(__dirname, requestUrl.pathname);
    fs.exists(filename, function(exists) {
      if (exists) {
        serveFile(filename, res);
      } else {
        res.writeHead(404);
        res.end();
      }
    });
  }
}).listen(3000, function(err) {
  if (err) throw err;
  console.log('Listening on 3000...')
});
