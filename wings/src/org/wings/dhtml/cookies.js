/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://wings.mercatis.de).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 * @author <a href="mailto:andre@lison.de">Andre Lison</a>
 */

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
