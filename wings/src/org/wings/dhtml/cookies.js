  function getCookie(name) { // use: getCookie("name");
    var re = new RegExp(name + "=([^;]+)");
    var value = re.exec(document.cookie);
    return (value != null) ? unescape(value[1]) : null;
  }

  var today = new Date();
  var expiry = new Date(today.getTime() + 15 * 60 * 1000); // plus 15 min

  function setCookie(name, value) { // use: setCookie("name", value);
    document.cookie=name + "=" + escape(value) + "; expires=" + expiry.toGMTString();
  }
